package org.example.bank_rest_p_n.contoller;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.details.MyUserDetails;
import org.example.bank_rest_p_n.model.dto.MyUserRegisterDTO;
import org.example.bank_rest_p_n.model.dto.MyUserUpdateDTO;
import org.example.bank_rest_p_n.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Boolean registerUser(@RequestBody MyUserRegisterDTO requestDTO) {
        return userService.register(requestDTO);
    }

    @PutMapping("/update_my_info")
    public Boolean updateMyInfo(@AuthenticationPrincipal MyUserDetails userDetails,
                                @RequestBody MyUserUpdateDTO requestDTO) {
        return userService.updateInfo(userDetails.myUser(), requestDTO);
    }
}
