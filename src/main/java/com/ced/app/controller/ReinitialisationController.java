package com.ced.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ced.app.service.ReinitialisationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReinitialisationController {
    @Autowired
    private ReinitialisationService reinitialisationService;

    @GetMapping("/resetbase")
    public String resetbase(HttpSession session, Model model)
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
        String erreur = "";
        try {
            reinitialisationService.resetdatabase();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            erreur = e.getMessage();
        }

        model.addAttribute("erreur", erreur);
        return "reset_base";
       
    }
}
