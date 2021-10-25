package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import core.Item;
import core.Warehouse;
import data.DataPersistence;
import data.WarehouseFileSaver;
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

import org.junit.jupiter.api.AfterEach;
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
import java.util.List;

@ExtendWith(ApplicationExtension.class)
class WarehouseControllerTest {
  
  private static final String INP_BARCODE = "#inpBarcode";
  private static final String INP_WEIGHT = "#inpWeight";
  private static final String INP_DIMENSIONS_HEIGTH = "#inpDimensionsHeigth";
  private static final String INP_DIMENSIONS_WIDTH = "#inpDimensionsWidth";
  private static final String INP_DIMENSIONS_LENGTH = "#inpDimensionsLength";
  private static final String INP_PLACEMENT_SHELF = "#inpPlacementShelf";
  private static final String INP_PLACEMENT_ROW = "#inpPlacementRow";
  private static final String INP_PLACEMENT_SECTION = "#inpPlacementSection";
  private static final String INP_RETAILER_PRICE = "#inpRetailerPrice";
  private static final String INP_SALES_PRICE = "#inpSalesPrice";
  private static final String INP_ORDINARY_PRICE = "#inpOrdinaryPrice";
  private static final String INP_AMOUNT = "#inpAmount";
  private static final String INP_BRAND = "#inpBrand";
  private static final String ADD_ITEM_BUTTON = "#addItemButton";
  private static final String DETAILS_VIEW_SAVE_BUTTON = "#btnSave";
  private static final String DETAILS_VIEW_DELETE_BUTTON = "#btnDelete";
  private static final String WAREHOUSE_NEW_ITEM_INPUTFIELD  = "#newProductName";
  private static final String ITEM_LIST = "#itemList";
  private static final String DETAILS_VIEW = "#detailsViewScrollPane";

  private WarehouseController warehouseController;
  private Parent root;
  private Warehouse originalWarehouse;
  private static final String FILENAME = "warehouse";
  private final DataPersistence dataPersistence = new WarehouseFileSaver(FILENAME);
  private Button addItemButtonCopy;

  @Start
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Warehouse.fxml"));
    root = loader.load();
    warehouseController = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
    
