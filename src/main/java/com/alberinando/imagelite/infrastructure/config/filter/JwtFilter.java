package com.alberinando.imagelite.infrastructure.config.filter;

import com.alberinando.imagelite.domain.entities.User;
import com.alberinando.imagelite.domain.services.UserServices;
import com.alberinando.imagelite.infrastructure.exceptions.InvalidTokenException;
import com.alberinando.imagelite.infrastructure.security.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final Jwt jwt;
    private final UserServices userServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = getToken(request);
        if (authorization != null) {
            try {
                String email = jwt.getEmailFromToken(authorization);
                User user = userServices.findUserByEmail(email);
                setUserAsAuthenticated(user);
            } catch (InvalidTokenException e) {
                log.error("Token inválido: {}",e.getMessage());
                throw new InvalidTokenException(e.getMessage());
            } catch (Exception e){
                log.error("Erro na validação do token: {}",e.getMessage());
                throw new InvalidTokenException(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/v1/users");
    }

    private void setUserAsAuthenticated(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            String[] authHeaderParts = authHeader.split(" ");
            if (authHeaderParts.length == 2){
                return authHeaderParts[1];
            }
        }
        return null;
    }
}
