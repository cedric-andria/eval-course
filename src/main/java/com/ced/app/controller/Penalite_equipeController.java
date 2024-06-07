package com.ced.app.controller;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ced.app.service.Penalite_equipeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Penalite_equipeController {
    @Autowired
    private Penalite_equipeService penalite_equipeService;

    @PostMapping("/ajouterpenalite")
    public String ajouterpenalite(@RequestParam(name = "idetape", required = true) String idetape, @RequestParam(name = "idequipe", required = true) String idequipe, @RequestParam(name = "valeur", required = true) String valeur, HttpSession session, Model model)
    {
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        if (!session.getAttribute("profil").toString().equalsIgnoreCase("admin")) {
            return "redirect:/";
        }
        System.out.println("valeur : " + valeur);
        LocalTime matchedLocalTime = null;
        try {
            matchedLocalTime = LocalTime.parse(valeur);
            penalite_equipeService.save(Integer.parseInt(idetape), Integer.parseInt(idequipe), matchedLocalTime);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "redirect:/gotolistepenalites";
        }
        return "redirect:/gotoajoutpenalite";
    }

    @GetMapping("/supprimerpenalite")
    public String supprimerpenalite(@RequestParam(name = "idpenalite", required = true) String idpenalite, HttpSession session, Model model)
    {
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        if (!session.getAttribute("profil").toString().equalsIgnoreCase("admin")) {
            return "redirect:/";
        }
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        try {
            penalite_equipeService.deleteByPk(Integer.parseInt(idpenalite));
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("penalites_equipe", penalite_equipeService.getAll());
            return "liste-penalite-admin";

        }
        return "redirect:/gotolistepenalites";
    }
   
}
