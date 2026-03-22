# 🥗 Food Factory API

API REST Spring Boot permettant de consulter les données nutritionnelles de produits alimentaires via leur code-barres, de gérer un panier utilisateur, et d'identifier les additifs présents dans les produits.

---

## 📋 Sommaire

- [Stack technique](#stack-technique)
- [Lancer l'application](#lancer-lapplication)
- [Ports et URLs utiles](#ports-et-urls-utiles)
- [Architecture](#architecture)
- [Concepts clés implémentés](#concepts-clés-implémentés)
- [Endpoints API](#endpoints-api)


---

## Structure du projet

```
Nutrition/
├── src/main/java/com/Tp2/Nutrition/
│   ├── Controllers/          → Points d'entrée HTTP
│   ├── Services/             → Logique métier + Factory
│   ├── Repository/           → Accès base de données (Spring Data JPA)
│   ├── Data/
│   │   ├── Dtos/             → Objets de transfert (API externe → app)
│   │   ├── Entity/           → Entités JPA (app → base de données)
│   │   └── Model/            → Modèles de réponse (app → client)
│   ├── Exceptions/           → Exceptions métier + handler global
│   ├── Configurations/       → Beans Spring (RestTemplate...)
│   └── runner/               → DataLoader (import CSV au démarrage)
├── src/main/resources/
│   ├── application.properties
│   ├── logback-spring.xml    → Configuration des logs
│   └── additifs.csv          → 300+ additifs alimentaires
├── src/test/                 → Tests unitaires (JUnit 5 + Mockito)
├── docker-compose.yml        → PostgreSQL + Seq + seq-input-gelf
└── init.sql                  → Données initiales PostgreSQL
```
---

## Stack technique

| Technologie | Rôle |
|---|---|
| Java 21 + Spring Boot 4 | Framework applicatif |
| Spring Data JPA + Hibernate | Persistance des données |
| PostgreSQL 15 | Base de données relationnelle |
| OpenFoodFacts API | Source externe de données nutritionnelles |
| Springdoc OpenAPI (Swagger) | Documentation interactive de l'API |
| Seq + GELF | Visualisation des logs structurés |
| Docker Compose | Orchestration des services |
| JUnit 5 + Mockito | Tests unitaires |

---

## Lancer l'application

### Prérequis

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installé et démarré
- Java 21 ([Temurin](https://adoptium.net/))
- Maven 3.9+

### Étape 1 — Démarrer les services Docker

```bash
docker compose up -d
```

Cela démarre **trois conteneurs** :

| Conteneur | Rôle | Port |
|---|---|---|
| `nutritionApplicationDb` | Base de données PostgreSQL | `5433` |
| `seq` | Interface web de visualisation des logs | `8090` |
| `seq-input-gelf` | Proxy qui reçoit les logs de l'app et les envoie à Seq | `12201/udp` |

> ⚠️ **Pourquoi le port 5433 et pas 5432 ?**
>
> Le port par défaut de PostgreSQL est `5432`. Si vous avez déjà PostgreSQL installé
> localement sur votre machine (ce qui est fréquent sur les postes de développeurs),
> il tourne déjà sur ce port et Docker ne pourrait pas le mapper sans conflit.
>
> Pour éviter toute erreur au démarrage, **le port externe est volontairement décalé à `5433`**.
> En interne, le conteneur PostgreSQL écoute toujours sur `5432` — seul le mapping
> hôte change. L'application Spring Boot est configurée en conséquence dans
> `application.properties` :
> ```
> spring.datasource.url=jdbc:postgresql://localhost:5433/nutritionApplicationDb
> ```

### Étape 2 — Démarrer l'application Spring Boot

```bash
cd Nutrition
mvn spring-boot:run
```

Au démarrage, le `DataLoader` vérifie si la table des additifs est vide et importe
automatiquement le fichier `src/main/resources/additifs.csv` (plus de 300 additifs).

### Étape 3 — Vérifier que tout fonctionne

```
✅ API :     http://localhost:8081/swagger-ui/index.html#/
✅ Logs :    http://localhost:8090/#/events?range=1d
✅ DB :      localhost:5433 (user: postgres / password: postgres)
```

### Lancer les tests

```bash
cd Nutrition
mvn test
```

---

## Ports et URLs utiles

| Service | URL | Description |
|---|---|---|
| **Swagger UI** | http://localhost:8081/swagger-ui/index.html#/ | Documentation interactive — testez tous les endpoints depuis le navigateur |
| **Seq (logs)** | http://localhost:8090/#/events?range=1d | Visualisation des logs structurés en temps réel |
| **API (base)** | http://localhost:8081/api | Point d'entrée de l'API REST |

---

## Architecture

L'application suit une architecture **en couches** strictement séparée.
Chaque couche a une responsabilité unique et ne connaît que la couche immédiatement
en dessous d'elle.

```
┌─────────────────────────────────────────────────────────┐
│                      CLIENT HTTP                         │
│              (Swagger UI, Postman, curl...)               │
└────────────────────────┬────────────────────────────────┘
                         │ requête HTTP
                         ▼
┌─────────────────────────────────────────────────────────┐
│                   COUCHE CONTROLLERS                      │
│                                                           │
│   NutritionController          BasketController           │
│   GET /api/Product             POST /api/Basket/add       │
│                                GET  /api/Basket           │
│                                DEL  /api/Basket           │
│                                                           │
│  → Reçoit les requêtes HTTP                               │
│  → Valide les paramètres d'entrée                         │
│  → Délègue immédiatement au service                       │
│  → Ne contient AUCUNE logique métier                      │
└────────────────────────┬────────────────────────────────┘
                         │ appel de méthode
                         ▼
┌─────────────────────────────────────────────────────────┐
│                    COUCHE SERVICES                        │
│                                                           │
│   INutritionService (interface)                           │
│        └── NutritionService (implémentation)              │
│                                                           │
│   BasketService                                           │
│                                                           │
│   NutritionFactory   ◄── convertit les représentations   │
│   CsvImportService   ◄── import CSV au démarrage         │
│                                                           │
│  → Contient toute la logique métier                       │
│  → Orchestre les appels aux repositories                  │
│  → Communique avec l'API OpenFoodFacts                    │
└──────────┬─────────────────────────┬───────────────────┘
           │                         │
           ▼                         ▼
┌──────────────────┐      ┌──────────────────────────────┐
│  COUCHE REPOS    │      │      API EXTERNE              │
│                  │      │                               │
│ NutritionRepo    │      │  OpenFoodFacts                │
│ BasketRepository │      │  world.openfoodfacts.org      │
│ AdditifRepository│      │  /api/v0/product/{code}.json  │
│                  │      │                               │
│ → Spring Data JPA│      │ → Interrogée uniquement si    │
│ → Zéro SQL manuel│      │   le produit n'est pas en BDD │
└────────┬─────────┘      └──────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────┐
│                  BASE DE DONNÉES                          │
│                PostgreSQL (Docker)                        │
│                                                           │
│  table Products      table Panier                         │
│  table additifs      table basket_products                │
└─────────────────────────────────────────────────────────┘
```

### Flux d'une requête produit

```
GET /api/Product?barcode=3057640257773
         │
         ▼
  NutritionController.getData()
         │
         ▼
  NutritionService.getNutritionData()
         │
         ├─── produit en BDD ? ──── OUI ──► NutritionFactory.entityToResponseModel()
         │                                          │
         │                                          ▼
         │                                    ResponseModel
         │
         └─── NON ──► appel OpenFoodFacts API
                             │
                             ▼
                      NutritionFactory.dtoToResponseModel()
                             │
                             ├──► NutritionScoreDto  (calcule score + grade)
                             ├──► AdditifRepository  (enrichit les additifs)
                             │
                             ▼
                      sauvegarde en BDD (NutritionRepository.save())
                             │
                             ▼
                        ResponseModel
```

### Modèles de données

```
OpenFoodFactsDto          ResponseModel            NutritionEntity
(API externe)             (réponse API)            (base de données)
─────────────             ─────────────            ────────────────
product                   barCode          ◄──►    barcode
  ├── product_name   ──►  name                     nom
  ├── nutriscore_data ──► score                     scoreNutrition
  │     └── components    grade                     grade
  │           ├── neg  ─► color                     couleur
  │           └── pos      additives  ◄──►          additives[]
  └── additives_tags ──►  (enrichis via
                           AdditifRepository)
```

---

## Concepts clés implémentés

### 1. Injection de dépendances (IoC)

Spring Boot gère l'instanciation et l'injection de toutes les dépendances.
Nous utilisons exclusivement l'**injection par constructeur** (et non par champ `@Autowired`),
ce qui rend les dépendances explicites et facilite les tests unitaires.

```java
// ✅ Injection par constructeur — dépendances visibles, testable facilement
@Service
public class NutritionService implements INutritionService {
    private final NutritionRepository nutritionRepository;
    private final NutritionFactory nutritionFactory;

    public NutritionService(NutritionRepository repo, NutritionFactory factory) {
        this.nutritionRepository = repo;
        this.nutritionFactory = factory;
    }
}

// ❌ Injection par champ — évitée car cache les dépendances
@Autowired
private NutritionRepository nutritionRepository;
```
### 2. Pattern Factory (NutritionFactory)

La `NutritionFactory` centralise toutes les conversions entre représentations.
Si le format de l'API OpenFoodFacts change, **un seul fichier** est à modifier.

```
OpenFoodFactsDto ──dtoToResponseModel()──► ResponseModel
NutritionEntity ──entityToResponseModel()──► ResponseModel
ResponseModel ──responseModelToEntity()──► NutritionEntity
```

### 3. Gestion des exceptions centralisée

Toutes les exceptions sont interceptées par le `GlobalExceptionHandler`
(annoté `@RestControllerAdvice`). Les services lèvent des exceptions métier
explicites ; le handler les traduit en réponses HTTP structurées.

```
Service lève                    Handler traduit en
────────────────────────────    ──────────────────────────────────────
ProductNotFoundException    ──► 404 Not Found  + message JSON
InvalidBasketRequestException──► 400 Bad Request + message JSON
CsvImportException          ──► 500 Internal Server Error + message JSON
Exception (catch-all)       ──► 500 Internal Server Error + message générique
```

Format de réponse d'erreur :
```json
{
  "timestamp": "2025-01-15T14:32:00",
  "status": 404,
  "error": "Not Found",
  "message": "Aucun produit trouve pour le code-barres 123."
}
```

### 4. Logs structurés avec Seq

En production, les `System.out.println` sont **interdits** — ils ne permettent pas
de filtrer, rechercher ou alerter sur les logs. L'application utilise **SLF4J + Logback**
avec un appender GELF qui envoie chaque log en JSON structuré vers Seq.

```
Application Spring Boot
    │  SLF4J log.info("Produit '{}' trouvé", barcode)
    │
    ▼
Logback (logback-spring.xml)
    │  format GELF (JSON) via logback-gelf
    │  UDP port 12201
    ▼
seq-input-gelf (Docker)
    │  HTTP
    ▼
Seq (Docker) ──► UI http://localhost:8090
```

Avantages : recherche full-text, filtres par niveau/logger/message,
alertes configurables, rétention configurable.

### 5. Cache base de données

Pour éviter d'appeler l'API OpenFoodFacts à chaque requête (latence réseau,
quota éventuel), chaque produit consulté est **automatiquement sauvegardé en BDD**.
Les appels suivants pour le même code-barres sont servis directement depuis PostgreSQL.

---

## Endpoints API

### Produits

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/Product?barcode={code}` | Données nutritionnelles d'un produit |

### Panier

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/Basket/add?email={email}` | Ajouter des produits au panier |
| `GET` | `/api/Basket?email={email}` | Récupérer les produits du panier |
| `DELETE` | `/api/Basket/removeProduct?email={email}&barcode={code}` | Supprimer un produit du panier |
| `DELETE` | `/api/Basket?email={email}` | Supprimer le panier entier |

La documentation interactive complète (avec possibilité de tester les endpoints)
est disponible sur **http://localhost:8081/swagger-ui/index.html#/**.

