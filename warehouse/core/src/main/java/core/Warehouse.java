package core;

import core.CoreConst.SortOption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing a physical warehouse that contains different items in various amounts.
 */
public class Warehouse {
  private EntityCollection<Item> itemCollection;
  private User currentUser;
  private Collection<User> users;

  public Warehouse() {
    itemCollection = new EntityCollection<Item>();
    users = new ArrayList<>();
  }

  public void addItem(Item item) {
    if (itemCollection.contains(item.getId())) {
      throw new IllegalArgumentException("Item with this id is already in the warehouse!");
    }
    itemCollection.add(item);
  }

  public void addItem(String name, int amount) {
    Item item = new Item(name, amount);
    addItem(item);
  }

  public Item removeItem(Item item) {
    return removeItem(item.getId());
  }

  public Item removeItem(String id) {
    return itemCollection.remove(id);
  }

  public Item getItem(String id) {
    return itemCollection.get(id);
  }

  public boolean itemExists(String id) {
    return itemCollection.contains(id);
  }

  /**
   * Adds the item, replaces it if it already exists.
   *
   * @return true if it was added, false if it replaced
   */
  public boolean putItem(Item item) {
    return itemCollection.put(item);
  }

  public Collection<Item> findItemsbyName(String name) {
    return itemCollection.getAllFiltered(item -> item.getName().equals(name));
  }

  public Collection<Item> findItemsbyBrand(String brand) {
    return itemCollection.getAllFiltered(item -> item.getBrand().equals(brand));
  }

  public Collection<Item> findItemsWithAmountLessThan(int amount) {
    return itemCollection.getAllFiltered(item -> item.getAmount() < amount);
  }

  public Collection<Item> findItemsWithAmountMoreThan(int amount) {
    return itemCollection.getAllFiltered(item -> item.getAmount() > amount);
  }

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

  public void addItemsListener(EntityCollectionListener<Item> listener) {
    itemCollection.addListener(listener);
  }

  public void removeItemsListener(EntityCollectionListener<Item> listener) {
    itemCollection.removeListener(listener);
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    if (!containsUser(currentUser.getUserName(), currentUser.getPassword(), currentUser.isAdmin())) {
      throw new IllegalArgumentException("The user list does not contain this user.");
    }
    this.currentUser = currentUser;
  }

  public boolean isAdmin() {
    if (currentUser == null) {
      return false;
    } else {
      return currentUser.isAdmin();
    }
  }

  public void removeCurrentUser() {
    this.currentUser = null;
  }

  public Collection<User> getUsers() {
    return users;
  }

  public boolean containsUser(String username, String password, boolean admin) {
    return getUsers()
      .stream()
      .anyMatch(user -> user.getUserName().equals(username) && user.getPassword().equals(password) && user.isAdmin() == admin);
  }

  public boolean containsUserByUsername(String username) {
    return getUsers().stream().anyMatch(user -> user.getUserName().equals(username));
  }

  public void addUser(User user) {
    if (containsUserByUsername(user.getUserName())) {
      throw new IllegalArgumentException("Username already taken.");
    }
    users.add(user);
  }
}
