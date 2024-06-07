package com.ced.app.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ced.app.model.Affectation_coureur;
import com.ced.app.model.Coureur;
import com.ced.app.model.Equipe;
import com.ced.app.model.Etape;
import com.ced.app.service.Affectation_coureurService;
import com.ced.app.service.CoureurService;
import com.ced.app.service.EquipeService;
import com.ced.app.service.EtapeService;
import com.ced.app.service.Histo_etape_coureurService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AffectationController {
    @Autowired
    private Affectation_coureurService affectation_coureurService;

    @Autowired
    private CoureurService coureurService;

    @Autowired
    private EtapeService etapeService;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private Histo_etape_coureurService histo_etape_coureurService;

    @PostMapping("/affectercoureur")
    public String affectercoureur(@RequestParam(name = "idcoureur", required = true) String idcoureur, @RequestParam(name = "idetape", required = true) String idetape, HttpSession session, Model model)
    {
        if (session.getAttribute("equipe_session") == null) {
            return "redirect:/";
        }
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        int rowsInserted = 0;
        Coureur matchedCoureur = null;
        Etape matchedEtape = null;
        model.addAttribute("idetape", idetape);

        List<Affectation_coureur> tabaffectation_equipe = new ArrayList<>();
        List<Affectation_coureur> tabaffectation = affectation_coureurService.getAffectationOf(Integer.parseInt(idetape));

        try {
            matchedCoureur = coureurService.findByPk(Integer.parseInt(idcoureur));
            matchedEtape = etapeService.getByPk(Integer.parseInt(idetape));
            model.addAttribute("etape", matchedEtape);

            for (Affectation_coureur affectation : tabaffectation) {
                //verifier hoe mitovy ve le equipe resahana dia avy eo filtrena
                if (affectation.getCoureur().getEquipe().getPk() == matchedCoureur.getEquipe().getPk()) {
                    tabaffectation_equipe.add(affectation);
                }
            }
            if (tabaffectation_equipe.size() >= matchedEtape.getNbcoureur_equipe()) {
                //raha efa tratra le limite
                model.addAttribute("warning_affectation", "Vous ne pouvez plus choisir de coureur");
                model.addAttribute("tabcoureurs", equipeService.findAvailableCoureursOf((Equipe)session.getAttribute("equipe_session"), matchedEtape));

                return "affectation-coureur-etape";

            }

            //raha correct dia manao insert
            rowsInserted = affectation_coureurService.save(Integer.parseInt(idcoureur), Integer.parseInt(idetape));
            tabaffectation_equipe.clear();
            model.addAttribute("tabcoureurs", equipeService.findAvailableCoureursOf((Equipe)session.getAttribute("equipe_session"), matchedEtape));

            //averina alaina ny nombre
            tabaffectation = affectation_coureurService.getAffectationOf(Integer.parseInt(idetape));
            for (Affectation_coureur affectation : tabaffectation) {
                //verifier hoe mitovy ve le equipe resahana dia avy eo filtrena
                if (affectation.getCoureur().getEquipe().getPk() == matchedCoureur.getEquipe().getPk()) {
                    tabaffectation_equipe.add(affectation);
                }
            }
            if (tabaffectation_equipe.size() >= matchedEtape.getNbcoureur_equipe()) {
                //raha efa tratra le limite
                model.addAttribute("warning_affectation", "Vous ne pouvez plus choisir de coureur");
                return "affectation-coureur-etape";
            }
            model.addAttribute("warning_affectation", "Vous pouvez encore choisir " + (matchedEtape.getNbcoureur_equipe() - tabaffectation_equipe.size()) + "coureur(s)");
            return "affectation-coureur-etape";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/gotoaffectercoureur?idetape=" + idetape;
    }

    @PostMapping("/affectertempscoureur")
    public String affectertempscoureur(@RequestParam(name = "idcoureur", required = true) String idcoureur, @RequestParam(name = "idetape", required = true) String idetape, @RequestParam(name = "datedepart", required = true) String datedepart, @RequestParam(name = "datearrivee", required = true) String datearrivee, @RequestParam(name = "secondesarrivee", required = true) String secondesarrivee, @RequestParam(name = "secondesdepart", required = true) String secondesdepart, HttpSession session, Model model)
    {
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        model.addAttribute("idetape", idetape);
        model.addAttribute("warning_affectation_temps", "");
        System.out.println("idcoureur : " + idcoureur);
        int rowsInserted = 0;
        Etape matchedEtape = null;
        Coureur matchedCoureur = null;
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        if (!session.getAttribute("profil").toString().equalsIgnoreCase("admin")) {
            return "redirect:/";
        }
        if ((Integer.parseInt(secondesarrivee) > 60) || (Integer.parseInt(secondesarrivee) < 0)) {
            System.out.println("secondes arrivee invalid");
            return "redirect:/gotoaffectertempscoureur?idetape=" + idetape;
        }
        try {
            matchedEtape = etapeService.getByPk(Integer.parseInt(idetape));
            matchedCoureur = coureurService.findByPk(Integer.parseInt(idcoureur));
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/attemptloginuser?pageNumber=0";

        }
        model.addAttribute("etape", matchedEtape);
        String formattedDateDepart = matchedEtape.getDate_depart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        model.addAttribute("datedepartetape", formattedDateDepart);
        model.addAttribute("tabcoureurs", affectation_coureurService.getAffectedCoureurOf(matchedEtape.getPk()));

        //traitement
        Affectation_coureur affectation_coureur_matched = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        List<Etape> etapesPrecedentes = etapeService.getPreviousEtapeOf(Integer.parseInt(idetape));
        List<Etape> etapesSuivantes = etapeService.getNextEtapeOf(Integer.parseInt(idetape));
        LocalDateTime parseddatedepart = LocalDateTime.parse(datedepart + ":" + secondesdepart, formatter);
        LocalDateTime parseddatearrivee = LocalDateTime.parse(datearrivee + ":" + secondesarrivee, formatter);

        //si datedepart superieur a date arrivee
        if (parseddatedepart.isAfter(parseddatearrivee)) {
            model.addAttribute("warning_affectation_temps", "L'heure de depart ne doit pas etre ulterieure a l'heure d'arrivee");
            return "affectation-temps-coureur-admin";
        }
        if (etapesPrecedentes.size() != 0) {
            Etape etapePrecedente = etapesPrecedentes.get(0);
            //si datedepart trop tot par rapport au depart de l'etape precedente
            if ((etapePrecedente.getDate_depart().isEqual(parseddatedepart)) || (etapePrecedente.getDate_depart().isAfter(parseddatedepart))) {
                model.addAttribute("warning_affectation_temps", "Date de depart trop tot par rapport au depart de l'etape : " + matchedEtape.getDate_depart().format(formatter));
                return "affectation-temps-coureur-admin";
            }
        }

        if (etapesSuivantes.size() != 0) {
            Etape etapeSuivante = etapesSuivantes.get(0);
            //si datedepart trop tot par rapport au depart de l'etape precedente
            if ((etapeSuivante.getDate_depart().isEqual(parseddatearrivee)) || (etapeSuivante.getDate_depart().isBefore(parseddatearrivee))) {
                model.addAttribute("warning_affectation_temps", "Date arrivee trop tard par rapport au depart de l'etape suivant : " + etapeSuivante.getDate_depart().format(formatter));
                return "affectation-temps-coureur-admin";
            }
        }

        //raha tsy ampy ilay nb coureurs equipe dia tsy afaka manao insert
        if (affectation_coureurService.getAffectedCoureurOf(matchedCoureur.getEquipe().getPk(), Integer.parseInt(idetape)).size() < matchedEtape.getNbcoureur_equipe()) {
            model.addAttribute("warning_affectation_temps", "L'equipe de ce coureur n'a pas nombre de coureurs requis (" + matchedEtape.getNbcoureur_equipe() +") pour l'etape : " + matchedEtape.getNom());
            return "affectation-temps-coureur-admin";
        }
        
        //raha miotra ilay nb coureurs equipe dia tsy afaka manao insert
        if (affectation_coureurService.getAffectedCoureurOf(matchedCoureur.getEquipe().getPk(), Integer.parseInt(idetape)).size() > matchedEtape.getNbcoureur_equipe()) {
            model.addAttribute("warning_affectation_temps", "L'equipe de ce coureur compte trop de coureurs requis pour l'etape : " + matchedEtape.getNom() + "(" + matchedEtape.getNbcoureur_equipe() + " requis)");
            return "affectation-temps-coureur-admin";
        }

        try {
            affectation_coureur_matched = affectation_coureurService.getAffectationOf(Integer.parseInt(idcoureur), Integer.parseInt(idetape));
            //contrainte raha efa nisy histo inseré mitovy sady any am calcul classement misy manao limit 1
            if (histo_etape_coureurService.getByAffectation(affectation_coureur_matched.getPk()).size() != 0) {
                //raha efa ao sady efa misy temps
                if (histo_etape_coureurService.getByAffectation(affectation_coureur_matched.getPk()).get(0).getHeurearrivee() != null){
                    model.addAttribute("warning_affectation_temps", "Les performances de ce coureur pour cette etape ont deja ete enregistres");
                    return "affectation-temps-coureur-admin";
                }
            }
            //contrainte raha efa nisy histo inseré mitovy sady any am calcul classement misy manao limit 1
            //rehefa ok daholo dia afaka manao insertion
            rowsInserted = histo_etape_coureurService.save(affectation_coureur_matched.getPk(), parseddatedepart, parseddatearrivee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "redirect:/gotoaffectertempscoureur?idetape=" + idetape;
    }

}
