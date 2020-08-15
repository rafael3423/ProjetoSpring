/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.security;

import com.example.demo2.api.security.jwt.JwtAuthenticationDto;
import com.example.demo2.api.security.jwt.JwtToken;
import com.example.demo2.api.security.jwt.TokenDto;
import com.example.demo2.domain.exception.NegocioException;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author note
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";

    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping()
    public TokenDto gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto authenticationDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getSenha()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
            String token = jwtToken.obterToken(userDetails);
            return new TokenDto(token);

        } catch (Exception ex) {
            throw new NegocioException("Erro validadndo lançamento.");
        }

    }

    @PostMapping("/refresh")
    public TokenDto gerarRefreshTokenJwt(HttpServletRequest request) {
        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

        if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }

        if (!token.isPresent()) {
            throw new NegocioException("Token não informado.");
        } else if (!jwtToken.tokenValido(token.get())) {
            throw new NegocioException("Token inválido ou expirado!.");
        }

        String refreshedToken = jwtToken.refreshToken(token.get());
        
        return new TokenDto(refreshedToken);
    }
}








