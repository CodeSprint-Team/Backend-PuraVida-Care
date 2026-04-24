package com.cenfotec.backendcodesprint.logic.ClientAgenda.Mapper;

import com.cenfotec.backendcodesprint.logic.ClientAgenda.DTO.AgendaBookingResponseDTO;
import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import org.springframework.stereotype.Component;

@Component
public class ClientAgendaMapper {

    public AgendaBookingResponseDTO toDTO(ServiceBooking b) {
        AgendaBookingResponseDTO dto = new AgendaBookingResponseDTO();

        // ── Booking
        dto.setBookingId(b.getId());
        dto.setBookingStatus(b.getBookingStatus());
        dto.setScheduledAt(b.getScheduledAt());
        dto.setAgreedPrice(b.getAgreedPrice());
        dto.setAgreedPriceMode(b.getAgreedPriceMode());
        dto.setOriginLatitude(b.getOriginLatitude());
        dto.setOriginLongitude(b.getOriginLongitude());
        dto.setDestinationLatitude(b.getDestinationLatitude());
        dto.setDestinationLongitude(b.getDestinationLongitude());
        dto.setRejectionReason(b.getRejectionReason());
        dto.setCreatedAt(b.getCreated());
        dto.setPreviousScheduledAt(b.getPreviousScheduledAt());
        dto.setRescheduleReason(b.getRescheduleReason());
        dto.setCancellationReason(b.getCancellationReason());

        // ── Servicio
        dto.setServiceTitle(b.getCareService().getTitle());
        dto.setServiceDescription(b.getCareService().getServiceDescription());
        dto.setCategoryName(b.getCareService().getServiceCategory().getCategoryName());

        // ── Proveedor
        var pp = b.getCareService().getProviderProfile();
        dto.setProviderProfileId(pp.getId());
        dto.setProviderFullName(pp.getUser().getUserName() + " " + pp.getUser().getLastName());
        dto.setProviderTypeName(pp.getProviderType().getTypeName());
        dto.setProviderPhone(pp.getPhone());
        dto.setProviderZone(pp.getZone());
        dto.setProviderProfileImage(pp.getProfileImage());

        // ── Adulto mayor
        var sp = b.getSeniorProfile();
        if (sp != null) {
            dto.setSeniorProfileId(sp.getId());
            dto.setSeniorFullName(
                    sp.getUser() != null
                            ? sp.getUser().getUserName() + " " + sp.getUser().getLastName()
                            : "No asignado"
            );
            dto.setSeniorAddress(sp.getAddress() != null ? sp.getAddress() : "Sin dirección");
        } else {
            dto.setSeniorProfileId(null);
            dto.setSeniorFullName("No asignado");
            dto.setSeniorAddress("Sin dirección");
        }

        // ── Cliente
        var cp = b.getClientProfile();
        dto.setClientProfileId(cp.getId());
        dto.setClientFullName(cp.getUser().getUserName() + " " + cp.getUser().getLastName());

        return dto;
    }
}