package com.ced.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ced.app.model.Categorie;
import com.ced.app.model.Etape;
import com.ced.app.service.CategorieService;
import com.ced.app.service.ClassementService;
import com.ced.app.service.EtapeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClassementController {
    @Autowired
    private ClassementService classementService;

    @Autowired CategorieService categorieService;

    @Autowired
    private EtapeService etapeService;

    @GetMapping("/getclassement_etape")
    public String getclassement_etape(HttpSession session, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int elementsPerPage, @RequestParam(name = "idetape", defaultValue = "1", required = true) String idetape, Model model)
    {
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
        if (pageNumber < 0) {
            // Redirect to the first page if page value is negative
            return "redirect:/getclassement_etape?pageNumber=0";
        }
        Etape matchedEtape = null;
        try {
            matchedEtape = etapeService.getByPk(Integer.parseInt(idetape));
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/attemptloginuser?pageNumber=0";
        }
        model.addAttribute("etape", matchedEtape);
        model.addAttribute("classement_etape", classementService.getClassementEtape(Integer.parseInt(idetape)));
        model.addAttribute("tabetapes", etapeService.findAllOrderByRang());
        return "classement_par_etape";
    }

    @GetMapping("/getclassement_equipe_categorie")
    public String getclassement_equipe_categorie(HttpSession session, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int elementsPerPage, @RequestParam(name = "idcategorie", defaultValue = "0", required = true) String idcategorie, Model model)
    {
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
        if (pageNumber < 0) {
            // Redirect to the first page if page value is negative
            return "redirect:/getclassement_etape?pageNumber=0";
        }
        int pkcategorie = Integer.parseInt(idcategorie);
        Categorie matchedCategorie = null;
        try {
            matchedCategorie = categorieService.findByPk(pkcategorie);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/attemptloginuser?pageNumber=0";
        }
        model.addAttribute("categorie", matchedCategorie);
        model.addAttribute("classement_equipe", classementService.getClassementEquipe(pkcategorie));
        model.addAttribute("tabcategories", categorieService.getAll());
        return "classement_par_equipe";
    }
}
