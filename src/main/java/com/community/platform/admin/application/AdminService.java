package com.community.platform.admin.application;

import com.community.platform.admin.dto.AdminStatisticsResponse;
import com.community.platform.content.infrastructure.persistence.CommentRepository;
import com.community.platform.content.infrastructure.persistence.PostRepository;
import com.community.platform.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 기능 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 관리자 통계 조회
     */
    public AdminStatisticsResponse getStatistics() {
        log.info("관리자 통계 조회");

        long totalUsers = userRepository.count();
        long totalPosts = postRepository.count();
        long totalComments = commentRepository.count();

        // 오늘 신규 사용자 (임시로 0)
        long newUsersToday = 0;

        // 오늘 신규 게시글 (임시로 0)
        long newPostsToday = 0;

        // 활성 사용자 수
        long activeUsers = userRepository.countByStatus(com.community.platform.user.domain.UserStatus.ACTIVE);

        return AdminStatisticsResponse.builder()
                .totalUsers(totalUsers)
                .totalPosts(totalPosts)
                .totalComments(totalComments)
                .totalReports(0L) // TODO: Report 기능 구현 시 추가
                .activeUsers(activeUsers)
                .newUsersToday(newUsersToday)
                .newPostsToday(newPostsToday)
                .pendingReports(0L) // TODO: Report 기능 구현 시 추가
                .build();
    }


}
