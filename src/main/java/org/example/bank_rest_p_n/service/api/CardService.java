package org.example.bank_rest_p_n.service.api;

import org.example.bank_rest_p_n.model.dto.*;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.model.entity.MyUser;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    CardResponseDTO createCard(String userId);
    List<CardResponseDTO> findUsersCardsById(String userId, int pageNumber);
    Boolean transaction(MyUser myUser, TransactionCardRequestDTO requestDTO);
    Boolean updateCardStatus(UpdateStateDTO requestDTO);
    List<MyCard> findCardsByFilter(FilterCardDTO filterCardDTO, int pageNumber);
    Boolean deleteCard(DeleteCardDTO requestDTO);
    BigDecimal getBalanceOfCard(String id, String cardNumber);
    Boolean topUp(TopUpDTO requestDTO);
}
