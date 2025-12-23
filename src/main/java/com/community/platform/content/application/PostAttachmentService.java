package com.community.platform.content.application;

import com.community.platform.content.domain.AttachmentType;
import com.community.platform.content.domain.Post;
import com.community.platform.content.domain.PostAttachment;
import com.community.platform.content.exception.PostNotFoundException;
import com.community.platform.content.infrastructure.persistence.PostAttachmentRepository;
import com.community.platform.content.infrastructure.persistence.PostRepository;
import com.community.platform.content.infrastructure.storage.FileStorageService;
import com.community.platform.shared.domain.DomainEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 첨부파일 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostAttachmentService {

    private final PostAttachmentRepository attachmentRepository;
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;
    private final DomainEventService domainEventService;

    /**
     * 첨부파일 업로드 (다중 파일 지원)
     */
    @Transactional
    public List<PostAttachment> uploadAttachments(Long postId, List<MultipartFile> files) {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        List<PostAttachment> attachments = new ArrayList<>();
        int displayOrder = attachmentRepository.countByPostId(postId).intValue();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // 파일 타입 판별
            String extension = extractExtension(file.getOriginalFilename());
            AttachmentType fileType = AttachmentType.fromExtension(extension);

            // 파일 저장
            String filePath = fileStorageService.saveFile(file, fileType);

            // 엔티티 생성
            PostAttachment attachment = PostAttachment.create(
                    postId,
                    fileStorageService.generateUniqueFileName(file.getOriginalFilename()),
                    file.getOriginalFilename(),
                    fileType,
                    filePath,
                    file.getSize(),
                    file.getContentType(),
                    displayOrder++
            );

            attachments.add(attachmentRepository.save(attachment));
            domainEventService.publishEvents(attachment);

            log.info("첨부파일 업로드 완료. postId: {}, fileName: {}", postId, file.getOriginalFilename());
        }

        return attachments;
    }

    /**
     * 첨부파일 삭제
     */
    @Transactional
    public void deleteAttachment(Long attachmentId, Long userId) {
        PostAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("첨부파일을 찾을 수 없습니다"));

        // 게시글 작성자 확인
        Post post = postRepository.findById(attachment.getPostId())
                .orElseThrow(() -> new PostNotFoundException(attachment.getPostId()));

        if (!post.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("첨부파일을 삭제할 권한이 없습니다");
        }

        // 파일 시스템에서 삭제
        try {
            fileStorageService.deleteFile(attachment.getFilePath());
        } catch (Exception e) {
            log.error("파일 시스템 삭제 실패: {}", attachment.getFilePath(), e);
        }

        // DB에서 삭제
        attachmentRepository.delete(attachment);

        log.info("첨부파일 삭제 완료. attachmentId: {}, postId: {}", attachmentId, attachment.getPostId());
    }

    /**
     * 게시글의 모든 첨부파일 조회
     */
    @Transactional(readOnly = true)
    public List<PostAttachment> getPostAttachments(Long postId) {
        return attachmentRepository.findByPostIdOrderByDisplayOrder(postId);
    }

    /**
     * 게시글 삭제 시 모든 첨부파일 삭제
     */
    @Transactional
    public void deleteAllAttachmentsByPost(Long postId) {
        List<PostAttachment> attachments = attachmentRepository.findByPostIdOrderByDisplayOrder(postId);

        for (PostAttachment attachment : attachments) {
            try {
                fileStorageService.deleteFile(attachment.getFilePath());
            } catch (Exception e) {
                log.error("파일 시스템 삭제 실패: {}", attachment.getFilePath(), e);
            }
        }

        attachmentRepository.deleteByPostId(postId);
        log.info("게시글 첨부파일 전체 삭제 완료. postId: {}, count: {}", postId, attachments.size());
    }

    /**
     * 파일 확장자 추출
     */
    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("파일 확장자를 찾을 수 없습니다");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
