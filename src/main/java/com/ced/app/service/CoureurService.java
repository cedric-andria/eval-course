package com.ced.app.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Coureur;
import com.ced.app.repository.CoureurRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CoureurService {
    @Autowired
    private CoureurRepository coureurRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public Coureur findByPk(int pk)
    {
        List<Coureur> tabcoureur = new ArrayList<>();
        Coureur matchedCoureur = null;
        String nativeQuery = "SELECT * FROM coureur where pk = :pk";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Coureur.class);
        query.setParameter("pk", pk);
        tabcoureur = query.getResultList();
        matchedCoureur = tabcoureur.get(0);
        
        return matchedCoureur;
    }

    public Coureur findByPk(Connection connect, int pk) throws Exception
    {
        Statement stmt = null;
        ResultSet rst = null;
        List<Coureur> tabcoureur = new ArrayList<>();
        Coureur matchedCoureur = null;
        String query = "select * from coureur where pk = " + pk;

        try {
            stmt = connect.createStatement();
            rst = stmt.executeQuery(query);
            if(!rst.isBeforeFirst())
            {
                throw new Exception("Coureur findbyPk(connection) vide");
            }
            while (rst.next()) {
                tabcoureur.add(new Coureur(rst.getInt("pk"), rst.getString("dossard"), rst.getString("nom")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            rst.close();
            stmt.close();
        }
        
        matchedCoureur = tabcoureur.get(0);
        return matchedCoureur;

    }

    @SuppressWarnings("unchecked")
    public List<Coureur> getCoureursOf(int idequipe)
    {
        List<Coureur> tabCoureurs = new ArrayList<>();
        String nativeQuery = "SELECT * FROM coureur where idequipe = :idequipe";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Coureur.class);
        query.setParameter("idequipe", idequipe);
        tabCoureurs = query.getResultList();
        return tabCoureurs;
    }
}
