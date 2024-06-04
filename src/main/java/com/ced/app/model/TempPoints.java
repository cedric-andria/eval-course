package com.ced.app.model;

public class TempPoints {
    private int classement;
    private double points;
    private int linenumber;

    public TempPoints(int classement, double points, int linenumber) {
        this.classement = classement;
        this.points = points;
        this.linenumber = linenumber;
    }
    public TempPoints(int classement, double points) {
        this.classement = classement;
        this.points = points;
    }
    public TempPoints() {
    }
    public int getClassement() {
        return classement;
    }
    public void setClassement(int classement) {
        this.classement = classement;
    }
    public double getPoints() {
        return points;
    }
    public void setPoints(double points) {
        this.points = points;
    }
    public int getLinenumber() {
        return linenumber;
    }
    public void setLinenumber(int linenumber) {
        this.linenumber = linenumber;
    }
}
