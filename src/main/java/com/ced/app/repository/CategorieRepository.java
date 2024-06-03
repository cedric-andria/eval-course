package com.ced.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ced.app.model.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer>{
    
}
