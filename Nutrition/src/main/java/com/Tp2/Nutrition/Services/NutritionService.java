package com.Tp2.Nutrition.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Tp2.Nutrition.Data.Dtos.NutritionScoreDto;
import com.Tp2.Nutrition.Data.Dtos.OpenFoodFactsDto;
import com.Tp2.Nutrition.Data.Model.ResponseModel;

@Service
public class NutritionService {

    private final RestTemplate restTemplate;

    public NutritionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseModel getNutritionData(String productCode) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + productCode + ".json";
        OpenFoodFactsDto response = restTemplate.getForObject(url, OpenFoodFactsDto.class);

        ResponseModel responseModel = new ResponseModel();

        NutritionScoreDto nutritionScoreDto = new NutritionScoreDto(response.product.nutriscoreData);

        responseModel.name = response.product.name;
        responseModel.score = nutritionScoreDto.getScore();


        return responseModel;
    }
}