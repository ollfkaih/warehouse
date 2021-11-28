# Dokumentasjon for innlevering 2

Innlevering 2 implementerer et enkelt lagersystem som kan holde styr på hvilke varer brukeren har i lageret sitt og detaljer om hver vare. Følgende data kan lagres i programmet:

- Merke
- Priser (ordinærpris, salgspris og innkjøpspris)
- Plassering på lager (støtte for delplasseringer)
- Lagerbeholdning (antall)
- Størrelse (lengde, bredde og høyde)
- Vekt
- Strekkode

Det enkle brukergrensesnittet fra første lansering er i stor grad videreført. I release 2 vil hvert produkt åpnes i et nytt vindu kalt detaljvindu hvor man kan redigere detaljene. +/- knappene i hovedvinduet skjules frem til man har den spesifikke raden i fokus. Det er lagt inn mulighet for login med brukernavn og passord, og å logge ut igjen.

Listen over varer og antall blir lagret til disk i en JSON-fil slik som i release 1. Ettersom detaljvinduet fungerer både som brukerens metode for å lese og endre detaljer om en vare har vi valgt å ikke lagre dataene i detaljvinduet før brukeren trykker på lagreknappen. På denne måten unngår vi ikke-tiltenkt lagring ved å kreve at brukeren aktivt må lagre.

## Brukerhistorier

[Brukerhistorie 2 og 3](/docs/release2/userStoriesRelease2.md)
er relevant for denne innleveringen.
Funksjonalitet laget basert på første brukerhistorie, [`us-1`](/docs/release1/userStoriesRelease1.md), ligger fortsatt i appen, men knappen for å slette varer er flyttet til det nye detaljvinduet, som nevnt over.

## Prosessen bak versjon 2

I [creatingRelease2.md](/docs/release2/creatingRelease2.md) finner du noen av valgene vi har tatt i andre versjon av appen vår, og litt om hva som har fungert bra og dårlig med verktøy, språk og samarbeid.

## Designskisse

Designskissen under var utgangspunktet for detaljvinduet i versjon 2.  
![Designskisse innlevering 2](/docs/release2/Warehouse_p2.png)
