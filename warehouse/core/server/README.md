# Warehouse core.server

Denne pakken inneholder kjernelogikken til warehouse-serveren.

Den inneholder klassen `ServerWarehouse` som representerer varehuset, og inneholder alle egenskapene som er relevant for serveren.

Under ligger et diagram som viser hvordan `ServerWarehouse` holder kontroll på varer, brukere og innloggingsøkter. Metodene i de forskjellige klassene er utelatt, og kun interne felter vises.  
![PlantUML-diagram of ClientWarehouse](/docs/resources/serverWarehouse.png)
