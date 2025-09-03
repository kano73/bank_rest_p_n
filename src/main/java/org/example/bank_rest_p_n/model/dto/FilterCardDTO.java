package org.example.bank_rest_p_n.model.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.example.bank_rest_p_n.model.enumClass.CardStatus;

import java.math.BigDecimal;

/**
 * FilterCardDTO — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Getter
@Setter
public class FilterCardDTO {

    private String ownerId;

    private String cardId;

    private CardStatus status;

    private String ownerFirstName;

    private String ownerLastName;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;
}
