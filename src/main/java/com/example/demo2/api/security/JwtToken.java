/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.security;

import com.auth0.jwt.interfaces.Claim;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author suporte_vi
 *
 */
@Component
public class JwtToken {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_ROLE = "role";
    static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    /**
     * Obtem o username(email) contido no token JWT
     *
     * @param token
     * @return String
     */
    public String getUsernameFromToken(String token) {

        String username;
        try {
            Claim claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception ex) {
            username = null;

        }
        return username;
    }

    /**
     * Retorna data de expiração
     *
     * @param token
     * @return String
     */
    public Date getExpirationDateFromToken(String token) {

        Date expiration;
        try {
            Claim claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception ex) {
            expiration = null;

        }
        return expiration;
    }

    /**
     * Cria novo Token
     *
     * @param token
     * @return String
     */
    public String refreshToken(String token) {

        String refreshedToken;
        try {
            Claim claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = gerarToken(claims);
        } catch (Exception ex) {
            refreshedToken = null;

        }
        return refreshedToken;
    }

    /**
     * Verifica e retorna se um token JWT é valido
     *
     * @param token
     * @return boolean
     */
    public boolean tokenValido(String token) {
        return !tokenExpirado(token);
    }

    /**
     * Retorna um novo token JWT com base nos dados do usuario
     *
     * @param userDetails
     * @return String
     */
    public String obterToken(UserDetails userDatails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
        claims.put(CLAIM_KEY_CREATED, new Date());

        return gerarToken(claims);
    }

}

