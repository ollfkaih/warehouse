# Warehouse ui
Denne modulen implementerer UI-logikken til warehouse-appen, med javaFX.

Hovedlogikken i modulen utføres i `WarehouseController` som bruker et `Warehouse` fra `core`-modulen for å holde styr på tingene som legges inn i varehuset. Kontrolleren har logikk for å vise tingene i brukergrensesnittet og for å gjøre det mulig å gjøre endringer i varehuset fra grensesnittet. `ui`-modulen bruker også `data`-modulen for å lagre varehuset etter alle endringer, og laste det inn når appen åpnes.