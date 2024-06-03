package com.ced.app.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Affectation_coureur;
import com.ced.app.model.Coureur;
import com.ced.app.model.Etape;
import com.ced.app.repository.Affectation_coureurRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class Affectation_coureurService {
    @Autowired
    private Affectation_coureurRepository affectation_coureurRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Affectation_coureur> getAffectationOf(int idetape)
    {
        List<Affectation_coureur> tabaffectation = new ArrayList<>();
        String select_query = "SELECT * FROM Affectation_coureur where idetape = :idetape";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Affectation_coureur.class);
        native_query.setParameter("idetape", idetape);
        tabaffectation = native_query.getResultList();
        return tabaffectation;
    }

    @Transactional
    public int save(int idcoureur, int idetape) throws Exception
    {
        String nativeQuery = "Insert into Affectation_coureur (idcoureur, idetape) values (:idcoureur, :idetape)";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter("idcoureur", idcoureur);
        query.setParameter("idetape", idetape);
        int rowInserted = query.executeUpdate();
        System.out.println("Affectation coureur inserted : " + rowInserted);
        return rowInserted;
    }

    @SuppressWarnings("unchecked")
    public List<Coureur> getAffectedCoureurOf(int idetape)
    {
        List<Coureur> tabcoureurs = new ArrayList<>();
        String select_query = "SELECT * FROM Coureur where pk IN (select idcoureur from affectation_coureur where idetape = :idetape)";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Coureur.class);
        native_query.setParameter("idetape", idetape);
        tabcoureurs = native_query.getResultList();
        for (Coureur coureur : tabcoureurs) {
            // coureur.setAge(Period.between(coureur.getDatenaissance(), LocalDate.now()).getYears());
            coureur.setAge(LocalDate.now().getYear() - coureur.getDatenaissance().getYear());

        }
        return tabcoureurs;
    }

    @SuppressWarnings("unchecked")
    public List<Coureur> getAffectedCoureurOf(int idequipe, int idetape)
    {
        List<Coureur> tabcoureur = new ArrayList<>();
        String select_query = "SELECT * FROM Coureur where pk IN (select idcoureur from affectation_coureur where idetape = :idetape) and idequipe = :idequipe";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Coureur.class);
        native_query.setParameter("idetape", idetape);
        native_query.setParameter("idequipe", idequipe);
        tabcoureur = native_query.getResultList();
        return tabcoureur;
    }

    public Affectation_coureur getAffectationOf(int idcoureur, int idetape)
    {
        Affectation_coureur affectation = null;
        String select_query = "SELECT * FROM Affectation_coureur where idetape = :idetape and idcoureur = :idcoureur order by pk desc limit 1";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Affectation_coureur.class);
        native_query.setParameter("idetape", idetape);
        native_query.setParameter("idcoureur", idcoureur);
        affectation = (Affectation_coureur)native_query.getSingleResult();
        return affectation;
    }
}
