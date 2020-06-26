package com.example.demo2.domain.repository;

import com.example.demo2.domain.model.Ordemservico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author note
 */
@Repository
public interface OrdemServicoRepository extends JpaRepository<Ordemservico, Long> {
    
}









