package com.ced.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TempResultat {
    private int etape_rang;
    private String numero_dossard;
    private String nom;
    private String genre;
    private LocalDate date_naissance;
    private String equipe;
    private LocalDateTime arrivee;
    private int linenumber;

    public TempResultat(int etape_rang, String numero_dossard, String nom, String genre, LocalDate date_naissance,
            String equipe, LocalDateTime arrivee) {
        this.etape_rang = etape_rang;
        this.numero_dossard = numero_dossard;
        this.nom = nom;
        this.genre = genre;
        this.date_naissance = date_naissance;
        this.equipe = equipe;
        this.arrivee = arrivee;
    }
    public TempResultat(int etape_rang, String numero_dossard, String nom, String genre, LocalDate date_naissance,
            String equipe, LocalDateTime arrivee, int linenumber) {
        this.etape_rang = etape_rang;
        this.numero_dossard = numero_dossard;
        this.nom = nom;
        this.genre = genre;
        this.date_naissance = date_naissance;
        this.equipe = equipe;
        this.arrivee = arrivee;
        this.linenumber = linenumber;
    }
    public TempResultat() {
    }
    public int getEtape_rang() {
        return etape_rang;
    }
    public void setEtape_rang(int etape_rang) {
        this.etape_rang = etape_rang;
    }
    public String getNumero_dossard() {
        return numero_dossard;
    }
    public void setNumero_dossard(String numero_dossard) {
        this.numero_dossard = numero_dossard;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public LocalDate getDate_naissance() {
        return date_naissance;
    }
    public void setDate_naissance(LocalDate date_naissance) {
        this.date_naissance = date_naissance;
    }
    public String getEquipe() {
        return equipe;
    }
    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }
    public LocalDateTime getArrivee() {
        return arrivee;
    }
    public void setArrivee(LocalDateTime arrivee) {
        this.arrivee = arrivee;
    }
    public int getLinenumber() {
        return linenumber;
    }
    public void setLinenumber(int linenumber) {
        this.linenumber = linenumber;
    }
}
