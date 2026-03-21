package com.cenfotec.backendcodesprint.logic.ProviderSearch.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Mapper.CostaRicaProvinceMapper;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProviderSpecification {

    public static Specification<ProviderProfile> build(ProviderSearchDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Solo proveedores activos
            predicates.add(cb.equal(root.get("providerState"), "active"));

            // ── Nombre ────────────────────────────────────────────────
            if (filter.getName() != null && !filter.getName().isBlank()) {
                String pattern = "%" + filter.getName().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("user").get("userName")), pattern),
                        cb.like(cb.lower(root.get("user").get("lastName")), pattern)
                ));
            }

            if (filter.getZone() != null && !filter.getZone().isBlank()) {
                if (CostaRicaProvinceMapper.isProvince(filter.getZone())) {
                    List<String> cantons = CostaRicaProvinceMapper
                            .getCantonsForProvince(filter.getZone());

                    List<Predicate> zonePredicates = new ArrayList<>();
                    for (String canton : cantons) {
                        zonePredicates.add(cb.like(
                                cb.lower(root.get("zone")),
                                "%" + canton.toLowerCase() + "%"
                        ));
                    }
                    zonePredicates.add(cb.like(
                            cb.lower(root.get("zone")),
                            "%" + filter.getZone().toLowerCase() + "%"
                    ));
                    predicates.add(cb.or(zonePredicates.toArray(new Predicate[0])));
                } else {
                    predicates.add(cb.like(
                            cb.lower(root.get("zone")),
                            "%" + filter.getZone().toLowerCase() + "%"
                    ));
                }
            }

            // ── Rating mínimo ─────────────────────────────────────────
            if (filter.getMinRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("averageRating"), filter.getMinRating()));
            }

            // ── Solo verificados ──────────────────────────────────────
            if (Boolean.TRUE.equals(filter.getVerifiedOnly())) {
                predicates.add(cb.isTrue(root.get("verified")));
            }

            // ── Precio ────────────────────────────────────────────────
            if (filter.getMinPrice() != null || filter.getMaxPrice() != null) {
                Join<Object, Object> services = root.join("careServices", JoinType.LEFT);
                predicates.add(cb.equal(services.get("publicationState"), "published"));
                if (filter.getMinPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(
                            services.get("basePrice"), filter.getMinPrice()));
                }
                if (filter.getMaxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(
                            services.get("basePrice"), filter.getMaxPrice()));
                }
                query.distinct(true);
            }

            // ── Categoría ─────────────────────────────────────────────
            if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
                Join<Object, Object> services = root.join("careServices", JoinType.LEFT);
                Join<Object, Object> category = services.join("serviceCategory", JoinType.LEFT);
                predicates.add(cb.like(
                        cb.lower(category.get("categoryName")),
                        "%" + filter.getCategory().toLowerCase() + "%"
                ));
                predicates.add(cb.equal(services.get("publicationState"), "published"));
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}