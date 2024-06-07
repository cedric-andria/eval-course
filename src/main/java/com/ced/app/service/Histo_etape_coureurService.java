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
import org.springframework.transaction.annotation.Transactional;


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

        //test raha efa misy dia atao update tsony
        if (getByAffectation(idaffectation).size() != 0) {
            nativeQuery =  "Update Histo_etape_coureur set heuredepart = :heuredepart, heurearrivee = :heurearrivee where idaffectation = :idaffectation";
            jakarta.persistence.Query updatequery = entityManager.createNativeQuery(nativeQuery);
            updatequery.setParameter("idaffectation", idaffectation);
            updatequery.setParameter("heuredepart", heuredepart);
            updatequery.setParameter("heurearrivee", heurearrivee);
            rowsInserted = updatequery.executeUpdate();
            System.out.println("Histo etape updated : " + rowsInserted);

            return rowsInserted;
        }
        
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

    @SuppressWarnings("unchecked")
    public List<Histo_etape_coureur> getByEtapeAndEquipe(int idetape, int idequipe)
    {
        List<Histo_etape_coureur> tabhisto = new ArrayList<>();
        String nativeQuery = "select h.id, h.idaffectation, h.heuredepart, h.heurearrivee, h.points, h.rang, h.pk from histo_etape_coureur h join affectation_coureur a on a.pk = h.idaffectation join coureur c on c.pk = a.idcoureur join equipe e on e.pk = c.idequipe where a.idetape = :idetape and c.idequipe = :idequipe";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Histo_etape_coureur.class);
        query.setParameter("idetape", idetape);
        query.setParameter("idequipe", idequipe);

        tabhisto = query.getResultList();
        return tabhisto;
    }
}
