package com.Tp2.Nutrition.Data.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entité JPA représentant un produit alimentaire en base de données.
 * Mappée sur la table "Products".
 */
@Entity
@Table(name = "Products")
public class NutritionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Code-barres unique du produit (ex: "3057640257773"). */
    @Column(nullable = false)
    private String barcode;

    /** Nom du produit. */
    @Column(nullable = false)
    private String nom;

    /**
     * Score nutritionnel calculé à partir des données OpenFoodFacts.
     * Plusieurs produits peuvent avoir le même score — pas de contrainte unique.
     */
    @Column
    private int scoreNutrition;

    /** Grade nutritionnel textuel (ex: "Bon", "Degueu"). */
    @Column
    private String grade;

    /** Couleur associée au grade (ex: "Green", "Red"). */
    @Column
    private String couleur;

    /**
     * Codes des additifs présents dans le produit (ex: ["E322", "E471"]).
     * Stockés sous forme de tableau de chaînes.
     */
    @Column
    private String[] additives;

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

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