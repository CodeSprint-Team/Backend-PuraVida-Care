package com.cenfotec.backendcodesprint.logic.Review.Repository;

import com.cenfotec.backendcodesprint.logic.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProviderProfile_Id(Long providerProfileId);
    List<Review> findByClientProfile_Id(Long clientProfileId);
    List<Review> findBySeniorProfile_Id(Long seniorProfileId);
    boolean existsByServiceBooking_IdAndClientProfile_Id(Long bookingId, Long clientProfileId);
    boolean existsByServiceBooking_IdAndSeniorProfile_Id(Long bookingId, Long seniorProfileId);
}