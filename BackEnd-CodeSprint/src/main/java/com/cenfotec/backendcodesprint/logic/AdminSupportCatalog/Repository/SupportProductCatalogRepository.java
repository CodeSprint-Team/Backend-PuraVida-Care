package com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Repository;

import com.cenfotec.backendcodesprint.logic.Model.SupportProductCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportProductCatalogRepository extends JpaRepository<SupportProductCatalog, Long> {

    List<SupportProductCatalog> findByActiveTrue();

    List<SupportProductCatalog> findByCategory(String category);
}
