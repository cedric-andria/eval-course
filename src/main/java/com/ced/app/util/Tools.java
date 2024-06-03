/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ced.app.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;

public class Tools {
    
    //TableName
    public static String getNomTable(Object objet){
        return objet.getClass().getAnnotation(Table.class).nom();
    }
    
    //Annoted Column
    public static Colonne getColonne(Field field){
        if(field.getAnnotation(Colonne.class) != null) return field.getAnnotation(Colonne.class);
        return null;
    }
    
    //All Annoted Column
    public static List<Colonne> getColonnes(Object objet){
        List<Colonne> liste = new Vector<Colonne>();
        Field[] champs = objet.getClass().getDeclaredFields();
        for (Field declaredField : champs) {
            if (getColonne(declaredField) != null) {
                liste.add(getColonne(declaredField));
            }
        }
        return liste;
    }
    
    //Column Value
    public static Object getValeurColonne(Object objet, Field field) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        String fieldName = field.getName();
        fieldName = fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
        return objet.getClass().getMethod("get" + fieldName).invoke(objet);
    }
    
    //All Column Value
    public static List<Object> getValeurColonnes(Object objet) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        List<Object> liste = new Vector<Object>();
        Field[] champs = objet.getClass().getDeclaredFields();
        for (Field declaredField : champs) {
            if (getColonne(declaredField) != null) {
                liste.add(getValeurColonne(objet, declaredField));
            }
        }
        return liste;
    }
    
}
