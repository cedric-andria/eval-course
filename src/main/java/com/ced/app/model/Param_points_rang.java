package com.ced.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Param_points_rang {
    private int rang;
    private double points;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    private LocalDateTime created_at;
    public Param_points_rang(int rang, double points, LocalDateTime created_at) {
        this.rang = rang;
        this.points = points;
        this.created_at = created_at;
    }
    public Param_points_rang(int rang, double points, int pk, LocalDateTime created_at) {
        this.rang = rang;
        this.points = points;
        this.pk = pk;
        this.created_at = created_at;
    }
    public Param_points_rang() {
    }
    public int getRang() {
        return rang;
    }
    public void setRang(int rang) {
        this.rang = rang;
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
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
