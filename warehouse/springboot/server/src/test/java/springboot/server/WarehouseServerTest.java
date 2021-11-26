package springboot.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.main.AuthSession;
import core.main.Item;
import core.main.LoginRequest;
import core.main.User;
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

import java.io.IOException;
import java.util.Collection;

@AutoConfigureMockMvc
@ContextConfiguration(classes = { WarehouseServerController.class, WarehouseService.class, WarehouseServerTest.TestApplicationConfig.class })
@WebMvcTest
public class WarehouseServerTest {
  public static final String TEST_FILE_NAME = "test-server-warehouse";
  private static final DataPersistence<Item> itemPersistence = new FileSaver<>(new TypeReference<>() {}, TEST_FILE_NAME + "-items");
  private static final DataPersistence<User> userPersistence = new FileSaver<>(new TypeReference<>() {}, TEST_FILE_NAME + "-users");

  private AuthSession auth;

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = DataUtils.getObjectMapper();

  @TestConfiguration
  public static class TestApplicationConfig {
    private final ObjectMapper objectMapper = DataUtils.getObjectMapper();

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
    clearSavedFiles();
  }

  @AfterEach
  public void cleanup() throws Exception {
    clearSavedFiles();
  }

  private void clearSavedFiles() throws IOException {
    itemPersistence.deleteAll();
    userPersistence.deleteAll();
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
        .accept(MediaType.APPLICATION_JSON)
        .header("auth-token", auth.getToken()))
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
    mockMvc.perform(MockMvcRequestBuilders
        .put(warehouseUrl("item", item.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("auth-token", auth.getToken())
        .content(itemJson))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  }

  @Test
  void testAddAndRemoveItem() throws Exception {
    registerAndLogin();

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

  private void register(User user) throws Exception {
    String json;
    try {
      json = objectMapper.writeValueAsString(user);
    } catch (Exception e) {
      fail(e.getMessage());
      return;
    }
    mockMvc.perform(MockMvcRequestBuilders
        .post(warehouseUrl("user", "register"))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  }

  private AuthSession login(LoginRequest request) throws Exception {
    String json;
    try {
      json = objectMapper.writeValueAsString(request);
    } catch (Exception e) {
      fail(e.getMessage());
      return null;
    }
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .post(warehouseUrl("user", "login"))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
  }

  void registerAndLogin() throws Exception {
    String password = "password123";
    User user = new User("TestUser", password, false);

    register(user);

    AuthSession authSession = login(new LoginRequest(user.getUserName(), password));

    assertNotNull(authSession);
    assertNotNull(authSession.getUser());
    assertNotNull(authSession.getToken());
    assertEquals(user.getId(), authSession.getUser().getId());
    assertEquals(user.getUserName(), authSession.getUser().getUserName());

    auth = authSession;
  }
}
