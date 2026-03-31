package com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    List<ServiceCategory> findByCategoryState(String categoryState);
}
