package ui;

import static core.main.CoreConst.SortOption;

import core.client.ClientWarehouse;
import core.client.LoadingListener;
import core.client.ServerInterface;
import core.main.EntityCollectionListener;
import core.main.Item;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Main Controller class. Controls the main Warehouse view.
 */
public class WarehouseController implements EntityCollectionListener<Item>, LoadingListener {
  private ClientWarehouse warehouse;

  @FXML private Label usernameLabel;
  @FXML private Button loginButton;
  @FXML private Button addItemButton;
  @FXML private AnchorPane root;
  @FXML private GridPane dividerGridPane;
  @FXML private ScrollPane itemContainer;
  @FXML private VBox itemList;
  @FXML private TextField searchInput;
  @FXML private ComboBox<String> sortBySelector;
  @FXML private Button orderByButton;
  @FXML private VBox sortAndOrderSelectors;
  @FXML private VBox titleAddandSearch;
  @FXML private AnchorPane statusAnchorPane;
  @FXML private Label statusLabel;
  @FXML private ImageView statusImage;
  @FXML private ImageView userImage;
  @FXML private Label loadingLabel;
  @FXML private ImageView loadingImage;

  private Stage stage;

  private Image emptySearch = new Image(getClass().getResourceAsStream("icons/search-minus.png"));
  private Image emptyDolly = new Image(getClass().getResourceAsStream("icons/person-dolly-empty.png"));
  private Image userEdit = new Image(getClass().getResourceAsStream("icons/user-edit-white.png")); 
  private Image userLock = new Image(getClass().getResourceAsStream("icons/user-lock-white.png")); 

  private SortOption sortBy = SortOption.DATE;
  private boolean ascending = true;

  private final Map<Item, DetailsViewController> detailsViewControllers = new HashMap<>();
  private LoginController loginController;
  private ServerSelectController serverSelectController;
  private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

  @FXML
  void initialize() {
    List<String> displaySortStrings = List.of("Antall", "Dato", "Navn", "Pris", "Vekt");
    sortBySelector.getItems().addAll(displaySortStrings);

    searchInput.textProperty().addListener((observable, oldValue, newValue) -> updateInventory());

    statusLabel.setWrapText(true);
    serverSelectController = new ServerSelectController(this);
    serverSelectController.showView();

    RotateTransition rt = new RotateTransition(Duration.millis(1000), loadingImage);
    rt.setByAngle(360);
    rt.setCycleCount(-1);
    rt.setAutoReverse(false);
    rt.play();
    executor.scheduleAtFixedRate(() -> updatePerSecond(), 0, 2, TimeUnit.SECONDS);
  }

  public void endExecutor() {
    executor.shutdown();
  }

  public void updatePerSecond() {
    if (warehouse != null) {
      Platform.runLater(warehouse::loadItems);
    }
  }

  protected void setStage(Stage stage) {
    this.stage = stage;
  }

  protected void showView() {
    stage.show();
    stage.requestFocus();
  }

  protected void hideView() {
    stage.hide();
  }

  public void loadPersistedData(ServerInterface server) {
    if (warehouse != null) {
      warehouse.removeItemsListener(this);
      warehouse.removeLoadingListener(this);
    }

    warehouse = new ClientWarehouse(server);
    warehouse.addItemsListener(this);
    warehouse.addLoadingListener(this);
    loginController = new LoginController(this, warehouse);
    updateInventory();
  }

  @FXML
  private void login() {
    if (warehouse.getCurrentUser() == null) {
      loginController.showLoginView();
    } else {
      Alert promptLogoutConfirmationAlert = new Alert(AlertType.WARNING);
      promptLogoutConfirmationAlert.setHeaderText("Er du sikker på at du vil logge ut?");
      promptLogoutConfirmationAlert.initStyle(StageStyle.UTILITY);
      ButtonType cancelLoginButton = new ButtonType("Nei", ButtonData.CANCEL_CLOSE);
      ButtonType confirmLoginButton = new ButtonType("Ja", ButtonData.OK_DONE);
     
      promptLogoutConfirmationAlert.getButtonTypes().setAll(cancelLoginButton, confirmLoginButton);

      promptLogoutConfirmationAlert
          .showAndWait()
          .filter(response -> response == confirmLoginButton)
          .ifPresent(response -> confirmLogout());
    }
    updateInventory();
  }

