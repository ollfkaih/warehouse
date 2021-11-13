package server;

import core.Entity;
import core.EntityCollection;
import core.Item;
import core.ServerWarehouse;
import core.User;
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

  private static <T extends Entity<T>> EntityCollection<T> getEntityCollection(DataPersistence<T> dataPersistence) {
    try {
      EntityCollection<T> itemEntityCollection = new EntityCollection<>();
      itemEntityCollection.addAll(dataPersistence.loadAll());
      return itemEntityCollection;
    } catch (Exception e) {
      System.out.println("LOADING SAVED ITEMS FAILED");
      return new EntityCollection<>();
    }
  }

  public WarehouseService(DataPersistence<Item> itemPersistence, DataPersistence<User> userPersistence) {
    warehouse = new ServerWarehouse(getEntityCollection(itemPersistence), getEntityCollection(userPersistence));

    EntityCollectionAutoPersistence<Item> autoItemPersistence = new EntityCollectionAutoPersistence<>(itemPersistence);
    warehouse.addItemsListener(autoItemPersistence);

    EntityCollectionAutoPersistence<User> autoUserPersistence = new EntityCollectionAutoPersistence<>(userPersistence);
    warehouse.addUserListener(autoUserPersistence);
  }

  public ServerWarehouse getWarehouse() {
    return warehouse;
  }
}
