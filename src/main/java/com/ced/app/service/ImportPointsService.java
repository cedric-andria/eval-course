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
import com.ced.app.model.TempPoints;
import com.ced.app.other.Tools;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ImportPointsService {
    @PersistenceContext
    private EntityManager entityManager;

    public HashMap<Integer, String> importCSV(String fileAbsPath) throws Exception
    {
        HashMap<Integer, String> errors = new HashMap<>();
        Connection connect = null;
        Statement stmt = null;
        try {
            connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");
            errors = Tools.ImportCsvByLine(connect, fileAbsPath, "temppoints", ",", true);
            stmt = connect.createStatement();
            if(!Tools.columnExists(connect, "temppoints", "linenumber"))
            {
                stmt.executeUpdate("alter table temppoints add column linenumber serial");
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

    public List<TempPoints> getTemptables() throws Exception
    {
        List<TempPoints> temptables = new ArrayList<>();
        Connection connect = null;
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            String query = "select * from temppoints";
            connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");
            stmt = connect.createStatement();
            rslt = stmt.executeQuery(query);
            while(rslt.next())
            {
                temptables.add(new TempPoints(rslt.getInt("classement"), rslt.getDouble("points"), rslt.getInt("linenumber")));
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

            String queryToParampoints = "insert into Param_points_rang (rang, points) select DISTINCT t.classement, t.points from temppoints t where (t.classement, t.points) NOT IN (select rang, points from Param_points_rang)";

            // connect = ConnectSQL.getConnection("postgres", "btp", "postgres", "root");
            connect.setAutoCommit(false);

            stmt = connect.createStatement();
            stmt.executeUpdate(queryToParampoints);

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

    public void resettemptable() throws Exception
    {
        Connection connect = null;
        Statement stmt = null;
        // ResultSet rslt = null;
        try {

            String queryToTable = "truncate temppoints";
            // String queryToLineNumber = "alter sequence tempdevis_linenumber_seq restart with 1";
            String queryToLineNumber = "select setval('temppoints_linenumber_seq', 1, false)";

            connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");

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
