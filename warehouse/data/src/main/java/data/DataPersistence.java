package data;

import core.Warehouse;

import java.io.IOException;

/**
 * Interface for persisting warehouse data.
 */
public interface DataPersistence {
  /**
   * Get a the saved warehouse.
   *
   * @return The Warehouse
   * @throws IOException if the saved warehouse cannot be read from file
   */
  Warehouse getWarehouse() throws IOException;

  /**
   * Save the warehouse.
   *
   * @param warehouse Warehouse to be saved
   * @throws IOException If the warehouse can't be written to file
   */
  void saveItems(Warehouse warehouse) throws IOException;
  
  void saveUsers(Warehouse warehouse) throws IOException;
}