package com.ced.app.model;

import java.util.List;

public class Classement_equipe {
    private List<Equipe> classement;
    private Categorie categorie;
    public Classement_equipe(List<Equipe> classement, Categorie categorie) {
        this.classement = classement;
        this.categorie = categorie;
    }
    public Classement_equipe() {
    }
    public List<Equipe> getClassement() {
        return classement;
    }
    public void setClassement(List<Equipe> classement) {
        this.classement = classement;
    }
    public Categorie getCategorie() {
        return categorie;
    }
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
