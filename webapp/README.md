# Warehouse-nettklient

En enkel nettside som lar deg koble til et varelager og se, legge til, slette og endre varer på lager.

Skjermbilder av appen finnes i de ulike undermappene i [docs](/docs), og de nyeste i [release3/readme](/docs/release3/README.md)

## Krav før du kan kjøre webappen

Du trenger [`yarn`](https://yarnpkg.com/) (evt. npm) for å kjøre nettsiden.  
I tillegg må du [kjøre nettjeneren](/warehouse/README.md#kjøre-java-tjeneren) vår i Java for å ha noen tjener å koble til.

## Kjøre webappen

1. Bytt mappe til `webapp`
2. Kjør `yarn`
3. Kjør så `yarn start`

Altså `cd webapp; yarn; yarn start`

## Integrasjonstester i Cypress

For å kunne kjøre integrasjonstestene til nettsiden, må først både [nettjeneren](/warehouse/README.md#kjøre-java-tjeneren) og [webappen](/webapp/README.md#kjøre-webappen) kjøre.  
Testene forutsetter at det ikke finnes noen varer på lageret fra før.  
I en tredje terminal kan du så skrive `npx cypress run` for å bare kjøre testene, eller `npx cypress open` for å åpne et gui hvor du kan kjøre og følge med på det som skjer i testene.

#### Eksterne kilder

Bildet på forsiden av Webappen er hentet fra <a href="https://storyset.com/hobby">Storyset</a>
