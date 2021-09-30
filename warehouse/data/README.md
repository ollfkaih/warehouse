# Warehouse data
Denne modulen utfører lagring og innlasting av data fra disk.

Modulen har et interface, `IDataPersistence` med en metode for lagring av et varehus, og en metode for innlasting. Dette interfacet blir implementert av `WarehouseFileSaver`
som utfører lagring og innlasting av data i en json fil som blir lagret til disk.
