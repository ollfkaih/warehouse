package server;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import core.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.User;
import data.DataPersistence;
import data.DataUtils;
import data.FileSaver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;

@AutoConfigureMockMvc
@ContextConfiguration(classes = { WarehouseServerController.class, WarehouseService.class, WarehouseServerTest.TestApplicationConfig.class })
@WebMvcTest
public class WarehouseServerTest {
  public static final String TEST_FILE_NAME = "test-server-warehouse";

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = DataUtils.createObjectMapper();

  @TestConfiguration
  public static class TestApplicationConfig {
    private final DataPersistence<Item> itemPersistence = new FileSaver<>(new TypeReference<>() {}, TEST_FILE_NAME + "-items");
    private final DataPersistence<User> userPersistence = new FileSaver<>(new TypeReference<>() {}, TEST_FILE_NAME + "-users");
    private final ObjectMapper objectMapper = DataUtils.createObjectMapper();

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
      return objectMapper;
    }

    @Bean
    public DataPersistence<Item> itemPersistence() {
      return itemPersistence;
    }

    @Bean
    public DataPersistence<User> userPersistence() {
      return userPersistence;
    }
  }

  @BeforeEach
  public void setup() throws Exception {
    removeAllItems();
  }

  @AfterEach
  public void cleanup() throws Exception {
    removeAllItems();
  }

  private String warehouseUrl(String... segments) {
    StringBuilder url = new StringBuilder("/" + WarehouseServerController.WAREHOUSE_SERVICE_PATH);
    for (String segment : segments) {
      url.append("/").append(segment);
    }
    return url.toString();
  }

  private Collection<Item> getItems() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .get(warehouseUrl("items"))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
  }

  @Test
  public void testGetEmptyWarehouseItems() throws Exception {
    Collection<Item> items = getItems();
    assertTrue(items.isEmpty());
  }

  private Item removeItem(Item item) throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .delete(warehouseUrl("item", item.getId()))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
  }

  private void addItem(Item item) throws Exception {
    String itemJson;
    try {
      itemJson = objectMapper.writeValueAsString(item);
    } catch (Exception e) {
      fail(e.getMessage());
      return;
    }
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .put(warehouseUrl("item", item.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(itemJson))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  }

  @Test
  void testAddAndRemoveItem() throws Exception {
    Item item = new Item("TestItem", 0);

    addItem(item);

    Collection<Item> items = getItems();
    assertFalse(items.isEmpty());
    assertEquals(items.size(), 1);
    assertTrue(items.contains(item));

    Item returnedItem = removeItem(item);

    items = getItems();
    assertTrue(items.isEmpty());
    assertEquals(item, returnedItem);
  }

  private void removeAllItems() throws Exception {
    Collection<Item> allItems = getItems();
    for (Item item : allItems) {
      removeItem(item);
    }
  }
}
