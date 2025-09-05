package org.example.bank_rest_p_n.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * TopUpDTO — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 04/09/2025
 */

@Getter
@Setter
public class TopUpDTO {

    private String cardId;

    private BigDecimal amount;
}
