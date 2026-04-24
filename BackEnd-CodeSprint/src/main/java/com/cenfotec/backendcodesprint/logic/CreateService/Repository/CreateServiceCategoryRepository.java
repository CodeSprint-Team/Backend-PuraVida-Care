package com.cenfotec.backendcodesprint.logic.CreateService.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
}
