# API dokumentasjon

Warehouse APIet lar applikasjoner enkelt hente og oppdatere produkter på en Warehouse-tjener.
Både java-applikasjonen og webappen bruker APIet, dermed blir det lett å bruke dem sammen ved å koble til samme tjener.

## Innlogging

Enkelte endepunkt krever innlogging for å fungere, dette gjøres med en `auth-token`.
Denne kan hentes ved å bruke Login-endepunktet, og må sendes med i http-header `auth-token` i forespørsler som krever innlogging.

## Endepunkter

De følgende endepunktene er tilgjengelige i APIet

### Hent alle produkter `GET` `/warehouse/items`

Henter en liste over alle produktene i varehuset.

#### Returnerer

Liste over alle produktene.

```
[
    {
        "id": "c0a744b8-592f-490f-9333-5f5b3bb1f93e",
        "name": "Produktnavn",
        "amount": 5,
        "barcode": "9304285709384",
        "brand": "Produsent",
        "regularPrice": 100.0,
        "salePrice": 100.0,
        "purchasePrice": 80.0,
        "section": "A",
        "row": "1",
        "shelf": "1",
        "weight": 0.3,
        "creationDate": "2021-11-24T11:13:28.039650000",
        "height": 5.0,
        "width": 20.0,
        "length": 15.0
    },
    ...
]
```

Alle feltene i produktene kan være `null` bortsett fra `id`, `name` og `amount`.

### Hent produkt `GET` `/warehouse/item/{id}`

Henter produktet med `id`en som er oppgitt i URLen.

#### Returnerer

```
{
    "id": "c0a744b8-592f-490f-9333-5f5b3bb1f93e",
    "name": "Produktnavn",
    "amount": 5,
    "barcode": "9304285709384",
    "brand": "Produsent",
    "regularPrice": 100.0,
    "salePrice": 100.0,
    "purchasePrice": 80.0,
    "section": "A",
    "row": "1",
    "shelf": "1",
    "weight": 0.3,
    "creationDate": "2021-11-24T11:13:28.039650000",
    "height": 5.0,
    "width": 20.0,
    "length": 15.0
}
```

Alle feltene i produktet kan være `null` bortsett fra `id`, `name` og `amount`.

### Oppdater/opprett produkt `PUT` `/warehouse/item/{id}` `Krever innlogging`

Oppdaterer produktet med `id`en som er oppgitt i URLen. Dersom produktet ikke allerede finnes blir det lagt til.

#### Forespørsel-`body`

```
{
    "id": "c0a744b8-592f-490f-9333-5f5b3bb1f93e",
    "name": "Produktnavn",
    "amount": 5,
    "barcode": "9304285709384",
    "brand": "Produsent",
    "regularPrice": 100.0,
    "salePrice": 100.0,
    "purchasePrice": 80.0,
    "section": "A",
    "row": "1",
    "shelf": "1",
    "weight": 0.3,
    "creationDate": "2021-11-24T11:13:28.039650000",
    "height": 5.0,
    "width": 20.0,
    "length": 15.0
}
```

Alle feltene i produktet kan være `null` bortsett fra `id`, `name` og `amount`.

#### Returnerer

Dersom produktet ble lagt til:

```
true
```

Dersom produktet ble oppdatert:

```
false
```

### Slett produkt `DELETE` `/warehouse/item/{id}` `Krever innlogging`

Sletter produktet med `id`en oppgitt i URLen.

#### Returnerer

Det slettede produktet

```
{
    "id": "c0a744b8-592f-490f-9333-5f5b3bb1f93e",
    "name": "Produktnavn",
    "amount": 5,
    "barcode": "9304285709384",
    "brand": "Produsent",
    "regularPrice": 100.0,
    "salePrice": 100.0,
    "purchasePrice": 80.0,
    "section": "A",
    "row": "1",
    "shelf": "1",
    "weight": 0.3,
    "creationDate": "2021-11-24T11:13:28.039650000",
    "height": 5.0,
    "width": 20.0,
    "length": 15.0
}
```

Alle feltene i produktet kan være `null` bortsett fra `id`, `name` og `amount`.

### Login `POST` `/warehouse/user/login`

#### Forespørsel-`body`

```
{
    "username": "Navn Navnesen",
    "password": "password123"
}
```

#### Returnerer

Den innloggede brukeren og en autentiseringstoken.

```
{
    "user": {
        "id": "03e687c9-1a79-4746-aaa4-36a33af4a4d7",
        "username": "Navn Navnesen",
        "password": "c4ca4238a0b923820dcc509a6f75849b"
    },
    "token": "376735c2-69b3-4b00-8abe-87635985f60a"
}
```

`token` kan brukes til autentisering av forespørsler som krever innlogging ved å sette http-header `auth-token` til verdien av feltet .

### Registrering `POST` `/warehouse/user/register`

#### Forespørsel-`body`

Brukeren som skal legges til

```
{
    "id": "03e687c9-1a79-4746-aaa4-36a33af4a4d7",
    "username": "Navn Navnesen",
    "password": "passord123"
}
```
