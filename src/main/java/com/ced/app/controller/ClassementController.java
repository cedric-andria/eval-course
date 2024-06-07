package com.ced.app.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ced.app.model.Categorie;
import com.ced.app.model.Classement_equipe;
import com.ced.app.model.Equipe;
import com.ced.app.model.Etape;
import com.ced.app.service.CategorieService;
import com.ced.app.service.ClassementService;
import com.ced.app.service.EtapeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.itextpdf.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Controller
public class ClassementController {
    @Autowired
    private ClassementService classementService;

    @Autowired CategorieService categorieService;

    @Autowired
    private EtapeService etapeService;

    @GetMapping("/getclassement_etape")
    public String getclassement_etape(HttpSession session, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int elementsPerPage, @RequestParam(name = "idetape", defaultValue = "1", required = true) String idetape, @RequestParam(name = "idcategorie", defaultValue = "1", required = true) String idcategorie, Model model)
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
        Categorie matchedCategorie = null;
        try {
            matchedCategorie = categorieService.findByPk(Integer.parseInt(idcategorie));
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/attemptloginuser?pageNumber=0";
        }
        model.addAttribute("etape", matchedEtape);
        model.addAttribute("categorie", matchedCategorie);
        model.addAttribute("classement_etape", classementService.getClassementEtape(Integer.parseInt(idetape)));
        model.addAttribute("classement_etape_categorie", classementService.getClassementEtapeWithCat(Integer.parseInt(idetape), Integer.parseInt(idcategorie)));
        model.addAttribute("tabetapes", etapeService.findAllOrderByRang());
        model.addAttribute("tabcategories", categorieService.getAll());


