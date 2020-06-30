/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.domain.service;

import com.example.demo2.domain.exception.NegocioException;
import com.example.demo2.domain.model.Cliente;
import com.example.demo2.domain.model.Comentario;
import com.example.demo2.domain.model.Ordemservico;
import com.example.demo2.domain.model.StatusOrdemServico;
import com.example.demo2.domain.repository.ClienteRepository;
import com.example.demo2.domain.repository.ComentarioRepository;
import com.example.demo2.domain.repository.OrdemServicoRepository;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author note
 */
@Service
public class GestaoOrdemServico {

    @Autowired
    OrdemServicoRepository ordemServicoRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    ClienteRepository clienteRepository;

    public Ordemservico criar(Ordemservico ordemServico) {

        Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId()).orElseThrow(() -> new NegocioException("cliente não encontrado!."));

        ordemServico.setCliente(cliente);
        ordemServico.setStatus(StatusOrdemServico.ABERTA);
        ordemServico.setDataAbertura(OffsetDateTime.now());

        return ordemServicoRepository.save(ordemServico);
    }

    public Comentario adicionarComentario(Long ordemServicoId, String descricao) {

        Ordemservico ordemServico = ordemServicoRepository.findById(ordemServicoId).orElseThrow(() -> new NegocioException("Ordem de servico não encontrada"));

        Comentario comentario = new Comentario(null, descricao, ordemServico, OffsetDateTime.now());
        comentarioRepository.save(comentario);
        comentario.setOrdemservico(null);

        return comentario;
    }

    public void finalizar(Long ordemServicoId) {

        Ordemservico ordemServico = ordemServicoRepository.findById(ordemServicoId).orElseThrow(() -> new NegocioException("Ordem de servico não encontrada"));

        if(!StatusOrdemServico.ABERTA.equals(ordemServico.getStatus())){
            throw new NegocioException("Esta O.S não pode ser finalizada");
        }
        
        ordemServico.setStatus(StatusOrdemServico.FINALIZADA);
        ordemServico.setDataFinalizacao(OffsetDateTime.now());
        
        ordemServicoRepository.save(ordemServico);
        
    }

}

