package com.ced.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ced.app.data.ConnectSQL;
import com.ced.app.model.Affectation_coureur;
import com.ced.app.model.TempResultat;
import com.ced.app.other.Tools;

@Service
public class ImportResultatService {

    @Autowired
    private Affectation_coureurService affectation_coureurService;

    public HashMap<Integer, String> importCSV(String fileAbsPath, Connection connect) throws Exception
    {
        HashMap<Integer, String> errors = new HashMap<>();
        // Connection connect = null;
        Statement stmt = null;
        PreparedStatement prpstmt = null;
        try {
            // connect = ConnectSQL.getConnection("postgres", "btp", "postgres", "root");
            errors = Tools.ImportCsvByLine(connect, fileAbsPath, "tempresultat", ",", true);

            stmt = connect.createStatement();
            if (!Tools.columnExists(connect, "tempresultat", "linenumber")) {
                stmt.executeUpdate("alter table tempresultat add column linenumber serial");
            }
           
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally{
            // if (connect != null) {
            //     if (!connect.isClosed()) {
            //         connect.close();
            //     }
            // }
            if (stmt != null)
            {
                if (!stmt.isClosed()) {
                    stmt.close();
                }
            }
            if (prpstmt != null) {
                if (!prpstmt.isClosed()) {
                    prpstmt.close();
                }
            }
        }
        return errors;
    }

    public List<TempResultat> getTemptables(Connection connect) throws Exception
    {
        List<TempResultat> temptables = new ArrayList<>();
        // Connection connect = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            String query = "select * from tempresultat";
            // System.out.println("0 probleme avant connexion");
            connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");
            stmt = connect.createStatement();
            // System.out.println("0 probleme apres createStatement");
            rslt = stmt.executeQuery(query);
            // System.out.println("0 probleme apres executeQuery");
            // System.out.println("Eto mbola tsisy probleme");
            while(rslt.next())
            {
                temptables.add(new TempResultat(rslt.getInt("etape_rang"), rslt.getString("numero_dossard"), rslt.getString("nom"), rslt.getString("genre"),  rslt.getDate("date_naissance").toLocalDate(), rslt.getString("equipe"), rslt.getTimestamp("arrivee").toLocalDateTime(), rslt.getInt("linenumber")));
            }
            // System.out.println("Vita while");
        } 
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally{
            if (connect != null) {
                try {
                    rslt.close();
                    stmt.close();
                    // if (!connect.isClosed()) {
                    //     connect.close();
                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return temptables;
    }

    public void processSplitInsertion(Connection connect) throws Exception
    {
        // Connection connect = null;
        Statement stmt = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // ResultSet rslt = null;
        try {
            String queryToGenre = "insert into Genre (nom) select distinct genre from tempresultat where genre NOT IN (select nom from genre)";
            String queryToUtilisateur = "insert into Utilisateur (login, mdp, profil) select distinct t.equipe, t.equipe, 'equipe' as profil from tempresultat t where t.equipe, t.equipe, profil NOT IN (select login, mdp, profil from utilisateur)";
            String queryToEquipe = "insert into Equipe (nom) select DISTINCT equipe from tempresultat where equipe NOT IN (select nom from equipe)";
            String queryToCoureur = "insert into Coureur (dossard, nom, idgenre, datenaissance, idequipe) select DISTINCT t.numero_dossard, t.genre, g.pk, t.date_naissance, e.pk from tempresultat t JOIN genre g ON g.nom = t.genre JOIN equipe e ON e.nom = t.equipe where t.genre, g.pk, t.date_naissance, e.pk NOT IN (select dossard, nom, idgenre, datenaissance, idequipe from coureur)";
            String queryToAffectation = "insert into Affectation_coureur (idcoureur, idcetape) select DISTINCT c.pk, e.pk from tempresultat t join coureur c on c.nom = t.nom join etape e on e.rang = t.etape_rang where c.pk, e.pk NOT IN (select idcoureur, idetape from Affectation_coureur)";

            stmt = connect.createStatement();
            stmt.executeUpdate(queryToGenre);
            stmt.executeUpdate(queryToUtilisateur);
            stmt.executeUpdate(queryToEquipe);
            stmt.executeUpdate(queryToCoureur);
            stmt.executeUpdate(queryToAffectation);

            for (Affectation_coureur affectation : affectation_coureurService.getAll(connect)) {
                String queryToHisto_etape_coureur = "insert into Histo_etape_coureur (idaffectation, heuredepart, heurearrivee) select DISTINCT " + affectation.getPk() + " as idaffectation, '" + affectation.getEtape().getDate_depart().format(formatter) + "' as heuredepart, t.arrivee from tempresultat t join affectation a on a.pk = idaffectation where idaffectation NOT IN (select idaffectation from Histo_etape_coureur)";
                stmt.executeUpdate(queryToHisto_etape_coureur);
            }

            // connect = ConnectSQL.getConnection("postgres", "btp", "postgres", "root");
            // connect.setAutoCommit(false);

            //raha tsisy erreur 
            // try {
            //     connect.commit();
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
        } 
        catch (Exception e) {
            e.printStackTrace();
            try {
                connect.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        }
        finally{
            if (connect != null) {
                try {
                    // if (!connect.isClosed()) {
                    //     connect.close();
                    // }
                    if (!stmt.isClosed()) {
                        stmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void resettemptable(Connection connect) throws Exception
    {
        // Connection connect = null;
        Statement stmt = null;
        // ResultSet rslt = null;
        try {

            String queryToTable = "truncate tempresultat";
            // String queryToLineNumber = "alter sequence tempdevis_linenumber_seq restart with 1";
            String queryToLineNumber = "select setval('tempresultat_linenumber_seq', 1, false)";

            // connect = ConnectSQL.getConnection("postgres", "btp", "postgres", "root");

            stmt = connect.createStatement();
            stmt.executeUpdate(queryToTable);
            // stmt.executeUpdate(queryToLineNumber);
            stmt.execute(queryToLineNumber);
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                connect.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        }
        finally{
            if (connect != null) {
                try {
                    // if (!connect.isClosed()) {
                    //     connect.close();
                    // }
                    if (!stmt.isClosed()) {
                        stmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
