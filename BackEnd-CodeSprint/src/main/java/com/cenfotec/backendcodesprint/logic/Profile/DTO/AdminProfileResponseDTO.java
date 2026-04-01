package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminProfileResponseDTO {

    private Long          id;
    private String        fullName;
    private String        email;
    private String        role;
    private String        photoUrl;
    private LocalDateTime createdAt;
    private SystemStatsDTO systemStats;

    @Data
    public static class SystemStatsDTO {
        private long totalUsers;
        private long totalProviders;
        private long activeServices;
        private long totalReviews;
    }
}