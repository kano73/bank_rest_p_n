package org.example.bank_rest_p_n.contoller;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.dto.CreateCardRequestDTO;
import org.example.bank_rest_p_n.model.dto.TransactionCardRequestDTO;
import org.example.bank_rest_p_n.model.dto.UpdateStateDTO;
import org.example.bank_rest_p_n.model.dto.UsersIdDTO;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminController — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CardService cardService;

    @PostMapping("/new_card_for_user")
    public MyCard newCardForUser(@RequestBody CreateCardRequestDTO requestDTO) {
        return cardService.createCard(requestDTO.getUserId());
    }

    @GetMapping("/users_cards")
    public List<MyCard> myAccounts(@RequestBody UsersIdDTO userId,
                                   @RequestParam("page") int page) {
        return cardService.findUsersCards(userId.getId(), page);
    }

    @PutMapping("/change_card_state")
    public Boolean changeCardState(@RequestBody UpdateStateDTO requestDTO) {
        return cardService.updateCardState(requestDTO);
    }
}
