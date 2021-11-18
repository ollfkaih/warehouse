package core;

import core.server.ServerInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Listens to changes in the ClientWarehouse, sends all changes to the server
 * and updates Warehouse items with data fetched from the server.
 */
public class ClientWarehouseUpdater {
  private final ServerInterface server;
  private final ClientWarehouse warehouse;

  private final Collection<LoadingListener> loadingListeners = new ArrayList<>();
  private CompletableFuture<Void> loader;

  private boolean sendUpdates = true;

  public ClientWarehouseUpdater(ClientWarehouse warehouse, ServerInterface server) {
    this.warehouse = warehouse;
    this.server = server;

    warehouse.addItemsListener(new EntityCollectionListener<>() {
      @Override
      public void entityAdded(Item item) {
        if (sendUpdates) {
          server.putItem(item, warehouse.getAuthSession()).thenAccept(unused -> loadItems());
        }
      }

      @Override
      public void entityUpdated(Item item) {
        if (sendUpdates) {
          server.putItem(item, warehouse.getAuthSession()).thenAccept(unused -> loadItems());
        }
      }

      @Override
      public void entityRemoved(Item item) {
        if (sendUpdates) {
          server.removeItem(item.getId(), warehouse.getAuthSession()).thenAccept(unused -> loadItems());
        }
      }
    });
  }

  public void loadItems() {
    if (loader != null && !loader.isDone()) {
      loader.cancel(true);
    } else {
      notifyLoading();
    }
    loader = server.getItems().thenAccept(this::handleLoadedItems);
  }

  private void handleLoadedItems(Collection<Item> loadedItems) {
    // disable sending updates to the server as these updates are fetched from the
    // server
    sendUpdates = false;

    // remove ghost items
    for (Item item : warehouse.getAllItems()) {
      if (!loadedItems.contains(item)) {
        warehouse.removeItem(item);
      }
    }
    // update/add items
    for (Item loadedItem : loadedItems) {
      try {
        warehouse.putItem(loadedItem);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    sendUpdates = true;

    notifyStoppedLoading();
  }

  private void notifyLoading() {
    for (LoadingListener listener : loadingListeners) {
      listener.startedLoading();
    }
  }

  private void notifyStoppedLoading() {
    for (LoadingListener listener : loadingListeners) {
      listener.stoppedLoading();
    }
  }

  public void addLoadingListener(LoadingListener listener) {
    this.loadingListeners.add(listener);
  }

  public void removeLoadingListener(LoadingListener listener) {
    this.loadingListeners.remove(listener);
  }
}
