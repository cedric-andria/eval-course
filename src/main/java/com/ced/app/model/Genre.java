package com.ced.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Genre {
    private String id;
    private String nom;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    public Genre(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }
    public Genre(String id, String nom, int pk) {
        this.id = id;
        this.nom = nom;
        this.pk = pk;
    }
    public Genre() {
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
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
}
