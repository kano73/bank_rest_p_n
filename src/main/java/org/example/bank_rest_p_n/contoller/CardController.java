package org.example.bank_rest_p_n.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.details.MyUserDetails;
import org.example.bank_rest_p_n.model.dto.CardNumberDTO;
import org.example.bank_rest_p_n.model.dto.CardResponseDTO;
import org.example.bank_rest_p_n.model.dto.TransactionCardRequestDTO;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.service.CardService;
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

    private final CardService cardService;

    @PostMapping("/new_card")
    public MyCard newCard(@AuthenticationPrincipal MyUserDetails userDetails) {
        return cardService.createCard(userDetails.myUser().getId());
    }

    @GetMapping("/my_cards")
    public List<CardResponseDTO> myAccounts(@AuthenticationPrincipal MyUserDetails userDetails,
                                            @RequestParam("page") int page) {
        return cardService.findUsersCardsById(userDetails.myUser().getId(), page);
    }

    @GetMapping("/balance")
    public BigDecimal balance(@AuthenticationPrincipal MyUserDetails userDetails,
                              @RequestBody CardNumberDTO requestDTO) {
        return cardService.getBalanceOfCard(userDetails.myUser().getId(), requestDTO.getCardNumber());
    }

    @PutMapping("/transaction")
    public Boolean updateCardInfo(@AuthenticationPrincipal MyUserDetails userDetails,
                                  @RequestBody @Valid TransactionCardRequestDTO requestDTO){
        return cardService.transaction(userDetails.myUser(), requestDTO);
    }
}
