package org.example.bank_rest_p_n.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bank_rest_p_n.exception.NoDataFoundException;
import org.example.bank_rest_p_n.model.details.MyUserDetails;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.model.enumClass.TokenType;
import org.example.bank_rest_p_n.repository.UserRepository;
import org.example.bank_rest_p_n.service.AuthServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtRefreshTokensFilter extends OncePerRequestFilter {

//    private static final Set<String> ALLOWED_PATHS = Set.of("/login", "/register", "/logout");

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository myUserRepository;
    private final AuthServiceImpl authServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
/*
        if (ALLOWED_PATHS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
*/

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = Arrays.stream(cookies).filter(item ->
                item.getName().equals(TokenType.accessToken.name()))
                .findFirst().map(Cookie::getValue).orElse(null);

        if (jwtTokenUtil.isTokenNotValid(token)) {
            final String refreshToken = Arrays.stream(cookies).filter(item ->
                    item.getName().equals("refreshToken"))
                    .findFirst().map(Cookie::getValue).orElse(null);

            if (jwtTokenUtil.isTokenNotValid(refreshToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            String userId = jwtTokenUtil.getUserIdFromToken(refreshToken);

            token = jwtTokenUtil.generateAccessToken(userId);

            ResponseCookie accessCookie = authServiceImpl.tokenToCookie(token, TokenType.accessToken);
            ResponseCookie refreshCookie =
                    authServiceImpl.tokenToCookie(jwtTokenUtil.generateRefreshToken(userId), TokenType.refreshToken);

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String userId = jwtTokenUtil.getUserIdFromToken(token);

            MyUser user = myUserRepository.findById(userId).orElseThrow(() ->
                    new NoDataFoundException("No user found with id " + userId));

            String role = user.getRole().name();

            Collection<SimpleGrantedAuthority> authorities =
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(new MyUserDetails(user), null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}