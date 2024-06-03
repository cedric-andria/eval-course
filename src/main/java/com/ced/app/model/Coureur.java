package com.ced.app.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Coureur {
    private String dossard;
    private String nom;
    @ManyToOne
    @JoinColumn(name = "idgenre")
    private Genre genre;
    private LocalDate datenaissance;
    private double pointtotal;
    @ManyToOne
    @JoinColumn(name = "idequipe")
    private Equipe equipe;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    @Transient
    private int age;
    @Transient 
    private LocalDateTime heuredepart;
    @Transient 
    private LocalDateTime heurearrivee;
    @Transient
    private int rang;
    @ManyToMany
    @JoinTable(
        name = "Categorie_coureur", joinColumns = @JoinColumn(name = "idcoureur"), inverseJoinColumns = @JoinColumn(name = "idcategorie")
    )
    private List<Categorie> categories;
    
    public Coureur(String dossard, String nom, Genre genre, LocalDate datenaissance, double pointtotal, Equipe equipe) {
        this.dossard = dossard;
        this.nom = nom;
        this.genre = genre;
        this.datenaissance = datenaissance;
        this.pointtotal = pointtotal;
        this.equipe = equipe;
    }
    public Coureur(String dossard, String nom, Genre genre, LocalDate datenaissance, double pointtotal, Equipe equipe,
            int pk) {
        this.dossard = dossard;
        this.nom = nom;
        this.genre = genre;
        this.datenaissance = datenaissance;
        this.pointtotal = pointtotal;
        this.equipe = equipe;
        this.pk = pk;
    }
    public Coureur() {
    }
    public String getDossard() {
        return dossard;
    }
    public void setDossard(String dossard) {
        this.dossard = dossard;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public LocalDate getDatenaissance() {
        return datenaissance;
    }
    public void setDatenaissance(LocalDate datenaissance) {
        this.datenaissance = datenaissance;
    }
    public double getPointtotal() {
        return pointtotal;
    }
    public void setPointtotal(double pointtotal) {
        this.pointtotal = pointtotal;
    }
    public Equipe getEquipe() {
        return equipe;
    }
    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }
    public int getPk() {
        return pk;
    }
    public void setPk(int pk) {
        this.pk = pk;
    }
    public List<Categorie> getCategories() {
        return categories;
    }
    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public long getDuration()
    {
        return Duration.between(getHeuredepart(), getHeurearrivee()).toMillis();
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
    public int getRang() {
        return rang;
    }
    public void setRang(int rang) {
        this.rang = rang;
    }
}
