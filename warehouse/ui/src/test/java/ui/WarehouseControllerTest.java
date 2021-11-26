package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import core.main.Item;
import core.main.User;
import data.FileSaver;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import localserver.LocalServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotInterface;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
class WarehouseControllerTest {

  private static final String DETAILS_INPUT_NAME = "#nameInput";
  private static final String DETAILS_INPUT_BARCODE = "#barcodeInput";
  private static final String DETAILS_INPUT_WEIGHT = "#weightInput";
  private static final String DETAILS_INPUT_DIMENSIONS_HEIGHT = "#dimensionsHeightInput";
  private static final String DETAILS_INPUT_DIMENSIONS_WIDTH = "#dimensionsWidthInput";
  private static final String DETAILS_INPUT_DIMENSIONS_LENGTH = "#dimensionsLengthInput";
  private static final String DETAILS_INPUT_PLACEMENT_SHELF = "#placementShelfInput";
  private static final String DETAILS_INPUT_PLACEMENT_ROW = "#placementRowInput";
  private static final String DETAILS_INPUT_PLACEMENT_SECTION = "#placementSectionInput";
  private static final String DETAILS_INPUT_RETAILER_PRICE = "#retailerPriceInput";
  private static final String DETAILS_INPUT_SALES_PRICE = "#salesPriceInput";
  private static final String DETAILS_INPUT_ORDINARY_PRICE = "#ordinaryPriceInput";
  private static final String DETAILS_INPUT_AMOUNT = "#amountInput";
  private static final String DETAILS_INPUT_BRAND = "#brandInput";
  private static final String DETAILS_BUTTON_INCREMENT = "#incrementButton";
  private static final String DETAILS_BUTTON_DECREMENT = "#decrementButton";
  private static final String DETAILS_BUTTON_EDIT = "#editButton";
  private static final String DETAILS_BUTTON_SAVE = "#saveButton";
  private static final String DETAILS_BUTTON_DELETE = "#deleteButton";

  private static final String WAREHOUSE_BUTTON_INCREMENT = "#incrementButton";
  private static final String WAREHOUSE_BUTTON_DECREMENT = "#decrementButton";
  private static final String WAREHOUSE_INPUT_SEARCH = "#searchInput";
  private static final String WAREHOUSE_SELECTOR_SORT_BY = "#sortBySelector";
  private static final String WAREHOUSE_BUTTON_REVERSE_ORDER = "#reverseOrderButton";
  private static final String WAREHOUSE_BUTTON_ADD_ITEM = "#addItemButton";
  private static final String WAREHOUSE_BUTTON_LOGIN_LOGOUT = "#openLoginViewOrLogoutButton";

  private static final String WAREHOUSE_ITEM_LIST = "#itemList";
  private static final String WAREHOUSE_DETAILS_VIEW = "#detailsViewScrollPane";

  private static final String LOGIN_INPUT_USERNAME = "#usernameField";
  private static final String LOGIN_INPUT_PASSWORD = "#passwordField";
  private static final String LOGIN_BUTTON_LOGIN = "#loginUserButton";
  private static final String LOGIN_BUTTON_REGISTER = "#openRegisterViewButton";

  private static final String REGISTER_INPUT_USERNAME = "#usernameField";
  private static final String REGISTER_INPUT_PASSWORD = "#passwordField1";
  private static final String REGISTER_INPUT_REPEAT_PASSWORD = "#passwordField2";
  private static final String REGISTER_BUTTON_REGISTER = "#registerButton";

  private static final String DELETE_CONFIRMATION_DELETE_TEXT = "Slett";

  private String testUsername;
  private WarehouseController warehouseController;
  private Button addItemButtonCopy;

  @Start
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Warehouse.fxml"));
    Parent root = loader.load();
    warehouseController = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();

