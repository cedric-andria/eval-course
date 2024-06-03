package com.ced.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Histo_etape_coureur;
import com.ced.app.repository.Histo_etape_coureurRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class Histo_etape_coureurService {
    @Autowired 
    private Histo_etape_coureurRepository histo_etape_coureurRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Histo_etape_coureur save(Histo_etape_coureur histo_etape_coureur)
    {
        return histo_etape_coureurRepository.save(histo_etape_coureur);
    }

    @Transactional
    public int save(int idaffectation, LocalDateTime heuredepart, LocalDateTime heurearrivee)
    {
        int rowsInserted = 0;
        String nativeQuery = "Insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) values (:idaffectation, :heuredepart, :heurearrivee)";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter("idaffectation", idaffectation);
        query.setParameter("heuredepart", heuredepart);
        query.setParameter("heurearrivee", heurearrivee);
        rowsInserted = query.executeUpdate();
        System.out.println("Histo etape inserted : " + rowsInserted);
        return rowsInserted;
    }

    @SuppressWarnings("unchecked")
    public List<Histo_etape_coureur> getByAffectation(int idaffectation)
    {
        List<Histo_etape_coureur> tabhisto = new ArrayList<>();
        String nativeQuery = "Select * from Histo_etape_coureur where idaffectation = :idaffectation";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Histo_etape_coureur.class);
        query.setParameter("idaffectation", idaffectation);
        tabhisto = query.getResultList();
        return tabhisto;
    }
}
