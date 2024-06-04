package com.ced.app.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ced.app.data.ConnectSQL;
import com.ced.app.model.TempEtape;
import com.ced.app.model.TempResultat;
import com.ced.app.service.ImportEtapeService;
import com.ced.app.service.ImportResultatService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ImportEtapeResultatController {
    @Autowired
    private ImportEtapeService importEtapeService;

    @Autowired
    private ImportResultatService importResultatService;

    @PostMapping("/preparetempetaperesultat")
    public String preparetempetaperesultat(@RequestParam(name = "fileetape", required = true) MultipartFile fileetape, @RequestParam(name = "fileresultat", required = true) MultipartFile fileresultat, HttpSession session, Model model)
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

        HashMap<Integer, String> errorsetapes = new HashMap<>();
        HashMap<Integer, String> errorsresultats = new HashMap<>();

        String fileetapename = fileetape.getOriginalFilename();
        String fileresultatname = fileresultat.getOriginalFilename();

        try {
            byte[] bytesetape = fileetape.getBytes();
            byte[] bytesresultat = fileresultat.getBytes();

            Path pathetape = Paths.get("imports/" + fileetapename);
            Path pathresultat = Paths.get("imports/" + fileresultatname);

            Files.write(pathetape, bytesetape);
            Files.write(pathresultat, bytesresultat);

            File importedFileetape = new File("imports/" + fileetapename);
            File importedFileresultat = new File("imports/" + fileresultatname);

            String absPathetape = importedFileetape.getAbsolutePath();
            String absPathresultat = importedFileresultat.getAbsolutePath();

            errorsetapes = importEtapeService.importCSV(absPathetape);
            //tsy maintsy alefa ato ilay processimportetape satria misy donnees typemaison ilain'ny importResultat.importCSV
            Connection connect = null;
            try {
                connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");
                importEtapeService.processSplitInsertion(connect);
                errorsresultats = importResultatService.importCSV(absPathresultat, connect);
                session.setAttribute("connect", connect);
            } catch (Exception e) {
                e.printStackTrace();
                connect.rollback();
                connect.close();
            }
            //tsy atao finally close satria mbola apesain'ilay eo processsplit eo ambany
            // finally{
            //     if (connect != null) {
            //         try {
            //             if (!connect.isClosed()) {
            //                 connect.close();
            //             }
            //         }
            //         catch(Exception e)
            //         {
            //             e.printStackTrace();
            //         }
            //     }
            // }

            // errorsresultats = importDevisService.importCSV(absPathdevis, connect);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            if ((errorsetapes.size() != 0) || (errorsresultats.size() != 0)) {
            
                model.addAttribute("errorsetapes", errorsetapes);
                model.addAttribute("errorsresultats", errorsresultats);
    
                return "importetapesresultats";
            }
        } catch(Exception ex)
        {
            ex.printStackTrace();
            return "redirect:/gotouploadetapesresultats";
        }
        
        model.addAttribute("errorsetapes", new HashMap<>());
        model.addAttribute("errorsresultats", new HashMap<>());
        //miditra am verification erreur coherence de donnees
        return "page_error_import_etape_resultat";
    }

    @GetMapping("/processimportetaperesultat")
    public String processimportetaperesultat(HttpSession session, Model model)
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
        
        List<HashMap<Integer, String>> errorsetape = new ArrayList<>();
        List<HashMap<Integer, String>> errorsresultat = new ArrayList<>();

    
        List<TempEtape> temptablesetape = new ArrayList<>();
        List<TempResultat> temptablesresultat = new ArrayList<>();

        Connection connect = null;
        try {
            temptablesetape = importEtapeService.getTemptables();
            // connect = ConnectSQL.getConnection("postgres", "btp", "postgres", "root");
            connect = (Connection)session.getAttribute("connect");
            temptablesresultat = importResultatService.getTemptables(connect);

            //verification des contraintes
            if(temptablesetape.size() == 0)
            {
                errorsetape.add(new HashMap<>(Map.of(0, "Table temporaire vide. Veuillez reverifier l'import")));
                model.addAttribute("errorsetapes", errorsetape);

                return "importetapesresultats";
            }
            System.out.println("Vita if temptablesmt.size == 0");
            if(temptablesresultat.size() == 0)
            {
                errorsresultat.add(new HashMap<>(Map.of(0, "Table temporaire vide. Veuillez reverifier l'import")));
                model.addAttribute("errorsresultats", errorsresultat);

                return "importetapesresultats";
            }
            System.out.println("Vita if temptablesresultat.size == 0");

            for (TempEtape temptable : temptablesetape) {
                // if (temptable.getDateheuredebut().isAfter(temptable.getDateheurefin())) {
                //     errors.add(new HashMap<>(Map.of(temptable.getLinenumber(), "Date heure debut ne doit pas etre superieur a date heure fin")));
                // }
                // if (temptable.getAgeminimum() > 18)
                // {
                //     errors.add(new HashMap<>(Map.of(temptable.getLinenumber(), "La limite d'age ne doit pas etre superieur a 18 ans")));
                // }
                if (temptable.getLongueur() < 1) {
                    errorsetape.add(new HashMap<>(Map.of(temptable.getLinenumber(), "Distance trop petite")));
                }
                // izay contrainte omena am alea
            }

            // for (TempDevis temptable : temptablesdevis) {
                // if (temptable.getDateheuredebut().isAfter(temptable.getDateheurefin())) {
                //     errors.add(new HashMap<>(Map.of(temptable.getLinenumber(), "Date heure debut ne doit pas etre superieur a date heure fin")));
                // }
                // if (temptable.getAgeminimum() > 18)
                // {
                //     errors.add(new HashMap<>(Map.of(temptable.getLinenumber(), "La limite d'age ne doit pas etre superieur a 18 ans")));
                // }
                //izay contrainte omena am alea
            // }

            //raha misy erreurs dia miverina any ambony
            if ((errorsetape.size() != 0) || (errorsresultat.size() != 0)) {
                model.addAttribute("errorsetapes", errorsetape);
                model.addAttribute("errorsresultats", errorsresultat);
                // System.out.println("0 error avant reset table importmaison");
                // importMaisonTravauxService.resettemptable(connect);
                // System.out.println("0 error apres reset table importmaison");
                // System.out.println("0 error avant reset table import devis");
                // importDevisService.resettemptable(connect);
                // System.out.println("0 error apres reset table import devis");
                connect.rollback();
                connect = (Connection)session.getAttribute("connect");
                importEtapeService.resettemptable(connect);
                connect.commit();
                return "page_error_import_etape_resultat";
            }
            // Connection connect = null;
            try {
                System.out.println("tsisy olana avant processplit insertion resultat");
                // connect = ConnectSQL.getConnection("postgres", "btp", "postgres", "root");
                importResultatService.processSplitInsertion(connect);
                System.out.println("tsisy olana apres processplit insertion resultat");
                //raha efa tsisy erreur teo am split insert rehetra avy am travauxmaison sy devis zay vao mi-commit
                importEtapeService.resettemptable(connect);
                importResultatService.resettemptable(connect);
                connect.commit();
                // devisService.updateMontantTotalDevis_import(null, connect);
                
            } catch (Exception e) {
                e.printStackTrace();
                connect.rollback();
            }
            finally{
                if (connect != null) {
                    try {
                        if (!connect.isClosed()) {
                            connect.close();
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorsetape.add(new HashMap<>(Map.of(0, e.getMessage())));
            errorsresultat.add(new HashMap<>(Map.of(0, e.getMessage())));
            model.addAttribute("errorsetapes", errorsetape);
            model.addAttribute("errorsresultats", errorsresultat);
            return "page_error_import_etape_resultat";
        }
        finally{
            if (connect != null) {
                try {
                    if (!connect.isClosed()) {
                        connect.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // for (Integer key : errors.get(0).keySet()) {
        //     System.out.println("errors : " + errors.get(0).get(key));
        // }
        // try {
        //     importMaisonTravauxService.resettemptable();
        //     importDevisService.resettemptable();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        
        return "redirect:/gotouploadetapesresultats";
    }
}
