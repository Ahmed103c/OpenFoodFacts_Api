package com.Tp2.Nutrition.Data.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactsDto {
    public ProductDto product;
}
