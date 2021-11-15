package server;

import core.Item;
import core.ServerWarehouse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Contains the main REST-API methods.
 */
@RestController
@RequestMapping(WarehouseServerController.WAREHOUSE_SERVICE_PATH)
public class WarehouseServerController {
  public static final String WAREHOUSE_SERVICE_PATH = "warehouse";

  private final WarehouseService warehouseService;

  public WarehouseServerController(WarehouseService warehouseService) {
    this.warehouseService = warehouseService;
  }

  private ServerWarehouse getWarehouse() {
    return warehouseService.getWarehouse();
  }

  /**
   * Gets all items.
   *
   * @return all items
   */
  @GetMapping(path = "items")
  public Collection<Item> getItems() {
    return getWarehouse().getAllItems();
  }

  /**
   * Get an item.
   *
   * @param id items id
   * @return the item
   */
  @GetMapping(path = "item/{id}")
  public Item getItem(@PathVariable("id") String id) {
    return getWarehouse().getItem(id);
  }

  /**
   * Replaces or adds an Item.
   *
   * @param id the id of the Item
   * @param item the item to add
   * @return true if it was added, false if it replaced
   */
  @PutMapping(path = "item/{id}")
  public boolean putItem(@PathVariable("id") String id, @RequestBody Item item) {
    return getWarehouse().putItem(item);
  }

  /**
   * Removes the Item.
   *
   * @param id the id of the item.
   * @return the deleted item.
   */
  @DeleteMapping(path = "item/{id}")
  public Item removeItem(@PathVariable("id") String id) {
    return getWarehouse().removeItem(id);
  }
}

