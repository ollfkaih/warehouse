package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import core.Warehouse;

public class WarehouseFileSaver implements IDataPersistence {
    private final static String WAREHOUSE_FILE_EXTENSION = "json";
    
    @Override
    public Warehouse getWarehouse() throws IOException {
        var warehouseFilePath = getWarehouseFilePath();
        try (var is = new FileInputStream(warehouseFilePath.toFile())) {
            return readWarehouse(is);
        }
    }

    @Override
    public void saveWarehouse(Warehouse warehouse) throws IOException {
        var warehouseFilePath = getWarehouseFilePath();
        ensureWarehouseFolderExists();
        try (var os = new FileOutputStream(warehouseFilePath.toFile())) {
        	writeWarehouse(warehouse, os);
        }
    }

    private Path getWarehouseFolderPath() {
        return Path.of(System.getProperty("user.home"), "warehouse", "items");
    }

    private Path getWarehouseFilePath() {
        return getWarehouseFolderPath().resolve("warehouse." + WAREHOUSE_FILE_EXTENSION);
    }

    private boolean ensureWarehouseFolderExists() {
        try {
            Files.createDirectories(getWarehouseFolderPath());
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

	private Warehouse readWarehouse(InputStream is) throws IOException {
        return WarehouseSerializer.jsonToWarehouse(is);
	}

	private void writeWarehouse(Warehouse warehouse, OutputStream os) throws IOException {
        WarehouseSerializer.warehouseToJson(warehouse, os);
	}
}