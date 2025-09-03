package org.example.bank_rest_p_n.repository;

import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * CardRepository — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

public interface CardRepository extends CrudRepository<MyCard, String> {

    boolean existsByNumber(String number);

    List<MyCard> findAllByOwner(MyUser owner, Pageable pageable);

    MyCard findByNumber(String number);

    List<MyCard> findAllByOwner_Id(String ownerId, Pageable pageable);
}
