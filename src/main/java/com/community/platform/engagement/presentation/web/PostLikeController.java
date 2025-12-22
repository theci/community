package com.community.platform.engagement.presentation.web;

import com.community.platform.engagement.application.EngagementMapper;
import com.community.platform.engagement.application.PostLikeService;
import com.community.platform.engagement.domain.PostLike;
import com.community.platform.engagement.dto.LikeToggleResponse;
import com.community.platform.engagement.dto.PostLikeResponse;
import com.community.platform.shared.dto.ApiResponse;
import com.community.platform.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * 게시글 좋아요 관리 REST API Controller
 * 좋아요 추가/취소, 사용자별 좋아요 내역 조회 엔드포인트 제공
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final EngagementMapper engagementMapper;

    /**
     * 게시글 좋아요 토글 (추가/취소)
     * POST /api/v1/posts/{postId}/like
     */
    @PostMapping("/{postId}/like")
    public ApiResponse<LikeToggleResponse> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long currentUserId) { // TODO: Security 적용 후 @AuthenticationPrincipal 사용
        log.info("게시글 좋아요 토글: postId={}, userId={}", postId, currentUserId);

        PostLikeService.LikeResult result = postLikeService.toggleLike(currentUserId, postId);
        LikeToggleResponse response = LikeToggleResponse.builder()
                .postId(postId)
                .isLiked(result.isLiked())
                .totalLikeCount(result.getTotalLikeCount())
                .message(result.isLiked() ? "좋아요를 누르셨습니다" : "좋아요를 취소하셨습니다")
                .build();

        return ApiResponse.success(response);
    }

    /**
     * 게시글 좋아요 상태 확인
     * GET /api/v1/posts/{postId}/like/status
     */
    @GetMapping("/{postId}/like/status")
    public ApiResponse<Boolean> getLikeStatus(
            @PathVariable Long postId,
            @RequestParam Long currentUserId) {
        log.debug("게시글 좋아요 상태 확인: postId={}, userId={}", postId, currentUserId);

        boolean isLiked = postLikeService.isLikedByUser(currentUserId, postId);
        return ApiResponse.success(isLiked);
    }

    /**
     * 게시글 총 좋아요 수 조회
     * GET /api/v1/posts/{postId}/like/count
     */
    @GetMapping("/{postId}/like/count")
    public ApiResponse<Long> getLikeCount(@PathVariable Long postId) {
        log.debug("게시글 좋아요 수 조회: postId={}", postId);

        Long likeCount = postLikeService.getLikeCount(postId);
        return ApiResponse.success(likeCount);
    }

    /**
     * 게시글 좋아요한 사용자 목록 조회 (페이징)
     * GET /api/v1/posts/{postId}/likes
     */
    @GetMapping("/{postId}/likes")
    public ApiResponse<PageResponse<PostLikeResponse>> getPostLikes(
            @PathVariable Long postId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("게시글 좋아요한 사용자 목록 조회: postId={}, page={}", 
            postId, pageable.getPageNumber());

        Page<PostLike> likes = postLikeService.getPostLikes(postId, pageable);
        Page<PostLikeResponse> likeResponses = likes.map(engagementMapper::toPostLikeResponse);
        PageResponse<PostLikeResponse> response = PageResponse.of(likeResponses);

        return ApiResponse.success(response);
    }

    /**
     * 사용자가 좋아요한 게시글 목록 조회 (페이징)
     * GET /api/v1/users/me/likes
     */
    @GetMapping("/likes/me")
    public ApiResponse<PageResponse<PostLikeResponse>> getUserLikes(
            @RequestParam Long currentUserId, // TODO: Security 적용 후 제거
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("사용자 좋아요 내역 조회: userId={}, page={}", 
            currentUserId, pageable.getPageNumber());

        Page<PostLike> likes = postLikeService.getUserLikes(currentUserId, pageable);
        Page<PostLikeResponse> likeResponses = likes.map(engagementMapper::toPostLikeResponse);
        PageResponse<PostLikeResponse> response = PageResponse.of(likeResponses);

        return ApiResponse.success(response);
    }

    /**
     * 인기 게시글 조회 (좋아요 수 기준)
     * GET /api/v1/posts/popular
     */
    @GetMapping("/popular")
    public ApiResponse<PageResponse<Object[]>> getPopularPosts(
            @RequestParam(defaultValue = "7") int days,
            @PageableDefault(size = 10) Pageable pageable) {
        log.debug("인기 게시글 조회: days={}, page={}", days, pageable.getPageNumber());

        Page<Object[]> popularPosts = postLikeService.getPopularPosts(days, pageable);
        PageResponse<Object[]> response = PageResponse.of(popularPosts);

        return ApiResponse.success(response, 
            String.format("최근 %d일 인기 게시글 목록입니다", days));
    }

    /**
     * 좋아요 급상승 게시글 조회 (관리자/분석용)
     * GET /api/v1/posts/trending
     */
    @GetMapping("/trending")
    public ApiResponse<PageResponse<Object[]>> getTrendingPosts(
            @RequestParam(defaultValue = "24") int hours,
            @PageableDefault(size = 10) Pageable pageable) {
        log.debug("급상승 게시글 조회: hours={}, page={}", hours, pageable.getPageNumber());

        Page<Object[]> trendingPosts = postLikeService.getTrendingPosts(hours, pageable);
        PageResponse<Object[]> response = PageResponse.of(trendingPosts);

        return ApiResponse.success(response,
            String.format("최근 %d시간 급상승 게시글 목록입니다", hours));
    }

    // ========== Admin APIs (관리자 전용) ==========

    /**
     * 좋아요 통계 조회 (관리자 전용)
     * GET /api/v1/admin/likes/statistics
     */
    @GetMapping("/admin/likes/statistics")
    public ApiResponse<Object[]> getLikeStatistics(
            @RequestParam(defaultValue = "30") int days) {
        log.debug("좋아요 통계 조회: days={}", days);

        Object[] statistics = postLikeService.getLikeStatistics(days);
        return ApiResponse.success(statistics);
    }

    /**
     * 사용자별 좋아요 활동 조회 (관리자 전용)
     * GET /api/v1/admin/users/{userId}/like-activity
     */
    @GetMapping("/admin/users/{userId}/like-activity")
    public ApiResponse<Object[]> getUserLikeActivity(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "30") int days) {
        log.debug("사용자 좋아요 활동 조회: userId={}, days={}", userId, days);

        Object[] activity = postLikeService.getUserLikeActivity(userId, days);
        return ApiResponse.success(activity);
    }

    /**
     * 의심스러운 좋아요 패턴 감지 (관리자 전용)
     * GET /api/v1/admin/likes/suspicious
     */
    @GetMapping("/admin/likes/suspicious")
    public ApiResponse<PageResponse<Object[]>> getSuspiciousLikePatterns(
            @PageableDefault(size = 50) Pageable pageable) {
        log.debug("의심스러운 좋아요 패턴 감지");

        Page<Object[]> suspiciousPatterns = postLikeService.getSuspiciousLikePatterns(pageable);
        PageResponse<Object[]> response = PageResponse.of(suspiciousPatterns);

        return ApiResponse.success(response);
    }
}