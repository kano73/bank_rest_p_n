package org.example.bank_rest_p_n.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * TransactionCardRequestDTO — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Getter
@Setter
public class TransactionCardRequestDTO {
    @NotBlank
    private String fromCard;

    @NotBlank
    private String toCard;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