        return "classement_par_etape";
    }

    @GetMapping("/getclassement_equipe_categorie")
    public String getclassement_equipe_categorie(HttpSession session, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int elementsPerPage, @RequestParam(name = "idcategorie", defaultValue = "0", required = true) String idcategorie, Model model)
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
        if (pageNumber < 0) {
            // Redirect to the first page if page value is negative
            return "redirect:/getclassement_etape?pageNumber=0";
        }
        int pkcategorie = Integer.parseInt(idcategorie);
        
        Categorie matchedCategorie = null;
        if (pkcategorie != 0) {
            try {
            matchedCategorie = categorieService.findByPk(pkcategorie);
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/attemptloginuser?pageNumber=0";
            }
        }
        Classement_equipe class_equipe = classementService.getClassementEquipe(pkcategorie);
        for (int i = 0; i < class_equipe.getClassement().size(); i++) {
            if (i > 0 && class_equipe.getClassement().get(i).getPoints() == class_equipe.getClassement().get(i - 1).getPoints()) {
                // If the current racer has the same duration as the previous racer, assign the same rank
                class_equipe.getClassement().get(i).setHas_exaequo(1);
                class_equipe.getClassement().get(i - 1).setHas_exaequo(1);

                // System.out.println("class_equipe.getClassement().get(i).getHasexaequo : " + class_equipe.getClassement().get(i).getHas_exaequo());
            } else {
                // Otherwise, assign the current rank
                class_equipe.getClassement().get(i).setHas_exaequo(0);
                // System.out.println("tabequipes.get(i).getHasexaequo : " + tabequipes.get(i).getHas_exaequo());
            }
        }
        model.addAttribute("classement_equipe", class_equipe);
        HashMap<String, Double> equipepointsMap = new HashMap<>();
        for (Equipe equipe : classementService.getClassementEquipe(pkcategorie).getClassement()) {
            equipepointsMap.put(equipe.getNom(), equipe.getPoints());
        }
        model.addAttribute("yesString", "yes");

        model.addAttribute("tabcategories", categorieService.getAll());
        if (!session.getAttribute("profil").toString().equalsIgnoreCase("admin")) {
            model.addAttribute("equipepointsMap", new HashMap<>());
            model.addAttribute("gagnant", "");
            model.addAttribute("isAdmin", "no");
        }
        else{
            model.addAttribute("equipepointsMap", equipepointsMap);
            model.addAttribute("gagnant", classementService.getClassementEquipe(pkcategorie).getClassement().get(0).getNom());
            model.addAttribute("categorie", matchedCategorie);
            model.addAttribute("isAdmin", "yes");

        }
        return "classement_par_equipe";
    }

    @GetMapping("/exportcertificatPDF")
    public String exportcertificatPDF(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "idcategorie", defaultValue = "0", required = true) String idcategorie, @RequestParam(name = "gagnant", required = true) String gagnant, HttpSession session) throws IOException, DocumentException, com.lowagie.text.DocumentException 
    {
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        if (!session.getAttribute("profil").toString().equalsIgnoreCase("admin")) {
            return "redirect:/";
        }
        // Définir les en-têtes de la réponse HTTP pour indiquer que le contenu est un fichier PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"certificat.pdf\"");
        
        String cat_certificat = "";
        if (Integer.parseInt(idcategorie) == 0) {
            cat_certificat = "Toutes categories";
        }
        else{
            cat_certificat = categorieService.findByPk(Integer.parseInt(idcategorie)).getNom();
        }

        String htmlContent = "<html>\r\n"+
        "<head>\r\n"+
            "<style type='text/css'>\r\n"+
                "body, html {\r\n"+
                    "margin: 0;\r\n"+
                    "padding: 0;\r\n"+
                "}\r\n"+
                "body {\r\n"+
                    "color: black;\r\n"+
                    "display: table;\r\n"+
                    "font-family: Georgia, serif;\r\n"+
                    "font-size: 24px;\r\n"+
                    "text-align: center;\r\n"+
                "}\r\n"+
                ".container {\r\n"+
                    "border: 20px solid tan;\r\n"+
                    "width: 750px;\r\n"+
                    "height: 563px;\r\n"+
                    "display: table-cell;\r\n"+
                    "vertical-align: middle;\r\n"+
                "}\r\n"+
                ".logo {\r\n"+
                    "color: tan;\r\n"+
                "}\r\n"+
                ".marquee {\r\n"+
                    "color: tan;\r\n"+
                    "font-size: 48px;\r\n"+
                    "margin: 20px;\r\n"+
                "}\r\n"+
                ".assignment {\r\n"+
                    "margin: 20px;\r\n"+
                "}\r\n"+
                ".person {\r\n"+
                    "border-bottom: 2px solid black;\r\n"+
                    "font-size: 32px;\r\n"+
                    "font-style: italic;\r\n"+
                    "margin: 20px auto;\r\n"+
                    "width: 400px;\r\n"+
                "}\r\n"+
                ".reason {\r\n"+
                    "margin: 20px;\r\n"+
                "}\r\n"+
            "</style>\r\n"+
        "</head>\r\n"+
        "<body>\r\n"+
            "<div class=\"container\">\r\n"+
                "<div class=\"logo\">\r\n"+
                    "Chère equipe\r\n"+
                "</div>\r\n"+
                "<div class=\"marquee\">\r\n"+
                    "Félicitations\r\n"+
                "</div>\r\n"+
                "<div class=\"assignment\">\r\n"+
                    "Ce certificat est attribué à l'équipe\r\n"+
                "</div>\r\n"+
                "<div class=\"person\">\r\n"+
                    "" + gagnant + "\r\n"+
                "</div>\r\n"+
                "<div class=\"reason\">\r\n"+
                    "Pour avoir remporté la course <br/>\r\n"+
                    "categorie <i>" + cat_certificat + "</i>\r\n"+
                "</div>\r\n"+
            "</div>\r\n"+
        "</body>\r\n"+
    "</html>";
        

        // Créer un objet ITextRenderer
        ITextRenderer renderer = new ITextRenderer();

        // Charger le contenu HTML dans l'objet ITextRenderer
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
    
        // Créer le flux de sortie pour le fichier PDF
        
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // Générer le fichier PDF dans le flux de sortie
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            // Fermer le flux de sortie
            outputStream.close();
            System.out.println("PDF output stream closed");
        }
        return "redirect:/getclassement_equipe_categorie";
    }
}
