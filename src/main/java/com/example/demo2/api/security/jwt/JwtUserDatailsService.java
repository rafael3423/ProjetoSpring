/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.security.jwt;

import com.example.demo2.api.security.user.Usuario;
import com.example.demo2.api.security.user.UsuarioService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author note
 */
@Service
public class JwtUserDatailsService implements UserDetailsService{
    
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Usuario> funcionario = usuarioService.buscarPorEmail(username);
        
        if(funcionario.isPresent()){
            return JwtUserFactory.create(funcionario.get());
        }
        
        
        throw new UsernameNotFoundException("Email não encontrado. "); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}


