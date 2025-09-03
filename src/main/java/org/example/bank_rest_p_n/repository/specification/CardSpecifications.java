package org.example.bank_rest_p_n.repository.specification;

import org.example.bank_rest_p_n.model.dto.FilterCardDTO;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class CardSpecifications {

    public static Specification<MyCard> filter(FilterCardDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getOwnerId() != null && !filter.getOwnerId().isBlank()) {
                String pattern = "%" + filter.getOwnerId() + "%";
                predicates.add(cb.like(root.get("owner").get("id"), pattern));
            }

            if (filter.getCardId() != null && !filter.getCardId().isBlank()) {
                String pattern = "%" + filter.getCardId() + "%";
                predicates.add(cb.like(root.get("id"), pattern));
            }

            if (filter.getStatus() != null){
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getOwnerFirstName() != null && !filter.getOwnerFirstName().isBlank()) {
                String pattern = "%" + filter.getOwnerFirstName() + "%";
                predicates.add(cb.like(root.get("owner").get("firstName"), pattern));
            }

            if (filter.getOwnerLastName() != null && !filter.getOwnerLastName().isBlank()) {
                String pattern = "%" + filter.getOwnerLastName() + "%";
                predicates.add(cb.like(root.get("owner").get("lastName"), pattern));
            }

            if (filter.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("balance"), filter.getMinAmount()));
            }

            if (filter.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("balance"), filter.getMaxAmount()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
