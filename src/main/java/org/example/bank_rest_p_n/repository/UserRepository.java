package org.example.bank_rest_p_n.repository;

import org.example.bank_rest_p_n.model.entity.MyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * UserRepository — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

public interface UserRepository extends CrudRepository<MyUser, String> {
    Optional<MyUser> findByEmailEqualsIgnoreCase(String email);
}
