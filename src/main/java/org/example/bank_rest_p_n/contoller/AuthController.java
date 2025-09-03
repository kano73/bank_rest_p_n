package org.example.bank_rest_p_n.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bank_rest_p_n.model.dto.AuthRequestDTO;
import org.example.bank_rest_p_n.service.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequestDTO request) {
        return authServiceImpl.loginProcess(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return authServiceImpl.logout();
    }
}
