package com.ced.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Coureur;
import com.ced.app.repository.CoureurRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CoureurService {
    @Autowired
    private CoureurRepository coureurRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public Coureur findByPk(int pk)
    {
        List<Coureur> tabcoureur = new ArrayList<>();
        Coureur matchedCoureur = null;
        String nativeQuery = "SELECT * FROM coureur where pk = :pk";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Coureur.class);
        query.setParameter("pk", pk);
        tabcoureur = query.getResultList();
        matchedCoureur = tabcoureur.get(0);
        
        return matchedCoureur;
    }
}
