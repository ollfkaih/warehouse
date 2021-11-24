package springboot.server;

import core.main.Entity;
import core.main.EntityCollection;
import core.main.Item;
import core.main.User;
import core.server.ServerWarehouse;
import data.DataPersistence;
import data.EntityCollectionAutoPersistence;
import org.springframework.stereotype.Service;

/**
 * Configures the warehouse service,
 * including autowired objects.
 */
@Service
public class WarehouseService {
  private final ServerWarehouse warehouse;

  public WarehouseService(DataPersistence<Item> itemPersistence, DataPersistence<User> userPersistence) {
    warehouse = new ServerWarehouse(getEntityCollection(itemPersistence), getEntityCollection(userPersistence));

    EntityCollectionAutoPersistence<Item> autoItemPersistence = new EntityCollectionAutoPersistence<>(itemPersistence);
    warehouse.addItemsListener(autoItemPersistence);

    EntityCollectionAutoPersistence<User> autoUserPersistence = new EntityCollectionAutoPersistence<>(userPersistence);
    warehouse.addUserListener(autoUserPersistence);
  }

  private static <T extends Entity<T>> EntityCollection<T> getEntityCollection(DataPersistence<T> dataPersistence) {
    try {
      EntityCollection<T> itemEntityCollection = new EntityCollection<>();
      itemEntityCollection.addAll(dataPersistence.loadAll());
      return itemEntityCollection;
    } catch (Exception e) {
      System.err.println("ERROR: Loading saved items failed");
      return new EntityCollection<>();
    }
  }

  public ServerWarehouse getWarehouse() {
    return warehouse;
  }
}
