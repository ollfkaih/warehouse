# Dokumentasjon for innlevering 3

Innlevering 3 utvider lagersystemet fra innlevering 2. Vi har valgt *både* å videreutvikle JavaFX-appen vår, i tillegg til å lage en nettleser-versjon av programmet i react.

I lagersystemet kan brukeren holde styr på hvilke varer vedkommendet har i lageret sitt, samt detaljer om hver vare. Følgende data lagres om hver vare og vises for brukeren:
- Produktnavn (obligatorisk)
- Produsent
- Lagerbeholdning (antall)
- Priser (ordinærpris, salgspris og innkjøpspris)
- Plassering på lager (seksjon, reol, hylle)
- Fysiske dimensjoner (lengde, bredde, høyde, vekt)
- Strekkode

Følgende data lagres, men vises ikke for brukeren:
- Produkt-id (lagres f.eks. i tilfellet bruker ønske å lage flere brukere med samme navn, men fra forskjellige produsenter)
- Opprettelsestidspunkt (gjør det mulig å sortere produktene etter opprettelsestidspunkt)

Brukergrensesnittet fra innlevering 2 er videreført og forbedret. En bruker som ikke er logget inn vil kun ha read-only acces i appen. Brukeren vil bli presentert med et hovedvindu som inneholder en liste over alle produktene på lager, og informasjonen om produktnavn, produsent og antall på lager. Brukeren vil også ha mulighet til å søke etter produkter i et søkefelt, samt å sortere varene etter produktnavn, antall og produksjonsdato ved å bruke en nedtrekksmeny. Ved å trykke på et produkt i listen vil brukeren få opp et vindu med detaljert informasjon om produktet.

En pålogget bruker vil i tillegg ha mulighet til å redigere varelageret. Brukeren kan legge til nye produkter ved å trykke på "Legg til nytt produkt"-knappet, samt redigere eksisterende produkter ved å trykke på "rediger"-knappen i detaljevinduet til det produktet du ønsker å redigere. Siden vareantall er den egenskapen som man oftest ønsker å endre på, er det også lagt til en snarvei der man kan endre vareantall ved å trykke på +/- knappene på høyre side av hvert element i produktlisten.

Brukeren har også mulighet til å logge seg av og på, samt å registrere nye brukere. Følgende data lagres om brukeren:
- Navn
- Passord (hashet)

## Brukerhistorier
[Brukerhistorie 2 og 3](/docs/release2/userStoriesRelease2.md)
er relevant for denne innleveringen.
Funksjonalitet laget basert på første brukerhistorie, [`us-1`](/docs/release1/userStoriesRelease1.md), ligger fortsatt i appen, men knappen for å slette varer er flyttet til det nye detaljvinduet, som nevnt over.

## Prosessen bak versjon 3
I [creatingRelease3.md](/docs/release3/creatingRelease3.md) finner du noen av valgene vi har tatt i den tredje versjonen av appen vår, og litt om hva som har fungert bra og dårlig med verktøy, språk og samarbeid.

## Bildeeksempler: java-app
Hovedvindu og detaljevindu til en upålogget bruker:
![Warehouse upålogget](/docs/release3/Warehouse1_release3.png)
![DetailsView upålogget](/docs/release3/DetailsView1_release3.png)

Hovedvindu og detaljevinduer til en pålogget bruker:  
![Warehouse pålogget](/docs/release3/Warehouse2_release3.png)
![DetailsView pålogget](/docs/release3/DetailsView2_release3.png)


## Bildeeksempler: web-app


## Designskisse
Designskissen under var utgangspunktet for detaljvinduet i versjon 3.  
//TODO DESIGNSKISSE TODO FREDRIK