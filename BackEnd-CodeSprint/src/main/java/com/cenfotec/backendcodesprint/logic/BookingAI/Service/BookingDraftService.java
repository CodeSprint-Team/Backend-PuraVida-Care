package com.cenfotec.backendcodesprint.logic.BookingAI.Service;

import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.*;
import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Repository.ProviderSearchRepository;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Repository.ProviderSpecification;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Repository.ServiceBookingRepository;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.ServiceBookingResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Mapper.ServiceBookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BookingDraftService {

    private final ServiceBookingRepository bookingRepository;
    private final ServiceBookingMapper bookingMapper;
    private final ProviderSearchRepository providerSearchRepository;

    private final Map<String, BookingDraftRequestDto> draftsCache = new ConcurrentHashMap<>();

    public BookingDraftResponseDto createDraft(BookingDraftRequestDto req, Long clientId) {

        if (isBlank(req.getOriginAddress()) || isBlank(req.getDestinationAddress())) {
            return error("INCOMPLETE_DATA", "Falta origen/destino");
        }

        LocalDateTime date = parseDate(req.getScheduledAt());
        if (date == null || date.isBefore(LocalDateTime.now())) {
            return error("INVALID_DATE", "Fecha inválida");
        }

        if (req.getZone() != null) {
            ProviderSearchDTO filter = new ProviderSearchDTO();
            filter.setCategory(req.getCategory());
            filter.setZone(req.getZone());

            long count = providerSearchRepository.count(
                    ProviderSpecification.build(filter)
            );

            if (count == 0) {
                return error("NO_PROVIDERS", "No hay proveedores en esa zona");
            }
        }

        String draftId = UUID.randomUUID().toString();
        draftsCache.put(draftId, req);

        return BookingDraftResponseDto.builder()
                .draftId(draftId)
                .category(req.getCategory())
                .scheduledAt(req.getScheduledAt())
                .originAddress(req.getOriginAddress())
                .destinationAddress(req.getDestinationAddress())
                .zone(req.getZone())
                .success(true)
                .build();
    }

    @Transactional
    public ServiceBookingResponseDto confirmDraft(String draftId, Long clientId) {

        BookingDraftRequestDto draft = draftsCache.get(draftId);
        if (draft == null) {
            throw new RuntimeException("Draft no existe");
        }

        ServiceBooking booking = new ServiceBooking();
        booking.setBookingStatus("PENDIENTE");

        ServiceBooking saved = bookingRepository.save(booking);

        draftsCache.remove(draftId);

        return bookingMapper.toResponse(saved);
    }

    private LocalDateTime parseDate(String text) {
        try {
            return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    private BookingDraftResponseDto error(String code, String msg) {
        return BookingDraftResponseDto.builder()
                .success(false)
                .errorCode(code)
                .errorMessage(msg)
                .build();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
