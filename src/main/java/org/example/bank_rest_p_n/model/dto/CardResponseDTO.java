package org.example.bank_rest_p_n.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.model.enumClass.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CardResponseDTO — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Getter
@Setter
public class CardResponseDTO {
    private String id;
    private String maskedNumber;
    private LocalDate expiryDate;
    private CardStatus status;
    private BigDecimal balance;

    public CardResponseDTO(MyCard card) {
        this.id = card.getId();
        this.maskedNumber = card.getNumber().length() > 5? maskNumber(card.getNumber()) : "****";
        this.expiryDate = card.getExpiryDate();
        this.status = card.getStatus();
        this.balance = card.getBalance();
    }

    private String maskNumber(String number) {
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}
