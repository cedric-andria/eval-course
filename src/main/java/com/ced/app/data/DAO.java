/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ced.app.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ced.app.util.Colonne;
import com.ced.app.util.Page;
import com.ced.app.util.Tools;


public class DAO {
    
    public static void save(Connection connect, Object objet) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, SQLException{
        
        String tableName = Tools.getNomTable(objet);
        List<Colonne> colonnes = Tools.getColonnes(objet);
        List<Object> valeurColonnes = null;
        try{
            valeurColonnes = Tools.getValeurColonnes(objet);
        }
        catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e){
            throw e;
        }
        
        String query = "insert into "+tableName+"(";
        
        int idcolonne = 0 , compteur = 0;
        
        Vector<String> colonneTemp = new Vector<String>();
        
        for(; idcolonne < colonnes.size(); idcolonne++){
            if(valeurColonnes.get(idcolonne) != null){
                colonneTemp.add(colonnes.get(idcolonne).nom());
                compteur++;
            }
        }
        int col = 0; 
        for(; col < colonneTemp.size() - 1; col++){
            query = query + colonneTemp.get(col) + ", ";
        }
        System.out.println(query);
        query = query + colonneTemp.get(col);
        
        query = query + ") values (";
        idcolonne = 0;
        for(; idcolonne < compteur - 1; idcolonne++){
            if(colonnes.get(idcolonne).primaryKey() == true && valeurColonnes.get(idcolonne) != null){
                query = query + "default, ";
            }
            query = query + "?, ";
        }
        query = query + "?)";

        System.out.println(query);
        
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int id = 1;
        
        for(int index = 0; index < valeurColonnes.size(); index++){
            if(valeurColonnes.get(index) != null){
                try {
                    statement.setObject(id, valeurColonnes.get(index));
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                id++;
            }
        }
        
        int exec = statement.executeUpdate();
        
        if(statement != null) statement.close();
    }
    
    
    public static List<Object> find(Connection connect, Object objet) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, NoSuchMethodException, InvocationTargetException, SQLException, InstantiationException{
        List<Object> liste = new Vector<Object>();
        
        String tableName = Tools.getNomTable(objet);
        List<Colonne> colonnes = Tools.getColonnes(objet);
        List<Object> valeurColonnes = null;
        try{
            valeurColonnes = Tools.getValeurColonnes(objet);
        }
        catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e){
            throw e;
        }
        
        String query = "select * from "+tableName+" where 0 = 0";
        
        int idcolonne = 0;
        
        for(; idcolonne < colonnes.size(); idcolonne++){
            if(valeurColonnes.get(idcolonne) != null){
                query = query + " and "+colonnes.get(idcolonne).nom()+" = ?";
            }
        }
        System.out.println(query);
        
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int id = 1;
        
        for(int index = 0; index < valeurColonnes.size(); index++){
            if(valeurColonnes.get(index) != null){
                try {
                    statement.setObject(id, valeurColonnes.get(index));
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                id++;
            }
        }
        
        ResultSet result = null;
        result = statement.executeQuery();
        
        Field[] champs = objet.getClass().getDeclaredFields();
        
        while(result.next()){
            Object temp = objet.getClass().newInstance();
            for(int t = 0; t < champs.length; t++){
                if(Tools.getColonne(champs[t]) != null){
                   temp.getClass().getMethod("set" + champs[t].getName().substring(0, 1).toUpperCase().concat(champs[t].getName().substring(1)), champs[t].getType()).invoke(temp, result.getObject(Tools.getColonne(champs[t]).nom()));
                }
            }
            liste.add(temp);
        }
  
        if(result != null)result.close();
        if(statement != null)statement.close();
        
        return liste;
    }
    
    public static void update(Connection connect, Object objet) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, SQLException, Exception{

        String tableName = Tools.getNomTable(objet);
        List<Colonne> colonnes = Tools.getColonnes(objet);
        List<Object> valeurColonnes = null;
        try{
            valeurColonnes = Tools.getValeurColonnes(objet);
        }
        catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e){
            throw e;
        }
        
        String query = "update "+tableName+" set ";
        
        int idcolonne = 0;
        
