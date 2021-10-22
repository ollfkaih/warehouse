# Prosessen bak versjon 2 
Dette dokumentet er ment å kort dokumentere hvordan vi har samarbeidet for å lage andre versjon av Warehouse-appen, og skal kort si hva som har fungert bra og dårlig. Basert på dette skal vi vurdere hvordan vi best mulig kan arbeide fremover mot lansering 3. I tillegg skal vi nevne  valg vi har tatt i prosessen og grunnlaget for disse.

Koden er delt opp i modulene 'core', 'data', 'ui'.

## Hva vi skal fortsette med/unngå i fremtiden

### Unngå: Store issues som endrer på mange filer over lang tid 
Spesielt issue #13 gjorde oss oppmerksomme på dette problemet: Den tilhørende grena endret på mange filer, og de som jobbet i andre grener samtidig måtte rebase på gren 13 for å ha tilgang til det nye UI-et.  
Noe av problemet er at issuet ble laget før release 1 var ferdig (som en idé), men ikke delt opp i flere issues før vi begynte å jobbe på det.  
Den beste løsningen er å dele opp issuet når man ser at dette vil
- medføre endringer i mange filer, og/eller
- ta betydelig tid før det blir flettet inn i main.  

Som et eksempel, burde 13 være delt opp i følgende issues:

- (Tegne mockup av designet for hånd/i tegneprogram)
- Lage en prototype av designet i FXML (uten fungerende felt/logikk bak)
- Koble FXML sammen med enkel kontrollerlogikk
- Skrive mer kontrollerlogikk, f.eks. validering av tekstsfelt eller visning av feilmeldinger  

Hadde vi gjort det på denne måten, hadde man ikke behøvd å rebase så mange andre grener på 13, eller å merge andre grener inn i 13 fordi man trengte filene i issue 13.
I tillegg får alle tilgang til, og kan jobbe med, det nye designet tidligere, og man får færre merge-konflikter.

### Unngå: Definer kravspesifikasjon til relase for sent
Vi må unngå å endre på kravspesifikasjon mot slutten av hver release. Vi bør også definere klarere mål i det vi starter med en release.
En utfordring med å definere klare mål er at vi ofte kommer med gode ideer underveis i releasene, som igjen gjør at spesifikasjonene endres.
Vi har også erfart at vi bør bruke brukerhistoriene mer aktivt i utviklingsprosessen, og unngå å endre på disse underveis.
I release 3 bør vi derfor begynne med å lage brukerhistoriene, og finne ut hva som skal med i koden før vi går i gang med å skrive kode.

### Fortsette: Parprogrammering
På issuene av større størrelse har vi i stor grad benyttett par-programmering. Vi har endret på parene ved hver issue. Vår erfaring er at det er en god og effektiv arbeidsmetode.
Det er fint å ha to hoder som kan løse problemene som oppstår i tillegg til at minst to på gruppa forstår logikken. 

### Fortsette: Faste ukentlige møter
Vi har hatt faste fysiske møter to ganger i uken gjennom hele release 2. 
På møtene starter vi med et Stand-up-møte hvor hvert gruppemedlem går gjennom hva vedkommende har gjort siden sist og planlegger å få fullført på møtet. Videre diskuterer vi nye ideer, oppretter issues, utdeler oppgaver og setter opp par-programmering ved behov.

### Fortsette: Bruk av verktøy for kontinuerlig integrasjon (CI/CD)
Vi har satt opp pipelines i GitLab som kjører ved hver commit. Pipelinen bruker et Docker-image og forsøker å bygge og kjøre testene vi har skrevet i tillegg til å kjøre SpotBugs for å oppdage feil og CheckStyle for å verifisere at koden ser i henhold ut. 
Vi syntes det er problematisk å ikke ha mulighet til å kjøre ui-testene hodeløst i pipelinen. Dette kan skape en falsk trygghet ved at testene i pipelinen blir grønne selv om ui-testene ikke har blitt kjørt.

### Fortsette: Bruk av gode commitmeldinger og Squashe-commits
Vi er fornøyde med å bruke "squash-commits" funksjonen for å redusere antall commits i main, slik at commit-historikken blir ryddigere. Vi er også fornøyde med detaljnivået i commit-meldingene som gjør det enkelt for oss å se hva som har blitt gjort i tidligere merges.




