package com.cenfotec.backendcodesprint.logic.FilteredHome.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FilteredHomeResponseDTO {

    private Long markerId;
    private Long bookingId;
    private String bookingStatus;
    private LocalDateTime scheduledAt;
    private String clientName;
    private String serviceTitle;

    private String category;
    private String roomName;
    private String title;
    private String description;
}
