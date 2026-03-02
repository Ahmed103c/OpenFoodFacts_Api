package com.Tp2.Nutrition.Data.Dtos;

import java.util.List;

import com.Tp2.Nutrition.Data.Entity.AdditifEntity;


public class ProductInfoDto {
 
    public String name;

    public String brands;

    public NutritionDataDto nutriscoreData;

    public List<AdditifEntity> additivesTags;
}
