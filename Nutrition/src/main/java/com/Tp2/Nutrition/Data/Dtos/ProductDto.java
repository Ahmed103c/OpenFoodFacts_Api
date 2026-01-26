package com.Tp2.Nutrition.Data.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {

    @JsonProperty("product_name")
    public String name;

    public String brands;

    @JsonProperty("nutriscore_data")
    public NutritionDataDto nutriscoreData;
}
