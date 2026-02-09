package com.Tp2.Nutrition.Services;

import org.springframework.stereotype.Component;

import com.Tp2.Nutrition.Data.Dtos.NutritionScoreDto;
import com.Tp2.Nutrition.Data.Dtos.OpenFoodFactsDto;
import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;

@Component
public class NutritionFactory {

    // Entity -> ResponseModel (depuis la base de données)
    public ResponseModel entityToResponseModel(NutritionEntity entity) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.barCode = entity.getBarcode();
        responseModel.name = entity.getNom();
        responseModel.score = entity.getScoreNutrition();
        responseModel.grade = entity.getGrade();
        responseModel.color = entity.getCouleur();
        return responseModel;
    }
    
    // DTO -> ResponseModel (depuis l'API)
    public ResponseModel dtoToResponseModel(OpenFoodFactsDto dto, String barcode) {
        NutritionScoreDto nutritionScoreDto = new NutritionScoreDto(dto.product.nutriscoreData);
        
        ResponseModel responseModel = new ResponseModel();
        responseModel.barCode = barcode;
        responseModel.name = dto.product.name;
        responseModel.score = nutritionScoreDto.getScore();
        responseModel.grade = nutritionScoreDto.grade;
        responseModel.color = nutritionScoreDto.couleur;
        return responseModel;
    }
    
    // ResponseModel -> Entity (pour sauvegarder)
    public NutritionEntity responseModelToEntity(ResponseModel responseModel) {
        NutritionEntity entity = new NutritionEntity();
        entity.setBarcode(responseModel.barCode);
        entity.setNom(responseModel.name);
        entity.setScoreNutrition(responseModel.score);
        entity.setGrade(responseModel.grade);
        entity.setCouleur(responseModel.color);
        return entity;
    }
}