# Dokumentasjon for innlevering 1

Innlevering 1 implementerer et enkelt lagersystem som kan holde styr på hvilke varer brukeren har i lageret sitt og hvor mange av hver vare de har. 
Det er et enkelt brukergrensesnitt der brukeren kan se en liste over varene som finnes på lageret og antallet. Brukeren kan også endre antallet av varer og fjerne eller legge til varetyper. 
Listen over varer og antall blir lagret til disk i en JSON-fil, slik at dataen forblir selv om brukeren lukker appen.

## Brukerhistorie
[Finnes her](/docs/release1/userStoriesRelease1.md)
Kun `us-1` er relevant for denne innleveringen

For lagersjefen fra us-1 er det nyttig at alle varene på lageret vises som en liste der antallet av hver vare er lett synlig. Dersom lagersjefen trenger å endre antallet av en vare (f.eks. når noe blir solgt) er det lett tilgjengelige knapper for å øke eller minke antallet av varene. Når lagersjefen får inn en ny vare er det lett å legge den til ved hjelp av tekstfeltet, og hvis en vare går ut av sortimentet kan den enkelt slettes. Lagersjefen vil også sette pris på at programmet lagrer varene automatisk hver gang det blir gjort en endring, slik at det ikke er noen fare for at endringer går tapt når programmet lukkes. 

## Designskisse
![Designskisse innlevering 1](/docs/release1/Warehouse_p1.png)
