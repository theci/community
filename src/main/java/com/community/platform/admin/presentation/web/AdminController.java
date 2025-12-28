package com.community.platform.admin.presentation.web;

import com.community.platform.admin.application.AdminService;
import com.community.platform.admin.dto.AdminStatisticsResponse;
import com.community.platform.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 관리자 통계 조회
     * GET /api/v1/admin/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<AdminStatisticsResponse>> getStatistics() {
        log.info("관리자 통계 조회");

        AdminStatisticsResponse statistics = adminService.getStatistics();

        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

}
