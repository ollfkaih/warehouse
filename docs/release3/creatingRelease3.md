# Prosessen bak versjon 3
Dette dokumentet er ment å kort dokumentere hvordan vi har samarbeidet for å lage tredje og siste versjon av Warehouse-appen, og skal kort si hva som har fungert bra og dårlig.  I tillegg skal vi nevne valg vi har tatt i prosessen og grunnlaget for disse.

Koden er delt opp i modulene 'core', 'data', 'ui'.

## Hva vi er fornøyd/misfornøyd med

### Misfornøyd: Store issues og branches som endrer på mange filer over lang tid
Vi har blitt flinkere på å ikke lage for store branches, men har alikevel opplevt et par ganger at noen i gruppen har laget litt for store issues/grener. Issue #154 burde for eksempel bli splittet opp i flere issues som man kunne jobbe på hver for seg, siden den inneholdt såpass mange endringer i flere ulike filer. Dermed oppsto en del oppstå en del merge-conflikter som måtte løses når denne grenen skulle pushes til main.

Det er ikke alltid lett å vite i forkant at en issue for stor. Dette er fordi noen issues plutselig kan være mye vanskeligere og kreve flere endringer en man i forkant trodde. Men en nøkkelregel vi har lært av denne erfaringen er at alltid er bedre å ha mange litt for små issues, enn én alt for stor issue.

### Fornøyd: god bruk av kravspesifikasjoner og brukerhistorie
Vi har i release 3 blitt flinkere til å skrive og aktivt bruke brukerhistoriene fra starten. Vi har også blitt flinkere på å definere klare mål i det vi startet med en release og ungått og endre på dem underveis.

### Fornøyd: Parprogrammering
På store issues har vi i stor grad benyttett par-programmering. Det har ikke vært faste par, men vi har byttet på parene etter behov. Vår erfaring er at det er en god og effektiv arbeidsmetode. Det er fint å ha to hoder som kan løse problemene som oppstår. I tillegg ungår vi dermed at det kun er én person som har satt seg inn i issuet.  

### Fornøyd: Faste ukentlige møter
Vi har hatt faste fysiske møter to ganger i uken gjennom hele release 1, 2 og 3. 
På møtene starter vi med et Stand-up-møte hvor hvert gruppemedlem går gjennom hva vedkommende har gjort siden sist og planlegger å få fullført på møtet. Videre diskuterer vi nye ideer, oppretter issues, utdeler oppgaver og setter opp par-programmering ved behov. 

### Fornøyd: Bruk av verktøy for kontinuerlig integrasjon (CI/CD)
Vi har satt opp pipelines i GitLab som kjører ved hver commit. Pipelinen bruker et Docker-image og forsøker å bygge og kjøre testene vi har skrevet. I tillegg kjører piplinen SpotBugs for å oppdage feil, CheckStyle og Prettier check for å verifisere at koden ser bra og uniform ut.
Vi syntes det er problematisk å ikke ha mulighet til å kjøre ui-testene hodeløst i pipelinen. Dette kan skape en falsk trygghet ved at testene i pipelinen blir grønne selv om ui-testene ikke har blitt kjørt.

### Fornøyd: Bruk av gode commitmeldinger og Squashe commits
Vi mener selv vi har vært flinke på å skrive gode og oversiktlige commit-meldinger. Vi er fornøyde med å bruke "squash commits" funksjonen for å redusere antall commits i main, slik at commit-historikken blir ryddigere. Vi er også fornøyde med detaljnivået i commit-meldingene som gjør det enkelt for oss å se hva som har blitt gjort i tidligere merges. 

### God fokus på MMI
Vi har forøkt å tenke over hvordan appen blir brukt av andre mennesker, og designe appen slik at den all funksjonaliteten er mest mulig intuitiv og følger etablerte konvensjoner for design. Vi har i tillegg utført flere uformelle brukertester for å se om testbrukerne brukte appen slik den var tiltenkt.