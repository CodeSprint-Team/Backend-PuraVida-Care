package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ChecklistCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistCategoryRepository extends JpaRepository<ChecklistCategory, Long> {
    List<ChecklistCategory> findByIsActiveTrueOrderByOrderIndexAsc();
    Optional<ChecklistCategory> findByName(String name);
}
