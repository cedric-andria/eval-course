package com.ced.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Histo_etape_coureur {
    private String id;
    @ManyToOne
    @JoinColumn(name = "idaffectation")
    private Affectation_coureur affectation;
    private LocalDateTime heuredepart;
    private LocalDateTime heurearrivee;
    private double points;
    private int rang;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    public Histo_etape_coureur(String id, Affectation_coureur affectation, LocalDateTime heuredepart,
            LocalDateTime heurearrivee, double points, int rang) {
        this.id = id;
        this.affectation = affectation;
        this.heuredepart = heuredepart;
        this.heurearrivee = heurearrivee;
        this.points = points;
        this.rang = rang;
    }
    public Histo_etape_coureur(String id, Affectation_coureur affectation, LocalDateTime heuredepart,
            LocalDateTime heurearrivee, double points, int rang, int pk) {
        this.id = id;
        this.affectation = affectation;
        this.heuredepart = heuredepart;
        this.heurearrivee = heurearrivee;
        this.points = points;
        this.rang = rang;
        this.pk = pk;
    }
    public Histo_etape_coureur() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Affectation_coureur getAffectation() {
        return affectation;
    }
    public void setAffectation(Affectation_coureur affectation) {
        this.affectation = affectation;
    }
    public LocalDateTime getHeuredepart() {
        return heuredepart;
    }
    public void setHeuredepart(LocalDateTime heuredepart) {
        this.heuredepart = heuredepart;
    }
    public LocalDateTime getHeurearrivee() {
        return heurearrivee;
    }
    public void setHeurearrivee(LocalDateTime heurearrivee) {
        this.heurearrivee = heurearrivee;
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
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
}