  protected void confirmLogin() {
    usernameLabel.setText(warehouse.getCurrentUser().getUserName());
    usernameLabel.setVisible(true);
    loginButton.setText("Logg ut");
    userImage.setImage(userEdit);
    updateInventory();
    detailsViewControllers.values().forEach((DetailsViewController detailsViewController) -> detailsViewController.close());
  }

  private void confirmLogout() {
    warehouse.logout();
    userImage.setImage(userLock);
    usernameLabel.setText("");
    loginButton.setText("Logg inn");
    userImage.setImage(userLock);
    detailsViewControllers.values().forEach((DetailsViewController detailsViewController) -> detailsViewController.close());
  }

  protected void close() {
    stage.close();
  }

  /**
   * Deletes the current list of items if it exists and creates a new list. If the list is empty
   * (due to no items or no search results) it will display a corresponding info-message.
   */
  @FXML
  private void updateInventory() {
    addItemButton.setVisible(warehouse.getCurrentUser() != null); // set "Legg til produkt" visible when user is logged in
    itemList.getChildren().clear();
    List<Item> items = getItems();
    for (int i = 0; i < items.size(); i++) {
      ItemElementAnchorPane itemElement = new ItemElementAnchorPane(items.get(i));

      String id = items.get(i).getId();
      if (warehouse.getCurrentUser() != null) {
        itemElement.getDecrementButton().setOnAction(e -> decrementAmount(id));
        itemElement.getIncrementButton().setOnAction(e -> incrementAmount(id));
      } else {
        itemElement.getDecrementButton().setDisable(true);
        itemElement.getIncrementButton().setDisable(true);
        addItemButton.setVisible(false);
      }

      if (warehouse.getItem(id).getAmount() == 0) {
        itemElement.getDecrementButton().setDisable(true);
      }

      itemList.getChildren().add(itemElement);

      int index = i;

      itemElement.setOnMouseClicked(e -> openDetailsView(items.get(index)));
      itemElement.setOnMouseEntered(e -> hover(itemElement, index));
      itemElement.setOnMouseExited(e -> notHover(itemElement, index));
      notHover(itemElement, index);
    }

    if (items.isEmpty()) {
      displayEmptyMessage(items);
    } else {
      statusAnchorPane.setVisible(false);
    }
  }

  private void displayEmptyMessage(List<Item> items) {
    statusAnchorPane.setVisible(true);
    if (searchInput.getText().equals("")) {
      statusLabel.setText("Warehouse har ingen elementer");
      statusImage.setImage(emptyDolly);
    } else {
      statusImage.setImage(emptySearch);
      statusLabel.setText("Ingen resultater");
    }
  }
  
  private void openDetailsView(Item item) {
    if (! detailsViewControllers.containsKey(item)) {
      detailsViewControllers.put(item, new DetailsViewController(item, this.warehouse, this));
    }
    detailsViewControllers.get(item).showDetailsView();
  }

  private void notHover(ItemElementAnchorPane itemElement, int i) {
    itemElement.getStyleClass().removeAll(Arrays.asList("hoverOverDark", "hoverOverLight"));
    if (i % 2 == 0) {
      itemElement.getStyleClass().add("notHoverOverLight");
    } else { 
      itemElement.getStyleClass().add("notHoverOverDark");
    }
    itemElement.setButtonsVisible(false);
  }

