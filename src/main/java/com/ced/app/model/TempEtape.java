package com.ced.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TempEtape {
    private String etape;
    private double longueur;
    private int nb_coureur;
    private int rang;
    private LocalDate date_depart;
    private LocalTime heure_depart;
    private int linenumber;
    public TempEtape(String etape, double longueur, int nb_coureur, int rang, LocalDate date_depart,
            LocalTime heure_depart) {
        this.etape = etape;
        this.longueur = longueur;
        this.nb_coureur = nb_coureur;
        this.rang = rang;
        this.date_depart = date_depart;
        this.heure_depart = heure_depart;
    }
    public TempEtape(String etape, double longueur, int nb_coureur, int rang, LocalDate date_depart,
            LocalTime heure_depart, int linenumber) {
        this.etape = etape;
        this.longueur = longueur;
        this.nb_coureur = nb_coureur;
        this.rang = rang;
        this.date_depart = date_depart;
        this.heure_depart = heure_depart;
        this.linenumber = linenumber;
    }
    public TempEtape() {
    }
    public String getEtape() {
        return etape;
    }
    public void setEtape(String etape) {
        this.etape = etape;
    }
    public double getLongueur() {
        return longueur;
    }
    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }
    public int getNb_coureur() {
        return nb_coureur;
    }
    public void setNb_coureur(int nb_coureur) {
        this.nb_coureur = nb_coureur;
    }
    public int getRang() {
        return rang;
    }
    public void setRang(int rang) {
        this.rang = rang;
    }
    public LocalDate getDate_depart() {
        return date_depart;
    }
    public void setDate_depart(LocalDate date_depart) {
        this.date_depart = date_depart;
    }
    public LocalTime getHeure_depart() {
        return heure_depart;
    }
    public void setHeure_depart(LocalTime heure_depart) {
        this.heure_depart = heure_depart;
    }
    public int getLinenumber() {
        return linenumber;
    }
    public void setLinenumber(int linenumber) {
        this.linenumber = linenumber;
    }


}
