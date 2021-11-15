package core;

import core.server.ServerInterface;

/**
 * Listens to changes in the ClientWarehouse, sends all changes to the server and updates Warehouse items with data fetched from the server.
 */
public class ClientWarehouseUpdater {
  private final ServerInterface server;
  private final ClientWarehouse warehouse;

  private boolean sendUpdates = true;

  public ClientWarehouseUpdater(ClientWarehouse warehouse, ServerInterface server) {
    this.warehouse = warehouse;
    this.server = server;

    warehouse.addItemsListener(new EntityCollectionListener<>() {
      @Override
      public void entityAdded(Item item) {
        if (sendUpdates) {
          server.addItem(item).thenAccept(unused -> loadItems());
        }
      }

      @Override
      public void entityUpdated(Item item) {
        if (sendUpdates) {
          server.putItem(item).thenAccept(unused -> loadItems());
        }
      }

      @Override
      public void entityRemoved(Item item) {
        if (sendUpdates) {
          server.removeItem(item.getId()).thenAccept(unused -> loadItems());
        }
      }
    });
  }

  public void loadItems() {
    server
        .getItems()
        .thenAccept(loadedItems -> {
          // disable sending updates to the server as these updates are fetched from the server
          sendUpdates = false;

          // remove ghost items
          for (Item item : warehouse.getAllItems()) {
            if (!loadedItems.contains(item)) {
              warehouse.removeItem(item);
            }
          }
          // update/add items
          for (Item loadedItem : loadedItems) {
            warehouse.putItem(loadedItem);
          }
          sendUpdates = true;
        });
  }
}
