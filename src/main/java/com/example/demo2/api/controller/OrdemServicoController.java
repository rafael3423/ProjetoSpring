/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.controller;

import com.example.demo2.domain.exception.NegocioException;
import com.example.demo2.domain.model.Comentario;
import com.example.demo2.domain.model.Ordemservico;
import com.example.demo2.domain.repository.OrdemServicoRepository;
import com.example.demo2.domain.service.GestaoOrdemServico;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author note
 */
@RestController
@RequestMapping("ordens-servico")
public class OrdemServicoController {

    @Autowired
    private GestaoOrdemServico gestaoOrdemServico;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Ordemservico criar(@Valid @RequestBody Ordemservico ordemServico) {

        return gestaoOrdemServico.criar(ordemServico);
    }

    @GetMapping()
    public List<Ordemservico> listar() {

        return ordemServicoRepository.findAll();
    }

    @GetMapping("/{ordemservicoId}")
    public ResponseEntity<Ordemservico> buscar(@PathVariable Long ordemservicoId) {

        Optional<Ordemservico> ordemServico = ordemServicoRepository.findById(ordemservicoId);

        if (!ordemServico.isEmpty()) {
            return ResponseEntity.ok(ordemServico.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{ordemservicoId}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public Comentario comentar(@PathVariable Long ordemservicoId, @Valid @RequestBody Comentario comentarioInput) {

        Comentario comentario = gestaoOrdemServico.adicionarComentario(ordemservicoId, comentarioInput.getDescricao());

        return comentario;
    }
    
    @GetMapping("/{ordemservicoId}/comentarios")
    public List<Comentario> listarComentarios(@PathVariable Long ordemservicoId){
        Ordemservico ordemservico = ordemServicoRepository.findById(ordemservicoId)
                .orElseThrow(()-> new NegocioException("Ordem de servico não existe."));
        
        ordemservico.getComentarios().forEach((os)->{
            os.setOrdemservico(null);
        });
        
        return ordemservico.getComentarios() ;
    }
    
    @PutMapping("/{ordemservicoId}/finalizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
       public void comentar(@PathVariable Long ordemservicoId) {

        gestaoOrdemServico.finalizar(ordemservicoId);

    }
}







