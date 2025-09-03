package org.example.bank_rest_p_n.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.bank_rest_p_n.model.enumClass.CardStatus;

/**
 * UpdateStateDTO — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Getter
@Setter
public class UpdateStateDTO {
    @NotNull
    private CardStatus status;

    @NotNull
    private String cardNumber;
}
