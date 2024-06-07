package com.ced.app.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ced.app.model.Categorie;
import com.ced.app.model.Classement_equipe;
import com.ced.app.model.Coureur;
import com.ced.app.model.Equipe;
import com.ced.app.model.Etape;
import com.ced.app.service.ClassementService;
import com.ced.app.service.CoureurService;
import com.ced.app.service.EquipeService;
import com.ced.app.service.EtapeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EquipeController {
    @Autowired
    private EquipeService equipeService;

    @Autowired
    private CoureurService coureurService;

    @Autowired
    private EtapeService etapeService;

    @Autowired
    private ClassementService classementService;

    @GetMapping("/gotodetailsequipe")
    public String gotodetailsequipe(HttpSession session, @RequestParam(name = "idequipe", required = true) String idequipe, Model model)
    {
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        String profil = null;
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        profil = session.getAttribute("profil").toString();
        
        HashMap<Integer, Double> hashMapTotalPointsInEtape = new HashMap<>();

        List<Coureur> tabCoureursOfEquipe = coureurService.getCoureursOf(Integer.parseInt(idequipe));
        List<Etape> tabEtapes = etapeService.findAll();

        for (Etape etape : tabEtapes) {
            double pointEtape = 0;
            List<Coureur> coureursClassesHorsEquipe = classementService.getClassementEtape(etape.getPk()).getClassement();
            for (Coureur coureur : coureursClassesHorsEquipe) {
                for (Coureur coureur_equipe : tabCoureursOfEquipe) {
                    if(coureur.getPk() ==coureur_equipe.getPk())
                    {
                        pointEtape += coureur.getPointtotal();
                    }
                }
            }
            hashMapTotalPointsInEtape.put(etape.getPk(), pointEtape);
        }

        model.addAttribute("etapes", etapeService.findAll());
        model.addAttribute("hashMapPointsEtapes", hashMapTotalPointsInEtape);
        
        return "details_equipe_par_coureur";
    }

}
