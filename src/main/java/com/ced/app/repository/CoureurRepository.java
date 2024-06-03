package com.ced.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ced.app.model.Coureur;

@Repository
public interface CoureurRepository extends JpaRepository<Coureur, Integer>{
    
}
