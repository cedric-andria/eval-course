/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ced.app.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author itu
 */
public class Fonctions {
    
    public static String[] getFieldNames(Object objet){
        Field[] field = objet.getClass().getDeclaredFields();
        String[] liste = new String[field.length];
        for(int i = 0; i < field.length; i++){
            liste[i] = field[i].getName();
        }
        return liste;
    }

}
