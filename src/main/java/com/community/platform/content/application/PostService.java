package com.community.platform.content.application;

import com.community.platform.content.domain.*;
import com.community.platform.content.exception.CategoryNotFoundException;
import com.community.platform.content.exception.PostNotFoundException;
import com.community.platform.content.infrastructure.persistence.*;
import com.community.platform.user.exception.UserNotFoundException;
import com.community.platform.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 관리 애플리케이션 서비스
 * 게시글 생명주기와 관련된 비즈니스 로직 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;

    /**
     * 새 게시글 작성 (임시저장 상태로 생성)
     */
    @Transactional
    public Post createPost(Long authorId, Long categoryId, String title, String content, 
                          ContentType contentType, List<String> tagNames) {
        log.info("게시글 작성 시작. authorId: {}, title: {}", authorId, title);
        
        // 작성자 존재 확인
        validateUserExists(authorId);
        
        // 카테고리 조회
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        
        // 게시글 생성
        Post post = Post.create(authorId, category, title, content, contentType);
        Post savedPost = postRepository.save(post);
        
        // 태그 처리 및 연결
        if (tagNames != null && !tagNames.isEmpty()) {
            attachTagsToPost(savedPost, tagNames);
        }
        
        log.info("게시글 작성 완료. postId: {}", savedPost.getId());
        return savedPost;
    }

    /**
     * 게시글 발행 (임시저장 → 발행 상태 변경)
     */
    @Transactional
    public void publishPost(Long postId, Long authorId) {
        log.info("게시글 발행 처리. postId: {}, authorId: {}", postId, authorId);
        
        Post post = getPostById(postId);
        
        // 작성자 권한 확인
        validateAuthorPermission(post, authorId);
        
        // 게시글 발행
        post.publish();
        
        log.info("게시글 발행 완료. postId: {}", postId);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updatePost(Long postId, Long authorId, String title, String content, 
                          List<String> tagNames) {
        log.info("게시글 수정 처리. postId: {}, authorId: {}", postId, authorId);
        
        Post post = getPostById(postId);
        
        // 작성자 권한 확인
        validateAuthorPermission(post, authorId);
        
        // 게시글 내용 수정
        post.updateContent(title, content);
        
        // 기존 태그 연결 제거 후 새 태그 연결
        if (tagNames != null) {
            postTagRepository.deleteByPostId(postId);
            if (!tagNames.isEmpty()) {
                attachTagsToPost(post, tagNames);
            }
        }
        
        log.info("게시글 수정 완료. postId: {}", postId);
    }

    /**
     * 게시글 삭제 (소프트 삭제)
     */
    @Transactional
    public void deletePost(Long postId, Long authorId) {
        log.info("게시글 삭제 처리. postId: {}, authorId: {}", postId, authorId);
        
        Post post = getPostById(postId);
        
        // 작성자 권한 확인
        validateAuthorPermission(post, authorId);
        
        // 게시글 삭제
        post.delete();
        
        log.info("게시글 삭제 완료. postId: {}", postId);
    }

    /**
     * 게시글 상세 조회 (조회수 증가)
     */
    @Transactional
    public Post getPostWithViewCount(Long postId) {
        Post post = getPostById(postId);
        
        // 발행된 게시글만 조회수 증가
        if (post.isPublished()) {
            post.increaseViewCount();
        }
        
        return post;
    }

    /**
     * 게시글 조회 (조회수 증가 없음)
     */
    public Post getPostById(Long postId) {
        return postRepository.findByIdWithCategory(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    /**
     * 발행된 게시글 목록 조회 (메인 페이지용)
     */
    public Page<Post> getPublishedPosts(Pageable pageable) {
        return postRepository.findByStatusOrderByPublishedAtDesc(PostStatus.PUBLISHED, pageable);
    }

    /**
     * 카테고리별 게시글 목록 조회
     */
    public Page<Post> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryIdAndStatus(categoryId, PostStatus.PUBLISHED, pageable);
    }

    /**
     * 작성자별 게시글 목록 조회
     */
    public Page<Post> getPostsByAuthor(Long authorId, Pageable pageable) {
        return postRepository.findByAuthorIdAndStatusOrderByCreatedAtDesc(
                authorId, PostStatus.PUBLISHED, pageable);
    }

    /**
     * 게시글 검색 (제목, 내용)
     */
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchByKeywordAndStatus(keyword, PostStatus.PUBLISHED, pageable);
    }

    /**
     * 복합 조건 게시글 검색 (QueryDSL 활용)
     */
    public Page<Post> searchPostsWithFilters(String keyword, Long categoryId, 
                                           List<String> tagNames, LocalDateTime startDate, 
                                           LocalDateTime endDate, Pageable pageable) {
        return postRepository.searchPostsWithFilters(
                keyword, categoryId, tagNames, PostStatus.PUBLISHED, 
                startDate, endDate, pageable);
    }

    /**
     * 인기 게시글 조회 (좋아요, 조회수 기반)
     */
    public Page<Post> getPopularPosts(int daysAgo, Pageable pageable) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(daysAgo);
        return postRepository.findPopularPostsWithScore(PostStatus.PUBLISHED, fromDate, pageable);
    }

    /**
     * 트렌딩 게시글 조회 (최근 활동 기반)
     */
    public Page<Post> getTrendingPosts(int hourRange, Pageable pageable) {
        return postRepository.findTrendingPosts(PostStatus.PUBLISHED, hourRange, pageable);
    }

    /**
     * 공지사항 목록 조회
     */
    public List<Post> getNoticePosts() {
        return postRepository.findNoticePostsByStatus(PostStatus.PUBLISHED);
    }

    /**
     * 게시글을 공지사항으로 설정 (관리자 권한 필요)
     */
    @Transactional
    public void markAsNotice(Long postId) {
        log.info("게시글 공지사항 설정. postId: {}", postId);
        
        Post post = getPostById(postId);
        post.markAsNotice();
        
        log.info("게시글 공지사항 설정 완료. postId: {}", postId);
    }

    /**
     * 공지사항 해제
     */
    @Transactional
    public void unmarkAsNotice(Long postId) {
        log.info("게시글 공지사항 해제. postId: {}", postId);
        
        Post post = getPostById(postId);
        post.unmarkAsNotice();
        
        log.info("게시글 공지사항 해제 완료. postId: {}", postId);
    }

    /**
     * 유사한 게시글 조회 (태그 기반)
     */
    public List<Post> getSimilarPosts(Long postId, int limit) {
        return postRepository.findSimilarPosts(postId, limit);
    }

    /**
     * 사용자별 게시글 통계 조회
     */
    public Long getPostCountByAuthor(Long authorId) {
        return postRepository.countByAuthorIdAndStatus(authorId, PostStatus.PUBLISHED);
    }

    /**
     * 게시글에 태그 연결
     */
    private void attachTagsToPost(Post post, List<String> tagNames) {
        for (String tagName : tagNames) {
            Tag tag = findOrCreateTag(tagName);
            PostTag postTag = PostTag.create(post, tag);
            postTagRepository.save(postTag);
        }
    }

    /**
     * 태그 조회 또는 생성
     */
    private Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                    Tag newTag = Tag.create(tagName, null);
                    return tagRepository.save(newTag);
                });
    }

    /**
     * 사용자 존재 여부 확인
     */
    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * 작성자 권한 확인
     */
    private void validateAuthorPermission(Post post, Long userId) {
        if (!post.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 수정/삭제할 수 있습니다.");
        }
    }
}