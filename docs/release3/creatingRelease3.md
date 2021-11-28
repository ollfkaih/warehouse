# Prosessen bak versjon 3

Dette dokumentet er ment å kort dokumentere hvordan vi har samarbeidet for å lage tredje og siste versjon av Warehouse-appen, og skal kort si hva som har fungert bra og dårlig. I tillegg skal vi nevne valg vi har tatt i prosessen og grunnlaget for disse.

## Hva vi er fornøyd/misfornøyd med

### Misfornøyd: Store issues og branches som endrer på mange filer over lang tid

Vi har blitt flinkere på å ikke lage for store branches, men har alikevel opplevt et par ganger at noen i gruppen har laget litt for store issues/grener. Issue #154 burde for eksempel bli splittet opp i flere issues som man kunne jobbe på hver for seg, siden den inneholdt såpass mange endringer i flere ulike filer. Dermed oppsto en del oppstå en del merge-conflikter som måtte løses når denne grenen skulle pushes til main.

Det er ikke alltid lett å forutse at en issue vil bli for stor. Ofte kan "enkle" issues vise seg å være mer komplekse og kreve flere endringer enn man i forkant trodde. En ting vi har lært av store issues er at det alltid er bedre å ha mange litt for små issues, enn én alt for stor issue.

### Fornøyd: god bruk av kravspesifikasjoner og brukerhistorie

Vi har i release 3 blitt flinkere til å skrive og aktivt bruke brukerhistoriene fra starten av. Vi har også blitt flinkere på å definere klare mål i startfasen av en release og ungått å endre dem nevneverdig underveis.

### Fornøyd: Parprogrammering

På større issues har vi i stor grad benyttett par-programmering. Det har ikke vært faste par, men vi har byttet på parene etter behov. Vår erfaring er at det er en god og effektiv arbeidsmetode. Man står sjeldenere fast i et problem, da to hoder tenker raskere enn et. I tillegg sørger det for at minst to personer kjenner koden godt.

### Fornøyd: Faste ukentlige møter

Vi har hatt faste fysiske møter to ganger i uken gjennom hele release 1, 2 og 3.
På møtene starter vi med et Stand-up-møte hvor hvert gruppemedlem går gjennom hva vedkommende har gjort siden sist og planlegger å få fullført på møtet. Videre diskuterer vi nye ideer, oppretter issues, utdeler oppgaver og setter opp par-programmering ved behov.

### Fornøyd: Bruk av verktøy for kontinuerlig integrasjon (CI/CD)

Vi har satt opp pipelines i GitLab som kjører ved hver commit. Pipelinen bruker et Docker-image og forsøker å bygge og kjøre testene vi har skrevet. Vi syntes det er problematisk å ikke ha mulighet til å kjøre ui-testene hodeløst i pipelinen. Dette kan skape en falsk trygghet ved at testene i pipelinen blir grønne selv om ui-testene ikke har blitt kjørt.

Pipelinen gjør følgende:

Java:

- bygger java-appen
- kjører SpotBugs for å finne kodefeil
- kjører java-testene
- kjører CheckStyle for å sjekke at koden er riktig formatert

Webapp:

- kjører Yarn build
- kjører Cypress-tester
- kjører Pretiier for å sjekke at koden er riktig formatert

### Fornøyd: Bruk av gode commitmeldinger og Squashe commits

Vi mener selv vi stort sett har vært flinke på å skrive gode og oversiktlige commit-meldinger. Vi er fornøyde med å bruke "squash commits" funksjonen der det er hensiktsmessig for å redusere antall commits i main, slik at commit-historikken blir ryddigere. Vi er også fornøyde med detaljnivået i commit-meldingene som gjør det enkelt for oss å se hva som har blitt gjort i tidligere merges.

### God fokus på MMI

Vi har lagt vekt på å lage brukervennlige brukergrensesnitt. Vi har brukt brukerhistoriene for å prøve å se for oss den typiske brukeren og behovene vedkommende har, for deretter å bruke dette i designprosessen. Designprosessen har bestått av å lage designskisser (mockups) for deretter å implementere designet i både Java-appen og Web-appen.

Vi har i tillegg utført flere uformelle brukertester for å se om testbrukerne brukte appen slik den var tiltenkt. Dette har gitt oss nyttig innsikt og formet våre designvalg.
