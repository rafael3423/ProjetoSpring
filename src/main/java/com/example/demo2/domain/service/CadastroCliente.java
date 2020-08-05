/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.domain.service;

import com.example.demo2.domain.dto.ClienteDTO;
import com.example.demo2.domain.exception.NegocioException;
import com.example.demo2.domain.model.Cliente;
import com.example.demo2.domain.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CadastroCliente {
    
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteDTO salvar(Cliente cliente) {
        
        
        Cliente clienteExistente = clienteRepository.findByEmail(cliente.getEmail());

        if (clienteExistente != null && !clienteExistente.equals(cliente)) {
            throw new NegocioException("Email ja cadastrado!");
        }
        
                ClienteDTO clienteDTO = modelMapper.map(clienteRepository.save(cliente),ClienteDTO.class);

        
        return clienteDTO;
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

}






