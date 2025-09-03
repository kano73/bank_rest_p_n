package org.example.bank_rest_p_n.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.exception.IllegalOperation;
import org.example.bank_rest_p_n.model.dto.*;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.model.enumClass.CardStatus;
import org.example.bank_rest_p_n.repository.CardRepository;
import org.example.bank_rest_p_n.repository.specification.CardSpecifications;
import org.example.bank_rest_p_n.service.api.CardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * CardServiceImpl — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardGeneratorImpl cardGeneratorImpl;

    @Value("${page.size.product:10}")
    private Integer PAGE_SIZE;

    @Transactional()
    public MyCard createCard(String userId) {
        MyCard newCard = cardGeneratorImpl.generateCard(userId);

        return cardRepository.save(newCard);
    }

    public List<CardResponseDTO> findUsersCardsById(String userId, int pageNumber) {
        return cardRepository.findAllByOwner_Id(userId, PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent()
                .stream()
                .map(CardResponseDTO::new).toList();
    }

    @Transactional
    public Boolean transaction(MyUser myUser, @Valid TransactionCardRequestDTO requestDTO) {
        try {

            MyCard fromCard = cardRepository.findByNumber(requestDTO.getFromCard());
            MyCard toCard = cardRepository.findByNumber(requestDTO.getToCard());

            if((fromCard == null || toCard == null) ||
                    (!fromCard.getOwner().getId().equals(myUser.getId()) ||
                            !toCard.getOwner().getId().equals(myUser.getId())) ) {
                throw new IllegalOperation("Illegal operation: you are not owner of card/cards");
            }

            if( !fromCard.getStatus().equals(CardStatus.ACTIVE) || !toCard.getStatus().equals(CardStatus.ACTIVE)) {
                throw new IllegalOperation("Illegal operation: card/cards is blocked");
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

    public Boolean updateCardStatus(UpdateStateDTO requestDTO) {
        MyCard card = cardRepository.findByNumber(requestDTO.getCardNumber());
        card.setStatus(requestDTO.getStatus());
        cardRepository.save(card);
        return true;
    }

    public List<MyCard> findCardsByFilter(@NotNull FilterCardDTO filterCardDTO, int pageNumber) {
        return cardRepository.findAll(CardSpecifications.filter(filterCardDTO),PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();
    }

    public Boolean deleteCard(@Valid DeleteCardDTO requestDTO) {
        return cardRepository.deleteByNumber(requestDTO.getCardNumber());
    }

    public BigDecimal getBalanceOfCard(String id, String cardNumber) {
        MyCard byNumber = cardRepository.findByNumber(cardNumber);
        if(!byNumber.getOwner().getId().equals(id)) {
            throw new IllegalOperation("Illegal operation: you are not owner of card");
        }
        return byNumber.getBalance();
    }
}
