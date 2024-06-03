package com.ced.app.controller;

import org.springframework.stereotype.Controller;

@Controller
public class CategorieController {
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
            return "redirect:/gotoclassement_etape";
        }
        model.addAttribute("etape", matchedEtape);
        model.addAttribute("classement_etape", classementService.getClassementEtape(Integer.parseInt(idetape)));
        model.addAttribute("tabetapes", etapeService.findAllOrderByRang());
        return "classement_par_etape";
       
    }
}
