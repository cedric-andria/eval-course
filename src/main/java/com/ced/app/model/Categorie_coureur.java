package com.ced.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Categorie_coureur {
    @ManyToOne
    @JoinColumn(name = "idcoureur")
    private Coureur coureur;
    @ManyToOne
    @JoinColumn(name = "idcategorie")
    private Categorie categorie;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    private LocalDateTime created_at;
    public Categorie_coureur(Coureur coureur, Categorie categorie, LocalDateTime created_at) {
        this.coureur = coureur;
        this.categorie = categorie;
        this.created_at = created_at;
    }
    public Categorie_coureur(Coureur coureur, Categorie categorie, int pk, LocalDateTime created_at) {
        this.coureur = coureur;
        this.categorie = categorie;
        this.pk = pk;
        this.created_at = created_at;
    }
    public Categorie_coureur() {
    }
    public Coureur getCoureur() {
        return coureur;
    }
    public void setCoureur(Coureur coureur) {
        this.coureur = coureur;
    }
    public Categorie getCategorie() {
        return categorie;
    }
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
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
