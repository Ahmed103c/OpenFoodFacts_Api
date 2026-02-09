-- init.sql
CREATE TABLE IF NOT EXISTS Products (
    id SERIAL PRIMARY KEY,
    barcode VARCHAR(50) NOT NULL,
    nom VARCHAR(255) NOT NULL,
    score_nutrition INTEGER
);

-- Données initiales
INSERT INTO Products (barcode, nom, score_nutrition) VALUES 
    ('3057640257773', 'Eau Minérale Naturelle', 0);