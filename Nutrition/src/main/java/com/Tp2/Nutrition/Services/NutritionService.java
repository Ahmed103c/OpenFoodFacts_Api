package com.Tp2.Nutrition.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Tp2.Nutrition.Data.Dtos.NutritionScoreDto;
import com.Tp2.Nutrition.Data.Dtos.OpenFoodFactsDto;
import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Repository.NutritionRepository;

@Service
public class NutritionService {

    private final RestTemplate restTemplate;
    private final NutritionRepository nutritionRepository;

    public NutritionService(RestTemplate restTemplate, NutritionRepository nutritionRepository) {
        this.restTemplate = restTemplate;
        this.nutritionRepository = nutritionRepository;
    }

    public NutritionEntity saveNutritionData(NutritionEntity nutritionEntity) {
        return nutritionRepository.save(nutritionEntity);
    }

    public Optional<NutritionEntity> getNutritionByBarcode(String barcode) {
        return nutritionRepository.findByBarcode(barcode);
    }
    
    public ResponseModel getNutritionData(String productCode) {

        if(nutritionRepository.findByBarcode(productCode).isPresent()){
            System.out.println("Data found in database for barcode: " + productCode);  
            NutritionEntity nutritionEntity = nutritionRepository.findByBarcode(productCode).get();
            ResponseModel responseModel = new ResponseModel();
            responseModel.barCode = nutritionEntity.getBarcode();
            responseModel.name = nutritionEntity.getNom();
            responseModel.score = nutritionEntity.getScoreNutrition();
            return responseModel;
        }
        else{
            String url = "https://world.openfoodfacts.org/api/v0/product/" + productCode + ".json";
            OpenFoodFactsDto response = restTemplate.getForObject(url, OpenFoodFactsDto.class);

            ResponseModel responseModel = new ResponseModel();

            NutritionScoreDto nutritionScoreDto = new NutritionScoreDto(response.product.nutriscoreData);
            responseModel.barCode = productCode;
            responseModel.name = response.product.name;
            responseModel.score = nutritionScoreDto.getScore();

            NutritionEntity nutritionEntity = new NutritionEntity();
            nutritionEntity.setBarcode(responseModel.barCode);
            nutritionEntity.setNom(responseModel.name);
            nutritionEntity.setScoreNutrition(responseModel.score);

            nutritionRepository.save(nutritionEntity);

            return responseModel;
        }
    }
}