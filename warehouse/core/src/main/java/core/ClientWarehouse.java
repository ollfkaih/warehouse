package core;

import core.CoreConst.SortOption;
import core.server.AuthSession;
import core.server.LoginRequest;
import core.server.ServerInterface;

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

  public List<Item> getItemsSortedAndFiltered(SortOption options, boolean ascendingOrder, String filterText) {
    if (filterText.isEmpty()) {
      return getAllItemsSorted(options, ascendingOrder);
    }
    return getAllItemsSorted(options, ascendingOrder)
        .stream()
        .filter(item ->
            item.getName().toLowerCase().contains(filterText.toLowerCase())
            || (item.getBarcode() != null && item.getBarcode().equals(filterText)))
       .collect(Collectors.toList());
  }

  private List<Item> getAllItemsSorted(SortOption options, boolean ascendingOrder) {
    Comparator<Item> comparator = switch (options) {
      case NAME -> Comparator.comparing(Item::getName, String::compareToIgnoreCase);
      case AMOUNT -> Comparator.comparingInt(Item::getAmount);
      case PRICE -> Comparator.comparing(Item::getCurrentPrice, Comparator.nullsLast(Comparator.naturalOrder()));
      case WEIGHT -> Comparator.comparing(Item::getWeight, Comparator.nullsLast(Comparator.naturalOrder()));
      case DATE -> Comparator.comparing(Item::getCreationDate);
      default -> throw new IllegalArgumentException("SortOption " + options + " not supported");
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
        .login(new LoginRequest(username, password))
        .thenAccept(authSession1 -> this.authSession = authSession1);
  }

  public User getCurrentUser() {
    if (authSession == null) {
      return null;
    }
    return authSession.getUser();
  }

  public AuthSession getAuthSession() {
    return authSession;
  }

  public CompletableFuture<Void> register(User currentUser) {
    return server.register(currentUser);
  }

  public void logout() {
    this.authSession = null;
  }

  public void addLoadingListener(LoadingListener listener) {
    updater.addLoadingListener(listener);
  }

  public void removeLoadingListener(LoadingListener listener) {
    updater.removeLoadingListener(listener);
  }
}
