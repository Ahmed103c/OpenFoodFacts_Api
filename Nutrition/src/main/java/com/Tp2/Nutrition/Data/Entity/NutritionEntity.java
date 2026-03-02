package com.Tp2.Nutrition.Data.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Products")
public class NutritionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String nom;
    
    @Column(unique = true)
    private int scoreNutrition;

    @Column()
    public String grade;

    @Column()
    public String couleur;

    @Column()
    public String[] additives;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getScoreNutrition() {
        return scoreNutrition;
    }

    public void setScoreNutrition(int scoreNutrition) {
        this.scoreNutrition = scoreNutrition;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String[] getAdditives() {
        return additives;
    }

    public void setAdditives(String[] additives) {
        this.additives = additives;
    }
    
}
