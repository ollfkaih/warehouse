# Warehouse
En enkel app som viser lagerstatus for et varelager. Mer dokumentasjon om de ulike  versjonene finnes i [/docs](/docs), og siste lansering i [readme.md](/docs/release3/README.md).

Bildet under viser et skjermbilde av appen, med en liste over lagerførte varer i vinduet til høyre og to detaljvinduer til venstre som lar deg se og endre egenskapene til hver enkel vare.

<img src="docs/release3/screenshots/java_app.png" width="100%" />

## Prosjektets oppbygning
Prosjektet består av tre moduler som utfører ulike oppgaver i appen.
- [`core`](/warehouse/core/README.md) utfører kjernelogikken, som omhandler data om varehuset og tingene som er i det.
- [`ui`](/warehouse/ui/README.md) inneholder alt som kreves for å kjøre brukergrensesnittet.
- [`data`](/warehouse/data/README.md) brukes for lagring og henting av data fra disk.  
- `localserver` brukes for å emulere en tjener når man kjører appen med et lokalt varelager.
- `springboot.server` inneholder koden som kjøres for å starte en nettjener.

[`report`](/warehouse/report/README.md) eksisterer kun for å lage JaCoCo-rapporter.

## Brukerhistorier
Vi har laget noen brukerhistorier til de forskjellige versjonene av appen.  
[Her](/docs/release1/userStoriesRelease1.md) finner du vår første historie til versjon 1 av applikasjonen, og [her](/docs/release2/userStoriesRelease2.md) finner du brukerhistorie #2 og #3, til andre versjon av appen. [Her](/docs/release3/userStoriesRelease3.md) finner du brukerhistorie #4, #5 og #6, til tredje versjon av appen. 

## Kjøre Java-tjeneren
1. Sørg for at du bytter mappe til `warehouse`
2. Kjør `mvn install` (Om du ønsker å spare litt tid, kan du bruke `mvn install -DskipTests -DskipUiTests`)
3. Bytt mappe til `springboot/server`
4. Kjør `mvn spring-boot:run`

## Kjøre Java-klienten

1. Sørg for at du bytter mappe til `warehouse`
2. Kjør `mvn install` (Om noen tester skulle feile og du forstatt vil kjøre appen, kan du bruke `mvn install -DskipTests -DskipUiTests`)
3. Bytt så mappe til `ui`
4. Kjør programmet med `mvn javafx:run`

## Bygge java-klienten

For å bygge en kjørbar (portabel) versjon av appen, må du først bytte gren til `package-java-app`, og så følge instruksjonene i readme-en i [ui](/warehouse/ui) der.
