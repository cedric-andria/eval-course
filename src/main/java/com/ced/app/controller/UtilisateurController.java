package com.ced.app.controller;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ced.app.model.Affectation_coureur;
import com.ced.app.model.Coureur;
import com.ced.app.model.Equipe;
import com.ced.app.model.Etape;
import com.ced.app.model.Histo_etape_coureur;
import com.ced.app.model.Utilisateur;
import com.ced.app.service.Affectation_coureurService;
import com.ced.app.service.ClassementService;
import com.ced.app.service.EquipeService;
import com.ced.app.service.EtapeService;
import com.ced.app.service.Penalite_equipeService;
import com.ced.app.service.UtilisateurService;
import jakarta.servlet.http.HttpSession;


@Controller
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private EtapeService etapeService;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private Affectation_coureurService affectation_coureurService;

    @Autowired
    private Penalite_equipeService penalite_equipeService;

    @Autowired
    private ClassementService classementService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String connexion(Model model) {
        
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);

        return "connexion_utilisateur";
    }

    @PostMapping("/attemptloginuser")
    public String attemptloginuser(HttpSession session, @RequestParam(name="login", required=true) String login, @RequestParam(name="mdp", required=true) String mdp, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int elementsPerPage, Model model)
    {
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        Utilisateur matchedUser = null;
        String profil = null;
        Equipe equipe = null;
        try {
            matchedUser = utilisateurService.getMatchedUser(login, mdp).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Login ou mot de passe errone");
            
            return "connexion_utilisateur";
        }
        profil = matchedUser.getProfil();
        session.setAttribute("profil", profil);
        List<Etape> tabetapes = etapeService.findAll();
        tabetapes.sort(Comparator.comparingInt(Etape::getRang));
        // Page<Etape> pageEtape = etapeService.findPagedEtape(pageNumber, elementsPerPage, "rang", "asc");
        // List<Etape> tabetapes = pageEtape.getContent();
        // model.addAttribute("currentPage", pageNumber);
        // model.addAttribute("nbpages", pageEtape.getTotalPages());
        if (profil.equalsIgnoreCase("admin")) {
            model.addAttribute("etapes", tabetapes);
            return "liste-etapes-admin";
        }
        // mbola ilay Jour 1
        // if (profil.equalsIgnoreCase("equipe")) {
        //     equipe = equipeService.findByPkUser(matchedUser.getPk()).get(0);
        //     session.setAttribute("equipe_session", equipe);
    
        //     model.addAttribute("etapes", tabetapes);
        //     return "liste-etapes-equipe";
        // }
        // mbola ilay Jour 1
        if (profil.equalsIgnoreCase("equipe")) {
            System.out.println("pk user : " + matchedUser.getPk());
            equipe = equipeService.findByPkUser(matchedUser.getPk()).get(0);
            session.setAttribute("equipe_session", equipe);
            session.setAttribute("utilisateur", matchedUser);
            LinkedHashMap<Integer, List<Histo_etape_coureur>> hashHistoEtape = equipeService.findHistoByEtapeAndEquipe(equipe.getPk());
            model.addAttribute("hashHistoEtape", hashHistoEtape);
            model.addAttribute("equipe", equipe);
            model.addAttribute("etapes", tabetapes);
            model.addAttribute("hashmapMessageExceed", new HashMap<Integer, String>());
            return "liste-etapes-equipe";
        }

        model.addAttribute("error", "profil inconnu");
        return "connexion_utilisateur";
    }

    @GetMapping("/attemptloginuser")
    public String rewriteattemptloginurl(HttpSession session, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int elementsPerPage, Model model)
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
            return "redirect:/attemptloginuser?pageNumber=0";
        }
       
        // Page<Etape> pageEtape = etapeService.findPagedEtape(pageNumber, elementsPerPage, "rang", "asc");
        //  if (pageNumber > pageEtape.getTotalPages() - 1) {
        //     // Redirect to the last page if page value is exceeding
        //     if (pageEtape.getTotalPages() != 0) {
        //         return "redirect:/attemptloginuser?pageNumber=" + (pageEtape.getTotalPages() - 1);
        //     }
        // }
        // pageNumber = 0;
        // pageEtape = etapeService.findPagedEtape(0, elementsPerPage, "rang", "asc");

        // List<Etape> tabetapes = pageEtape.getContent();
        // model.addAttribute("currentPage", pageNumber);
        // model.addAttribute("nbpages", pageEtape.getTotalPages());
        
        List<Etape> tabetapes = etapeService.findAll();
        tabetapes.sort(Comparator.comparingInt(Etape::getRang));

        if (profil.equalsIgnoreCase("admin")) {
            model.addAttribute("etapes", tabetapes);
            return "liste-etapes-admin";
        }
        if (profil.equalsIgnoreCase("equipe")) {
            if (session.getAttribute("equipe_session") == null)
            {
                return "redirect:/";
            }
            model.addAttribute("etapes", tabetapes);
            Equipe equipe = (Equipe)session.getAttribute("equipe_session");
            LinkedHashMap<Integer, List<Histo_etape_coureur>> hashHistoEtape = equipeService.findHistoByEtapeAndEquipe(equipe.getPk());
            model.addAttribute("hashHistoEtape", hashHistoEtape);
            model.addAttribute("equipe", equipe);
            model.addAttribute("etapes", tabetapes);
            model.addAttribute("hashmapMessageExceed", new HashMap<Integer, String>());

            return "liste-etapes-equipe";
        }
        return "connexion_utilisateur";
    }

    @GetMapping("/gotoaffectercoureur")
    public String gotoaffectercoureur(@RequestParam(name = "idetape", required = true) String idetape, HttpSession session, Model model)
    {
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        if (session.getAttribute("equipe_session") == null) {
            return "redirect:/";
        }
        Etape matchedEtape = null;
        try {
            matchedEtape = etapeService.getByPk(Integer.parseInt(idetape));
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/attemptloginuser?pageNumber=0";
        }
        model.addAttribute("etape", matchedEtape);
        model.addAttribute("idetape", idetape);


        //feature message d'erreur 
        List<Coureur> tabCoureurAffectes_equipe = affectation_coureurService.getAffectedCoureurOf(Integer.parseInt(idetape), ((Equipe)session.getAttribute("equipe_session")).getPk());
        if (tabCoureurAffectes_equipe.size() >= matchedEtape.getNbcoureur_equipe()) {
            HashMap<Integer, String> hashmapMessageExceed = new HashMap<>();
            hashmapMessageExceed.put(matchedEtape.getPk(), "Vous avez atteint la limite de coureur pour cette etape");

            List<Etape> tabetapes = etapeService.findAll();
            tabetapes.sort(Comparator.comparingInt(Etape::getRang));
            model.addAttribute("etapes", tabetapes);

            Equipe equipe = (Equipe)session.getAttribute("equipe_session");
            LinkedHashMap<Integer, List<Histo_etape_coureur>> hashHistoEtape = equipeService.findHistoByEtapeAndEquipe(equipe.getPk());
            model.addAttribute("hashHistoEtape", hashHistoEtape);
            model.addAttribute("equipe", equipe);
            model.addAttribute("hashmapMessageExceed", hashmapMessageExceed);

            return "liste-etapes-equipe";
        }
        //feature message d'erreur 

        model.addAttribute("warning_affectation", "");
        model.addAttribute("tabcoureurs", equipeService.findAvailableCoureursOf((Equipe)session.getAttribute("equipe_session"), matchedEtape));
        // session.setAttribute("idetape", idetape);
        return "affectation-coureur-etape";
    }

    @GetMapping("/gotoaffectertempscoureur")
    public String gotoaffectertempscoureur(@RequestParam(name = "idetape", required = true) String idetape, HttpSession session, Model model)
    {
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        if (!session.getAttribute("profil").toString().equalsIgnoreCase("admin")) {
            return "redirect:/";
        }
        Etape matchedEtape = null;
        try {
            matchedEtape = etapeService.getByPk(Integer.parseInt(idetape));
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/attemptloginuser?pageNumber=0";
        }
        String formattedDateDepart = matchedEtape.getDate_depart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        model.addAttribute("warning_affectation_temps", "");
        model.addAttribute("etape", matchedEtape);
        model.addAttribute("datedepartetape", formattedDateDepart);
        model.addAttribute("tabcoureurs", affectation_coureurService.getAffectedCoureurOf(matchedEtape.getPk()));
        model.addAttribute("idetape", idetape);
        // session.setAttribute("idetape", idetape);
        return "affectation-temps-coureur-admin";
    }

    @GetMapping("/gotouploadetapesresultats")
    public String gotouploadetapesresultats(HttpSession session, Model model)
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
        model.addAttribute("errorsetapes", new ArrayList<String>());
        model.addAttribute("errorsresultats", new ArrayList<String>());

        return "importetapesresultats";
    }

    @GetMapping("/gotouploadpoints")
    public String gotouploadpoints(HttpSession session, Model model)
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
        model.addAttribute("errorspoints", new ArrayList<String>());

        return "importpoints";
    }

    @GetMapping("/gotogeneratecategorie")
    public String gotogeneratecategorie(HttpSession session, Model model)
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
        model.addAttribute("erreur", "");

        return "generation_categorie";
    }

    @GetMapping("/gotolistepenalites")
    public String gotolistepenalites(HttpSession session, Model model)
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
        model.addAttribute("erreur", "");
        model.addAttribute("penalites_equipe", penalite_equipeService.getAll());

        return "liste-penalite-admin";
    }

    @GetMapping("/gotoajoutpenalite")
    public String gotoajoutpenalite(HttpSession session, Model model)
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
        model.addAttribute("erreur", "");
        model.addAttribute("etapes", etapeService.findAll());
        model.addAttribute("equipes", equipeService.getAll());
        
        return "ajout-penalite-admin";
    }

    @GetMapping("/gotoresetbase")
    public String gotoresetbase(HttpSession session, Model model)
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
        return "reset_base";
    }

    @GetMapping("/gotolisteetape_resultatadmin")
    public String gotolisteetape_resultatadmin(HttpSession session, Model model)
    {
        if (session.getAttribute("profil") == null) {
            return "redirect:/";
        }
        if (!session.getAttribute("profil").toString().equalsIgnoreCase("admin")) {
            System.out.println("mandalo tsy admin ny profil");
            return "redirect:/";
        }
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        List<Etape> tabEtapes = etapeService.findAll();
        tabEtapes.sort(Comparator.comparingInt(Etape::getRang));
        model.addAttribute("etapes", tabEtapes);
        return "liste-etapes-resultats-admin";
    }

    @GetMapping("/voir_resultat_alea_jour4")
    public String voir_resultat_alea_jour4(@RequestParam(name = "idetape", required = true) String idetape, HttpSession session, Model model)
    {
        model.addAttribute("imports", StaticImportController.head_imports);
        model.addAttribute("sidebar", StaticImportController.sidebar);
        model.addAttribute("settings", StaticImportController.settings);
        model.addAttribute("footer", StaticImportController.footer);
        model.addAttribute("foot_imports", StaticImportController.foot_imports);
        model.addAttribute("classement_equipe", classementService.getClassementEtape(Integer.parseInt(idetape)));
        Etape matchedEtape = null;
        try {
            matchedEtape = etapeService.getByPk(Integer.parseInt(idetape));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("etape", matchedEtape);

        return "classement_chrono_pen_final-admin";
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session, Model model)
    {
        session.invalidate();
        return "redirect:/";
    }

}
