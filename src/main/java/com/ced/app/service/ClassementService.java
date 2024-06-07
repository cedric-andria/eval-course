package com.ced.app.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import com.ced.app.model.Penalite_equipe;

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

    @Autowired
    private Penalite_equipeService penalite_equipeService;

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
        Etape matchedEtape = null;
        try {
            matchedEtape = etapeService.getByPk(idetape);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Classement_etape classement_etape = new Classement_etape();
        List<Histo_etape_coureur> tabHisto = new ArrayList<>();
        String nativeQuery = "select h.* from histo_etape_coureur h JOIN affectation_coureur a on a.pk = h.idaffectation where a.idetape = :idetape";
        
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Histo_etape_coureur.class);
        query.setParameter("idetape", idetape);
        tabHisto = query.getResultList();

        List<Coureur> coureurs_filtre_partemps = new ArrayList<>();
        for (Histo_etape_coureur histo : tabHisto) {
            //attention eto misy pointeur mifampiditra

            Coureur coureur_hydrate_perf = new Coureur(histo.getAffectation().getCoureur());
            coureur_hydrate_perf.setHeuredepart(histo.getHeuredepart());
            coureur_hydrate_perf.setHeurearrivee(histo.getHeurearrivee());
            coureur_hydrate_perf.setRang(0);
            coureur_hydrate_perf.setPointtotal(0);
            coureurs_filtre_partemps.add(coureur_hydrate_perf);
        }
        // System.out.println("Coureurs non filtres (getclassement etape) : " + coureurs_filtre_partemps.size());

        // Comparator to sort with 0 at the end
        Comparator<Coureur> withZeroAtEnd = new Comparator<Coureur>() {
            //miampy penalite (raha misy)
            @Override
            public int compare(Coureur p1, Coureur p2) {
                long penalite_to_add_p1 = 0;
                long penalite_to_add_p2 = 0;

                List<Penalite_equipe> tabPenalite_equipe_of_coureurP1 = penalite_equipeService.getByEtapeAndEquipe(idetape, p1.getEquipe().getPk());
                for (Penalite_equipe penalite_p1 : tabPenalite_equipe_of_coureurP1) {
                    penalite_to_add_p1 += (penalite_p1.getValeur().toNanoOfDay() / 1000000);
                }
                List<Penalite_equipe> tabPenalite_equipe_of_coureurP2 = penalite_equipeService.getByEtapeAndEquipe(idetape, p2.getEquipe().getPk());
                for (Penalite_equipe penalite_p2 : tabPenalite_equipe_of_coureurP2) {
                    penalite_to_add_p2 += (penalite_p2.getValeur().toNanoOfDay() / 1000000);
                }

                p1.setChronomisypenalite(p1.getDuration() + penalite_to_add_p1);
                p1.setChronotsisypenalite(p1.getDuration());
                p2.setChronomisypenalite(p2.getDuration() + penalite_to_add_p2);
                p2.setChronotsisypenalite(p2.getDuration());


                if (p1.getDuration() == 0 && (p2.getDuration() != 0)) {
                    return 1; // Move person with 0 to the right (end)
                } else if (p1.getDuration() != 0 && (p2.getDuration() == 0)) {
                    return -1; // Move person without 0 to the left (beginning)
                } else {
                    return Long.compare(p1.getDuration() + penalite_to_add_p1, p2.getDuration() + penalite_to_add_p2); // Default ascending sort for non-zero values
                }
            }
            //miampy penalite (raha misy)
        };

        // Sort the list using Collections.sort with the comparator
        Collections.sort(coureurs_filtre_partemps, withZeroAtEnd);
        // coureurs_filtre_partemps.sort(Comparator.comparingLong(Coureur::getDuration));

        // System.out.println("etape : " + matchedEtape.getNom());
        // System.out.println("coureurs_filtre_partemps apres sort par get duration : ");
        // for (Coureur coureur : coureurs_filtre_partemps) {
        //     System.out.println("Nom : " + coureur.getNom());
        // }
        // System.out.println("Coureur indice 0 " + coureurs_filtre_partemps.get(0).getNom());

        int currentRank = 1;
        for (int i = 0; i < coureurs_filtre_partemps.size(); i++) {
            if (i > 0 && coureurs_filtre_partemps.get(i).getChronomisypenalite() == coureurs_filtre_partemps.get(i - 1).getChronomisypenalite()) {
                // If the current racer has the same duration as the previous racer, assign the same rank
                coureurs_filtre_partemps.get(i).setRang(coureurs_filtre_partemps.get(i - 1).getRang());
                coureurs_filtre_partemps.get(i).setPointtotal(coureurs_filtre_partemps.get(i).getPointtotal() + getCorrespondingPoints(coureurs_filtre_partemps.get(i).getRang()));
                continue;
            } else {
                // Otherwise, assign the current rank
                coureurs_filtre_partemps.get(i).setRang(currentRank);
            }
            coureurs_filtre_partemps.get(i).setPointtotal(coureurs_filtre_partemps.get(i).getPointtotal() + getCorrespondingPoints(coureurs_filtre_partemps.get(i).getRang()));
            currentRank ++;
        }
        // System.out.println("coureurs_filtre_partemps apres sort par get duration : ");
        for (Coureur coureur : coureurs_filtre_partemps) {
            System.out.println("Nom : " + coureur.getNom() + " - Rang : " + coureur.getRang());
        }

        for (Coureur coureur : coureurs_filtre_partemps) {
            // coureur.setAge(Period.between(coureur.getDatenaissance(), LocalDate.now()).getYears());
            coureur.setAge(LocalDate.now().getYear() - coureur.getDatenaissance().getYear());
            //alea jour 4, avoir en meme temps chrono initial, penalite et chrono final
            long valeurPenalite = 0;
            List<Penalite_equipe> tabPenaliteCoureur = penalite_equipeService.getByEtapeAndEquipe(idetape, coureur.getEquipe().getPk());
            for (Penalite_equipe penaliteCoureur : tabPenaliteCoureur) {
                valeurPenalite += (penaliteCoureur.getValeur().toNanoOfDay() / 1000000);
            }
            coureur.setValeurPenalite(valeurPenalite);
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
            //attention eto misy pointeur mifampiditra
            Coureur coureur_hydrate_perf = new Coureur(histo.getAffectation().getCoureur());
            coureur_hydrate_perf.setHeuredepart(histo.getHeuredepart());
            coureur_hydrate_perf.setHeurearrivee(histo.getHeurearrivee());
            coureur_hydrate_perf.setRang(0);
            coureur_hydrate_perf.setPointtotal(0);
            coureurs_filtre_partemps.add(coureur_hydrate_perf);
        }
        System.out.println("Coureurs non filtres : " + coureurs_filtre_partemps.size());


        // Comparator to sort with 0 at the end
        Comparator<Coureur> withZeroAtEnd = new Comparator<Coureur>() {
            //miampy penalite (raha misy)
            @Override
            public int compare(Coureur p1, Coureur p2) {
                long penalite_to_add_p1 = 0;
                long penalite_to_add_p2 = 0;

                List<Penalite_equipe> tabPenalite_equipe_of_coureurP1 = penalite_equipeService.getByEtapeAndEquipe(idetape, p1.getEquipe().getPk());
                for (Penalite_equipe penalite_p1 : tabPenalite_equipe_of_coureurP1) {
                    penalite_to_add_p1 += (penalite_p1.getValeur().toNanoOfDay() / 1000000);
                }
                List<Penalite_equipe> tabPenalite_equipe_of_coureurP2 = penalite_equipeService.getByEtapeAndEquipe(idetape, p2.getEquipe().getPk());
                for (Penalite_equipe penalite_p2 : tabPenalite_equipe_of_coureurP2) {
                    penalite_to_add_p2 += (penalite_p2.getValeur().toNanoOfDay() / 1000000);
                }

                p1.setChronomisypenalite(p1.getDuration() + penalite_to_add_p1);
                p1.setChronotsisypenalite(p1.getDuration());
                p2.setChronomisypenalite(p2.getDuration() + penalite_to_add_p2);
                p2.setChronotsisypenalite(p2.getDuration());

                if ((p1.getDuration() == 0) && (p2.getDuration() != 0)) {
                    return 1; // Move person with 0 to the right (end)
                } else if ((p1.getDuration() != 0) && (p2.getDuration() == 0)) {
                    return -1; // Move person without 0 to the left (beginning)
                } else {
                    return Long.compare((p1.getDuration() + penalite_to_add_p1), (p2.getDuration() + penalite_to_add_p2)); // Default ascending sort for non-zero values
                }
            }
            //miampy penalite (raha misy)
        };

        // Sort the list using Collections.sort with the comparator
        Collections.sort(coureurs_filtre_partemps, withZeroAtEnd);
        // coureurs_filtre_partemps.sort(Comparator.comparingLong(Coureur::getDuration));


        //esorina izay tsy mifanaraka @ categorie
        Iterator<Coureur> iterator = coureurs_filtre_partemps.iterator();
        while (iterator.hasNext()) {
            List<Integer> coureurCategoriesPk = new ArrayList<>();
            for (Categorie categorieCoureur : iterator.next().getCategories()) {
                coureurCategoriesPk.add(categorieCoureur.getPk());
            }
            if (!coureurCategoriesPk.contains(idcategorie)) {
                System.out.println("Tsy categorie " + idcategorie + " ity coureur ity");
                iterator.remove();
            }
        }
        // for (Coureur coureur : coureurs_filtre_partemps) {
        //     List<Integer> coureurCategoriesPk = new ArrayList<>();
        //     for (Categorie categoriesCoureur : coureur.getCategories()) {
        //         coureurCategoriesPk.add(categoriesCoureur.getPk());
        //     }
        //     if (!coureurCategoriesPk.contains(idcategorie)) {
        //         coureurs_filtre_partemps.remove(coureur);
        //     }
        // }
        // System.out.println("Coureurs filtres par categorie: " + coureurs_filtre_partemps.size());
        //esorina izay tsy mifanaraka @ categorie

        int currentRank = 1;
        for (int i = 0; i < coureurs_filtre_partemps.size(); i++) {
            if (i > 0 && coureurs_filtre_partemps.get(i).getChronomisypenalite() == coureurs_filtre_partemps.get(i - 1).getChronomisypenalite()) {
                // If the current racer has the same duration as the previous racer, assign the same rank
                coureurs_filtre_partemps.get(i).setRang(coureurs_filtre_partemps.get(i - 1).getRang());
                coureurs_filtre_partemps.get(i).setPointtotal(coureurs_filtre_partemps.get(i).getPointtotal() + getCorrespondingPoints(coureurs_filtre_partemps.get(i).getRang()));
                continue;
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
            System.out.println("mandalo categorie = 0");
            //toutes categories
            // List<Classement_etape> tabclassement_etape = new ArrayList<>();
            List<Equipe> tabequipes = equipeService.getAll();

            for(Etape etape : etapeService.findAll())
            {
                etape.setClassement_etape((getClassementEtape(etape.getPk())));
            }

            // int pointEquipe = 0;
            for (Equipe equipe : tabequipes) {
                System.out.println("Equipe : " + equipe.getNom() + " - point depart : " + equipe.getPoints());
                for (Etape etape : etapeService.findAll()) {
                    // tabclassement_etape.add(getClassementEtape(etape.getPk()));
                    for (Coureur coureur : etape.getClassement_etape().getClassement()) {
                        if (coureur.getEquipe().getPk() == equipe.getPk()) {
                            // System.out.println("coureur " + coureur.getNom() + " pointtotal : " + coureur.getPointtotal());
                            equipe.setPoints(equipe.getPoints() + coureur.getPointtotal());
                        }
                        //raha tsy misy ilay equipe
                    }
                }
                System.out.println("Points equipe : " + equipe.getPoints());
            }
            // System.out.println("tabclassement_etape size : " + tabclassement_etape.size());
            tabequipes.sort(Comparator.comparingDouble(Equipe::getPoints).reversed());
            int currentRank = 1;
            for (int i = 0; i < tabequipes.size(); i++) {
                if (i > 0 && tabequipes.get(i).getPoints() == tabequipes.get(i - 1).getPoints()) {
                    // If the current racer has the same duration as the previous racer, assign the same rank
                    tabequipes.get(i).setRang(tabequipes.get(i - 1).getRang());
                    // tabequipes.get(i - 1).setHas_exaequo(1);
                    // System.out.println("tabequipes.get(i).getHasexaequo : " + tabequipes.get(i).getHas_exaequo());
                } else {
                    // Otherwise, assign the current rank
                    tabequipes.get(i).setRang(currentRank);
                    // tabequipes.get(i).setHas_exaequo(0);
                    // System.out.println("tabequipes.get(i).getHasexaequo : " + tabequipes.get(i).getHas_exaequo());

                }
                currentRank ++;
            }
            classement_equipe.setClassement(tabequipes);
            return classement_equipe;

        }

        //avec categorie
        // matchedCategorie = categorieService.findByPk(idcategorie);
        // List<Classement_etape> classement_etapes = new ArrayList<>();
        // List<Equipe> tabequipes_etapes = equipeService.getAll();
        // System.out.println("mandalo if categorie != 0");
        // for (Etape etape : etapeService.findAll()) {

        //     classement_etapes.add(getClassementEtapeWithCat(etape.getPk(), idcategorie));

        //     int pointEquipe = 0;
        //     for (Equipe equipe : tabequipes_etapes) {
        //         pointEquipe = 0;
        //         for (Classement_etape classement_etape : classement_etapes) {
        //             for (Coureur coureur : classement_etape.getClassement()) {
        //                 if (coureur.getEquipe().getPk() == equipe.getPk()) {
        //                     pointEquipe += coureur.getPointtotal();
        //                     equipe.setPoints(equipe.getPoints() + pointEquipe);
        //                 }
        //             }
        //         }
        //     }
        // }
        // tabequipes_etapes.sort(Comparator.comparingDouble(Equipe::getPoints));
        // int currentRank = 1;
        // for (int i = 0; i < tabequipes_etapes.size(); i++) {
        //     if (i > 0 && tabequipes_etapes.get(i).getPoints() == tabequipes_etapes.get(i - 1).getPoints()) {
        //         // If the current racer has the same duration as the previous racer, assign the same rank
        //         tabequipes_etapes.get(i).setRang(tabequipes_etapes.get(i - 1).getRang());
        //     } else {
        //         // Otherwise, assign the current rank
        //         tabequipes_etapes.get(i).setRang(currentRank);
        //     }
        //     currentRank ++;
        // }
        // classement_equipe.setClassement(tabequipes_etapes);

        // return classement_equipe;

        List<Equipe> tabequipes = equipeService.getAll();

        for(Etape etape : etapeService.findAll())
        {
            etape.setClassement_etape((getClassementEtapeWithCat(etape.getPk(), idcategorie)));
        }

        // int pointEquipe = 0;
        for (Equipe equipe : tabequipes) {
            System.out.println("Equipe : " + equipe.getNom() + " - point depart : " + equipe.getPoints());
            for (Etape etape : etapeService.findAll()) {
                // tabclassement_etape.add(getClassementEtape(etape.getPk()));
                for (Coureur coureur : etape.getClassement_etape().getClassement()) {
                    if (coureur.getEquipe().getPk() == equipe.getPk()) {
                        // System.out.println("coureur " + coureur.getNom() + " pointtotal : " + coureur.getPointtotal());
                        equipe.setPoints(equipe.getPoints() + coureur.getPointtotal());
                    }
                    //raha tsy misy ilay equipe
                }
            }
            // System.out.println("Points equipe : " + equipe.getPoints());
        }
        // System.out.println("tabclassement_etape size : " + tabclassement_etape.size());
        tabequipes.sort(Comparator.comparingDouble(Equipe::getPoints).reversed());
        int currentRank = 1;
        for (int i = 0; i < tabequipes.size(); i++) {
            if (i > 0 && tabequipes.get(i).getPoints() == tabequipes.get(i - 1).getPoints()) {
                // If the current racer has the same duration as the previous racer, assign the same rank
                tabequipes.get(i).setRang(tabequipes.get(i - 1).getRang());
            } else {
                // Otherwise, assign the current rank
                tabequipes.get(i).setRang(currentRank);
            }
            currentRank ++;
        }
        classement_equipe.setClassement(tabequipes);
        return classement_equipe;
    }
}
