package org.example.bank_rest_p_n.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.dto.DeleteCardDTO;
import org.example.bank_rest_p_n.model.dto.CreateCardRequestDTO;
import org.example.bank_rest_p_n.model.dto.FilterCardDTO;
import org.example.bank_rest_p_n.model.dto.UpdateStateDTO;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminCardPanelController — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCardPanelController {

    private final CardService cardService;

    @PostMapping("/new_card_for_user")
    public MyCard newCardForUser(@RequestBody CreateCardRequestDTO requestDTO) {
        return cardService.createCard(requestDTO.getUserId());
    }

    @GetMapping("/users_cards")
    public List<MyCard> myAccounts(@RequestBody @NotNull FilterCardDTO filterCardDTO,
                                   @RequestParam("pageNumber") int pageNumber) {
        return cardService.findCardsByFilter(filterCardDTO, pageNumber);
    }

    @PutMapping("/change_card_status")
    public Boolean changeCardStatus(@RequestBody UpdateStateDTO requestDTO) {
        return cardService.updateCardStatus(requestDTO);
    }

    @DeleteMapping("/delete_card")
    public Boolean deleteCard(@RequestBody @Valid DeleteCardDTO requestDTO){
        return cardService.deleteCard(requestDTO);
    }

}
