# 8INF853 - Prixbanque Microservices

## Le MVP doit répondre aux exigences suivantes :

### a) fonctionnelles :

1. Les clients doivent pouvoir de créer un compte (moyenne priorité)
    > [account-service](https://github.com/christianfs/8INF853-prixbanque/tree/main/prixbanque/account-service)

2. Les clients doivent pouvoir s'authentifier (basse priorité)
    > [authentication-service](https://github.com/christianfs/8INF853-prixbanque/tree/main/prixbanque/authentication-service)

3. Les clients doiventi pouvoir consulter leur relevé actuel (moyenne priorité)
    > [statement-service](https://github.com/christianfs/8INF853-prixbanque/tree/main/prixbanque/statement-service)

4. Les clients doivent pouvoir envoyer un virement à un autre client (haute priorité)
    > [transfer-service](https://github.com/christianfs/8INF853-prixbanque/tree/main/prixbanque/transfer-service)

### b) de développement  :

- [x] utiliser le contrôle de version
- [x] utiliser des mécanismes de «build»  (comme le Travis, le Maven, le Gradle, etc)
- [ ] optionnel: utiliser une version préliminaire des techniques de «Continuous Integration»
    - Outils suggérées:  Jenkins, TravisCI,  CircleCI, GitLab, etc

### c) architecteturaux :

- [x] créer un microservice pour chaque exigence fonctionnelle
- [ ] optionnel : créer des tests unitaires pour les microservices
- [x] optionnel : fournir un API gateway pour les microservices
- [x] utiliser dans l'architecture un load balancer
    - optionnel : Travailler avec [Docker Compose](https://docs.docker.com/compose)
- [x] utiliser comme outil de données le MongoDB ou le Firebase Database

### d) de documentation :

- [ ] créer un  guide d'installation et d'utilisation (simple) avec le instructions de déploiement dans le README.

## Preuve de concept:

1. Démontrez le nombre de transactions par seconde que le MVP peut effectuer et comment l'architecture y contribue.
Pour cela, utilisez la fonctionnalité de virement.

2. Enregistrer une vidéo avec la démo et mettez le lien dans le README.

## Rapport :

1. Introduction : avec context et problématique du projet 3

2. MVP : Description du MVP selon les sections du item (1)

3. Preuve de concept : Expliquez les objectifs de la preuve de concept, comment elle a été réalisée, quels outils étaient nécessaires et quels en ont été les résultats. Discuter la contribuition de l'architecture pour lé résultat obtenu.

4. Conclusion en tenant compte du travail effectué sur tous les projets (PP1, PP2, PP3) jusqu'à celui-ci.
