package core;

/**
 * Interface for listening to changes to a warehouse.
 */
public interface WarehouseListener {
  /**
   * Notifies that an Item has been added to the warehouse.
   */
  public void itemAddedToWarehouse(Item item);

  /**
   * Notifies that there has been a change to one or more items in the warehouse.
   */
  public void warehouseItemsUpdated();

  /**
   * Notifies that an item in the Warehouse has been deleted from the Warehouse.
   */
  public void itemRemovedFromWarehouse(Item item);
}
