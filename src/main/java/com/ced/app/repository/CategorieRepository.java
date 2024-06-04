package com.ced.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ced.app.model.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer>{
    List<Categorie> findByNom(String nom);
}
