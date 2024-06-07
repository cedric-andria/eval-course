package com.ced.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Equipe {
    private String id;
    private String nom;
    @ManyToOne
    @JoinColumn(name="idutilisateur")
    private Utilisateur utilisateur;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    @Transient
    private double points;
    @Transient
    private int rang;
    @Transient 
    private int has_exaequo;
    
    public Equipe(int pk) {
        this.pk = pk;
    }
    public Equipe(String id, String nom, Utilisateur utilisateur) {
        this.id = id;
        this.nom = nom;
        this.utilisateur = utilisateur;
    }
    public Equipe(String id, String nom, Utilisateur utilisateur, int pk) {
        this.id = id;
        this.nom = nom;
        this.utilisateur = utilisateur;
        this.pk = pk;
    }
    public Equipe() {
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
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
    public double getPoints() {
        return points;
    }
    public void setPoints(double points) {
        this.points = points;
    }
    public int getRang() {
        return rang;
    }
    public void setRang(int rang) {
        this.rang = rang;
    }
    public int getHas_exaequo() {
        return has_exaequo;
    }
    public void setHas_exaequo(int has_exaequo) {
        this.has_exaequo = has_exaequo;
    }

}
