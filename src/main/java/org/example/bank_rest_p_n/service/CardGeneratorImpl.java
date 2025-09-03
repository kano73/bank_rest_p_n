package org.example.bank_rest_p_n.service;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.model.enumClass.CardStatus;
import org.example.bank_rest_p_n.repository.CardRepository;
import org.example.bank_rest_p_n.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CardGeneratorImpl — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Service
@RequiredArgsConstructor
public class CardGeneratorImpl implements CardGenerator {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public MyCard generateCard(String ownerId) {
        MyUser user = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String cardNumber;
        do {
            cardNumber = CardNumberGenerator.generateCardNumber();
        } while (cardRepository.existsByNumber(cardNumber));

        MyCard card = new MyCard();
        card.setNumber(cardNumber);
        card.setOwner(user);
        card.setExpiryDate(LocalDate.now().plusYears(4));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);

        return cardRepository.save(card);
    }
}
