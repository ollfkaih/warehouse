package data;

import java.io.IOException;
import core.Warehouse;

public interface IDataPersistence {
	/**
	 * Get a the saved warehouse
	 * @return The Warehouse
	 * @throws IOException if the saved warehouse cannot be read from file
	 */
	Warehouse getWarehouse() throws IOException;
	
	/**
	 * Save the warehouse.
	 * @param Warehouse to be saved
	 * @throws IOException If the warehouse can't be written to file
	 */
	void saveWarehouse(Warehouse warehouse) throws IOException;
}