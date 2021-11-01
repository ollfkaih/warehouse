package restapi;

import core.Item;
import core.Warehouse;
import data.DataPersistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Used for all requests referring to an Item by id.
 */
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {

  private static final Logger LOG = LoggerFactory.getLogger(ItemResource.class);

  private final Warehouse warehouse;
  private final String id;
  private final Item item;

  @Context
  private DataPersistence dataPersistence;

  public void setDataPersistence(DataPersistence dataPersistence) {
    this.dataPersistence = dataPersistence;
  }
  
  public ItemResource(Warehouse warehouse, String id, Item item) {
    this.warehouse = warehouse;
    this.id = id;
    this.item = item;
  }

  private void checkLocalItem() {
    if (this.item == null) {
      throw new IllegalArgumentException("Item is null");
    }
  }

  private void checkReceivedItem(Item item) {
    if (!item.getId().equals(id)) {
      throw new IllegalArgumentException("Item id is different from url id");
    }
    if (item.getCreationDate().isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("Item cannot be created in the future");
    }
  }

  @GET
  public Item getItem() {
    checkLocalItem();
    LOG.debug("getItem({})", item.getName());
    return this.item;
  }

  /**
   * Replaces or adds an Item.
   *
   * @param item the item to add
   * @return true if it was added, false if it replaced
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean putItem(Item item) {
    checkReceivedItem(item);
    LOG.debug("putItem({})", item);
    boolean result = warehouse.putItem(item);
    return result;
  }

  /**
   * Adds a new Item.
   *
   * @param item the item to add
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postItem(Item item) {
    checkReceivedItem(item);
    LOG.debug("postItem({})", item);
    if (warehouse.itemExists(id)) {
      throw new IllegalStateException("Item already exists");
    }
    warehouse.addItem(item);
    return Response.created(UriBuilder.fromPath(WarehouseService.WAREHOUSE_SERVICE_PATH).path("item").path(id).build()).build();
  }

  @DELETE
  public boolean deleteItem() {
    checkLocalItem();
    warehouse.removeItem(id);
    return true;
  }
}
