package com.cenfotec.backendcodesprint.logic.Review.Mapper;

import com.cenfotec.backendcodesprint.logic.Model.*;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ClientProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.SeniorProfileRepository;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.CreateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.UpdateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Response.ReviewResponse;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Repository.ServiceBookingRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "CR"));

    @Autowired
    protected ProviderProfileRepository providerRepo;
    @Autowired
    protected ClientProfileRepository clientRepo;
    @Autowired
    protected SeniorProfileRepository seniorRepo;
    @Autowired
    protected ServiceBookingRepository bookingRepo;

    // ── Create DTO → Entity ─────────────────────────────────────
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerProfile", ignore = true)
    @Mapping(target = "serviceBooking", source = "serviceBookingId")
    @Mapping(target = "clientProfile", source = "clientProfileId")
    @Mapping(target = "seniorProfile", source = "seniorProfileId")
    @Mapping(target = "totalReview", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    public abstract Review toEntity(CreateReview dto);

    // ── Update DTO → Entity ─────────────────────────────────────
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "providerProfile", ignore = true)
    @Mapping(target = "serviceBooking", ignore = true)
    @Mapping(target = "clientProfile", ignore = true)
    @Mapping(target = "seniorProfile", ignore = true)
    @Mapping(target = "totalReview", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    public abstract void updateEntity(@MappingTarget Review entity, UpdateReview dto);

    // ── Entity → Response DTO ───────────────────────────────────
    @Mapping(target = "providerProfileId", source = "providerProfile.id")
    @Mapping(target = "serviceBookingId", source = "serviceBooking.id")
    @Mapping(target = "author", expression = "java(resolveAuthor(review))")
    @Mapping(target = "createdAt", expression = "java(formatDate(review))")
    public abstract ReviewResponse toResponse(Review review);

    // ── Resolvers ───────────────────────────────────────────────
    protected ServiceBooking mapBooking(Long id) {
        if (id == null) return null;
        return bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
    }

    protected ClientProfile mapClient(Long id) {
        if (id == null) return null;
        return clientRepo.findById(id).orElse(null);
    }

    protected SeniorProfile mapSenior(Long id) {
        if (id == null) return null;
        return seniorRepo.findById(id).orElse(null);
    }

    protected String resolveAuthor(Review r) {
        if (r.getClientProfile() != null) {
            return r.getClientProfile().getUser().getUserName()
                    + " " + r.getClientProfile().getUser().getLastName();
        } else if (r.getSeniorProfile() != null) {
            return r.getSeniorProfile().getUser().getUserName()
                    + " " + r.getSeniorProfile().getUser().getLastName();
        }
        return "Anónimo";
    }

    protected String formatDate(Review r) {
        return r.getCreated() != null ? r.getCreated().format(DATE_FMT) : "";
    }
}