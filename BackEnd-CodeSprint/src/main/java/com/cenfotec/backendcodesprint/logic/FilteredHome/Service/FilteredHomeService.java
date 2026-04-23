package com.cenfotec.backendcodesprint.logic.FilteredHome.Service;

import com.cenfotec.backendcodesprint.logic.FilteredHome.DTO.FilteredHomeResponseDTO;
import com.cenfotec.backendcodesprint.logic.FilteredHome.Mapper.FilteredHomeMapper;
import com.cenfotec.backendcodesprint.logic.FilteredHome.Repository.FilteredHomeRepository;
import com.cenfotec.backendcodesprint.logic.FilteredHome.Repository.HomeMarkerRepository;
import com.cenfotec.backendcodesprint.logic.Model.HomeBookingPermission;
import com.cenfotec.backendcodesprint.logic.Model.HomeMarker;
import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Service.ServiceBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilteredHomeService {

    private final FilteredHomeRepository repository;
    private final HomeMarkerRepository homeMarkerRepository;
    private final FilteredHomeMapper mapper;
    private final ServiceBookingService bookingService;

    @Transactional(readOnly = true)
    public List<FilteredHomeResponseDTO> getFilteredHome(
            Long bookingId,
            Long providerProfileId,
            String layer
    ) {
        ServiceBooking booking = bookingService.getValidatedBooking(bookingId, providerProfileId);
        String status = normalize(booking.getBookingStatus());

        if (isPending(status)) {
            return List.of();
        }

        Optional<HomeBookingPermission> permissionOpt =
                repository.findFirstByServiceBooking_IdOrderByIdDesc(bookingId);

        if (permissionOpt.isEmpty()) {
            return List.of();
        }

        HomeBookingPermission permission = permissionOpt.get();
        Long seniorProfileId = booking.getSeniorProfile().getId();
        String normalizedLayer = normalizeLayer(layer);

        return homeMarkerRepository.findBySeniorProfileIdAndActiveTrue(seniorProfileId).stream()
                .map(marker -> toVisibleDto(
                        marker,
                        permission,
                        status,
                        normalizedLayer,
                        bookingId,
                        booking.getBookingStatus(),
                        booking
                ))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<FilteredHomeResponseDTO> toVisibleDto(
            HomeMarker marker,
            HomeBookingPermission permission,
            String status,
            String normalizedLayer,
            Long bookingId,
            String bookingStatus,
            ServiceBooking booking
    ) {
        Optional<String> categoryOpt = toVisibleCategory(marker, permission, status);
        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }

        String category = categoryOpt.get();
        if (normalizedLayer != null && !normalizedLayer.equals(category)) {
            return Optional.empty();
        }

        return Optional.of(
                mapper.toDTO(
                        marker,
                        bookingId,
                        bookingStatus,
                        booking.getScheduledAt(),
                        booking.getClientProfile().getUser().getUserName(),
                        booking.getCareService().getTitle(),
                        toDisplayCategory(category)
                )
        );
    }

    private Optional<String> toVisibleCategory(
            HomeMarker marker,
            HomeBookingPermission permission,
            String bookingStatus
    ) {
        String category = normalizeLayer(marker.getType());
        if (category == null) {
            return Optional.empty();
        }

        boolean visible = switch (category) {
            case "SAFETY" -> Boolean.TRUE.equals(permission.getShareSafety());
            case "EMERGENCY" -> Boolean.TRUE.equals(permission.getShareEmergency());
            case "ACCESSIBILITY" -> Boolean.TRUE.equals(permission.getShareAccessibility());
            case "ROUTINE" -> Boolean.TRUE.equals(permission.getShareRoutine());
            case "MEDICINES" -> Boolean.TRUE.equals(permission.getShareMedicines()) && isInProgress(bookingStatus);
            case "KEYS" -> Boolean.TRUE.equals(permission.getShareKeys()) && isInProgress(bookingStatus);
            default -> false;
        };

        return visible ? Optional.of(category) : Optional.empty();
    }

    private String normalizeLayer(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        String value = normalize(raw);
        return switch (value) {
            case "SEGURIDAD", "SAFETY" -> "SAFETY";
            case "EMERGENCIA", "EMERGENCY" -> "EMERGENCY";
            case "ACCESIBILIDAD", "ACCESSIBILITY", "ACCESS" -> "ACCESSIBILITY";
            case "RUTINA", "ROUTINE" -> "ROUTINE";
            case "MEDICINAS", "MEDICAMENTOS", "MEDICINES", "MEDICINE" -> "MEDICINES";
            case "LLAVES", "KEYS", "KEY" -> "KEYS";
            default -> null;
        };
    }

    private String toDisplayCategory(String category) {
        return switch (category) {
            case "SAFETY" -> "Seguridad";
            case "EMERGENCY" -> "Emergencia";
            case "ACCESSIBILITY" -> "Accesibilidad";
            case "ROUTINE" -> "Rutina";
            case "MEDICINES" -> "Medicamentos";
            case "KEYS" -> "Llaves";
            default -> category;
        };
    }

    private boolean isPending(String status) {
        return "PENDIENTE".equals(status) || "SOLICITADA".equals(status);
    }

    private boolean isInProgress(String status) {
        return "EN_CURSO".equals(status);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }
}
