package com.Tp2.Nutrition.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.Tp2.Nutrition.Data.Entity.AdditifEntity;
import com.Tp2.Nutrition.Exceptions.CsvImportException;
import com.Tp2.Nutrition.Repository.AdditifRepository;



@Service
public class CsvImportService {

    @Autowired
    private AdditifRepository additifRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public void importerCsv(String cheminFichier) {
        List<AdditifEntity> additifs = new ArrayList<>();

        try {
            Resource resource = resourceLoader.getResource(cheminFichier); 

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

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
        } catch (IOException exception) {
            throw new CsvImportException("Impossible d'importer le fichier CSV des additifs.", exception);
        }

        additifRepository.saveAll(additifs);
        System.out.println(additifs.size() + " additifs importés.");
    }
}
