package com.Tp2.Nutrition.Services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tp2.Nutrition.Data.Entity.AdditifEntity;
import com.Tp2.Nutrition.Data.Model.Additif;
import com.Tp2.Nutrition.Repository.AdditifRepository;



@Service
public class CsvImportService {

    @Autowired
    private AdditifRepository additifRepository;

    public void importerCsv(String cheminFichier) throws Exception {
        List<AdditifEntity> additifs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(cheminFichier), StandardCharsets.UTF_8))) {

            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.isBlank()) continue;

                String[] colonnes = ligne.split(";", -1);
                if (colonnes.length < 3) continue;

                AdditifEntity a = new AdditifEntity();
                a.setCode(colonnes[0].trim());
                a.setName(colonnes[1].trim());
                a.setDanger(colonnes[2].trim());
                additifs.add(a);
            }
        }

        additifRepository.saveAll(additifs);
        System.out.println("✅ " + additifs.size() + " additifs importés.");
    }
}