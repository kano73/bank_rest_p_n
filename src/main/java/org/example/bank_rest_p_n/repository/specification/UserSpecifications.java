package org.example.bank_rest_p_n.repository.specification;

import org.example.bank_rest_p_n.model.dto.FilterUserDTO;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<MyUser> filter(FilterUserDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null && !filter.getId().isBlank()) {
                predicates.add(cb.like(root.get("id"), "%" + filter.getId() + "%"));
            }

            if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
            }

            if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
            }

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
            }

            if (filter.getRole() != null) {
                predicates.add(cb.equal(root.get("role"), filter.getRole()));
            }

            if (filter.getIsBlocked() != null) {
                predicates.add(cb.equal(root.get("isBlocked"), filter.getIsBlocked()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
