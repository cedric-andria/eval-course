package com.ced.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Categorie;
import com.ced.app.model.Categorie_coureur;
import com.ced.app.model.Coureur;
import com.ced.app.repository.CategorieRepository;
import com.ced.app.repository.CoureurRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class CategorieService {
    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private CoureurRepository coureurRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Categorie> getAll()
    {
        return categorieRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    public Categorie findByPk(int idcategorie)
    {
        List<Categorie> tabCategorie = new ArrayList<>();
        Categorie matchedCategorie = null;
        String nativeQuery = "SELECT * FROM categorie where pk = :pk";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Categorie.class);
        query.setParameter("pk", idcategorie);
        tabCategorie = query.getResultList();
        matchedCategorie = tabCategorie.get(0);
        
        return matchedCategorie;
    }

    public Categorie getByNom(String nom) throws Exception
    {
        Categorie matchedCategorie = null;
        try {
            matchedCategorie = categorieRepository.findByNom(nom).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("categorie get by nom vide");
        }
        return matchedCategorie;
    }

    public Categorie save(Categorie categorie)
    {
        return categorieRepository.save(categorie);
    }

    @Transactional
    public String generateCategorie() throws Exception
    {
        String erreur = "Categories generees avec succes";
        String[] categories = {"Homme", "Femme", "Junior"};
        String nativeQuery = null;
        int categorierowsInserted = 0;
        int categorie_coureurInserted = 0;

        for (String categorie : categories) {
            //raha efa mis yilay categorie dia tsy averina
            Categorie duplicatecategorie = null;
            try {
                duplicatecategorie = getByNom(categorie);
                if (duplicatecategorie != null) {
                    throw new Exception("Trying to insert duplicate value on Categorie");
                }
            } catch (Exception e) {
                // TODO: handle exception
                //tsy misy duplique satria get(0) null
            }
            //raha efa mis ilay categorie dia tsy averina

            nativeQuery = "Insert into categorie (nom) values (:nom)";
            jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Categorie.class);
            query.setParameter("nom", categorie);
            categorierowsInserted += query.executeUpdate();
        }
        System.out.println("categories inserted : " + categorierowsInserted);

        List<Coureur> tabCoureurs = coureurRepository.findAll();
        for (Coureur coureur : tabCoureurs) {
            //test genre
            if (coureur.getGenre().getNom().equalsIgnoreCase("H")) {
                Categorie matchedCategorie = getByNom("Homme");
                nativeQuery = "Insert into Categorie_coureur (idcoureur, idcategorie) values (:idcoureur , :idcategorie)";
                jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Categorie_coureur.class);
                query.setParameter("idcoureur", coureur.getPk());
                query.setParameter("idcategorie", matchedCategorie.getPk());
                categorie_coureurInserted += query.executeUpdate();
            }
            if (coureur.getGenre().getNom().equalsIgnoreCase("F")) {
                Categorie matchedCategorie = getByNom("Femme");
                nativeQuery = "Insert into Categorie_coureur (idcoureur, idcategorie) values (:idcoureur , :idcategorie)";
                jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Categorie_coureur.class);
                query.setParameter("idcoureur", coureur.getPk());
                query.setParameter("idcategorie", matchedCategorie.getPk());
                categorie_coureurInserted += query.executeUpdate();
            }
            //test genre

            //test age
            if ((LocalDate.now().getYear() - coureur.getDatenaissance().getYear()) < 18) {
                Categorie matchedCategorie = getByNom("Junior");
                nativeQuery = "Insert into Categorie_coureur (idcoureur, idcategorie) values (:idcoureur , :idcategorie)";
                jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Categorie_coureur.class);
                query.setParameter("idcoureur", coureur.getPk());
                query.setParameter("idcategorie", matchedCategorie.getPk());
                categorie_coureurInserted += query.executeUpdate();
            }
            //test age
            System.out.println("categorie_coureur inserted : " + categorie_coureurInserted);

        }
        return erreur;
    }
}
