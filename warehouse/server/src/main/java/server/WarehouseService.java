package server;

import com.fasterxml.jackson.core.type.TypeReference;
import core.Item;
import core.ServerWarehouse;
import core.User;
import data.DataPersistence;
import data.FileSaver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

/**
 * Configures the warehouse service,
 * including autowired objects.
 */
@Service
public class WarehouseService {
  private ServerWarehouse warehouse;
  private final DataPersistence<Collection<Item>> itemDataPersistence;
  private final DataPersistence<Collection<User>> userDataPersistence;

  public WarehouseService(ServerWarehouse warehouse) {
    this.warehouse = warehouse;
    this.itemDataPersistence = new FileSaver<>(new TypeReference<>() {}, "items");
    this.userDataPersistence = new FileSaver<>(new TypeReference<>() {}, "users");
  }

  public WarehouseService() {
    this(new ServerWarehouse());
  }

  public ServerWarehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(ServerWarehouse warehouse) {
    this.warehouse = warehouse;
  }

  public void saveItems() {
    if (warehouse != null) {
      try {
        itemDataPersistence.save(warehouse.getAllItems(), "warehouse-server");
      } catch (IllegalStateException | IOException e) {
        System.err.println("Couldn't save Items: " + e);
      }
    }
  }

  public void saveUsers() {
    if (warehouse != null) {
      try {
        userDataPersistence.save(warehouse.getUsers(), "warehouse-server");
      } catch (IllegalStateException | IOException e) {
        System.err.println("Couldn't save Items: " + e);
      }
    }
  }
}