    addItemButtonCopy = new Button(ADD_ITEM_BUTTON);
  }

  private void click(FxRobot robot, String string) {
    robot.clickOn(LabeledMatchers.hasText(string));
  }

  private String getRandomProductName() {
    return String.valueOf("TEST" + ((int) (Math.random() * (900000000)) + 100000000)); 
  }

  private Item getItemFromWarehouse(String name) {
    List<Item> itemList = warehouseController.getItems();
    Item testItem = null;
    for (int i = 0; i < itemList.size(); i++) {
      if (itemList.get(i).getName().equals(name)) {
        testItem = itemList.get(i);
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
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        List<Item> allItems = warehouseController.getItems();
        for (int i = 0; i < allItems.size(); i++) {
          warehouseController.removeItem(allItems.get(i).getId());
        }   
      }
    }); 
  } 
        
  // Thanks to Marcos Supridatta and Joel Stevick for this superb solution: https://stackoverflow.com/questions/12837592/how-to-scroll-to-make-a-node-within-the-content-of-a-scrollpane-visible
  private static void ensureVisible(ScrollPane scrollPane, Node node) {
    Bounds viewport = scrollPane.getViewportBounds();
    double contentHeight = scrollPane.getContent().localToScene(scrollPane.getContent().getBoundsInLocal()).getHeight();
    double nodeMinY = node.localToScene(node.getBoundsInLocal()).getMinY();
    double nodeMaxY = node.localToScene(node.getBoundsInLocal()).getMaxY();

    double vValueDelta = 0;
    double vValueCurrent = scrollPane.getVvalue();

    if (nodeMaxY < 0) {
        vValueDelta = (nodeMinY - viewport.getHeight()) / contentHeight;
    } else if (nodeMinY > viewport.getHeight()) {
        vValueDelta = (nodeMinY + viewport.getHeight()) / contentHeight;
    }
    scrollPane.setVvalue(vValueCurrent + vValueDelta);
  }

  private Node findNode(FxRobot robot, String fxid) {
    return robot.lookup(fxid).query();
  }

  private FxRobotInterface ensureVisibleClickOn(ScrollPane scrollPane, FxRobot robot, String fxid) {
    ensureVisible(scrollPane, findNode(robot, fxid));
    return robot.clickOn(fxid);
  }

  @BeforeEach
  void setup() {
    try {
      originalWarehouse = dataPersistence.getWarehouse();
    } catch (Exception e) {
      System.out.println("Unable to save loaded file");
    }
    removeAllItems();
  }

  @AfterEach
  void teardown() {
    try {
      dataPersistence.saveItems(originalWarehouse);
    } catch (Exception e) {
      System.out.println("Unable to revert to original warehouse");
    }
  }

  @Test
  @DisplayName("Test controller")
  void testController() {
    assertNotNull(warehouseController);
  }

  @Test
  @DisplayName("Add-item button exists")
  void testAddButtonExist() {
    FxAssert.verifyThat(addItemButtonCopy, LabeledMatchers.hasText(ADD_ITEM_BUTTON));
  }

  @Test
  @DisplayName("Test add and remove item")
  void testAddAndDelete(FxRobot robot) {
    final String testProductName = getRandomProductName();
    robot.clickOn(WAREHOUSE_NEW_ITEM_INPUTFIELD).write(testProductName);
    robot.clickOn(ADD_ITEM_BUTTON);
    assertNotNull(getItemFromWarehouse(testProductName), "unable to create item");
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChild(testProductName));
    click(robot, testProductName);
    ScrollPane detailsViewScrollPane = getDetailsViewController(getItemFromWarehouse(testProductName)).getScrollPane();
    ensureVisibleClickOn(detailsViewScrollPane, robot, "#btnEdit");
    ensureVisible(getDetailsViewController(getItemFromWarehouse(testProductName)).getScrollPane(), getDetailsViewController(getItemFromWarehouse(testProductName)).getDeleteButton());
    ensureVisibleClickOn(detailsViewScrollPane, robot, DETAILS_VIEW_DELETE_BUTTON);
    ensureVisibleClickOn(detailsViewScrollPane, robot, "Slett");
    assertNull(getItemFromWarehouse(testProductName), "unable to delete item");
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChildren(0, testProductName));
  }

  @Test
  @DisplayName("Test add item to warehouse and alter its properties")
  void testAddItem(FxRobot robot) {
    final String testProductName = getRandomProductName(); 
    robot.clickOn(WAREHOUSE_NEW_ITEM_INPUTFIELD).write(testProductName);
    robot.clickOn(ADD_ITEM_BUTTON);

    Item testItem = getItemFromWarehouse(testProductName);
    assertNotNull(testItem, "unable to locate the created Item");
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChild(testProductName));

    click(robot, testProductName);
    robot.clickOn("#btnEdit");
    DetailsViewController testProductViewController = getDetailsViewController(testItem);
    ScrollPane testProductViewScrollPane = testProductViewController.getScrollPane();
    assertNotNull(testProductViewController, "unable to find the detailsViewController of the opened item");

    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_BRAND).write("TestBrand");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_AMOUNT).push(KeyCode.BACK_SPACE).write("50");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_ORDINARY_PRICE).write("4000");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_SALES_PRICE).write("3000");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_RETAILER_PRICE).write("2000");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_PLACEMENT_SECTION).write("B");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_PLACEMENT_ROW).write("19");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_PLACEMENT_SHELF).write("3");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_DIMENSIONS_LENGTH).write("10");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_DIMENSIONS_WIDTH).write("20");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_DIMENSIONS_HEIGTH).write("4");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_WEIGHT).write("5.0");
    ensureVisibleClickOn(testProductViewScrollPane, robot, INP_BARCODE).write("6830473201734");
    ensureVisibleClickOn(testProductViewScrollPane, robot, DETAILS_VIEW_SAVE_BUTTON);

    verifyDetailView(testItem);

    robot.interact(() -> ((Stage) ((robot.lookup(DETAILS_VIEW).query())).getScene().getWindow()).close());

    click(robot, testProductName);

    verifyDetailView(testItem);
    
    robot.clickOn("#btnEdit");

    verifyDetailView(testItem);

    ensureVisibleClickOn(getDetailsViewController(getItemFromWarehouse(testProductName)).getScrollPane(), robot, DETAILS_VIEW_DELETE_BUTTON);
    robot.clickOn("Slett");
  }

  private void verifyDetailView(Item item) {
    FxAssert.verifyThat(INP_BRAND, TextInputControlMatchers.hasText(item.getBrand()));
    FxAssert.verifyThat(INP_AMOUNT, TextInputControlMatchers.hasText(String.valueOf(item.getAmount())));
    FxAssert.verifyThat(INP_ORDINARY_PRICE, TextInputControlMatchers.hasText(String.valueOf(item.getRegularPrice())));
    FxAssert.verifyThat(INP_SALES_PRICE, TextInputControlMatchers.hasText(String.valueOf(item.getSalePrice())));
    FxAssert.verifyThat(INP_RETAILER_PRICE, TextInputControlMatchers.hasText(String.valueOf(item.getPurchasePrice())));
    FxAssert.verifyThat(INP_PLACEMENT_SECTION, TextInputControlMatchers.hasText(item.getSection()));
    FxAssert.verifyThat(INP_PLACEMENT_ROW, TextInputControlMatchers.hasText(item.getRow()));
    FxAssert.verifyThat(INP_PLACEMENT_SHELF, TextInputControlMatchers.hasText(item.getShelf()));
    FxAssert.verifyThat(INP_DIMENSIONS_LENGTH, TextInputControlMatchers.hasText(String.valueOf(item.getLength())));
    FxAssert.verifyThat(INP_DIMENSIONS_WIDTH, TextInputControlMatchers.hasText(String.valueOf(item.getWidth())));
    FxAssert.verifyThat(INP_DIMENSIONS_HEIGTH, TextInputControlMatchers.hasText(String.valueOf(item.getHeight())));
    FxAssert.verifyThat(INP_WEIGHT, TextInputControlMatchers.hasText(String.valueOf(item.getWeight())));
    FxAssert.verifyThat(INP_BARCODE, TextInputControlMatchers.hasText(item.getBarcode()));
  }

  @Test
  @DisplayName("Test incrementButtons on frontpage")
  void testIncrementValues(FxRobot robot) {
    final String testProductName = getRandomProductName();
    robot.clickOn(WAREHOUSE_NEW_ITEM_INPUTFIELD).write(testProductName);
    robot.clickOn(ADD_ITEM_BUTTON);
    robot.push(KeyCode.BACK_SPACE);
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChild(testProductName));
    Item testItem = getItemFromWarehouse(testProductName);
    assertNotNull(testItem);
    assertEquals(0, testItem.getAmount());
    robot.moveTo(testProductName);
    robot.clickOn("#incrementButton");
    assertEquals(1, testItem.getAmount());
    robot.moveTo(testProductName);
    robot.clickOn("#decrementButton");
    assertEquals(0, testItem.getAmount());
  }

  @Test
  @DisplayName("Test incrementButtons on detailsView")
  void testDetailViewIncrement(FxRobot robot) {
    final String testProductName = getRandomProductName();
    robot.clickOn(WAREHOUSE_NEW_ITEM_INPUTFIELD).write(testProductName);
    robot.clickOn(ADD_ITEM_BUTTON);
    click(robot, testProductName);
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChild(testProductName));
    Item testItem = getItemFromWarehouse(testProductName);
    ScrollPane detailsViewScrollPane = getDetailsViewController(testItem).getScrollPane();
    assertNotNull(testItem);
    ensureVisibleClickOn(detailsViewScrollPane, robot, testProductName);

    ensureVisibleClickOn(detailsViewScrollPane, robot, "#btnEdit");

    ensureVisibleClickOn(detailsViewScrollPane, robot, "#btnIncrement");
    //ensureVisible(testProductViewController.getScrollPane(), testProductViewController.getSaveButton());
    ensureVisibleClickOn(detailsViewScrollPane, robot, "#btnSave");
    assertEquals(1, testItem.getAmount());

    ensureVisibleClickOn(detailsViewScrollPane, robot, "#btnEdit");

    ensureVisibleClickOn(detailsViewScrollPane, robot, "#btnDecrement");
    ensureVisibleClickOn(detailsViewScrollPane, robot, "#btnSave");
    assertEquals(0, testItem.getAmount());
  }

  private List<Node> findItemNodes(FxRobot robot) {
    return robot.lookup(ITEM_LIST).queryAs(VBox.class).getChildren();
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

  @Test
  @DisplayName("Test sorting and searching items on name")
  void testSortSearchItems(FxRobot robot) {
    robot.clickOn(WAREHOUSE_NEW_ITEM_INPUTFIELD).write("B");
    robot.clickOn(ADD_ITEM_BUTTON);
    robot.write("C");
    robot.clickOn(ADD_ITEM_BUTTON);  
    robot.write("A");
    robot.clickOn(ADD_ITEM_BUTTON);

    robot.clickOn("Sorter");
    selectOptionInComboBox(robot, "#sortBySelector", "Navn");
    verifyItemsInOrder(robot, "A", "B", "C");
    robot.clickOn("#orderByButton");
    verifyItemsInOrder(robot, "C", "B", "A");

    robot.clickOn("#searchInput");
    robot.write("A");
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChild("A"));
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChildren(0, "B"));
    FxAssert.verifyThat(ITEM_LIST, NodeMatchers.hasChildren(0, "C"));
  }
}
