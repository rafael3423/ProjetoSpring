/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.security;

import com.example.demo2.api.security.jwt.JwtAuthenticationDto;
import com.example.demo2.api.security.jwt.JwtToken;
import com.example.demo2.api.security.jwt.TokenDto;
import com.example.demo2.api.security.user.Usuario;
import com.example.demo2.api.security.user.UsuarioRepository;
import com.example.demo2.api.security.user.UsuarioService;
import com.example.demo2.api.utils.SenhaUtils;
import com.example.demo2.domain.exception.NegocioException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping()
    public TokenDto gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto authenticationDto) {

            Usuario usuario = usuarioRepository.findById(1L).orElseThrow(() -> new NegocioException("Usuario não encontrado."));
            usuario.setSenha(SenhaUtils.gerarBCrypt(authenticationDto.getSenha()));
            usuarioRepository.save(usuario);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getSenha()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
            String token = jwtToken.obterToken(userDetails);
            return new TokenDto(token);
    }

    @PostMapping("/cadastrarUsuario")
    public ResponseEntity<Void> criarUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario user = usuarioRepository.findByEmail(usuario.getEmail());
        if (user == null) {
            usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            throw new NegocioException("Usuario já existente.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/listarUsuarios")
    public List<Usuario> listarUsuarios() {
        
        return usuarioRepository.findAll();
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


