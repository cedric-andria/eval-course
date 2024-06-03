package com.ced.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Parametre_penalite {
    private String id;
    @ManyToOne
    @JoinColumn(name = "idetape")
    private Etape etape;
    private double points;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    public Parametre_penalite(String id, Etape etape, double points) {
        this.id = id;
        this.etape = etape;
        this.points = points;
    }
    public Parametre_penalite(String id, Etape etape, double points, int pk) {
        this.id = id;
        this.etape = etape;
        this.points = points;
        this.pk = pk;
    }
    public Parametre_penalite() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Etape getEtape() {
        return etape;
    }
    public void setEtape(Etape etape) {
        this.etape = etape;
    }
    public double getPoints() {
        return points;
    }
    public void setPoints(double points) {
        this.points = points;
    }
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
}
