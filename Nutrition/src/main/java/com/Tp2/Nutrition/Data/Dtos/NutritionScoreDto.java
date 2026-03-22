package com.Tp2.Nutrition.Data.Dtos;

import java.util.List;

import com.Tp2.Nutrition.Data.Dtos.NutritionDataDto.NutritionItem;

/**
 * DTO de calcul du score nutritionnel à partir des données Nutri-Score d'OpenFoodFacts.
 *
 * <p>Calcule automatiquement le score (points négatifs − points positifs),
 * puis en déduit un grade textuel et une couleur d'affichage.</p>
 *
 * <p>Grille de grades :</p>
 * <ul>
 *   <li>score &lt; 0  → "Trop Bon" (Green)</li>
 *   <li>0 à 2        → "Bon" (Light Green)</li>
 *   <li>3 à 10       → "Mangeable" (Yellow)</li>
 *   <li>11 à 18      → "Mouai" (Orange)</li>
 *   <li>&gt; 18      → "Degueu" (Red)</li>
 * </ul>
 */
public class NutritionScoreDto {

    private int score;
    private int negativePoints;
    private int positivePoints;
    private List<NutritionItem> negativeItems;
    private List<NutritionItem> positiveItems;
    private String grade;
    private String couleur;

    /**
     * Construit le DTO en calculant le score et le grade à partir des composantes Nutri-Score.
     *
     * @param nutritionData les données de composantes positives/négatives issues d'OpenFoodFacts
     */
    public NutritionScoreDto(NutritionDataDto nutritionData) {
        this.negativePoints = 0;
        this.positivePoints = 0;
        this.negativeItems = nutritionData.components.getNegativeItems();
        this.positiveItems = nutritionData.components.getPositiveItems();

        for (NutritionItem item : negativeItems) {
            if (item.points != null) {
                this.negativePoints += item.getPoints();
            }
        }

        for (NutritionItem item : positiveItems) {
            if (item.points != null) {
                this.positivePoints += item.getPoints();
            }
        }

        this.score = negativePoints - positivePoints;
        assignGradeAndColor();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getScore() {
        return score;
    }

    public int getNegativePoints() {
        return negativePoints;
    }

    public int getPositivePoints() {
        return positivePoints;
    }

    public String getGrade() {
        return grade;
    }

    public String getCouleur() {
        return couleur;
    }

    public List<NutritionItem> getNegativeItems() {
        return negativeItems;
    }

    public List<NutritionItem> getPositiveItems() {
        return positiveItems;
    }

    // -------------------------------------------------------------------------
    // Méthode privée utilitaire
    // -------------------------------------------------------------------------

    /**
     * Détermine le grade textuel et la couleur associée en fonction du score calculé.
     */
    private void assignGradeAndColor() {
        if (this.score <= -1) {
            this.grade = "Trop Bon";
            this.couleur = "Green";
        } else if (this.score <= 2) {
            this.grade = "Bon";
            this.couleur = "Light Green";
        } else if (this.score <= 10) {
            this.grade = "Mangeable";
            this.couleur = "Yellow";
        } else if (this.score <= 18) {
            this.grade = "Mouai";
            this.couleur = "Orange";
        } else {
            this.grade = "Degueu";
            this.couleur = "Red";
        }
    }
}