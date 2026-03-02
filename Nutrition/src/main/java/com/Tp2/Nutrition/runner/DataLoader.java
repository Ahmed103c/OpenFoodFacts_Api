package com.Tp2.Nutrition.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Tp2.Nutrition.Repository.AdditifRepository;
import com.Tp2.Nutrition.Services.CsvImportService;


@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private AdditifRepository additifRepository;

    @Override
    public void run(String... args) throws Exception {
        if (additifRepository.count() == 0) {
            System.out.println("📂 Table vide → import du CSV...");
            csvImportService.importerCsv("C:\\Users\\legen\\Documents\\java_pro_tp2\\Nutrition\\src\\main\\resources\\additifs.csv");
        } else {
            System.out.println("✅ Données déjà présentes, import ignoré.");
        }
    }
}
