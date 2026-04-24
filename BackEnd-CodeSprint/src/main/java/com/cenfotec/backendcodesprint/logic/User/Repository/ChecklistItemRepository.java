package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByIsActiveTrueOrderByOrderIndexAsc();
    List<ChecklistItem> findByCategoryAndIsActiveTrueOrderByOrderIndexAsc(String category);
    List<ChecklistItem> findByRequiredTrueAndIsActiveTrue();
}
