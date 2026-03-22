package com.Tp2.Nutrition.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.Tp2.Nutrition.Data.Dtos.OpenFoodFactsDto;
import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Exceptions.ProductNotFoundException;
import com.Tp2.Nutrition.Repository.NutritionRepository;



@Service
public class NutritionService {

    private final RestTemplate restTemplate;
    private final NutritionRepository nutritionRepository;
    private final NutritionFactory nutritionFactory;

    public NutritionService(RestTemplate restTemplate, 
                           NutritionRepository nutritionRepository,
                           NutritionFactory nutritionFactory) {
        this.restTemplate = restTemplate;
        this.nutritionRepository = nutritionRepository;
        this.nutritionFactory = nutritionFactory;
    }

    public ResponseModel getNutritionData(String productCode) {
        if (productCode == null || productCode.isBlank()) {
            throw new ProductNotFoundException("Le code-barres du produit est obligatoire.");
        }

        Optional<NutritionEntity> existingData = nutritionRepository.findByBarcode(productCode);
        
        if (existingData.isPresent()) {
            System.out.println("Data found in database for barcode: " + productCode);
            return nutritionFactory.entityToResponseModel(existingData.get());
        }
        
        String url = "https://world.openfoodfacts.org/api/v0/product/" + productCode + ".json";
        OpenFoodFactsDto dto;
        try {
            dto = restTemplate.getForObject(url, OpenFoodFactsDto.class);
        } catch (RestClientException exception) {
            throw new ProductNotFoundException("Impossible de recuperer le produit pour le code-barres " + productCode + ".");
        }

        if (dto == null || dto.product == null) {
            throw new ProductNotFoundException("Aucun produit trouve pour le code-barres " + productCode + ".");
        }

        System.out.println("Data fetched from OpenFoodFacts API : " + dto); 
        
        ResponseModel responseModel = nutritionFactory.dtoToResponseModel(dto, productCode);
        
        NutritionEntity entity = nutritionFactory.responseModelToEntity(responseModel);
        nutritionRepository.save(entity);
        
        return responseModel;
    }
}
