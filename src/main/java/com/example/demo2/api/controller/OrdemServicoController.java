/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.api.controller;

import com.example.demo2.domain.dto.ComentarioDTO;
import com.example.demo2.domain.dto.OrdemservicoDTO;
import com.example.demo2.domain.exception.NegocioException;
import com.example.demo2.domain.model.Comentario;
import com.example.demo2.domain.model.Ordemservico;
import com.example.demo2.domain.repository.OrdemServicoRepository;
import com.example.demo2.domain.service.GestaoOrdemServico;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*") // permite que qualquer dominio tenha acesso aos dados

public class OrdemServicoController {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private GestaoOrdemServico gestaoOrdemServico;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemservicoDTO criar(@Valid @RequestBody Ordemservico ordemServico) {

        return gestaoOrdemServico.criar(ordemServico);
    }

    @GetMapping()
    public List<OrdemservicoDTO> listar() {

        List<Ordemservico> ordemservicos = ordemServicoRepository.findAll();
        List<OrdemservicoDTO> ordemservicosDTO = new ArrayList();

        ordemservicos.forEach((os) -> {
            OrdemservicoDTO ordemservicoDTO = modelMapper.map(os, OrdemservicoDTO.class);
            ordemservicosDTO.add(ordemservicoDTO);
        });

        return ordemservicosDTO;
    }

    @GetMapping("/{ordemservicoId}")
    public OrdemservicoDTO buscar(@PathVariable Long ordemservicoId) {

        Ordemservico ordemServico = ordemServicoRepository.findById(ordemservicoId).orElseThrow(() -> new NegocioException("Ordem de servico não encontrada"));

        return modelMapper.map(ordemServico, OrdemservicoDTO.class);
    }

    @PostMapping("/{ordemservicoId}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public ComentarioDTO comentar(@PathVariable Long ordemservicoId, @Valid @RequestBody Comentario comentarioInput) {

        return gestaoOrdemServico.adicionarComentario(ordemservicoId, comentarioInput.getDescricao());
    }

    @GetMapping("/{ordemservicoId}/comentarios")
    public List<Comentario> listarComentarios(@PathVariable Long ordemservicoId) {
        Ordemservico ordemServico = ordemServicoRepository.findById(ordemservicoId)
                .orElseThrow(() -> new NegocioException("Ordem de servico não existe."));

        

        return modelMapper.map(ordemServico.getComentarios(), OrdemservicoDTO.class);
    }

    @PutMapping("/{ordemservicoId}/finalizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void comentar(@PathVariable Long ordemservicoId) {

        gestaoOrdemServico.finalizar(ordemservicoId);

    }
}
