package com.Tp2.Nutrition.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tp2.Nutrition.Data.Entity.NutritionEntity;

@Repository
public interface NutritionRepository extends JpaRepository<NutritionEntity, Integer> {
    Optional<NutritionEntity> findByBarcode(String barcode);
}
