package com.ced.app.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Penalite_equipe;
import com.ced.app.repository.Penalite_equipeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;


@Service
public class Penalite_equipeService {
    @Autowired
    private Penalite_equipeRepository penalite_equipeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Penalite_equipe> getAll()
    {
        return penalite_equipeRepository.findAll();
    }

    @Transactional
    public void deleteByPk(int pk)
    {
        penalite_equipeRepository.deleteById(pk);
    }

    @Transactional
    public int save(int idetape, int idequipe, LocalTime valeur)
    {
        int rowsInserted = 0;
        String nativeQuery = "Insert into penalite_equipe (idetape, idequipe, valeur) values (:idetape, :idequipe, :valeur)";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter("idetape", idetape);
        query.setParameter("idequipe", idequipe);
        query.setParameter("valeur", valeur);

        rowsInserted = query.executeUpdate();
        System.out.println("penalite inserted : " + rowsInserted);
        return rowsInserted;
    }

    @SuppressWarnings("unchecked")
    public List<Penalite_equipe> getByEtapeAndEquipe(int idetape, int idequipe)
    {
        List<Penalite_equipe> tabPenalitesEquipeInEtape = new ArrayList<>();
        String nativeQuery = "Select * from penalite_equipe where idetape = :idetape and idequipe = :idequipe";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Penalite_equipe.class);
        query.setParameter("idetape", idetape);
        query.setParameter("idequipe", idequipe);

        tabPenalitesEquipeInEtape = query.getResultList();

        return tabPenalitesEquipeInEtape;
    }
}
