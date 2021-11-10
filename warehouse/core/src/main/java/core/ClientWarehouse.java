package core;

import core.CoreConst.SortOption;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Class representing a physical warehouse that contains different items in various amounts.
 * Contains a local copy of the warehouse data, and makes calls to the server for updating and fetching updated data.
 */
public class ClientWarehouse extends BaseWarehouse {
  private AuthSession authSession;

  private final ClientWarehouseUpdater updater;
  private final ServerInterface server;

  public ClientWarehouse(ServerInterface server) {
    this.updater = new ClientWarehouseUpdater(this, server);
    this.server = server;
    updater.loadItems();
  }

  public Item removeItem(Item item) {
    return removeItem(item.getId());
  }

  @Override
  public List<Item> getAllItems() {
    return getAllItemsSorted(SortOption.DATE, true);
  }

  public List<Item> getItemsSortedAndFiltered(SortOption options, boolean ascendingOrder, String filterText) {
    List<Item> sortedItems = getAllItemsSorted(options, ascendingOrder);
    return sortedItems
    .stream()
    .filter(item ->
        item.getName().toLowerCase().contains(filterText.toLowerCase()) || (item.getBarcode() != null && item.getBarcode().equals(filterText)))
    .collect(Collectors.toList());
  }

  public List<Item> getAllItemsSorted(SortOption options, boolean ascendingOrder) {
    Comparator<Item> comparator = switch (options) {
      case NAME -> Comparator.comparing(Item::getName, String::compareToIgnoreCase);
      case AMOUNT -> Comparator.comparingInt(Item::getAmount);
      case PRICE -> Comparator.comparing(Item::getCurrentPrice, Comparator.nullsLast(Comparator.naturalOrder()));
      case WEIGHT -> Comparator.comparing(Item::getWeight, Comparator.nullsLast(Comparator.naturalOrder()));
      case DATE -> Comparator.comparing(Item::getCreationDate);
      default -> Comparator.comparing(Item::getCreationDate);
    };
    Comparator<Item> nameComparator = Comparator.comparing(Item::getName);
    Comparator<Item> creationDateComparator = Comparator.comparing(Item::getCreationDate);
    if (!ascendingOrder) {
      comparator = comparator.reversed();
      nameComparator = nameComparator.reversed();
      creationDateComparator = creationDateComparator.reversed();
    }
    comparator = comparator
        .thenComparing(nameComparator)
        .thenComparing(creationDateComparator);
    return new ArrayList<>(itemCollection.getAllSorted(comparator));
  }

  public CompletableFuture<Void> login(String username, String password) {
    return server
        .login(username, password)
        .thenAccept(authSession -> this.authSession = authSession);
  }

  public User getCurrentUser() {
    if (authSession == null) {
      return null;
    }
    return authSession.getUser();
  }

  public CompletableFuture<Void> register(User currentUser) {
    return server.register(currentUser);
  }

  public void logout() {
    this.authSession = null;
  }
}
