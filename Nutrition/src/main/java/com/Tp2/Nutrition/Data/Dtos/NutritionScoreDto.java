package com.Tp2.Nutrition.Data.Dtos;

import java.util.List;

import com.Tp2.Nutrition.Data.Dtos.NutritionDataDto.NutritionItem;

public class NutritionScoreDto {
    public int score;
    public int negativePoints;
    public int positivePoints;
    public List<NutritionItem> negativeItems;
    public List<NutritionItem> positiveItems;
    public String grade;
    public String couleur;


    public NutritionScoreDto(NutritionDataDto nutritionData) {

        this.negativePoints = 0;
        this.positivePoints = 0;
        this.negativeItems = nutritionData.components.getNegativeItems();
        this.positiveItems = nutritionData.components.getPositiveItems();

        for (NutritionItem item : negativeItems) {
            if(item.points != null) {
                this.negativePoints += item.getPoints();
            }
        }

        for (NutritionItem item : positiveItems) {
            if(item.points != null) {
                this.positivePoints += item.getPoints();
            }
        }

        this.score = negativePoints - positivePoints;

        if(this.score <= -1) {
            this.grade = "Trop Bon";
            this.couleur = "Green";
        } else if(this.score >= 0 && this.score <= 2) {
            this.grade = "Bon";
            this.couleur = "Light Green";
        } else if(this.score >= 3 && this.score <= 10) {
            this.grade = "Mangeable";
            this.couleur = "Yellow";
        } else if(this.score >= 11 && this.score <= 18) {
            this.grade = "Mouai";
            this.couleur = "Orange";
        } else {
            this.grade = "Degueu";
            this.couleur = "Red";
        }

    }

    public int getScore() {
        return score;
    }   
}
