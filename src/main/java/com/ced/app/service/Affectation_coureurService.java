package com.ced.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.model.Affectation_coureur;
import com.ced.app.model.Coureur;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;


@Service
public class Affectation_coureurService {
    @Autowired
    private Histo_etape_coureurService histo_etape_coureurService;

    @Autowired
    private CoureurService coureurService;

    @Autowired
    private EtapeService etapeService;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Affectation_coureur> getAffectationOf(int idetape)
    {
        List<Affectation_coureur> tabaffectation = new ArrayList<>();
        String select_query = "SELECT * FROM Affectation_coureur where idetape = :idetape";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Affectation_coureur.class);
        native_query.setParameter("idetape", idetape);
        tabaffectation = native_query.getResultList();
        return tabaffectation;
    }

    @Transactional
    public int save(int idcoureur, int idetape) throws Exception
    {
        String nativeQuery = "Insert into Affectation_coureur (idcoureur, idetape) values (:idcoureur, :idetape)";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter("idcoureur", idcoureur);
        query.setParameter("idetape", idetape);
        int rowAffectation_Inserted = query.executeUpdate();
        System.out.println("Affectation coureur inserted : " + rowAffectation_Inserted);

        if (rowAffectation_Inserted != 0) {
            Affectation_coureur affectation_matched = getAffectationOf(idcoureur, idetape);
            histo_etape_coureurService.save(affectation_matched.getPk(), affectation_matched.getEtape().getDate_depart(), null);
        }
        return rowAffectation_Inserted;
    }

    @SuppressWarnings("unchecked")
    public List<Coureur> getAffectedCoureurOf(int idetape)
    {
        List<Coureur> tabcoureurs = new ArrayList<>();
        String select_query = "SELECT * FROM Coureur where pk IN (select idcoureur from affectation_coureur where idetape = :idetape)";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Coureur.class);
        native_query.setParameter("idetape", idetape);
        tabcoureurs = native_query.getResultList();
        for (Coureur coureur : tabcoureurs) {
            // coureur.setAge(Period.between(coureur.getDatenaissance(), LocalDate.now()).getYears());
            coureur.setAge(LocalDate.now().getYear() - coureur.getDatenaissance().getYear());

        }
        return tabcoureurs;
    }

    @SuppressWarnings("unchecked")
    public List<Coureur> getAffectedCoureurOf(int idequipe, int idetape)
    {
        List<Coureur> tabcoureur = new ArrayList<>();
        String select_query = "SELECT * FROM Coureur where pk IN (select idcoureur from affectation_coureur where idetape = :idetape) and idequipe = :idequipe";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Coureur.class);
        native_query.setParameter("idetape", idetape);
        native_query.setParameter("idequipe", idequipe);
        tabcoureur = native_query.getResultList();
        return tabcoureur;
    }

    public Affectation_coureur getAffectationOf(int idcoureur, int idetape)
    {
        Affectation_coureur affectation = null;
        String select_query = "SELECT * FROM Affectation_coureur where idetape = :idetape and idcoureur = :idcoureur order by pk desc limit 1";
        jakarta.persistence.Query native_query = entityManager.createNativeQuery(select_query, Affectation_coureur.class);
        native_query.setParameter("idetape", idetape);
        native_query.setParameter("idcoureur", idcoureur);
        affectation = (Affectation_coureur)native_query.getSingleResult();
        return affectation;
    }

    public List<Affectation_coureur> getAll(Connection connect) throws Exception
    {
        List<Affectation_coureur> tabAffectation_coureurs = new ArrayList<>();
        Statement stmt = null;
        ResultSet rst = null;
        try {
            stmt = connect.createStatement();
            rst = stmt.executeQuery("select * from affectation_coureur");
            if(!rst.isBeforeFirst()){
                throw new Exception("affectation_coureur(connection) get all vide");
            }
            while (rst.next()) {
                tabAffectation_coureurs.add(new Affectation_coureur(rst.getString("id"), coureurService.findByPk(connect, rst.getInt("idcoureur")), etapeService.findByPk(connect, rst.getInt("idetape")), rst.getInt("pk")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally{
            rst.close();
            stmt.close();
        }
        return tabAffectation_coureurs;
    }

    public List<Affectation_coureur> getAllOrderByPk(Connection connect) throws Exception
    {
        List<Affectation_coureur> tabAffectation_coureurs = new ArrayList<>();
        Statement stmt = null;
        ResultSet rst = null;
        try {
            stmt = connect.createStatement();
            rst = stmt.executeQuery("select * from affectation_coureur order by pk asc");
            if(!rst.isBeforeFirst()){
                System.out.println("Vide ilay getAllOrderByPk");
                throw new Exception("affectation_coureur(connection) get all vide");
            }
            while (rst.next()) {
                tabAffectation_coureurs.add(new Affectation_coureur(rst.getString("id"), coureurService.findByPk(connect, rst.getInt("idcoureur")), etapeService.findByPk(connect, rst.getInt("idetape")), rst.getInt("pk")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally{
            rst.close();
            stmt.close();
        }
        return tabAffectation_coureurs;
    }
}
