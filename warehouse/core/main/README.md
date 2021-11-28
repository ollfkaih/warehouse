# Warehouse core.main

Denne pakken inneholder kjernelogikken til warehouse-programmet.

Den inneholder klassen `BaseWarehouse` som er en abstrakt klasse som definerer noen nyttige funksjoner som implementasjoner av Warehouse kan bruke.  
Klassene `Item` og `User` representeres varer og brukere som varehuset inneholder, og begge arver fra `Entity`, fordi begge f.eks. har en id som brukes til fillagring og til å skille mellom varer med like navn.  
Alle klasser som arver fra `Entity` får grensesnitt for å lytte til endringer i disse klassene, og en metode `doBatchChanges` som lar deg endre flere egenskaper til brukeren eller varen samtidig uten å varsle lytterne for hver enkelt endring.
`EntityCollection` lytter til alle alle sine entities, og kan igjen bli lyttet på og varsle lytterne sine.
`AuthSession` brukes for å gi tokens som må sendes med hver API-request som krever at man er innlogget.
