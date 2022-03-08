<h1 align="center">
    <img src="https://dl.dropboxusercontent.com/s/9sbp8u14x290gcv/billscan-logo-dark.svg?dl=0" alt="BillScan" height="75">
</h1>

<p>
Mit BillScan können Rechnungen hochgeladen und per Machine Learning ausgewertet sowie digital archiviert werden. Dabei werden Informationen zu Kaufdatum, Geschäften, Produkten und Preisen ausgelesen, intelligent abgespeichert sowie kategorisiert.
</p>

<p align="center">
  <a href="#team">Team</a> •
  <a href="#architektur">Architektur</a> •
  <a href="#anleitung">Anleitung</a>
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

- Java und Kotlin installiert
- Gradle und npm installiert
- IDE (IntelliJ IDEA) mit Kotlin Erweiterung installiert
- Docker Desktop installiert und Docker Hub Account eingerichtet
- Datenbankserver und MySQL-Datenbank verfügbar
- Mailserver verfügbar
- Git installiert

### Installation

1. Lokale Kopie des GitHub-Repositories erstellen
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

---
> FH Joanneum, Informationsmanagement (IMA19), BUEPA
