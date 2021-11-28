# Warehouse UI

Denne modulen implementerer UI-logikken til warehouse-appen, med javaFX.

Hovedlogikken i modulen utføres i `WarehouseController` som bruker et `ClientWarehouse` fra `core`-modulen for å holde styr på tingene som legges inn i varehuset. Kontrolleren har logikk for å vise tingene i brukergrensesnittet og for å gjøre det mulig å gjøre endringer i varehuset fra grensesnittet. `ui`-modulen bruker også `data`-modulen for å lagre varehuset etter alle endringer, og laste det inn når appen åpnes.

En separat kontroller, `DetailsViewController`, håndterer detaljvinduene til hver vare på lager. Dette vinduet er måten man hovedsaklig gjør endringer på varer. `BarcodeCreator` sørger for at vi kan laste inn strekkoder (på EAN13-format) i detaljvinduet.

`RegisterController` og `LoginController` er kontrollerne for å henholdsvis registrere en ny bruker og å logge inn.
