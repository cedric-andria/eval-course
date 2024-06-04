package com.ced.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Etape {
    private String id;
    private String nom;
    private double longueur;
    private int nbcoureur_equipe;
    private int rang;
    private LocalDateTime date_depart;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    @Transient
    private Classement_etape classement_etape;

    public Etape(int pk, LocalDateTime date_depart) {
        this.pk = pk;
        this.date_depart = date_depart;
    }
    public Etape(String id, String nom, double longueur, int nbcoureur_equipe, int rang, LocalDateTime date_depart) {
        this.id = id;
        this.nom = nom;
        this.longueur = longueur;
        this.nbcoureur_equipe = nbcoureur_equipe;
        this.rang = rang;
        this.date_depart = date_depart;
    }
    public Etape(String id, String nom, double longueur, int nbcoureur_equipe, int rang, LocalDateTime date_depart,
            int pk) {
        this.id = id;
        this.nom = nom;
        this.longueur = longueur;
        this.nbcoureur_equipe = nbcoureur_equipe;
        this.rang = rang;
        this.date_depart = date_depart;
        this.pk = pk;
    }
    public Etape() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public double getLongueur() {
        return longueur;
    }
    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }
    public int getNbcoureur_equipe() {
        return nbcoureur_equipe;
    }
    public void setNbcoureur_equipe(int nbcoureur_equipe) {
        this.nbcoureur_equipe = nbcoureur_equipe;
    }
    public int getRang() {
        return rang;
    }
    public void setRang(int rang) {
        this.rang = rang;
    }
    public LocalDateTime getDate_depart() {
        return date_depart;
    }
    public void setDate_depart(LocalDateTime date_depart) {
        this.date_depart = date_depart;
    }
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
    public Classement_etape getClassement_etape() {
        return classement_etape;
    }
    public void setClassement_etape(Classement_etape classement_etape) {
        this.classement_etape = classement_etape;
    }
}