    addItemButtonCopy = new Button(WAREHOUSE_BUTTON_ADD_ITEM);
  }

  private String getRandomProductName() {
    return "TEST" + ((int) (Math.random() * (900000000)) + 100000000);
  }

  private Item getItemFromWarehouse(String name) {
    List<Item> itemList = warehouseController.getItems();
    Item testItem = null;
    for (Item item : itemList) {
      if (item.getName().equals(name)) {
        testItem = item;
      }
    }
    return testItem;
  }

  private DetailsViewController getDetailsViewController(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null");
    }
    return warehouseController.getDetailViewControllers().get(item);
  }

  private void removeAllItems() {
    // Creating a new runner to avoid updating GUI while Stage is not running
    Platform.runLater(() -> {
      List<Item> allItems = warehouseController.getItems();
      for (Item item : allItems) {
        warehouseController.removeItem(item.getId());
      }
    });
  } 
        
  /**
   * Ensures that a Node in a ScrollPane is visible.
   *
   * @author Marcos Supridatta
   * @author Joel Stevick
   * @see ExternalURL https://stackoverflow.com/questions/12837592/how-to-scroll-to-make-a-node-within-the-content-of-a-scrollpane-visible
   * @param scrollPane containing the node
   * @param node to make visible
   */
  private static void ensureVisible(ScrollPane scrollPane, Node node) {
    Bounds viewport = scrollPane.getViewportBounds();
    double contentHeight = scrollPane.getContent().localToScene(scrollPane.getContent().getBoundsInLocal()).getHeight();
    double nodeMinY = node.localToScene(node.getBoundsInLocal()).getMinY();
    double nodeMaxY = node.localToScene(node.getBoundsInLocal()).getMaxY();

    double verticalValueDelta = 0;

    if (nodeMaxY < 0) {
      verticalValueDelta = (nodeMinY - viewport.getHeight()) / contentHeight;
    } else if (nodeMinY > viewport.getHeight()) {
      verticalValueDelta = (nodeMinY + viewport.getHeight()) / contentHeight;
    }
    scrollPane.setVvalue(scrollPane.getVvalue() + verticalValueDelta);
  }

  private Node findNode(FxRobot robot, String fxid) {
    return robot.lookup(fxid).query();
  }

  private FxRobotInterface ensureVisibleClickOn(ScrollPane scrollPane, FxRobot robot, String fxid) {
    ensureVisible(scrollPane, findNode(robot, fxid));
    return robot.clickOn(fxid);
  }

  private void createNewItem(FxRobot robot, final String testProductName) {
    robot.clickOn(WAREHOUSE_BUTTON_ADD_ITEM);
    List<DetailsViewController> detailsViewControllers = new ArrayList<>(warehouseController
        .getDetailViewControllers()
        .values());

    ScrollPane detailsViewScrollPane = detailsViewControllers.get(detailsViewControllers.size() - 1).getScrollPane();
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_INPUT_NAME)
        .write(testProductName);
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_SAVE);
    Stage stage = ((Stage) ((robot.lookup(WAREHOUSE_DETAILS_VIEW).query())).getScene().getWindow());
    robot.interact(stage::close);
  }

  private void login(FxRobot robot) {
    robot.clickOn(WAREHOUSE_BUTTON_LOGIN_LOGOUT);
    if (testUsername == null) {
      register(robot);
    }
    robot.clickOn(LOGIN_INPUT_USERNAME).write(testUsername);
    robot.clickOn(LOGIN_INPUT_PASSWORD).write("passord");
    robot.clickOn(LOGIN_BUTTON_LOGIN);
  }

  private void register(FxRobot robot) {
    testUsername = getRandomProductName();
    robot.clickOn(WAREHOUSE_BUTTON_LOGIN_LOGOUT);
    robot.clickOn(LOGIN_BUTTON_REGISTER);
    robot.clickOn(REGISTER_INPUT_USERNAME).write(testUsername);
    robot.clickOn(REGISTER_INPUT_PASSWORD).write("passord");
    robot.clickOn(REGISTER_INPUT_REPEAT_PASSWORD).write("passord");
    robot.clickOn(REGISTER_BUTTON_REGISTER);
  }

  /**
   * Verifies that the detailsView display the Items actual values.
   *
   * @param item corresponding to the detailsView to test 
   */
  private void verifyDetailView(Item item) {
    FxAssert.verifyThat(DETAILS_INPUT_BRAND, TextInputControlMatchers.hasText(item.getBrand()));
    FxAssert.verifyThat(DETAILS_INPUT_AMOUNT, TextInputControlMatchers.hasText(String.valueOf(item.getAmount())));
    FxAssert.verifyThat(DETAILS_INPUT_ORDINARY_PRICE, TextInputControlMatchers.hasText(String.valueOf(item.getRegularPrice())));
    FxAssert.verifyThat(DETAILS_INPUT_SALES_PRICE, TextInputControlMatchers.hasText(String.valueOf(item.getSalePrice())));
    FxAssert.verifyThat(DETAILS_INPUT_RETAILER_PRICE, TextInputControlMatchers.hasText(String.valueOf(item.getPurchasePrice())));
    FxAssert.verifyThat(DETAILS_INPUT_PLACEMENT_SECTION, TextInputControlMatchers.hasText(item.getSection()));
    FxAssert.verifyThat(DETAILS_INPUT_PLACEMENT_ROW, TextInputControlMatchers.hasText(item.getRow()));
    FxAssert.verifyThat(DETAILS_INPUT_PLACEMENT_SHELF, TextInputControlMatchers.hasText(item.getShelf()));
    FxAssert.verifyThat(DETAILS_INPUT_DIMENSIONS_LENGTH, TextInputControlMatchers.hasText(String.valueOf(item.getLength())));
    FxAssert.verifyThat(DETAILS_INPUT_DIMENSIONS_WIDTH, TextInputControlMatchers.hasText(String.valueOf(item.getWidth())));
    FxAssert.verifyThat(DETAILS_INPUT_DIMENSIONS_HEIGHT, TextInputControlMatchers.hasText(String.valueOf(item.getHeight())));
    FxAssert.verifyThat(DETAILS_INPUT_WEIGHT, TextInputControlMatchers.hasText(String.valueOf(item.getWeight())));
    FxAssert.verifyThat(DETAILS_INPUT_BARCODE, TextInputControlMatchers.hasText(item.getBarcode()));
  }

  private List<Node> findItemNodes(FxRobot robot) {
    return robot.lookup(WAREHOUSE_ITEM_LIST).queryAs(VBox.class).getChildren();
  }

  private void selectOptionInComboBox(FxRobot robot, String comboBoxQuery, String option) {
    ComboBox<String> comboBox = robot.lookup(comboBoxQuery).queryAs(ComboBox.class);

    robot.interact(() -> {
      comboBox.getSelectionModel().select(option);
    });
  }

  private void verifyItemsInOrder(FxRobot robot, String... itemNames) {
    List<Node> items = findItemNodes(robot);
    for (int i = 0; i < itemNames.length; i++) {
      FxAssert.verifyThat(items.get(i), NodeMatchers.hasChild(itemNames[i]));
    }
  }

  @BeforeEach
  void setup() throws IOException {
    FileSaver<Item> itemFileSaver = new FileSaver<>(new TypeReference<>() {}, "ui_test" + "-items");
    FileSaver<User> userFileSaver = new FileSaver<>(new TypeReference<>() {}, "ui_test" + "-users");
    itemFileSaver.deleteAll();
    userFileSaver.deleteAll();

    LocalServer server = new LocalServer(itemFileSaver, userFileSaver);
    Platform.runLater(() -> warehouseController.loadPersistedData(server));
  }

  @Test
  @DisplayName("Test controller")
  void testController() {
    assertNotNull(warehouseController);
  }

  @Test
  @DisplayName("Add-item button exists")
  void testAddButtonExist() {
    FxAssert.verifyThat(addItemButtonCopy, LabeledMatchers.hasText(WAREHOUSE_BUTTON_ADD_ITEM));
  }

  @Test
  @DisplayName("Test add and remove item")
  void testAddAndDelete(FxRobot robot) {
    login(robot);

    final String testProductName = getRandomProductName();
    createNewItem(robot, testProductName);
    assertNotNull(getItemFromWarehouse(testProductName), "unable to create item");
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChild(testProductName));
    
    robot.clickOn(testProductName);
    ScrollPane detailsViewScrollPane = getDetailsViewController(getItemFromWarehouse(testProductName)).getScrollPane();
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_EDIT);
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_DELETE);
    
    robot.clickOn(DELETE_CONFIRMATION_DELETE_TEXT);
    assertNull(getItemFromWarehouse(testProductName), "unable to delete item");
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChildren(0, testProductName));
    
    removeAllItems();
  }

  @Test
  @DisplayName("Test add item to warehouse and alter its properties")
  void testAddItem(FxRobot robot) {
    login(robot);

    final String testProductName = getRandomProductName(); 
    createNewItem(robot, testProductName);

    Item testItem = getItemFromWarehouse(testProductName);
    assertNotNull(testItem, "unable to locate the created Item");
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChild(testProductName));

    robot.push(KeyCode.BACK_SPACE);
    robot.clickOn(testProductName);
    robot.clickOn(DETAILS_BUTTON_EDIT);
    robot.push(KeyCode.BACK_SPACE);
    DetailsViewController testProductViewController = getDetailsViewController(testItem);
    ScrollPane testProductViewScrollPane = testProductViewController.getScrollPane();
    assertNotNull(testProductViewController, "unable to find the detailsViewController of the opened item");

    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_BRAND).write("TestBrand");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_AMOUNT).push(KeyCode.BACK_SPACE).write("50");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_ORDINARY_PRICE).write("4000");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_SALES_PRICE).write("3000");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_RETAILER_PRICE).write("2000");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_PLACEMENT_SECTION).write("B");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_PLACEMENT_ROW).write("19");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_PLACEMENT_SHELF).write("3");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_DIMENSIONS_LENGTH).write("10");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_DIMENSIONS_WIDTH).write("20");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_DIMENSIONS_HEIGHT).write("4");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_WEIGHT).write("5.0");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_INPUT_BARCODE).write("6830473201734");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_BUTTON_SAVE);

    verifyDetailView(testItem);
    robot.interact(() -> ((Stage) ((robot.lookup(WAREHOUSE_DETAILS_VIEW).query())).getScene().getWindow()).close());

    robot.clickOn(testProductName);
    verifyDetailView(testItem);
    
    robot.clickOn(DETAILS_BUTTON_EDIT);
    verifyDetailView(testItem);

    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_BUTTON_DELETE);
    robot.clickOn(DELETE_CONFIRMATION_DELETE_TEXT);

    removeAllItems();
  }

  @Test
  @DisplayName("Test incrementButtons on frontpage")
  void testIncrementValues(FxRobot robot) {
    login(robot);
    final String testProductName = getRandomProductName();
    createNewItem(robot, testProductName);
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChild(testProductName));
    
    Item testItem = getItemFromWarehouse(testProductName);
    assertNotNull(testItem);
    assertEquals(0, testItem.getAmount());
    
    robot.moveTo(testProductName);
    robot.clickOn(WAREHOUSE_BUTTON_INCREMENT);
    assertEquals(1, testItem.getAmount());
    
    robot.moveTo(testProductName);
    robot.clickOn(WAREHOUSE_BUTTON_DECREMENT);
    assertEquals(0, testItem.getAmount());

    removeAllItems();
  }

  @Test
  @DisplayName("Test incrementButtons on detailsView")
  void testDetailViewIncrement(FxRobot robot) {
    login(robot);
    final String testProductName = getRandomProductName();
    createNewItem(robot, testProductName);
    robot.clickOn(testProductName);
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChild(testProductName));
    
    Item testItem = getItemFromWarehouse(testProductName);
    ScrollPane detailsViewScrollPane = getDetailsViewController(testItem).getScrollPane();
    assertNotNull(testItem);

    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_EDIT);
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_INCREMENT);
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_SAVE);
    assertEquals(1, testItem.getAmount());

    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_EDIT);
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_DECREMENT);
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_BUTTON_SAVE);
    assertEquals(0, testItem.getAmount());

    removeAllItems();
  }

  @Test
  @DisplayName("Test sorting and searching items on name")
  void testSortSearchItems(FxRobot robot) {
    login(robot);
    createNewItem(robot, "A");
    createNewItem(robot, "B");
    createNewItem(robot, "C");

    robot.clickOn(WAREHOUSE_SELECTOR_SORT_BY);
    selectOptionInComboBox(robot, WAREHOUSE_SELECTOR_SORT_BY, "Navn");
    verifyItemsInOrder(robot, "A", "B", "C");
    robot.clickOn(WAREHOUSE_BUTTON_REVERSE_ORDER);
    verifyItemsInOrder(robot, "C", "B", "A");

    robot.clickOn(WAREHOUSE_INPUT_SEARCH).write("A");
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChild("A"));
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChildren(0, "B"));
    FxAssert.verifyThat(WAREHOUSE_ITEM_LIST, NodeMatchers.hasChildren(0, "C"));
    robot.clickOn(WAREHOUSE_INPUT_SEARCH).push(KeyCode.BACK_SPACE);

    removeAllItems();
  }
}
