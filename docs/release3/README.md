# Dokumentasjon for innlevering 3

Innlevering 3 utvider lagersystemet fra innlevering 2. Vi har valgt _både_ å videreutvikle JavaFX-appen vår, i tillegg til å lage en nettleser-versjon av programmet i react.

I lagersystemet kan brukeren holde styr på hvilke varer vedkommendet har i lageret sitt, samt detaljer om hver vare. Følgende data lagres om hver vare og vises for brukeren:

- Produktnavn (obligatorisk)
- Produsent
- Lagerbeholdning (antall)
- Priser (ordinærpris, salgspris og innkjøpspris)
- Plassering på lager (seksjon, reol, hylle)
- Fysiske dimensjoner (lengde, bredde, høyde, vekt)
- Strekkode

Følgende data lagres, men vises ikke for brukeren:

- Produkt-id (lagres f.eks. i tilfellet bruker ønske å lage flere produkter med samme navn, men fra forskjellige produsenter)
- Opprettelsestidspunkt (gjør det mulig å sortere produktene etter opprettelsestidspunkt)

Brukergrensesnittet til JavaFX-appen er en videreføring fra innlevering 2. En bruker som ikke er logget inn vil kun ha read-only acces i appen. Brukeren vil bli presentert med et hovedvindu som inneholder en liste over alle produktene på lager, og informasjonen om produktnavn, produsent og antall på lager. Brukeren vil også ha mulighet til å søke etter produkter i et søkefelt, samt å sortere varene etter produktnavn, antall og produksjonsdato ved å bruke en nedtrekksmeny. Ved å trykke på et produkt i listen vil brukeren få opp et vindu med detaljert informasjon om produktet.

En pålogget bruker vil i tillegg ha mulighet til å redigere varelageret. Brukeren kan legge til nye produkter ved å trykke på "Legg til nytt produkt"-knappet, samt redigere eksisterende produkter ved å trykke på "rediger"-knappen i detaljevinduet til det produktet du ønsker å redigere. Siden vareantall er den egenskapen som man oftest ønsker å endre på, er det også lagt til en snarvei der man kan endre vareantall ved å trykke på +/- knappene på høyre side av hvert element i produktlisten.

Brukeren har også mulighet til å logge seg av og på, samt å registrere nye brukere. Følgende data lagres om brukeren:

- Navn
- Passord (hashet)

Det lagres også en bruker-id for hver bruker. Denne er ikke strengt nødvendig ettersom brukernavn er unike, men den gjør implementasjonen av applikasjonen enklere ettersom noen funksjoner kan brukes både til varer og brukere. I tillegg muligjør det endring av brukernavn som en fremtidig funksjon.

Brukergrensesnittet til web-appen likner for det meste på JavaFX-appen, men med et par viktige forskjeller. Når man åpner webappen blir man for eksempel møtt med et påloggingsvindu som krever at man logger seg på for å kunne se hovedsiden. I tillegg er registrering av ny bruker fjernet. Dette er på grunn av sikkerhet, slik at folk ikke bare kan koble seg til serveren og registrere en ny bruker.

Layouten er også litt annerledes. For eksempel får man opp detaljinformasjon på høyre side av hovedvinduet når man trykker på en vare, i stedet for at det åpnes et helt nytt vindu.

Strukturen på React-prosjektet følger ikke samme trelags-struktur som Java-prosjektet, istedenfor er nettsiden delt opp i mange komponenter som har egne interne tilstander, og logikken som kreves for å vise innholdet i den komponenten på skjermen.

## Brukerhistorier

[Brukerhistorie 4, 5 og 6](/docs/release3/userStoriesRelease3.md)
er relevant for denne innleveringen.
Funksjonalitet laget basert på de første brukerhistoriene, [`us-1`](/docs/release1/userStoriesRelease1.md) og [2 og 3](/docs/release2/userStoriesRelease2.md), ligger fortsatt i appen, men knappen for å slette varer er flyttet til det nye detaljvinduet, som nevnt over.

## Prosessen bak versjon 3

I [creatingRelease3.md](/docs/release3/creatingRelease3.md) finner du noen av valgene vi har tatt i den tredje versjonen av appen vår, og litt om hva som har fungert bra og dårlig med verktøy, språk og samarbeid.

## Bildeeksempler: java-app

Hovedvindu og detaljevindu til en upålogget bruker:  
![Warehouse upålogget](/docs/release3/screenshots/Warehouse1_release3.png)
![DetailsView upålogget](/docs/release3/screenshots/DetailsView1_release3.png)

Hovedvindu og detaljevinduer til en pålogget bruker:  
![Warehouse pålogget](/docs/release3/screenshots/Warehouse2_release3.png)
![DetailsView pålogget](/docs/release3/screenshots/DetailsView2_release3.png)

## Designskisser: web-app

Web-appen er basert på følgende mockups:

<img src="docs/release3/mockups/Webapp_Splashpage.png" alt="Splashpage" width="33%" />
<img src="docs/release3/mockups/Webapp_Dashboard.png" alt="Dashboard" width="33%" />
<img src="docs/release3/mockups/Webapp_DetailsView.png" alt="DetailsView" width="33%" />

## Bildeeksempler: web-app

Forside:
![Påloggingsvindu](/docs/release3/screenshots/Webapp_login.png)

Hovedvindu:
![Hovedvindu](/docs/release3/screenshots/Webapp_main.png)
