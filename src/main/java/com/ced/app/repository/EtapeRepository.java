package com.ced.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ced.app.model.Etape;

@Repository
public interface EtapeRepository extends JpaRepository<Etape, Integer>{
    Page<Etape> findAll(Pageable pageable);
}
