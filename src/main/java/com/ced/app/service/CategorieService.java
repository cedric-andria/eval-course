package com.ced.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Categorie;
import com.ced.app.repository.CategorieRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CategorieService {
    @Autowired
    private CategorieRepository categorieRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Categorie> getAll()
    {
        return categorieRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    public Categorie findByPk(int idcategorie)
    {
        List<Categorie> tabCategorie = new ArrayList<>();
        Categorie matchedCategorie = null;
        String nativeQuery = "SELECT * FROM categorie where pk = :pk";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Categorie.class);
        query.setParameter("pk", idcategorie);
        tabCategorie = query.getResultList();
        matchedCategorie = tabCategorie.get(0);
        
        return matchedCategorie;
    }
}
