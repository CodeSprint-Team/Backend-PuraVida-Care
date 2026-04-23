package com.cenfotec.backendcodesprint.logic.FilteredHome.Repository;

import com.cenfotec.backendcodesprint.logic.Model.HomeBookingPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilteredHomeRepository extends JpaRepository<HomeBookingPermission, Long> {

    List<HomeBookingPermission> findByServiceBooking_Id(Long bookingId);

    Optional<HomeBookingPermission> findFirstByServiceBooking_IdOrderByIdDesc(Long bookingId);
}
