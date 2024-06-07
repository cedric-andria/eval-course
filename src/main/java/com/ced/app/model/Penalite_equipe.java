package com.ced.app.model;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Penalite_equipe {
    private String id;
    @ManyToOne
    @JoinColumn(name = "idequipe")
    private Equipe equipe;
    @ManyToOne
    @JoinColumn(name = "idetape")
    private Etape etape;
    private LocalTime valeur;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    
    public Penalite_equipe(Equipe equipe, Etape etape, LocalTime valeur) {
        this.equipe = equipe;
        this.etape = etape;
        this.valeur = valeur;
    }
    public Penalite_equipe(String id, Equipe equipe, Etape etape) {
        this.id = id;
        this.equipe = equipe;
        this.etape = etape;
    }
    public Penalite_equipe(String id, Equipe equipe, Etape etape, int pk) {
        this.id = id;
        this.equipe = equipe;
        this.etape = etape;
        this.pk = pk;
    }
    public Penalite_equipe() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Equipe getEquipe() {
        return equipe;
    }
    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
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
    public LocalTime getValeur() {
        return valeur;
    }
    public void setValeur(LocalTime valeur) {
        this.valeur = valeur;
    }

}
