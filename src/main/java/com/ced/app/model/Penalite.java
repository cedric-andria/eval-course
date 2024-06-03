package com.ced.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Penalite {
    private String id;
    @ManyToOne
    @JoinColumn(name = "idparametre")
    private Parametre_penalite parametre;
    @ManyToOne
    @JoinColumn(name = "idcoureur")
    private Coureur coureur;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    private LocalDateTime created_at;
    public Penalite(String id, Parametre_penalite parametre, Coureur coureur, LocalDateTime created_at) {
        this.id = id;
        this.parametre = parametre;
        this.coureur = coureur;
        this.created_at = created_at;
    }
    public Penalite(String id, Parametre_penalite parametre, Coureur coureur, int pk, LocalDateTime created_at) {
        this.id = id;
        this.parametre = parametre;
        this.coureur = coureur;
        this.pk = pk;
        this.created_at = created_at;
    }
    public Penalite() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Parametre_penalite getParametre() {
        return parametre;
    }
    public void setParametre(Parametre_penalite parametre) {
        this.parametre = parametre;
    }
    public Coureur getCoureur() {
        return coureur;
    }
    public void setCoureur(Coureur coureur) {
        this.coureur = coureur;
    }
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
