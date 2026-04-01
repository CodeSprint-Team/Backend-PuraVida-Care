package com.cenfotec.backendcodesprint.logic.ServiceBooking.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {

    @EntityGraph(attributePaths = {
            "careService.providerProfile",
            "clientProfile",
            "seniorProfile"
    })
    List<ServiceBooking> findAllByCareService_ProviderProfile_Id(Long providerProfileId);

    @EntityGraph(attributePaths = {
            "careService.providerProfile",
            "clientProfile",
            "seniorProfile"
    })
    List<ServiceBooking> findByCareService_ProviderProfile_IdAndBookingStatus(Long id, String bookingStatus);

    @EntityGraph(attributePaths = {"careService.providerProfile"})
    @Query("SELECT sb FROM ServiceBooking sb WHERE sb.id = :id")
    Optional<ServiceBooking> findByIdWithProvider(@Param("id") Long id);
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

    @EntityGraph(attributePaths = {
            "careService",
            "careService.providerProfile",
            "careService.providerProfile.user",
            "clientProfile",
            "seniorProfile"
    })
    List<ServiceBooking> findByClientProfile_IdAndBookingStatus(Long clientProfileId, String bookingStatus);

    @EntityGraph(attributePaths = {
            "careService",
            "careService.providerProfile",
            "careService.providerProfile.user",
            "clientProfile",
            "seniorProfile"
    })
    List<ServiceBooking> findBySeniorProfile_IdAndBookingStatus(Long seniorProfileId, String bookingStatus);
}


