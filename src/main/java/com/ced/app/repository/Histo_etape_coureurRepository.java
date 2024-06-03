package com.ced.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ced.app.model.Histo_etape_coureur;

@Repository
public interface Histo_etape_coureurRepository extends JpaRepository<Histo_etape_coureur, Integer>{
    
}
