package com.Tp2.Nutrition.Data.Dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NutritionDataDto {

    public Components components;
    
    public static class Components {
        @JsonProperty("negative")
        public List<NutritionItem> negativeItems;

        @JsonProperty("positive")
        public List<NutritionItem> positiveItems;


        public List<NutritionItem> getNegativeItems() {
            return negativeItems;
        }

        public List<NutritionItem> getPositiveItems() {
            return positiveItems;
        }
    }

    public static class NutritionItem {

        @JsonProperty("id")
        public String name;

        public int points;

        public String unit;

        public int value;

        public int getPoints() {
            return points;
        }
    }

}

