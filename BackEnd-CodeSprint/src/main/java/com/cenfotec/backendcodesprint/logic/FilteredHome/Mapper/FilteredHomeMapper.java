package com.cenfotec.backendcodesprint.logic.FilteredHome.Mapper;

import com.cenfotec.backendcodesprint.logic.FilteredHome.DTO.FilteredHomeResponseDTO;
import com.cenfotec.backendcodesprint.logic.Model.HomeMarker;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FilteredHomeMapper {

    public FilteredHomeResponseDTO toDTO(
            HomeMarker marker,
            Long bookingId,
            String bookingStatus,
            LocalDateTime scheduledAt,
            String clientName,
            String serviceTitle,
            String category
    ) {
        FilteredHomeResponseDTO dto = new FilteredHomeResponseDTO();

        dto.setMarkerId(marker.getId());
        dto.setBookingId(bookingId);
        dto.setBookingStatus(bookingStatus);
        dto.setScheduledAt(scheduledAt);
        dto.setClientName(clientName);
        dto.setServiceTitle(serviceTitle);
        dto.setCategory(category);
        dto.setRoomName(marker.getRoom());
        dto.setTitle(marker.getTitle());
        dto.setDescription(marker.getDescription());

        return dto;
    }
}
