package com.ced.app.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Coureur;
import com.ced.app.model.Equipe;
import com.ced.app.model.Etape;
import com.ced.app.repository.EquipeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class EquipeService {
    @Autowired
    EquipeRepository equipeRepository;

    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Equipe> findByPkUser(int pkUser)
    {
        List<Equipe> tabequipes = new ArrayList<>();
        String nativeQuery = "SELECT * FROM equipe where idutilisateur = :idutilisateur";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Equipe.class);
        // System.out.println("pk User : " + pkUser);
        query.setParameter("idutilisateur", pkUser);
        tabequipes = query.getResultList();
        // System.out.println("tabequipes size : " + tabequipes.size());
        return tabequipes;
    }

    @SuppressWarnings("unchecked")
    public List<Coureur> findAvailableCoureursOf(Equipe equipe, Etape etape)
    {
        List<Coureur> tabcoureurs = new ArrayList<>();
        String nativeQuery = "SELECT c.* FROM coureur c LEFT JOIN affectation_coureur ac ON c.pk = ac.idcoureur AND ac.idetape = :idetape where c.idequipe = :idequipe AND ac.idcoureur IS NULL";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Coureur.class);
        // System.out.println("pk User : " + pkUser);
        query.setParameter("idequipe", equipe.getPk());
        query.setParameter("idetape", etape.getPk());

        tabcoureurs = query.getResultList();
        for (Coureur coureur : tabcoureurs) {
            // coureur.setAge(Period.between(coureur.getDatenaissance(), LocalDate.now()).getYears());
            coureur.setAge(LocalDate.now().getYear() - coureur.getDatenaissance().getYear());

        }
        // System.out.println("tabcoureurs size : " + tabcoureurs.size());
        return tabcoureurs;
    }

    public List<Equipe> getAll()
    {
        return equipeRepository.findAll();
    }

}
