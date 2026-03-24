package com.cenfotec.backendcodesprint.logic.ProviderSearch.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderSearchRepository
        extends JpaRepository<ProviderProfile, Long>,
        JpaSpecificationExecutor<ProviderProfile> {

    @Override
    @EntityGraph(attributePaths = {
            "user",
            "providerType",
            "careServices",
            "careServices.serviceCategory"
    })
    List<ProviderProfile> findAll(Specification<ProviderProfile> spec);
}
