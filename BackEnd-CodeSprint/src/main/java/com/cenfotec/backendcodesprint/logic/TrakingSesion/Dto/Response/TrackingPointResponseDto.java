package com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingPointResponseDto {
    private Long trackingPointId;
    private Long trackingSessionId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long recordedAt;
}

