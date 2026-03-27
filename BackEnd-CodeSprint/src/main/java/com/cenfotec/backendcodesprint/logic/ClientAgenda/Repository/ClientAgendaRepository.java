package com.cenfotec.backendcodesprint.logic.ClientAgenda.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientAgendaRepository extends JpaRepository<ServiceBooking, Long> {

    // Lista completa de citas para un clientProfile
    @EntityGraph(attributePaths = {
            "careService",
            "careService.serviceCategory",
            "careService.providerProfile",
            "careService.providerProfile.user",
            "careService.providerProfile.providerType",
            "clientProfile",
            "clientProfile.user",
            "seniorProfile",
            "seniorProfile.user"
    })
    List<ServiceBooking> findByClientProfile_IdOrderByScheduledAtDesc(Long clientProfileId);

    // Detalle de una cita — verifica que pertenezca al cliente
    @EntityGraph(attributePaths = {
            "careService",
            "careService.serviceCategory",
            "careService.providerProfile",
            "careService.providerProfile.user",
            "careService.providerProfile.providerType",
            "clientProfile",
            "clientProfile.user",
            "seniorProfile",
            "seniorProfile.user"
    })
    Optional<ServiceBooking> findByIdAndClientProfile_Id(Long bookingId, Long clientProfileId);
}