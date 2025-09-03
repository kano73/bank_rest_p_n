package org.example.bank_rest_p_n.repository;

import org.example.bank_rest_p_n.model.entity.MyCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * CardRepository — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

public interface CardRepository extends CrudRepository<MyCard, String>,JpaSpecificationExecutor<MyCard> {

    boolean existsByNumber(String number);

    @EntityGraph(value = "Card.withOwner")
    MyCard findByNumber(String number);

    Boolean deleteByNumber(String number);

    Page<MyCard> findAllByOwner_Id(String ownerId, Pageable pageable);
}
