package org.example.bank_rest_p_n.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.exception.IllegalOperation;
import org.example.bank_rest_p_n.exception.NoDataFoundException;
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
import org.springframework.transaction.annotation.Isolation;
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public CardResponseDTO createCard(String userId) {
        MyCard newCard = cardGeneratorImpl.generateCard(userId);

        cardRepository.save(newCard);

        return new CardResponseDTO(newCard);
    }

    @Override
    public List<CardResponseDTO> findUsersCardsById(String userId, int pageNumber) {
        return cardRepository.findAllByOwner_Id(userId, PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent()
                .stream()
                .map(CardResponseDTO::new).toList();
    }

    @Transactional
    @Override
    public Boolean transaction(MyUser myUser, @Valid TransactionCardRequestDTO requestDTO) {
        MyCard fromCard = cardRepository.findById(requestDTO.getFromCardId())
                .orElseThrow(()-> new NoDataFoundException("No card found"));
        MyCard toCard = cardRepository.findById(requestDTO.getToCardId())
                .orElseThrow(()-> new NoDataFoundException("No card found"));

        if( !fromCard.getStatus().equals(CardStatus.ACTIVE) || !toCard.getStatus().equals(CardStatus.ACTIVE)) {
            throw new IllegalOperation("Illegal operation: card/cards is blocked");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(requestDTO.getAmount()));
        toCard.setBalance(toCard.getBalance().add(requestDTO.getAmount()));
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        return true;
    }

    @Transactional
    @Override
    public Boolean updateCardStatus(UpdateStateDTO requestDTO) {
        MyCard card = cardRepository.findById(requestDTO.getCardId())
                .orElseThrow(()-> new NoDataFoundException("No card found"));
        card.setStatus(requestDTO.getStatus());
        cardRepository.save(card);
        return true;
    }

    @Override
    public List<MyCard> findCardsByFilter(@NotNull FilterCardDTO filterCardDTO, int pageNumber) {
        return cardRepository.findAll(CardSpecifications.filter(filterCardDTO),PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Boolean deleteCard(@Valid DeleteCardDTO requestDTO) {
        return cardRepository.deleteByNumber(requestDTO.getCardId());
    }

    @Override
    public BigDecimal getBalanceOfCard(String id, String cardId) {
        MyCard byNumber = cardRepository.findById(cardId)
                .orElseThrow(()-> new NoDataFoundException("No card found"));
        if(!byNumber.getOwner().getId().equals(id)) {
            throw new IllegalOperation("Illegal operation: you are not owner of card");
        }
        return byNumber.getBalance();
    }

    @Transactional
    @Override
    public Boolean topUp(TopUpDTO requestDTO) {
        MyCard toCard = cardRepository.findById(requestDTO.getCardId()).orElseThrow(
                ()-> new NoDataFoundException("Card does not exists.")
        );

        if(!toCard.getStatus().equals(CardStatus.ACTIVE)) {
            throw new IllegalOperation("Illegal operation: card/cards is blocked");
        }

        toCard.setBalance(toCard.getBalance().add(requestDTO.getAmount()));
        cardRepository.save(toCard);
        return true;
    }
}
