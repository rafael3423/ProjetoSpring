package com.example.demo2.api.controller;

import com.example.demo2.domain.dto.ClienteDTO;
import com.example.demo2.domain.exception.AcessoNegadoException;
import com.example.demo2.domain.exception.NegocioException;
import com.example.demo2.domain.model.Cliente;
import com.example.demo2.domain.repository.ClienteRepository;
import com.example.demo2.domain.service.CadastroCliente;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clientes")
@CrossOrigin(origins = "*") // permite que qualquer dominio tenha acesso aos dados
public class ClientController {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CadastroCliente cadastroCliente;

    @GetMapping()
    public List<ClienteDTO> listar() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            List<ClienteDTO> clientesDTO = new ArrayList();

            clientes.forEach((c) -> {
                ClienteDTO clienteDTO = modelMapper.map(c, ClienteDTO.class);
                clientesDTO.add(clienteDTO);
            });

            return clientesDTO;
        } catch (Exception ex) {
            throw new AcessoNegadoException("Não autorizado");
        }
    }

    @GetMapping("/{clienteId}")
    public ClienteDTO buscar(@PathVariable Long clienteId) {
        try {

            Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new NegocioException("cliente não encontrado"));

            ClienteDTO clienteDTO = modelMapper.map(cliente, ClienteDTO.class);

            return clienteDTO;
        } catch (Exception ex) {
            throw new AcessoNegadoException("Não autorizado");
        }

    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO adicionar(@Valid @RequestBody Cliente cliente) {

        return cadastroCliente.salvar(cliente);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{clienteId}")
    public ResponseEntity<ClienteDTO> atualizar(@Valid @PathVariable Long clienteId, @RequestBody Cliente cliente) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new NegocioException("cliente não encontrado");
        }
        cliente.setId(clienteId);
        return ResponseEntity.ok(cadastroCliente.salvar(cliente));
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> deletar(@PathVariable Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }

        cadastroCliente.deletar(clienteId);

        return ResponseEntity.noContent().build();
    }

}


