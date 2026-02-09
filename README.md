# Food Factory API 

## Base de données

J'utilise directement l'image **postgres:15-alpine** via Docker pour faciliter l'utilisation et le testing de l'API. Aucune installation de PostgreSQL n'est donc requise.

**Configuration du port :** Le port est configuré sur **5433** au lieu du port par défaut 5432. Cela évite les conflits potentiels si PostgreSQL est déjà installé et tourne en arrière-plan sur votre machine.

**Données initiales :** Un fichier **init.sql** est inclus pour pré-remplir la table Products avec le produit **"Eau Minérale Naturelle"** au démarrage du conteneur.

**Persistance des données :** Lorsque vous utilisez l'endpoint GET pour récupérer un produit, celui-ci est automatiquement sauvegardé dans le **volume** du conteneur PostgreSQL pour une utilisation ultérieure.