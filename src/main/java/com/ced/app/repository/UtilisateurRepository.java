package com.ced.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ced.app.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer>{
    List<Utilisateur> findUtilisateurByLoginAndMdp(@Param("login") String login, @Param("mdp") String mdp);

}
