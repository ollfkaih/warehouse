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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import java.io.IOException;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
public class WarehouseControllerTest {
  
  private WarehouseController warehouseController;
  private Parent root;
  private Warehouse originalWarehouse;
  private static final String FILENAME = "warehouse";
  private final DataPersistence dataPersistence = new WarehouseFileSaver(FILENAME);
  private Button addItemButtonCopy;

  private final String addItemButtonText = "Legg til";
  private final String detailsViewSaveButton = "#btnSave";
  private final String detailsViewDeleteButton = "#btnDelete";
  private final String warehouseNewItemInputField  = "#newProductName";
  private final String itemList = "#itemList";
  private final String detailsView = "#detailsViewScrollPane";

  @Start
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Warehouse.fxml"));
    root = loader.load();
    warehouseController = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
    
    addItemButtonCopy = new Button(addItemButtonText);
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
    FxAssert.verifyThat(addItemButtonCopy, LabeledMatchers.hasText(addItemButtonText));
  }

  @Test
  @DisplayName("Test add and remove item")
  void testAddAndDelete(FxRobot robot) {
    final String testProductName = getRandomProductName();
    robot.clickOn(warehouseNewItemInputField).write(testProductName);
    click(robot, addItemButtonText);
    assertNotNull(getItemFromWarehouse(testProductName), "unable to create item");
    FxAssert.verifyThat(itemList, NodeMatchers.hasChild(testProductName));
    click(robot, testProductName);
    robot.clickOn("#btnEdit");
    ensureVisible(getDetailsViewController(getItemFromWarehouse(testProductName)).getScrollPane(), getDetailsViewController(getItemFromWarehouse(testProductName)).getDeleteButton());
    robot.clickOn(detailsViewDeleteButton);
    robot.clickOn("Slett");
    assertNull(getItemFromWarehouse(testProductName), "unable to delete item");
    FxAssert.verifyThat(itemList, NodeMatchers.hasChildren(0, testProductName));
  }

  @Test
  @DisplayName("Test add item to warehouse and alter its properties")
  void testAddItem(FxRobot robot) {
    final String testProductName = getRandomProductName(); 
    robot.clickOn(warehouseNewItemInputField).write(testProductName);
    click(robot, addItemButtonText);

    Item testItem = getItemFromWarehouse(testProductName);
    assertNotNull(testItem, "unable to locate the created Item");
    FxAssert.verifyThat(itemList, NodeMatchers.hasChild(testProductName));

    click(robot, testProductName);
    robot.clickOn("#btnEdit");
    DetailsViewController testProductViewController = getDetailsViewController(testItem);
    assertNotNull(testProductViewController, "unable to find the detailsViewController of the opened item");

    robot.clickOn("#inpBrand").write("TestBrand");
    robot.clickOn("#inpAmount").write("50");
    robot.clickOn("#inpOrdinaryPrice").write("4000");
    robot.clickOn("#inpSalesPrice").write("3000");
    robot.clickOn("#inpRetailerPrice").write("2000");
    robot.clickOn("#inpPlacementSection").write("B");
    robot.clickOn("#inpPlacementRow").write("19");
    robot.clickOn("#inpPlacementShelf").write("3");
    robot.clickOn("#inpDimensionsLength").write("10");
    robot.clickOn("#inpDimensionsWidth").write("20");
    robot.clickOn("#inpDimensionsHeigth").write("4");
    robot.clickOn("#inpWeight").write("5.0");
    robot.clickOn("#inpBarcode").write("6830473201734");
    ensureVisible(testProductViewController.getScrollPane(), testProductViewController.getSaveButton());
    robot.clickOn(detailsViewSaveButton);

    verifyDetailViewContainsChildren("TestBrand", "50", "4000.0", "2000.0", "B", "19", "3", "10.0", "20.0", "4.0", "5.0", "6830473201734");
    verifyDetailView(testItem);

    robot.interact(() -> ((Stage) ((robot.lookup(detailsView).query())).getScene().getWindow()).close());

    click(robot, testProductName);

    verifyDetailViewContainsChildren("TestBrand", "50", "4000.0", "2000.0", "B", "19", "3", "10.0", "20.0", "4.0", "5.0", "6830473201734");
    verifyDetailView(testItem);
    
    robot.clickOn("#btnEdit");

    verifyDetailViewContainsChildren("TestBrand", "50", "4000.0", "2000.0", "B", "19", "3", "10.0", "20.0", "4.0", "5.0", "6830473201734");
    verifyDetailView(testItem);

    ensureVisible(getDetailsViewController(getItemFromWarehouse(testProductName)).getScrollPane(), getDetailsViewController(getItemFromWarehouse(testProductName)).getDeleteButton());
    robot.clickOn(detailsViewDeleteButton);
    robot.clickOn("Slett");
  }

  private void verifyDetailViewContainsChildren(String... childTexts) {
    for (String text : childTexts) {
      FxAssert.verifyThat(detailsView, NodeMatchers.hasChild(text));
    }
  }

  private void verifyDetailView(Item item) {
    FxAssert.verifyThat("#inpBrand", TextInputControlMatchers.hasText(item.getBrand()));
    FxAssert.verifyThat("#inpAmount", TextInputControlMatchers.hasText(String.valueOf(item.getAmount())));
    FxAssert.verifyThat("#inpOrdinaryPrice", TextInputControlMatchers.hasText(String.valueOf(item.getRegularPrice())));
    FxAssert.verifyThat("#inpSalesPrice", TextInputControlMatchers.hasText(String.valueOf(item.getSalePrice())));
    FxAssert.verifyThat("#inpRetailerPrice", TextInputControlMatchers.hasText(String.valueOf(item.getPurchasePrice())));
    FxAssert.verifyThat("#inpPlacementSection", TextInputControlMatchers.hasText(item.getSection()));
    FxAssert.verifyThat("#inpPlacementRow", TextInputControlMatchers.hasText(item.getRow()));
    FxAssert.verifyThat("#inpPlacementShelf", TextInputControlMatchers.hasText(item.getShelf()));
    FxAssert.verifyThat("#inpDimensionsLength", TextInputControlMatchers.hasText(String.valueOf(item.getLength())));
    FxAssert.verifyThat("#inpDimensionsWidth", TextInputControlMatchers.hasText(String.valueOf(item.getWidth())));
    FxAssert.verifyThat("#inpDimensionsHeigth", TextInputControlMatchers.hasText(String.valueOf(item.getHeight())));
    FxAssert.verifyThat("#inpWeight", TextInputControlMatchers.hasText(String.valueOf(item.getWeight())));
    FxAssert.verifyThat("#inpBarcode", TextInputControlMatchers.hasText(item.getBarcode()));
  }

  @Test
  @DisplayName("Test incrementButtons on frontpage")
  void testIncrementValues(FxRobot robot) {
    final String testProductName = getRandomProductName();
    robot.clickOn(warehouseNewItemInputField).write(testProductName);
    click(robot, addItemButtonText);
    robot.push(KeyCode.BACK_SPACE);
    FxAssert.verifyThat(itemList, NodeMatchers.hasChild(testProductName));
    Item testItem = getItemFromWarehouse(testProductName);
    assertNotNull(testItem);
    assertEquals(0, testItem.getAmount());
    robot.moveTo(testProductName);
    robot.clickOn("#incrementButton");
    assertEquals(1, testItem.getAmount());
    robot.clickOn("#decrementButton");
    assertEquals(0, testItem.getAmount());
  }

  @Test
  @DisplayName("Test incrementButtons on detailsView")
  void testDetailViewIncrement(FxRobot robot) {
    final String testProductName = getRandomProductName();
    robot.clickOn(warehouseNewItemInputField).write(testProductName);
    click(robot, addItemButtonText);
    DetailsViewController testProductViewController = getDetailsViewController(getItemFromWarehouse(testProductName));
    FxAssert.verifyThat(itemList, NodeMatchers.hasChild(testProductName));
    Item testItem = getItemFromWarehouse(testProductName);
    assertNotNull(testItem);
    click(robot, testProductName);

    robot.clickOn("#btnEdit");

    ensureVisible(testProductViewController.getScrollPane(), testProductViewController.getDecrementButton());
    robot.clickOn("#btnIncrement");
    ensureVisible(testProductViewController.getScrollPane(), testProductViewController.getSaveButton());
    robot.clickOn("#btnSave");
    assertEquals(1, testItem.getAmount());

    robot.clickOn("#btnEdit");

    ensureVisible(testProductViewController.getScrollPane(), testProductViewController.getDecrementButton());
    robot.clickOn("#btnDecrement");
    ensureVisible(testProductViewController.getScrollPane(), testProductViewController.getSaveButton());
    robot.clickOn("#btnSave");
    assertEquals(0, testItem.getAmount());
  }
}
