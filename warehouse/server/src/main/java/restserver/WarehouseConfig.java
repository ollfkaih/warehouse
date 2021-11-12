package restserver;

import core.EntityCollectionListener;
import core.Item;
import core.ServerWarehouse;
import core.User;
import data.WarehouseFileSaver;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import restapi.WarehouseService;

import java.io.IOException;

/**
 * Configures the rest service, e.g. JSON support with Jackson and injectable
 * Warehouse and DataPersistence
 */
public class WarehouseConfig extends ResourceConfig implements EntityCollectionListener<Item> {

  private ServerWarehouse warehouse;
  private WarehouseFileSaver dataPersistence;

  public WarehouseConfig(ServerWarehouse warehouse) {
    setWarehouse(warehouse);
    dataPersistence = new WarehouseFileSaver("server-warehouse");

    // TODO: Replace WarehouseFileSaver with seperate DataPersistence objects for Items and Users and handle erros
    registerSetup();
  }

  public WarehouseConfig(WarehouseFileSaver dataPersistence) {
    this.dataPersistence = dataPersistence;
    try {
      setWarehouse(dataPersistence.getWarehouse());
    } catch (Exception e) {
      e.printStackTrace();
      setWarehouse(createDefaultWarehouse());
    }
    registerSetup();
  }

  public WarehouseConfig() {
    this("server-warehouse");
  }

  public WarehouseConfig(String fileName) {
    this(new WarehouseFileSaver(fileName));
  }

  private void registerSetup() {
    register(WarehouseService.class);
    register(WarehouseObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(WarehouseConfig.this.warehouse);
        bind(WarehouseConfig.this.dataPersistence);
      }
    });
  }

  private void saveWarehouse() {
    if (dataPersistence != null) {
      try {
        dataPersistence.saveItems(warehouse.getAllItems());
      } catch (IllegalStateException | IOException e) {
        System.err.println("Couldn't save");
      }
    }
  }

  public ServerWarehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(ServerWarehouse warehouse) {
    this.warehouse = warehouse;
    warehouse.addItemsListener(this);
  }

  private static ServerWarehouse createDefaultWarehouse() {
    ServerWarehouse warehouse = new ServerWarehouse();

    User defaultUser = new User("admin", "password", true);
    warehouse.addUser(defaultUser);

    return warehouse;
  }

  @Override
  public void entityAdded(Item item) {
    saveWarehouse();
  }

  @Override
  public void entityUpdated(Item item) {
    saveWarehouse();
  }

  @Override
  public void entityRemoved(Item item) {
    saveWarehouse();
  }
}