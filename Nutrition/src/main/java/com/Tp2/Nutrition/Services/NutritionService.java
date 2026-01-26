package com.Tp2.Nutrition.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NutritionService {

    private final RestTemplate restTemplate;

    public NutritionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getNutritionData(String productCode) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + productCode + ".json";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

}