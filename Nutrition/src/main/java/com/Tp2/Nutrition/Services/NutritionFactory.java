package com.Tp2.Nutrition.Services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.Tp2.Nutrition.Data.Dtos.NutritionScoreDto;
import com.Tp2.Nutrition.Data.Dtos.OpenFoodFactsDto;
import com.Tp2.Nutrition.Data.Entity.AdditifEntity;
import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Repository.AdditifRepository;

/**
 * Composant responsable des conversions entre les différentes représentations d'un produit :
 * DTO (OpenFoodFacts) → ResponseModel → Entity (base de données), et inversement.
 *
 * <p>Centralise toute la logique de mapping afin que les services restent simples
 * et que les changements de format n'impactent qu'une seule classe.</p>
 */
@Component
public class NutritionFactory {

    private final AdditifRepository additifRepository;

    /**
     * Injection par constructeur — préférable à {@code @Autowired} sur champ
     * car cela rend la dépendance explicite et facilite les tests unitaires.
     */
    public NutritionFactory(AdditifRepository additifRepository) {
        this.additifRepository = additifRepository;
    }

    // -------------------------------------------------------------------------
    // Méthodes publiques de conversion
    // -------------------------------------------------------------------------

    /**
     * Convertit une {@link NutritionEntity} (lue en base) en {@link ResponseModel} (réponse API).
     *
     * @param entity l'entité JPA à convertir
     * @return le modèle de réponse correspondant
     */
    public ResponseModel entityToResponseModel(NutritionEntity entity) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setBarCode(entity.getBarcode());
        responseModel.setName(entity.getNom());
        responseModel.setScore(entity.getScoreNutrition());
        responseModel.setGrade(entity.getGrade());
        responseModel.setColor(entity.getCouleur());
        responseModel.setAdditives(enrichirAdditifs(entity.getAdditives()));
        return responseModel;
    }

    /**
     * Convertit un {@link OpenFoodFactsDto} (réponse de l'API externe) en {@link ResponseModel}.
     * Calcule le score nutritionnel via {@link NutritionScoreDto}.
     *
     * @param dto     le DTO reçu de l'API OpenFoodFacts
     * @param barcode le code-barres du produit
     * @return le modèle de réponse correspondant
     */
    public ResponseModel dtoToResponseModel(OpenFoodFactsDto dto, String barcode) {
        NutritionScoreDto nutritionScoreDto = new NutritionScoreDto(dto.product.nutriscoreData);

        ResponseModel responseModel = new ResponseModel();
        responseModel.setBarCode(barcode);
        responseModel.setName(dto.product.name);
        responseModel.setScore(nutritionScoreDto.getScore());
        responseModel.setGrade(nutritionScoreDto.getGrade());
        responseModel.setColor(nutritionScoreDto.getCouleur());
        responseModel.setAdditives(enrichirAdditifs(dto.product.additivesTags));
        return responseModel;
    }

    /**
     * Convertit un {@link ResponseModel} en {@link NutritionEntity} pour la persistance en base.
     *
     * @param responseModel le modèle de réponse à persister
     * @return l'entité JPA correspondante
     */
    public NutritionEntity responseModelToEntity(ResponseModel responseModel) {
        NutritionEntity entity = new NutritionEntity();
        entity.setBarcode(responseModel.getBarCode());
        entity.setNom(responseModel.getName());
        entity.setScoreNutrition(responseModel.getScore());
        entity.setGrade(responseModel.getGrade());
        entity.setCouleur(responseModel.getColor());
        entity.setAdditives(
            responseModel.getAdditives().stream()
                .map(AdditifEntity::getCode)
                .toArray(String[]::new)
        );
        return entity;
    }

    // -------------------------------------------------------------------------
    // Méthode privée utilitaire
    // -------------------------------------------------------------------------

    /**
     * Enrichit une liste brute de codes additifs (format "en:e322") en entités complètes
     * en les recherchant dans la base de données locale.
     *
     * <p>Les codes non trouvés en base sont silencieusement ignorés.</p>
     *
     * @param additifsRaw tableau de codes bruts issus d'OpenFoodFacts
     * @return liste d'entités {@link AdditifEntity} trouvées en base
     */
    private List<AdditifEntity> enrichirAdditifs(String[] additifsRaw) {
        if (additifsRaw == null) return List.of();
        return Arrays.stream(additifsRaw)
            .map(a -> a.replace("en:", "").toUpperCase())   // "en:e322" → "E322"
            .map(code -> additifRepository.findByCode(code).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}