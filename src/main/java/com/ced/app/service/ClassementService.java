package com.ced.app.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Categorie;
import com.ced.app.model.Classement_equipe;
import com.ced.app.model.Classement_etape;
import com.ced.app.model.Coureur;
import com.ced.app.model.Equipe;
import com.ced.app.model.Etape;
import com.ced.app.model.Histo_etape_coureur;
import com.ced.app.model.Param_points_rang;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ClassementService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EtapeService etapeService;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private CategorieService categorieService;

    @SuppressWarnings("unchecked")
    public double getCorrespondingPoints(int rangCoureur)
    {
        Param_points_rang param_points_rang = null;
        List<Param_points_rang> tabParams = new ArrayList<>();
        String nativeQuery = "Select * from Param_points_rang where rang = :rang";
        
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Param_points_rang.class);
        query.setParameter("rang", rangCoureur);
        tabParams = query.getResultList();
        if(tabParams.size() == 0)
        {
            return 0;
        }
        param_points_rang = tabParams.get(0);
        return param_points_rang.getPoints();
    }

    @SuppressWarnings("unchecked")
    public Classement_etape getClassementEtape(int idetape)
    {
        Classement_etape classement_etape = new Classement_etape();
        List<Histo_etape_coureur> tabHisto = new ArrayList<>();
        String nativeQuery = "select h.* from histo_etape_coureur h JOIN affectation_coureur a on a.pk = h.idaffectation where a.idetape = :idetape";
        
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Histo_etape_coureur.class);
        query.setParameter("idetape", idetape);
        tabHisto = query.getResultList();

        List<Coureur> coureurs_filtre_partemps = new ArrayList<>();
        for (Histo_etape_coureur histo : tabHisto) {
            Coureur coureur_hydrate_perf = histo.getAffectation().getCoureur();
            coureur_hydrate_perf.setHeuredepart(histo.getHeuredepart());
            coureur_hydrate_perf.setHeurearrivee(histo.getHeurearrivee());
            coureur_hydrate_perf.setRang(histo.getRang());
            coureur_hydrate_perf.setPointtotal(histo.getPoints());
            coureurs_filtre_partemps.add(coureur_hydrate_perf);
        }
        System.out.println("Coureurs non filtres : " + coureurs_filtre_partemps.size());

        coureurs_filtre_partemps.sort(Comparator.comparingLong(Coureur::getDuration));
        int currentRank = 1;
        for (int i = 0; i < coureurs_filtre_partemps.size(); i++) {
            if (i > 0 && coureurs_filtre_partemps.get(i).getDuration() == coureurs_filtre_partemps.get(i - 1).getDuration()) {
                // If the current racer has the same duration as the previous racer, assign the same rank
                coureurs_filtre_partemps.get(i).setRang(coureurs_filtre_partemps.get(i - 1).getRang());
            } else {
                // Otherwise, assign the current rank
                coureurs_filtre_partemps.get(i).setRang(currentRank);
            }
            coureurs_filtre_partemps.get(i).setPointtotal(coureurs_filtre_partemps.get(i).getPointtotal() + getCorrespondingPoints(coureurs_filtre_partemps.get(i).getRang()));
            currentRank ++;
        }


        for (Coureur coureur : coureurs_filtre_partemps) {
            // coureur.setAge(Period.between(coureur.getDatenaissance(), LocalDate.now()).getYears());
            coureur.setAge(LocalDate.now().getYear() - coureur.getDatenaissance().getYear());

        }
        classement_etape.setClassement(coureurs_filtre_partemps);
        return classement_etape;
    }

    @SuppressWarnings("unchecked")
    public Classement_etape getClassementEtapeWithCat(int idetape, int idcategorie)
    {
        Classement_etape classement_etape = new Classement_etape();
        List<Histo_etape_coureur> tabHisto = new ArrayList<>();
        String nativeQuery = "select h.* from histo_etape_coureur h JOIN affectation_coureur a on a.pk = h.idaffectation where a.idetape = :idetape";
        
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Histo_etape_coureur.class);
        query.setParameter("idetape", idetape);
        tabHisto = query.getResultList();

        List<Coureur> coureurs_filtre_partemps = new ArrayList<>();
        for (Histo_etape_coureur histo : tabHisto) {
            Coureur coureur_hydrate_perf = histo.getAffectation().getCoureur();
            coureur_hydrate_perf.setHeuredepart(histo.getHeuredepart());
            coureur_hydrate_perf.setHeurearrivee(histo.getHeurearrivee());
            coureur_hydrate_perf.setRang(histo.getRang());
            coureur_hydrate_perf.setPointtotal(histo.getPoints());
            coureurs_filtre_partemps.add(coureur_hydrate_perf);
        }
        System.out.println("Coureurs non filtres : " + coureurs_filtre_partemps.size());

        coureurs_filtre_partemps.sort(Comparator.comparingLong(Coureur::getDuration));
        //esorina izay tsy mifanaraka @ categorie
        for (Coureur coureur : coureurs_filtre_partemps) {
            List<Integer> coureurCategoriesPk = new ArrayList<>();
            for (Categorie categoriesCoureur : coureur.getCategories()) {
                coureurCategoriesPk.add(categoriesCoureur.getPk());
            }
            if (!coureurCategoriesPk.contains(idetape)) {
                coureurs_filtre_partemps.remove(coureur);
            }
        }
        System.out.println("Coureurs filtres par categorie: " + coureurs_filtre_partemps.size());
        //esorina izay tsy mifanaraka @ categorie

        int currentRank = 1;
        for (int i = 0; i < coureurs_filtre_partemps.size(); i++) {
            if (i > 0 && coureurs_filtre_partemps.get(i).getDuration() == coureurs_filtre_partemps.get(i - 1).getDuration()) {
                // If the current racer has the same duration as the previous racer, assign the same rank
                coureurs_filtre_partemps.get(i).setRang(coureurs_filtre_partemps.get(i - 1).getRang());
            } else {
                // Otherwise, assign the current rank
                coureurs_filtre_partemps.get(i).setRang(currentRank);
            }
            coureurs_filtre_partemps.get(i).setPointtotal(coureurs_filtre_partemps.get(i).getPointtotal() + getCorrespondingPoints(coureurs_filtre_partemps.get(i).getRang()));
            currentRank ++;
        }


        for (Coureur coureur : coureurs_filtre_partemps) {
            // coureur.setAge(Period.between(coureur.getDatenaissance(), LocalDate.now()).getYears());
            coureur.setAge(LocalDate.now().getYear() - coureur.getDatenaissance().getYear());

        }
        classement_etape.setClassement(coureurs_filtre_partemps);
        return classement_etape;
    }

    public Classement_equipe getClassementEquipe(int idcategorie)
    {
        //raha 0 dia toutes categories
        Classement_equipe classement_equipe = new Classement_equipe();
        // List<Categorie> tabcategories = categorieService.getAll();
        // Categorie matchedCategorie = null;
        // List<Equipe> tabequipeclassement = equipeService.getAll();
        //toutes categories
        if (idcategorie == 0) {
            //toutes categories
            List<Classement_etape> classement_etapes = new ArrayList<>();
            List<Equipe> tabequipes_etapes = equipeService.getAll();

            for (Etape etape : etapeService.findAll()) {

                classement_etapes.add(getClassementEtape(etape.getPk()));

                int pointEquipe = 0;
                for (Equipe equipe : tabequipes_etapes) {
                    pointEquipe = 0;
                    for (Classement_etape classement_etape : classement_etapes) {
                        for (Coureur coureur : classement_etape.getClassement()) {
                            if (coureur.getEquipe().getPk() == equipe.getPk()) {
                                pointEquipe += coureur.getPointtotal();
                                equipe.setPoints(equipe.getPoints() + pointEquipe);
                            }
                        }
                    }
                }
            }
            tabequipes_etapes.sort(Comparator.comparingDouble(Equipe::getPoints));
            int currentRank = 1;
            for (int i = 0; i < tabequipes_etapes.size(); i++) {
                if (i > 0 && tabequipes_etapes.get(i).getPoints() == tabequipes_etapes.get(i - 1).getPoints()) {
                    // If the current racer has the same duration as the previous racer, assign the same rank
                    tabequipes_etapes.get(i).setRang(tabequipes_etapes.get(i - 1).getRang());
                } else {
                    // Otherwise, assign the current rank
                    tabequipes_etapes.get(i).setRang(currentRank);
                }
                currentRank ++;
            }
            classement_equipe.setClassement(tabequipes_etapes);
        }

        //avec categorie
        // matchedCategorie = categorieService.findByPk(idcategorie);
        List<Classement_etape> classement_etapes = new ArrayList<>();
        List<Equipe> tabequipes_etapes = equipeService.getAll();

        for (Etape etape : etapeService.findAll()) {

            classement_etapes.add(getClassementEtapeWithCat(etape.getPk(), idcategorie));

            int pointEquipe = 0;
            for (Equipe equipe : tabequipes_etapes) {
                pointEquipe = 0;
                for (Classement_etape classement_etape : classement_etapes) {
                    for (Coureur coureur : classement_etape.getClassement()) {
                        if (coureur.getEquipe().getPk() == equipe.getPk()) {
                            pointEquipe += coureur.getPointtotal();
                            equipe.setPoints(equipe.getPoints() + pointEquipe);
                        }
                    }
                }
            }
        }
        tabequipes_etapes.sort(Comparator.comparingDouble(Equipe::getPoints));
        int currentRank = 1;
        for (int i = 0; i < tabequipes_etapes.size(); i++) {
            if (i > 0 && tabequipes_etapes.get(i).getPoints() == tabequipes_etapes.get(i - 1).getPoints()) {
                // If the current racer has the same duration as the previous racer, assign the same rank
                tabequipes_etapes.get(i).setRang(tabequipes_etapes.get(i - 1).getRang());
            } else {
                // Otherwise, assign the current rank
                tabequipes_etapes.get(i).setRang(currentRank);
            }
            currentRank ++;
        }
        classement_equipe.setClassement(tabequipes_etapes);

        return classement_equipe;
    }
}
