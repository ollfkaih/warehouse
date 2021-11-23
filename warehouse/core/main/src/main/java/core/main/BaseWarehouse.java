package core.main;

import java.util.Collection;

/**
 * Base class for warehouses. Contains methods and data that is used both by server and client.
 */
public abstract class BaseWarehouse {
  protected EntityCollection<Item> itemCollection;

  public BaseWarehouse() {
    this(new EntityCollection<>());
  }

  public BaseWarehouse(EntityCollection<Item> itemCollection) {
    this.itemCollection = itemCollection;
  }

  public Collection<Item> getAllItems() {
    return itemCollection.getAll();
  }

  public void addItem(Item item) {
    itemCollection.add(item);
  }

  /**
   * Adds the item, replaces it if it already exists.
   *
   * @return true if it was added, false if it replaced
   */
  public boolean putItem(Item item) {
    return itemCollection.put(item);
  }

  public Item removeItem(String id) {
    return itemCollection.remove(id);
  }

  public Item getItem(String id) {
    return itemCollection.get(id);
  }

  public boolean containsItem(String id) {
    return itemCollection.contains(id);
  }

  public void addItemsListener(EntityCollectionListener<Item> listener) {
    itemCollection.addListener(listener);
  }

  public void removeItemsListener(EntityCollectionListener<Item> listener) {
    itemCollection.removeListener(listener);
  }
}
