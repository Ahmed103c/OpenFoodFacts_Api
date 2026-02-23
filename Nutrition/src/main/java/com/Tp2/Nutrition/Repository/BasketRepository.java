package com.Tp2.Nutrition.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tp2.Nutrition.Data.Entity.BasketEntity;




@Repository
public interface BasketRepository extends JpaRepository<BasketEntity, String> {     
    Optional<BasketEntity> findByUserEmail(String email);
}
