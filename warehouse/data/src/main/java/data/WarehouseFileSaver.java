package data;

import core.Warehouse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementation of DataPersistence that saves warehouse to json file on disk.
 */
public class WarehouseFileSaver implements DataPersistence {
  private static final String WAREHOUSE_FILE_EXTENSION = "json";
  private final String warehouseFileName;

  public WarehouseFileSaver(String filename) {
    this.warehouseFileName = filename;
  }

  @Override
  public Warehouse getWarehouse() throws IOException {
    Path itemsFilePath = getWarehouseFilePath("items");
    Path usersFilePath = getWarehouseFilePath("users");
    File itemsFile = itemsFilePath.toFile();
    File usersFile = usersFilePath.toFile();
    if (itemsFile.isFile() && usersFile.isFile()) {
      try (var items = new FileInputStream(itemsFile); var users = new FileInputStream(usersFile)) {
        return readWarehouse(items, users);
      }
    } else {
      return null;
    }
  }

  @Override
  public void saveItems(Warehouse warehouse) throws IOException {
    var warehouseFilePath = getWarehouseFilePath("items");
    ensureWarehouseFolderExists("items");
    try (var os = new FileOutputStream(warehouseFilePath.toFile())) {
      writeItems(warehouse, os);
    }
  }

  @Override
  public void saveUsers(Warehouse warehouse) throws IOException {
    var userFilePath = getWarehouseFilePath("users");
    ensureWarehouseFolderExists("users");
    try (var os = new FileOutputStream(userFilePath.toFile())) {
      writeUsers(warehouse, os);
    }
  }
  

  public void deleteWarehouse(String folder) throws IOException {
    Files.delete(getWarehouseFilePath(folder));
  }

  private Path getWarehouseFolderPath(String folder) {
    return Path.of(System.getProperty("user.home"), "warehouse", folder);
  }

  private Path getWarehouseFilePath(String folder) {
    return getWarehouseFolderPath(folder).resolve(warehouseFileName + "." + WAREHOUSE_FILE_EXTENSION);
  }

  private void ensureWarehouseFolderExists(String folder) throws IOException {
    Files.createDirectories(getWarehouseFolderPath(folder));
  }

  private Warehouse readWarehouse(InputStream items, InputStream users) throws IOException {
    return WarehouseSerializer.jsonToWarehouse(items, users);
  }

  private void writeItems(Warehouse warehouse, OutputStream os) throws IOException {
    WarehouseSerializer.itemsToJson(warehouse, os);
  }
  
  private void writeUsers(Warehouse warehouse, OutputStream os) throws IOException {
    WarehouseSerializer.usersToJson(warehouse, os);
  }

}