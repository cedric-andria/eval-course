package com.ced.app.service;

import java.sql.Connection;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import com.ced.app.data.ConnectSQL;

@Service
public class ReinitialisationService {
    public void resetdatabase() throws Exception
    {
        Connection connect = null;
        Statement stmt = null;
        // ResultSet rslt = null;
        try {

            String[] queriesToTable = {"truncate tempetape CASCADE", "truncate tempresultat CASCADE", "truncate temppoints CASCADE", "truncate Penalite_equipe CASCADE", "truncate Histo_etape_coureur CASCADE", "truncate Affectation_coureur CASCADE", "truncate Categorie_coureur CASCADE", "truncate Categorie CASCADE", "truncate Coureur CASCADE", "truncate Genre CASCADE", "truncate Equipe CASCADE", "truncate Etape CASCADE", "truncate Param_points_rang"};
            // String queryToLineNumber = "alter sequence tempdevis_linenumber_seq restart with 1";
            String[] queriesToLineNumber = {"select setval('temppoints_linenumber_seq', '1', false) CASCADE", "select setval('tempresultat_linenumber_seq', '1', false)", "select setval('tempetape_linenumber_seq', '1', false)", "select setval('scpenalite_equipe', '1', false)", "select setval('penalite_equipe_pk_seq', '1', false)", "select setval('schistoetapecoureur', '1', false)", "select setval('histo_etape_coureur_pk_seq', '1', false)", "select setval('scaffectation', '1', false)", "select setval('affectation_coureur_pk_seq', '1', false)", "select setval('categorie_coureur_pk_seq', '1', false)", "select setval('sccategorie', '1', false)", "select setval('categorie_pk_seq', '1', false)", "select setval('sccoureur', '1', false)", "select setval('coureur_pk_seq', '1', false)", "select setval('scgenre', '1', false)", "select setval('genre_pk_seq', '1', false)", "select setval('scequipe', '1', false)", "select setval('equipe_pk_seq', '1', false)", "select setval('scetape', '1', false)", "select setval('etape_pk_seq', '1', false)", "select setval('param_points_rang_pk_seq', '1', false)", "select setval('utilisateur_pk_seq', 1, true)"};

            connect = ConnectSQL.getConnection("postgres", "course", "postgres", "root");

            stmt = connect.createStatement();
            for(String queryToTable : queriesToTable)
            {
                stmt.executeUpdate(queryToTable);

            }
            for (String queryToLineNumber : queriesToLineNumber) {
                stmt.execute(queryToLineNumber);
            }
            // stmt.executeUpdate(queryToLineNumber);
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
