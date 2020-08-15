/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.security.jwt;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author note
 */
public class JwtAuthenticationDto {
 
    @NotBlank(message="Email não pode estar vazio.")
    @Email
    private String email;
    
    @NotBlank(message = "Não pode ser uma senha vazia.")
    private String senha;

    public JwtAuthenticationDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}


