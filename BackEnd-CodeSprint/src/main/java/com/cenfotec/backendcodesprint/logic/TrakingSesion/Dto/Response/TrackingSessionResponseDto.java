package com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingSessionResponseDto {
    private Long trackingSessionId;
    private Long bookingId;
    private Long providerProfileId;
    private String trackingState;
    private OffsetDateTime startedAt;
    private OffsetDateTime endedAt;
}
