package server;

import core.Item;
import core.ServerWarehouse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * WarehouseController contains the main REST-API methods.
 */
@RestController
@RequestMapping(WarehouseController.WAREHOUSE_SERVICE_PATH)
public class WarehouseController {
  public static final String WAREHOUSE_SERVICE_PATH = "springboot/warehouse";

  private final WarehouseService warehouseService;

  public WarehouseController(WarehouseService warehouseService) {
    this.warehouseService = warehouseService;
  }

  private ServerWarehouse getWarehouse() {
    return warehouseService.getWarehouse();
  }

  private void saveWarehouse() {
    warehouseService.saveItems();
    warehouseService.saveUsers();
  }

  /**
   * Gets all items.
   *
   * @return all items
   */
  @GetMapping(path = "")
  public Collection<Item> getItems() {
    return getWarehouse().getAllItems();
  }

  /**
   * Get an item.
   *
   * @param id items id
   * @return the item
   */
  @GetMapping(path = "/{id}")
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
  @PutMapping(path = "/{id}")
  public boolean putTodoList(@PathVariable("id") String id,
                             @RequestBody Item item) {
    boolean added = getWarehouse().putItem(item);
    saveWarehouse();
    return added;
  }

  /**
   * Removes the Item.
   *
   * @param id the id of the item.
   * @return the deleted item.
   */
  @DeleteMapping(path = "/{id}")
  public Item removeTodoList(@PathVariable("id") String id) {
    Item deleted = getWarehouse().removeItem(id);
    saveWarehouse();
    return deleted;
  }
}

