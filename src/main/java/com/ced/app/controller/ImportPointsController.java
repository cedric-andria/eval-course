package com.ced.app.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
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
import com.ced.app.model.TempPoints;
import com.ced.app.service.ImportPointsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ImportPointsController {
    @Autowired
    private ImportPointsService importPointsService;

    @PostMapping("/preparetemppoints")
    public String preparetemppoints(@RequestParam(name = "filepoints", required = true) MultipartFile filepoints, HttpSession session, Model model)
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
        HashMap<Integer, String> errorspoints = new HashMap<>();

        String filepointsname = filepoints.getOriginalFilename();

        try {
            byte[] bytespoints = filepoints.getBytes();

            Path pathpoints = Paths.get("imports/" + filepointsname);

            Files.write(pathpoints, bytespoints);

            File importedFilepoints = new File("imports/" + filepointsname);
            String absPathpoints = importedFilepoints.getAbsolutePath();

            errorspoints = importPointsService.importCSV(absPathpoints);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/gotouploadpaiement";
        }
        if (errorspoints.size() != 0)  {
            
            model.addAttribute("errors", errorspoints);

            return "importpaiement";
        }
        //miditra am verification erreur coherence de donnees
        model.addAttribute("errors", errorspoints);

        return "page_error_import_points";
    }

    @GetMapping("/processimportpoints")
    public String processimportpoints(HttpSession session, Model model)
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
        
        List<HashMap<Integer, String>> errors = new ArrayList<>();

    
        List<TempPoints> temptables = new ArrayList<>();

        try {
            temptables = importPointsService.getTemptables();
            System.out.println("temppoints size : " + temptables.size());
            //verification des contraintes
            if(temptables.size() == 0)
            {
                errors.add(new HashMap<>(Map.of(0, "Table temporaire vide. Veuillez reverifier l'import")));
                importPointsService.resettemptable();
                model.addAttribute("errors", errors);

                return "importpoints";
            }

            for (TempPoints temptable : temptables) {
                // if (temptable.getDateheuredebut().isAfter(temptable.getDateheurefin())) {
                //     errors.add(new HashMap<>(Map.of(temptable.getLinenumber(), "Date heure debut ne doit pas etre superieur a date heure fin")));
                // }
                // if (temptable.getAgeminimum() > 18)
                // {
                //     errors.add(new HashMap<>(Map.of(temptable.getLinenumber(), "La limite d'age ne doit pas etre superieur a 18 ans")));
                // }
                //izay contrainte omena am alea
            }

            //raha misy erreurs dia miverina any ambony
            if (errors.size() != 0) {
                model.addAttribute("errors", errors);
                importPointsService.resettemptable();
                return "page_error_import_points";
            }
            Connection connect = null;
            try {
                connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");
                importPointsService.processSplitInsertion(connect);
                //rehefa tsisy olana
                connect.commit();
                importPointsService.resettemptable();

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    importPointsService.resettemptable();
                } catch (Exception resetex) {
                    resetex.printStackTrace();
                }
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
            errors.add(new HashMap<>(Map.of(0, e.getMessage())));
            model.addAttribute("errors", errors);
            return "page_error_import_points";

        }
        // for (Integer key : errors.get(0).keySet()) {
        //     System.out.println("errors : " + errors.get(0).get(key));
        // }
        
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/gotouploadpoints";
    }
}
