package org.example.bank_rest_p_n.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.details.MyUserDetails;
import org.example.bank_rest_p_n.model.dto.CardIdDTO;
import org.example.bank_rest_p_n.model.dto.CardResponseDTO;
import org.example.bank_rest_p_n.model.dto.TopUpDTO;
import org.example.bank_rest_p_n.model.dto.TransactionCardRequestDTO;
import org.example.bank_rest_p_n.service.CardServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * CardController — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardServiceImpl cardServiceImpl;

    @PutMapping("/top_up")
    public Boolean topUp(@RequestBody @Valid TopUpDTO requestDTO) {
        return cardServiceImpl.topUp(requestDTO);
    }

    @PostMapping("/new_card")
    public CardResponseDTO newCard(@AuthenticationPrincipal MyUserDetails userDetails) {
        return cardServiceImpl.createCard(userDetails.myUser().getId());
    }

    @GetMapping("/my_cards")
    public List<CardResponseDTO> myAccounts(@AuthenticationPrincipal MyUserDetails userDetails,
                                            @RequestParam("page") int page) {
        return cardServiceImpl.findUsersCardsById(userDetails.myUser().getId(), page);
    }

    @GetMapping("/balance")
    public BigDecimal balance(@AuthenticationPrincipal MyUserDetails userDetails,
                              @RequestBody CardIdDTO requestDTO) {
        return cardServiceImpl.getBalanceOfCard(userDetails.myUser().getId(), requestDTO.getCardId());
    }

    @PutMapping("/transaction")
    public Boolean updateCardInfo(@AuthenticationPrincipal MyUserDetails userDetails,
                                  @RequestBody @Valid TransactionCardRequestDTO requestDTO){
        return cardServiceImpl.transaction(userDetails.myUser(), requestDTO);
    }
}
