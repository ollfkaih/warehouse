# Warehouse data
Denne modulen utfører lagring og innlasting av data.

### `DataPersistence`

Modulen har et grensesnitt, `DataPersistence<T>`, med metoder for lesing og lagring av data. 
Instanser av grensesnittet spesifiserer typen `T` som er typen av dataen som skal lagres eller leses. 
Det kan lagres flere instanser av datatypen, og hver lagrede instans lagres med en nøkkel som kan brukes for å hente dataen ut igjen.

`DataPersistence<T>`-grensesnittet definerer følgende metoder:
- loadAll() - henter alle lagrede elemeter
- load(String key) - henter et lagret element med den spesifiserte nøkkelen
- save(T object, String key) - lagrer et element med den spesifiserte nøkkelen
- delete(String key) - sletter det lagrede elementet med den spesifiserte nøkkelen
- deleteAll() - sletter alle lagrede elementer

### `FileSaver`
Modulen har også en implementasjon av dette grensesnittet, `FileSaver<T>`, som lagrer objektene som filer til disk.
Denne implementasjonen tar en mappe som argument når den blir konstruert, slik at en kan velge hvor i `user.home` filene skal lagres.

### `EntityCollectionAutoPersistence`
Klassen `EntityCollectionAutoPersistence` hjelper til med automatisk lagring av `Entity`-objekter som er i en `EntityCollection`. 
Her brukes lyttere til å automatisk lagre objektene hver gang det skjer en endring.

### `DataUtils`
Til slutt inneholder modulen `DataUtils` som kan brukes for å hente en jackson-`ObjectMapper` som fungerer med alle objektene som må serialiseres og deserialiseres i Warehouse.
Ettersom `ObjectMapper` er en relativt tung klasse å instansiere lager den kun én `ObjectMapper` som kan hentes av alle som trenger den. 