  private void hover(ItemElementAnchorPane itemElement, int i) {
    itemElement.setCursor(Cursor.HAND);
    itemElement.getStyleClass().removeAll(Arrays.asList("notHoverOverDark", "notHoverOverLight"));
    if (i % 2 == 0) {
      itemElement.getStyleClass().add("hoverOverLight");
    } else { 
      itemElement.getStyleClass().add("hoverOverDark");
    }
    if (warehouse.getCurrentUser() != null) {
      itemElement.setButtonsVisible(true);
    }
  }

  protected void removeDetailsViewController(Item item) {
    detailsViewControllers.remove(item);
    updateInventory();
  }

  protected List<Item> getItems() {
    return warehouse.getItemsSortedAndFiltered(sortBy, ascending, searchInput.getText());
  }

  @FXML
  private void addItem() {
    if (warehouse.getCurrentUser() != null) {
      Item item = new Item("Nytt produkt");
      openDetailsView(item);
      detailsViewControllers.get(item).toggleEditing();
    }
  }

  @FXML
  protected void removeItem(String id) {
    warehouse.removeItem(warehouse.getItem(id));
  }

  protected void incrementAmount(String id) {
    if (warehouse.getCurrentUser() != null) {
      warehouse.getItem(id).incrementAmount();
    }
  }

  protected void decrementAmount(String id) {
    if (warehouse.getCurrentUser() != null) {
      warehouse.getItem(id).decrementAmount();
    }
  }

  @FXML
  private void changeSortBy() {

    String value = "";
    if (sortBySelector.getValue() != null) {
      value = sortBySelector.getValue();
    }

    switch (value) {
      case "Dato" -> {
        sortBy = SortOption.DATE;
        ascending = true;
      }
      case "Antall" -> {
        sortBy = SortOption.AMOUNT;
        ascending = false;
      }
      case "Navn" -> {
        sortBy = SortOption.NAME;
        ascending = true;
      }
      case "Pris" -> {
        sortBy = SortOption.PRICE;
        ascending = true;
      }
      case "Vekt" -> {
        sortBy = SortOption.WEIGHT;
        ascending = true;
      }
      default -> sortBy = SortOption.NAME;
    }
    updateInventory();
  }

  @FXML
  private void changeOrderBy() {
    ascending = !ascending;

    if (ascending && orderByButton.getStyleClass().contains("descending")) {
      orderByButton.getStyleClass().remove("descending");
    }
    if (!ascending && !orderByButton.getStyleClass().contains("descending")) {
      orderByButton.getStyleClass().add("descending");
    }

    updateInventory();
  }

  protected boolean canExit() {
    boolean currentlyEditing = false;
    for (DetailsViewController controller : detailsViewControllers.values()) {
      if (controller.isEditing()) {
        currentlyEditing = true;
        break;
      }
    }
    return !currentlyEditing;
  }

  @Override
  public void entityAdded(Item item) {
    Platform.runLater(this::updateInventory);
  }

  @Override
  public void entityUpdated(Item item) {
    Platform.runLater(this::updateInventory);
  }

  @Override
  public void entityRemoved(Item item) {
    Platform.runLater(this::updateInventory);
  }

  // for testing purposes only
  protected HashMap<Item, DetailsViewController> getDetailViewControllers() {
    return new HashMap<>(detailsViewControllers);
  }

  @Override
  public void startedLoading() {
    Platform.runLater(() -> loadingLabel.setVisible(true));
  }

  @Override
  public void stoppedLoading() {
    Platform.runLater(() -> loadingLabel.setVisible(false));
  }

  public static void setRegionVisibility(Region region, boolean visible) {
    region.setDisable(!visible);
    region.setVisible(visible);
    region.setMinWidth(visible ? -1 : 0);
    region.setMinHeight(visible ? -1 : 0);
    region.setMaxWidth(visible ? -1 : 0);
    region.setMaxHeight(visible ? -1 : 0);
  }
} 
