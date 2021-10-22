# Warehouse
En enkel app som viser lagerstatus for et varelager. Mer dokumentasjon om de ulike  versjonene finnes i [/docs](/docs), og siste lansering i [readme.md](/docs/release2/README.md).

Bildet under viser et skjermbilde av appen, med en liste over lagerførte varer i vinduet til høyre og to detaljvinduer til venstre som lar deg se og endre egenskapene til hver enkel vare.

![Skjermbilde release2](/docs/release2/Warehouse-release2.png)

## Prosjektets oppbygning
Prosjektet består av tre modules som utfører ulike oppgaver i appen.
- [`core`](/warehouse/core/README.md) utfører kjernelogikken, som omhandler data om varehuset og tingene som er i det.
- [`ui`](/warehouse/ui/README.md) inneholder alt som kreves for å kjøre brukergrensesnittet.
- [`data`](/warehouse/data/README.md) brukes for lagring og henting av data fra disk.  

[`report`](/warehouse/report/README.md) eksisterer kun for å lage JaCoCo-rapporter.

## Brukerhistorier
Vi har laget noen brukerhistorier til de forskjellige versjonene av appen.  
[Her](/docs/release1/userStoriesRelease1.md) finner du vår første historie til versjon 1 av applikasjonen, og [her](/docs/release2/userStoriesRelease2.md) finner du brukerhistorie #2 og #3, til andre versjon av appen.