        Vector<String> colonneTemp = new Vector<String>();
        
        for(; idcolonne < colonnes.size(); idcolonne++){
            if(colonnes.get(idcolonne).primaryKey() == false && valeurColonnes.get(idcolonne) != null){
                colonneTemp.add(colonnes.get(idcolonne).nom());
            }
        }
        int col = 0; 
        for(; col < colonneTemp.size() - 1; col++){
            query = query + colonneTemp.get(col) + " = ? , ";
        }
        System.out.println(query);
        query = query + colonneTemp.get(col)+" = ?";
        
        query = query + " where 0 = 0";
        idcolonne = 0;
        for(; idcolonne < colonnes.size(); idcolonne++){
            if(colonnes.get(idcolonne).primaryKey() == true && valeurColonnes.get(idcolonne) != null){
                query = query + " and "+colonnes.get(idcolonne).nom()+" = ?";
            }
        }

        System.out.println(query);
        
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int id = 1;
        
        for(int index = 0; index < valeurColonnes.size(); index++){
            if(valeurColonnes.get(index) != null && colonnes.get(index).primaryKey() == false){
                try {
                    statement.setObject(id, valeurColonnes.get(index));
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                id++;
            }
        }
        for(int index = 0; index < valeurColonnes.size(); index++){
            if(valeurColonnes.get(index) != null && colonnes.get(index).primaryKey() == true){
                try {
                    statement.setObject(id, valeurColonnes.get(index));
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                id++;
            }
        }
        
        int exec = statement.executeUpdate();
        
        if(statement != null) statement.close();        
        
    }
    
    public static List<Integer> getNumberPage(Connection connect,Object objet) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, SQLException{
        List<Integer> liste = new Vector<Integer>();
        
        int numberPage = 0;
        
        String tableName = Tools.getNomTable(objet);
        List<Colonne> colonnes = Tools.getColonnes(objet);
        List<Object> valeurColonnes = null;
        try{
            valeurColonnes = Tools.getValeurColonnes(objet);
        }
        catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e){
            throw e;
        }
        
        String query = "select count(*) as numberpage from "+tableName+" where 0 = 0";
        
        int idcolonne = 0;
        
        for(; idcolonne < colonnes.size(); idcolonne++){
            if(valeurColonnes.get(idcolonne) != null){
                if(valeurColonnes.get(idcolonne).getClass().getSimpleName().toLowerCase().equals("string")) {
                    query = query + " and "+colonnes.get(idcolonne).nom()+" ilike ?";
                }
                else{
                    query = query + " and "+colonnes.get(idcolonne).nom()+" = ?";
                }
            }
        }
        
        System.out.println(query);
        
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int id = 1;
        
        for(int index = 0; index < valeurColonnes.size(); index++){
            if(valeurColonnes.get(index) != null){
                try {
                    if(valeurColonnes.get(index).getClass().getSimpleName().toLowerCase().equals("string")){
                        statement.setString(id, "%"+valeurColonnes.get(index)+"%");
                    }
                    else{
                        statement.setObject(id, valeurColonnes.get(index));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                id++;
            }
        }
        
        ResultSet result = statement.executeQuery();
        
        while(result.next()){
            numberPage = result.getInt("numberpage");
        }
        
        if(result != null)result.close();
        if(statement != null)statement.close();
        
        if(numberPage <= Page.defaultContains){
            liste.add(1);
            return liste;
        }
        
        int division = numberPage / Page.defaultContains;
        int reste = numberPage % Page.defaultContains;
        for(int  i = 1; i <= division; i++){
            liste.add(i);
            if(i == division && reste > 0){
                liste.add(i + 1);
            }
        }
        
        return liste;
    }
    
    public static List<Object> findWithPage(Connection connect, Object objet, int page) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, NoSuchMethodException, InvocationTargetException, SQLException, InstantiationException{
        List<Object> liste = new Vector<Object>();
        
        String tableName = Tools.getNomTable(objet);
        List<Colonne> colonnes = Tools.getColonnes(objet);
        List<Object> valeurColonnes = null;
        try{
            valeurColonnes = Tools.getValeurColonnes(objet);
        }
        catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e){
            throw e;
        }
        
        String query = "select * from "+tableName+" where 0 = 0";
        
        int idcolonne = 0;
        
        for(; idcolonne < colonnes.size(); idcolonne++){
            if(valeurColonnes.get(idcolonne) != null){
                if(valeurColonnes.get(idcolonne).getClass().getSimpleName().toLowerCase().equals("string")) {
                    query = query + " and "+colonnes.get(idcolonne).nom()+" ilike ?";
                }
                else{
                    query = query + " and "+colonnes.get(idcolonne).nom()+" = ?";
                }
            }
        }
        
        query = query + " limit ? offset ?";
        
        System.out.println(query);
        
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int id = 1;
        
        for(int index = 0; index < valeurColonnes.size(); index++){
            if(valeurColonnes.get(index) != null){
                try {
                    if(valeurColonnes.get(index).getClass().getSimpleName().toLowerCase().equals("string")){
                        statement.setString(id, "%"+valeurColonnes.get(index)+"%");
                    }
                    else{
                        statement.setObject(id, valeurColonnes.get(index));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                id++;
            }
        }
        statement.setInt(id, Page.defaultContains);
        statement.setInt(id + 1, (Page.defaultContains * (page - 1)));
        
        ResultSet result = statement.executeQuery();
        
        Field[] champs = objet.getClass().getDeclaredFields();
        while(result.next()){
            Object temp = objet.getClass().newInstance();
            for(int t = 0; t < champs.length; t++){
                if(Tools.getColonne(champs[t]) != null){
                    temp.getClass().getMethod("set" + champs[t].getName().substring(0, 1).toUpperCase().concat(champs[t].getName().substring(1)), champs[t].getType()).invoke(temp, result.getObject(Tools.getColonne(champs[t]).nom()));
                }
            }
            liste.add(temp);
        }
        
        if(result != null)result.close();
        if(statement != null)statement.close();
        
        return liste;
    }
    
    public static List<Object> findIlike(Connection connect, Object objet) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, NoSuchMethodException, InvocationTargetException, SQLException, InstantiationException{
        List<Object> liste = new Vector<Object>();
        
        String tableName = Tools.getNomTable(objet);
        List<Colonne> colonnes = Tools.getColonnes(objet);
        List<Object> valeurColonnes = null;
        try{
            valeurColonnes = Tools.getValeurColonnes(objet);
        }
        catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e){
            throw e;
        }
        
        String query = "select * from "+tableName+" where 0 = 0";
        
        int idcolonne = 0;
        
        for(; idcolonne < colonnes.size(); idcolonne++){
            if(valeurColonnes.get(idcolonne) != null){
                if(valeurColonnes.get(idcolonne).getClass().getSimpleName().toLowerCase().equals("string")) {
                    query = query + " and "+colonnes.get(idcolonne).nom()+" ilike ?";
                }
                else{
                    query = query + " and "+colonnes.get(idcolonne).nom()+" = ?";
                }
            }
        }
        
        System.out.println(query);
        
        PreparedStatement statement = null;
        try {
            statement = connect.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int id = 1;
        
        for(int index = 0; index < valeurColonnes.size(); index++){
            if(valeurColonnes.get(index) != null){
                try {
                    if(valeurColonnes.get(index).getClass().getSimpleName().toLowerCase().equals("string")){
                        statement.setString(id, "%"+valeurColonnes.get(index)+"%");
                    }
                    else{
                        statement.setObject(id, valeurColonnes.get(index));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                id++;
            }
        }

        ResultSet result = statement.executeQuery();
        
        Field[] champs = objet.getClass().getDeclaredFields();
        
        while(result.next()){
            Object temp = objet.getClass().newInstance();
            for(int t = 0; t < champs.length; t++){
                if(Tools.getColonne(champs[t]) != null){
                    temp.getClass().getMethod("set" + champs[t].getName().substring(0, 1).toUpperCase().concat(champs[t].getName().substring(1)), champs[t].getType()).invoke(temp, result.getObject(Tools.getColonne(champs[t]).nom()));
                }
            }
            liste.add(temp);
        }
        
        if(result != null)result.close();
        if(statement != null)statement.close();
        
        return liste;
    }
    
}
