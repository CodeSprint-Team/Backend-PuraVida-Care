package com.cenfotec.backendcodesprint.logic.Booking.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {

    // Carga careService → providerProfile, clientProfile → user, seniorProfile → user
    @EntityGraph(attributePaths = {
            "careService",
            "careService.providerProfile",
            "clientProfile",
            "clientProfile.user",
            "seniorProfile",
            "seniorProfile.user"
    })
    List<ServiceBooking> findByCareService_ProviderProfile_IdOrderByCreatedDesc(
            Long providerProfileId);

    @EntityGraph(attributePaths = {
            "careService",
            "careService.providerProfile",
            "clientProfile",
            "clientProfile.user",
            "seniorProfile",
            "seniorProfile.user"
    })
    List<ServiceBooking> findByCareService_ProviderProfile_IdAndBookingStatusOrderByCreatedDesc(
            Long providerProfileId,
            String bookingStatus);
}