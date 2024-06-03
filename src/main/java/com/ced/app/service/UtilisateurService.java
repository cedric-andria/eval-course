package com.ced.app.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Utilisateur;
import com.ced.app.repository.UtilisateurRepository;


@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository UtilisateurRepository;


    public List<Utilisateur> getAllUtilisateurs() {
        return UtilisateurRepository.findAll();
    }

    public void save(Utilisateur utilisateur)
    {
        UtilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> getMatchedUser(String login, String motdepasse)
    {
        return UtilisateurRepository.findUtilisateurByLoginAndMdp(login, motdepasse);
    }

    // public List<Utilisateur> getMatchedUser(String telephone)
    // {
    //     List<Utilisateur> matchedUser = UtilisateurRepository.findUtilisateurByTelephone(telephone);
    //     Utilisateur newUser = null;
    //     if(matchedUser.size() == 0)
    //     {
    //         newUser = new Utilisateur(null, null, telephone, "client");
    //         try {
    //             save(newUser);
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //         matchedUser.clear();
    //         matchedUser.add(newUser);
    //     }
    //     return matchedUser;

    // }
}
