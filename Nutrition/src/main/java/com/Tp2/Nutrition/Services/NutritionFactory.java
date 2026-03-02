package com.Tp2.Nutrition.Services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Tp2.Nutrition.Data.Dtos.NutritionScoreDto;
import com.Tp2.Nutrition.Data.Dtos.OpenFoodFactsDto;
import com.Tp2.Nutrition.Data.Entity.AdditifEntity;
import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Repository.AdditifRepository;

@Component
public class NutritionFactory {
    @Autowired
    private AdditifRepository additifRepository;  // ✅ ajout

    // Méthode utilitaire privée pour nettoyer + enrichir
    private List<AdditifEntity> enrichirAdditifs(String[] additifsRaw) {
        if (additifsRaw == null) return List.of();
        return Arrays.stream(additifsRaw)
            .map(a -> a.replace("en:", "").toUpperCase())  // "en:e322" → "E322"
            .map(code -> additifRepository.findByCode(code).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // Entity -> ResponseModel (depuis la base de données)
    public ResponseModel entityToResponseModel(NutritionEntity entity) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.barCode = entity.getBarcode();
        responseModel.name = entity.getNom();
        responseModel.score = entity.getScoreNutrition();
        responseModel.grade = entity.getGrade();
        responseModel.color = entity.getCouleur();
        responseModel.additives =  enrichirAdditifs(entity.getAdditives());
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
        responseModel.additives = enrichirAdditifs(dto.product.additivesTags); 
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
        entity.setAdditives(responseModel.additives.stream()
            .map(AdditifEntity::getCode)
            .toArray(String[]::new));
        return entity;
    }
}