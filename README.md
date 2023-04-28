# Recommendation Application

## Diagram
![Диаграмма без названия drawio (2)](https://user-images.githubusercontent.com/34836796/235269204-629ec5eb-29dd-4fc0-b635-43594ad017d8.png)

## Technologies

**Development**: Java 17, Spring Boot (MVC, Data JPA, Kafka), Spring Cloud (API Gateway, Kubernetes);

**Database**: MySql;

**Build tools**: Gradle, Docker, Docker Compose, Kubernetes, Bash;

**Testing**: Junit, Mockito, Testcontainers, Spring Test;

**Other**: Git, Rest API, SQRS, Event Sourcing;

## Usage

### Local

1) Go to docker compose folder: from the project root "cd .\docker\compose";
3) Start related services via docker compose: run "docker compose -d up";
4) Start application services in Idea.

### Prod

1) Go to scripts folder: from the project root "cd .\scripts";
2) Build docker images of services: run ".\build-project-images-layered.sh";
3) Start related services in Kubernetes: run ".\start-k8s-bootstrap-resources.sh";
4) Start application services in Kubernetes: run ".\start-k8s-service-resources.sh".

## For feedback
**e-mail:** artsiom_barodka@epam.com     

