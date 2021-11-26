# Warehouse core.main
Denne pakken inneholder kjernelogikken til warehouse-programmet. 

Den inneholder klassen `Warehouse` som representerer varehuset, og ved hjelp av klassene `Item` og `User` representeres varer og brukere som varehuset inneholder.  
`Warehouse` og `Item` har funksjoner for å legge til, fjerne eller endre egenskaper ved varene som er i varehuset. Både `Warehouse` og `Item` har nå grensesnitt for å lytte til endringer i disse klassene.
Brukerne kan endres ved hjelp av `Warehouse` og `User`.

Under ligger et diagram som viser hvordan `Warehouse` holder kontroll på `Item` og `User`-klassene. Metodene i de forskjellige klassene er utelatt, og kun interne felter vises.  
![PlantUML-diagram of core](/docs/resources/WarehouseStructure.svg)
