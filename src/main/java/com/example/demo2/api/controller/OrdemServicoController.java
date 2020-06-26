/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.controller;

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
    public Ordemservico criar(@Valid @RequestBody Ordemservico ordemServico){
        
        return gestaoOrdemServico.criar(ordemServico);
    }
    
    @GetMapping()
    public List<Ordemservico> listar(){
       
     return ordemServicoRepository.findAll();
    }
    
    @GetMapping("/{ordemservicoId}")
    public ResponseEntity<Ordemservico> buscar(@PathVariable Long ordemservicoId){
        
        Optional<Ordemservico> ordemServico = ordemServicoRepository.findById(ordemservicoId);
        
        if(!ordemServico.isEmpty()){
            return ResponseEntity.ok(ordemServico.get());
        }
        
        return ResponseEntity.notFound().build();
    }
}














