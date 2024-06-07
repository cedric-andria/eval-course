package com.ced.app.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Coureur;
import com.ced.app.model.Equipe;
import com.ced.app.model.Etape;
import com.ced.app.model.Histo_etape_coureur;
import com.ced.app.model.Penalite_equipe;
import com.ced.app.repository.EquipeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class EquipeService {
    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private Histo_etape_coureurService histo_etape_coureurService;

    @Autowired
    private EtapeService etapeService;

    @Autowired
    private Penalite_equipeService penalite_equipeService;

    @PersistenceContext
    private EntityManager entityManager;

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

    public LinkedHashMap<Integer, List<Histo_etape_coureur>> findHistoByEtapeAndEquipe(int idequipe)
    {
        LinkedHashMap<Integer, List<Histo_etape_coureur>> hashHistoEtape = new LinkedHashMap<>();
        List<Etape> tabEtape = etapeService.findAllOrderByRang();
        for (Etape etape : tabEtape) {
            List<Histo_etape_coureur> tabHistoEtape =  histo_etape_coureurService.getByEtapeAndEquipe(etape.getPk(), idequipe);
            // System.out.println("findHistoByEtapeAndEquipe taille");
            // System.out.println("findHistoByEtapeAndEquipe");
            for (Histo_etape_coureur histoetape : tabHistoEtape) {
                long penalite_to_add = 0;
                List<Penalite_equipe> tabPenaliteEquipe = penalite_equipeService.getByEtapeAndEquipe(histoetape.getAffectation().getEtape().getPk(), histoetape.getAffectation().getCoureur().getEquipe().getPk());
                for (Penalite_equipe penalite : tabPenaliteEquipe) {
                    penalite_to_add += penalite.getValeur().toNanoOfDay() / 1000000;
                }
                if (histoetape.getHeurearrivee() == null) {
                    histoetape.setChronomisypenalite(0);
                }
                else
                {
                    histoetape.setChronomisypenalite(Duration.between(histoetape.getHeuredepart(), histoetape.getHeurearrivee()).toMillis() + penalite_to_add);
                }
            }
            hashHistoEtape.put(etape.getPk(), tabHistoEtape);
        }

        return hashHistoEtape;
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
        List<Equipe> tabequipes = equipeRepository.findAll();
        for (Equipe equipe : tabequipes) {
            equipe.setPoints(0);
            equipe.setRang(0);
        }
        return tabequipes;
    }

}
