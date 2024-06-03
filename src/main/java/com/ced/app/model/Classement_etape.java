package com.ced.app.model;

import java.util.List;

public class Classement_etape {
    private List<Coureur> classement;
    private Categorie categorie;
    public Classement_etape(List<Coureur> classement, Categorie categorie) {
        this.classement = classement;
        this.categorie = categorie;
    }
    public Classement_etape() {
    }
    public List<Coureur> getClassement() {
        return classement;
    }
    public void setClassement(List<Coureur> classement) {
        this.classement = classement;
    }
    public Categorie getCategorie() {
        return categorie;
    }
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
