-- init.sql
CREATE TABLE IF NOT EXISTS Products (
    id SERIAL PRIMARY KEY,
    barcode VARCHAR(50) NOT NULL,
    nom VARCHAR(255) NOT NULL,
    score_nutrition INTEGER,
    grade VARCHAR(50),
    couleur VARCHAR(50)
);

-- Données initiales
INSERT INTO Products (barcode, nom, score_nutrition, grade, couleur) VALUES 
    ('3057640257773', 'Eau Minérale Naturelle', 0,'Trop Bon','Green');