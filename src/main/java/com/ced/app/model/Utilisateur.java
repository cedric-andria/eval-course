package com.ced.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Utilisateur {
    private String login;
    private String mdp;
    private String profil;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;

    public Utilisateur(String login, String mdp, String profil) {
        this.login = login;
        this.mdp = mdp;
        this.profil = profil;
    }

    public Utilisateur(String login, String mdp, String profil, int pk) {
        this.login = login;
        this.mdp = mdp;
        this.profil = profil;
        this.pk = pk;
    }

    public Utilisateur() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

}
