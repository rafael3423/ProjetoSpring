package com.example.demo2.domain.repository;

import com.example.demo2.domain.model.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
//    List<Cliente> findByName("");
//    List<Cliente> findByName();
    Cliente findByEmail(String email);
}









