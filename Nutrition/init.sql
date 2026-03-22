-- =============================================================================
-- init.sql — Initialisation de la base de données nutritionApplicationDb
-- Exécuté automatiquement au premier démarrage du conteneur PostgreSQL.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Table Products : produits nutritionnels mis en cache depuis OpenFoodFacts
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Products (
    id               SERIAL PRIMARY KEY,
    barcode          VARCHAR(50)  NOT NULL,
    nom              VARCHAR(255) NOT NULL,
    score_nutrition  INTEGER,
    grade            VARCHAR(50),
    couleur          VARCHAR(50),
    additives        TEXT[]       -- tableau de codes additifs (ex: {"E322","E471"})
);

-- -----------------------------------------------------------------------------
-- Table additifs : référentiel des additifs alimentaires (chargé via CSV)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS additifs (
    code          VARCHAR(10)  PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    niveau_danger VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- Table Panier : un panier par utilisateur, identifié par son email
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Panier (
    user_email VARCHAR(255) PRIMARY KEY
);

-- -----------------------------------------------------------------------------
-- Table basket_products : produits contenus dans chaque panier
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS basket_products (
    basket_email VARCHAR(255) NOT NULL REFERENCES Panier(user_email) ON DELETE CASCADE,
    barcode      VARCHAR(50)  NOT NULL
);

-- -----------------------------------------------------------------------------
-- Données initiales : un produit pré-chargé pour tester sans appel réseau
-- -----------------------------------------------------------------------------
INSERT INTO Products (barcode, nom, score_nutrition, grade, couleur, additives)
VALUES ('3057640257773', 'Eau Minérale Naturelle', 0, 'Trop Bon', 'Green', '{}')
ON CONFLICT DO NOTHING;