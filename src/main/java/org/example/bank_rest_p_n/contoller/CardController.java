package org.example.bank_rest_p_n.contoller;

import org.example.bank_rest_p_n.entity.MyCard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserController — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@RestController
public class UserController {

    @PostMapping("/new_card")
    public String newCard() {}

    @GetMapping("/my_cards")
    public List<MyCard> myAccounts() {}

    @PutMapping("/update_card_info")
    public Boolean updateCardInfo(){
        return true;
    }


}
