package restserver;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Item;
import data.WarehouseFileSaver;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restapi.WarehouseService;

import java.io.IOException;
import java.util.List;

public class WarehouseServiceTest extends JerseyTest {
  public static final String TEST_FILE_NAME = "test-server-warehouse";

  protected boolean shouldLog() {
    return true;
  }

  @Override
  protected ResourceConfig configure() {
    final WarehouseConfig config = new WarehouseConfig(TEST_FILE_NAME);
    if (shouldLog()) {
      enable(TestProperties.LOG_TRAFFIC);
      enable(TestProperties.DUMP_ENTITY);
      config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "FINEST");
    }
    return config;
  }

  private ObjectMapper objectMapper;

  @BeforeEach
  @Override
  public void setUp() throws Exception {
    super.setUp();
    objectMapper = new WarehouseObjectMapperProvider().getContext(getClass());
  }

  @AfterEach
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  private List<Item> getItems() {
    Response getResponse = target(WarehouseService.WAREHOUSE_SERVICE_PATH).path("/items")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();    

    assertEquals(200, getResponse.getStatus());
    
    try {
      return objectMapper.readValue(getResponse.readEntity(String.class), new TypeReference<List<Item>>() {
      });
    } catch (Exception e) {
      fail(e.getMessage());
    }
    return null;
  }

  @Test
  void testGetEmptyWarehouseItems() {
    List<Item> items = getItems();
    assertTrue(items.isEmpty());
  }

  private void addItem(Item item) {
    Entity<String> itemEntity;
    try {
      itemEntity = Entity.json(objectMapper.writeValueAsString(item));
    } catch (Exception e) {
      fail(e.getMessage());
      return;
    }
    Response postResponse = target(WarehouseService.WAREHOUSE_SERVICE_PATH).path("item").path(item.getId())
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(itemEntity);

    assertEquals(201, postResponse.getStatus());
  }


  @Test
  void testAddItem() {
    Item item = new Item("TestItem", 0);

    addItem(item);
    
    List<Item> items = getItems();
    assertFalse(items.isEmpty());
    System.out.println(item.getCreationDate());
    System.out.println(items.get(0).getCreationDate());
    assertTrue(item.equals(items.get(0)));
  }

  @AfterEach
  public void afterEach() {
    try {
      new WarehouseFileSaver(TEST_FILE_NAME).deleteWarehouse("items");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
