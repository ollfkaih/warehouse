package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import core.Warehouse;

public class WarehouseFileSaver implements IDataPersistence {
    private static final String WAREHOUSE_FILE_EXTENSION = "json";
    private final String WAREHOUSE_FILE_NAME;
    
    public WarehouseFileSaver(String filename) {
        this.WAREHOUSE_FILE_NAME = filename;
    }

    @Override
    public Warehouse getWarehouse() throws IOException {
        Path warehouseFilePath = getWarehouseFilePath();
        File warehouseFile = warehouseFilePath.toFile();
        if (warehouseFile.isFile()) {
            try (var is = new FileInputStream(warehouseFile)) {
                return readWarehouse(is);
            }
        } else {
            return null;
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

    public void deleteWarehouse() throws IOException {
        Files.delete(getWarehouseFilePath());
    }

    private Path getWarehouseFolderPath() {
        return Path.of(System.getProperty("user.home"), "warehouse", "items");
    }

    private Path getWarehouseFilePath() {
        return getWarehouseFolderPath().resolve(WAREHOUSE_FILE_NAME + "." + WAREHOUSE_FILE_EXTENSION);
    }

    private void ensureWarehouseFolderExists() throws IOException {
        Files.createDirectories(getWarehouseFolderPath());
    }

	private Warehouse readWarehouse(InputStream is) throws IOException {
        return WarehouseSerializer.jsonToWarehouse(is);
	}

	private void writeWarehouse(Warehouse warehouse, OutputStream os) throws IOException {
        WarehouseSerializer.warehouseToJson(warehouse, os);
	}
}