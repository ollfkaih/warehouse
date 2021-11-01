package restapi;

import core.Item;
import core.Warehouse;
import data.DataPersistence;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * The top-level rest service for Warehouse.
 */
@Path(WarehouseService.WAREHOUSE_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class WarehouseService {
  public static final String WAREHOUSE_SERVICE_PATH = "warehouse";

  private static final Logger LOG = LoggerFactory.getLogger(WarehouseService.class);

  @Context
  private Warehouse warehouse;

  @Context
  private DataPersistence dataPersistence;

  @GET
  @Path("/items")
  public List<Item> getItems() {
    LOG.debug("getItems: " + warehouse);
    return warehouse.getAllItems();
  }

  /**
   * Returns the ItemResource for the Item with the provided id
   * (as a resource to support chaining path elements).
   *
   * @param id the id of the Item.
   */
  @Path("/item/{id}")
  public ItemResource getItem(@PathParam("id") String id) {
    Item item;
    try {
      item = warehouse.getItem(id);
    } catch (Exception e) {
      item = null;
    }
    LOG.debug("Sub-resource for Item " + id + ": " + item);
    ItemResource itemResource = new ItemResource(warehouse, id, item);
    itemResource.setDataPersistence(dataPersistence);
    return itemResource;
  }
}
