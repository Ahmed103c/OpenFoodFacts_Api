package com.Tp2.Nutrition.Data.Model;

import java.util.List;

import com.Tp2.Nutrition.Data.Entity.AdditifEntity;

/**
 * Modèle de réponse renvoyé par l'API pour un produit alimentaire.
 * Contient les informations nutritionnelles essentielles et la liste des additifs enrichis.
 */
public class ResponseModel {

    /** Code-barres du produit. */
    private String barCode;

    /** Nom du produit. */
    private String name;

    /** Score nutritionnel calculé (négatif = très bon, élevé = mauvais). */
    private int score;

    /** Grade textuel associé au score (ex: "Bon", "Mangeable", "Degueu"). */
    private String grade;

    /** Couleur associée au grade pour l'affichage (ex: "Green", "Red"). */
    private String color;

    /** Liste des additifs identifiés dans le produit, enrichis depuis la base locale. */
    private List<AdditifEntity> additives;

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<AdditifEntity> getAdditives() {
        return additives;
    }

    public void setAdditives(List<AdditifEntity> additives) {
        this.additives = additives;
    }
}