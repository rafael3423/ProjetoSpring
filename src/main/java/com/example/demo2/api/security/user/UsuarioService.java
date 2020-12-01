/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.security.user;

import com.example.demo2.api.utils.SenhaUtils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author note
 */
@Service
 public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

     public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
    }

     public void cadastrarUsuario(Usuario usuario){
         usuario.setSenha(SenhaUtils.gerarBCrypt(usuario.getSenha()));
         
         if(usuario.getEmail().contains("admin")){
             usuario.setPerfil(PerfilEnum.ROLE_ADMIN);
         }else{
             usuario.setPerfil(PerfilEnum.ROLE_USER);
         }
         usuarioRepository.save(usuario);
     }
     
}
