package server;

import core.Item;
import core.User;
import core.server.AuthSession;
import core.server.LoginRequest;
import core.server.ServerWarehouse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

/**
 * Contains the main REST-API methods.
 */
@CrossOrigin
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

  private void verifyAuthentication(String token) {
    if (!getWarehouse().isValidAuthToken(token)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Auth token is invalid");
    }
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
  public boolean putItem(@PathVariable("id") String id, @RequestBody Item item, @RequestHeader("auth-token") String token) {
    verifyAuthentication(token);
    return getWarehouse().putItem(item);
  }

  /**
   * Removes the Item.
   *
   * @param id the id of the item.
   * @return the deleted item.
   */
  @DeleteMapping(path = "item/{id}")
  public Item removeItem(@PathVariable("id") String id, @RequestHeader("auth-token") String token) {
    verifyAuthentication(token);
    return getWarehouse().removeItem(id);
  }

  /**
   * Registers a new user.
   *
   * @param user the user to add
   */
  @PostMapping(path = "user/register")
  public void registerUser(@RequestBody User user) {
    try {
      getWarehouse().addUser(user);
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Logs in a user.
   *
   * @param loginRequest request containing the username and password
   * @return AuthSession with token and the signed-in user
   */
  @PostMapping(path = "user/login")
  public AuthSession loginUser(@RequestBody LoginRequest loginRequest) {
    try {
      return getWarehouse().login(loginRequest.getUsername(), loginRequest.getPassword());
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
