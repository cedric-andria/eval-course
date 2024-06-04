package com.ced.app.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ced.app.data.ConnectSQL;
import com.ced.app.model.TempEtape;
import com.ced.app.other.Tools;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ImportEtapeService {
    @PersistenceContext
    private EntityManager entityManager;

    public HashMap<Integer, String> importCSV(String fileAbsPath) throws Exception
    {
        HashMap<Integer, String> errors = new HashMap<>();
        Connection connect = null;
        Statement stmt = null;
        try {
            connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");
            errors = Tools.ImportCsvByLine(connect, fileAbsPath, "tempetape", ",", true);
            stmt = connect.createStatement();
            if(!Tools.columnExists(connect, "tempetape", "linenumber"))
            {
                stmt.executeUpdate("alter table tempetape add column linenumber serial");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally{
            if (connect != null) {
                if (!connect.isClosed()) {
                    connect.close();
                }
            }
            if(stmt != null)
            {
                if (!stmt.isClosed()) {
                    stmt.close();
                }
            }
        }
        return errors;
    }

    public List<TempEtape> getTemptables() throws Exception
    {
        List<TempEtape> temptables = new ArrayList<>();
        Connection connect = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            String query = "select * from tempetape";
            connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");
            stmt = connect.createStatement();
            rslt = stmt.executeQuery(query);
            while(rslt.next())
            {
                temptables.add(new TempEtape(rslt.getString("etape"), rslt.getDouble("longueur"), rslt.getInt("nb_coureur"), rslt.getInt("rang"), ((rslt.getDate("date_depart") != null) ? rslt.getDate("date_depart").toLocalDate() : LocalDate.now()), rslt.getTime("heure_depart").toLocalTime(), rslt.getInt("linenumber")));
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally{
            if (connect != null) {
                try {
                    if (!connect.isClosed()) {
                        connect.close();
                    }
                    rslt.close();
                    stmt.close();
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
        // ResultSet rslt = null;
        try {

            String queryToEtape = "insert into Etape (nom, longueur, nbcoureur_equipe, rang, date_depart) select DISTINCT etape, longueur, nb_coureur, rang, date_depart || ' ' || heure_depart from tempetape where etape NOT IN (select nom from etape)";

            // connect = ConnectSQL.getConnection("postgres", "btp", "postgres", "root");
            // connect.setAutoCommit(false);

            stmt = connect.createStatement();
            stmt.executeUpdate(queryToEtape);

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

            String queryToTable = "truncate tempetape";
            // String queryToLineNumber = "alter sequence tempdevis_linenumber_seq restart with 1";
            String queryToLineNumber = "select setval('tempetape_linenumber_seq', 1, false)";

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
