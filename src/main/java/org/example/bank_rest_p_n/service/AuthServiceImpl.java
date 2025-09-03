package org.example.bank_rest_p_n.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bank_rest_p_n.config.jwt.JwtTokenUtil;
import org.example.bank_rest_p_n.exception.ValidationFailedException;
import org.example.bank_rest_p_n.model.dto.AuthRequestDTO;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.model.enumClass.TokenType;
import org.example.bank_rest_p_n.service.api.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
@Getter
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserServiceImpl myUserServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${max.age.access:PT1H}")
    private Duration expirationTimeAccess;

    @Value("${max.age.refresh:P7D}")
    private Duration expirationTimeRefresh;

    public ResponseEntity<?> loginProcess(AuthRequestDTO request) {
        MyUser user = myUserServiceImpl.findUserByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        return generateTokensWithId(user.getId());
    }

    public ResponseEntity<?> generateTokensWithId(String id) {
        String token = jwtTokenUtil.generateAccessToken(id);
        String refreshToken = jwtTokenUtil.generateRefreshToken(id);

        return getResponseWithTokens(token, refreshToken);
    }

    public ResponseCookie tokenToCookie(String token, TokenType type) {
        if (type == TokenType.accessToken) {
            return ResponseCookie.from(type.toString(), token).httpOnly(true).path("/").maxAge(expirationTimeAccess).sameSite("Strict").build();
        } else if (type == TokenType.refreshToken) {
            return ResponseCookie.from(type.toString(), token).httpOnly(true).path("/").maxAge(expirationTimeRefresh).sameSite("Strict").build();
        } else {
            throw new ValidationFailedException("Unknown type");
        }
    }

    private ResponseEntity<?> getResponseWithTokens(String token, String newRefreshToken) {
        ResponseCookie accessCookie = tokenToCookie(token, TokenType.accessToken);

        ResponseCookie refreshCookie = tokenToCookie(newRefreshToken, TokenType.refreshToken);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString()).header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).body("Login successful");
    }

    public ResponseEntity<?> logout() {

        ResponseCookie accessCookie = tokenToCookie("", TokenType.accessToken);
        ResponseCookie refreshCookie = tokenToCookie("", TokenType.refreshToken);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString()).header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).body("Logged out");
    }
}
