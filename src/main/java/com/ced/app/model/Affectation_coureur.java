package com.ced.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Affectation_coureur {
    private String id;
    @ManyToOne
    @JoinColumn(name = "idcoureur")
    private Coureur coureur;
    @ManyToOne
    @JoinColumn(name = "idetape")
    private Etape etape;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    public Affectation_coureur(String id, Coureur coureur, Etape etape) {
        this.id = id;
        this.coureur = coureur;
        this.etape = etape;
    }
    public Affectation_coureur(String id, Coureur coureur, Etape etape, int pk) {
        this.id = id;
        this.coureur = coureur;
        this.etape = etape;
        this.pk = pk;
    }
    public Affectation_coureur() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Coureur getCoureur() {
        return coureur;
    }
    public void setCoureur(Coureur coureur) {
        this.coureur = coureur;
    }
    public Etape getEtape() {
        return etape;
    }
    public void setEtape(Etape etape) {
        this.etape = etape;
    }
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
}
