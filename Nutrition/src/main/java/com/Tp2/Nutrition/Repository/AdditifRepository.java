package com.Tp2.Nutrition.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tp2.Nutrition.Data.Entity.AdditifEntity;


@Repository
public interface AdditifRepository extends JpaRepository<AdditifEntity, String> {
    Optional<AdditifEntity> findByCode(String code);
}