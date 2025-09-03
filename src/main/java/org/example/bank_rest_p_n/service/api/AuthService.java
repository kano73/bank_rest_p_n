package org.example.bank_rest_p_n.service.api;

import org.example.bank_rest_p_n.model.dto.AuthRequestDTO;
import org.example.bank_rest_p_n.model.enumClass.TokenType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> loginProcess(AuthRequestDTO request);
    ResponseEntity<?> generateTokensWithId(String id);
    ResponseCookie tokenToCookie(String token, TokenType type);
    ResponseEntity<?> logout();
}
