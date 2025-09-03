package org.example.bank_rest_p_n.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.exception.IllegalOperation;
import org.example.bank_rest_p_n.model.dto.TransactionCardRequestDTO;
import org.example.bank_rest_p_n.model.dto.UpdateStateDTO;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.repository.CardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CardService — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardGenerator cardGenerator;

    @Value("${page.size.product:10}")
    private Integer PAGE_SIZE;

    @Transactional()
    public MyCard createCard(String userId) {
        MyCard newCard = cardGenerator.generateCard(userId);

        return cardRepository.save(newCard);
    }

    public List<MyCard> findUsersCards(String userId, int pageNumber) {
        return cardRepository.findAllByOwner_Id(userId, PageRequest.of(pageNumber, PAGE_SIZE));
    }

    @Transactional
    public Boolean transaction(MyUser myUser, @Valid TransactionCardRequestDTO requestDTO) {
        try {

            MyCard fromCard = cardRepository.findByNumber(requestDTO.getFromCard());
            MyCard toCard = cardRepository.findByNumber(requestDTO.getToCard());

            if((fromCard == null || toCard == null) ||
                    (!fromCard.getOwner().getId().equals(myUser.getId()) ||
                            !toCard.getOwner().getId().equals(myUser.getId())) ) {
                throw new IllegalOperation("Illegal operation");
            }

            fromCard.setBalance(fromCard.getBalance().subtract(requestDTO.getAmount()));
            toCard.setBalance(toCard.getBalance().add(requestDTO.getAmount()));
            cardRepository.save(fromCard);
            cardRepository.save(toCard);
            return true;

        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Transaction conflict, try again", e);
        }
    }

    public Boolean updateCardState(UpdateStateDTO requestDTO) {
        MyCard card = cardRepository.findByNumber(requestDTO.getCardNumber());
        card.setStatus(requestDTO.getStatus());
        cardRepository.save(card);
        return true;
    }
}
