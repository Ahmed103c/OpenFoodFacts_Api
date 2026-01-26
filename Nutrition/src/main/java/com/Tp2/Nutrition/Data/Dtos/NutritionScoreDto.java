package com.Tp2.Nutrition.Data.Dtos;

import java.util.List;
import com.Tp2.Nutrition.Data.Dtos.NutritionDataDto.NutritionItem;

public class NutritionScoreDto {
    public int score;
    public int negativePoints;
    public int positivePoints;
    public List<NutritionItem> negativeItems;
    public List<NutritionItem> positiveItems;


    public NutritionScoreDto(NutritionDataDto nutritionData) {

        this.negativePoints = 0;
        this.positivePoints = 0;
        this.negativeItems = nutritionData.components.getNegativeItems();
        this.positiveItems = nutritionData.components.getPositiveItems();

        for (NutritionItem item : negativeItems) {
            this.negativePoints += item.getPoints();
        }

        for (NutritionItem item : positiveItems) {
            this.positivePoints += item.getPoints();
        }

        this.score = negativePoints - positivePoints;

    }

    public int getScore() {
        return score;
    }   
}
