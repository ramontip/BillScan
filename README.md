<h1 align="center">
    <img src="https://dl.dropboxusercontent.com/s/9sbp8u14x290gcv/billscan-logo-dark.svg?dl=0" alt="BillScan" height="75">
</h1>

<p>
Mit BillScan können Rechnungen hochgeladen und per Machine Learning ausgewertet sowie digital archiviert werden. Dabei werden Informationen zu Kaufdatum, Geschäften, Produkten und Preisen ausgelesen, intelligent abgespeichert sowie kategorisiert.
</p>

<p align="center">
  <a href="#team">Team</a> •
  <a href="#architektur">Architektur</a> •
  <a href="#anleitung">Anleitung</a> •
  <a href="#screenshots">Screenshots</a>
</p>

## Team

- <a href="https://github.com/ramontip">Ramon Tippl</a> <b>(Projektleiter)</b>
- <a href="https://github.com/rolkef">Christopher Rolke</a>
- <a href="https://github.com/DavidSeb2020">David Sebernegg</a>

<p>Die Betreuung erfolgt durch DI DI (FH) Michael Nestler.</p>
<p>FH Joanneum, Informationsmanagement (IMA19), Bereichsübergreifende Projektarbeit, WS 2021/22</p>

## Architektur

- Webapplikation - Kotlin, Spring Framework / HTML, CSS, JS, Bootstrap
- Azure Cognitive Services - Machine Learning Integration
- Docker und Azure Web App Services - Deployment
- Azure Database for MySQL-Server - Deployment
- GitHub - Collaboration, Version Control, Backlog

## Anleitung

Im folgenden wird eine kurze Installationsanleitung für das Projekt gegeben. Eine ausführliche Anleitung mit Erklärungen
befindet sich in der Projektdokumentation. Alle Befehle müssen im Terminal innerhalb des Projektordners ausgeführt
werden.

### Voraussetzungen

- Java und Kotlin
- Gradle und npm
- IDE (IntelliJ IDEA) mit Kotlin Erweiterung
- Docker Desktop und Docker Hub Account
- Datenbankserver und MySQL-Datenbank
- Mailserver
- Git Installation

### Installation

1. Aktuellen Release [herunterladen](https://github.com/ramontip/BillScan/releases/latest)
2. Projektordner mit IntelliJ IDEA öffnen, `build.gradle.kts` öffnen und Gradle Dependencies installieren
3. npm Dependencies mit dem Befehl `npm install` installieren
4. `src/main/resources/application.properties` öffnen und Datenbank- sowie Mailserver-Konfiguration anpassen
5. `src/main/kotlin/net/billscan/billscan/controller/user/BillController.kt` öffnen und `VISION_API_ENDPOINT`
   sowie `VISION_API_KEY` anpassen
6. Bei Änderungen des CSS den Befehl `sass src/main/sass/bootstrap.scss src/main/resources/static/css/bootstrap.css`
   ausführen
7. Projekt lokal starten und testen

### Deployment

1. Docker Image mit dem Befehl `docker build -t dockerimage .` erstellen
2. Docker Image mit dem Befehl `docker run -p 8020:8020 dockerimage` ausführen
3. Docker Image in Docker Hub hochladen
4. Docker Hub mit Azure Web App Service verknüpfen
5. Eigene Domain mit Web App Service verknüpfen und SSL-Zertifikat erstellen

## Screenshots

![home](https://user-images.githubusercontent.com/50173436/164891577-fed44b4c-5a56-4b33-823d-0e5d1ccb9580.png)

![dashboard](https://user-images.githubusercontent.com/50173436/164891575-3967d79d-158c-4ce3-8a4d-404e03443771.png)

![bill-upload](https://user-images.githubusercontent.com/50173436/164891574-b7ab3ab7-a67f-4da5-a7f0-c26e5849d5bd.png)

![bill-check](https://user-images.githubusercontent.com/50173436/164891558-e1d079e0-c526-4053-babc-3ba4a8f0485e.png)

![bill-overview](https://user-images.githubusercontent.com/50173436/164891569-ae1c7e2f-542e-442c-9513-a87d8f8731c3.png)

![expenses](https://user-images.githubusercontent.com/50173436/164891576-08f2c4e5-59a8-4dba-ba24-23c12e69f40b.png)

![search](https://user-images.githubusercontent.com/50173436/164891579-d53200a2-612e-4f7c-9049-fa2ecdeda33e.png)

---
> FH Joanneum, Informationsmanagement (IMA19), BUEPA
