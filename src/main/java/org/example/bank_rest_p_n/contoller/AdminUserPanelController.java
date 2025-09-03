package org.example.bank_rest_p_n.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.dto.AdminUpdateUserDTO;
import org.example.bank_rest_p_n.model.dto.FilterUserDTO;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminUserPanelController — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@RestController
@RequiredArgsConstructor
public class AdminUserPanelController {
    private final UserService userService;

    @PutMapping("/block_user")
    private Boolean blockUser(@RequestBody @Valid AdminUpdateUserDTO requestDTO) {
        return userService.blockUser(requestDTO);
    }

    @DeleteMapping("/delete_user")
    private Boolean deleteUser(@RequestBody @Valid AdminUpdateUserDTO requestDTO) {
        return userService.deleteUser(requestDTO);
    }

    @GetMapping("/get_users")
    private List<MyUser> getUsers(@RequestBody FilterUserDTO requestDTO, @RequestParam("pageNumber") int pageNumber) {
        return userService.getUsersByFilter(requestDTO, pageNumber);
    }
}
