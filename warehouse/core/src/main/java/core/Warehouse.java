package core;

import core.CoreConst.SortOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class representing a physical warehouse that contains different items in various amounts.
 */
public class Warehouse {
  private Map<String, Item> items;

  private Collection<WarehouseListener> listeners = new ArrayList<>();
  private Map<String, ItemListener> itemListeners = new HashMap<>();

  public Warehouse() {
    items = new TreeMap<>();
  }

  public void addItem(Item item) {
    if (items.containsKey(item.getId())) {
      throw new IllegalArgumentException("Item cannot be added because id is taken");
    }
    if (!item.getName().isEmpty()) {
      items.putIfAbsent(item.getId(), item);
      notifyItemAdded(item);
      itemListeners.put(item.getId(), this::notifyItemsUpdated);
      item.addListener(itemListeners.get(item.getId()));
    }
  }

  public void addItem(String name, int amount) {
    Item item = new Item(name, amount);
    addItem(item);
  }

  public Item removeItem(Item item) {
    return removeItem(item.getId());
  }

  public Item removeItem(String id) {
    if (!items.containsKey(id)) {
      throw new IllegalArgumentException("Item id does not exist in warehouse");
    }
    Item item = items.get(id);
    item.removeListener(itemListeners.get(item.getId()));
    itemListeners.remove(item.getId());
    items.remove(id);
    notifyItemRemoved(item);
    return item;
  }

  public Item findItem(String id) {

    if (!items.containsKey(id)) {
      throw new IllegalArgumentException();
    }
    return items.get(id);
  }

  public List<Item> findItemsbyPredicate(Predicate<Item> predicate) {
    return items
        .values()
        .stream()
        .filter(predicate)
        .toList();
  }

  public List<Item> findItemsbyName(String name) {
    return findItemsbyPredicate(item -> item.getName().equals(name));
  }

  public List<Item> findItemsbyBrand(String brand) {
    return findItemsbyPredicate(item -> item.getBrand().equals(brand));
  }

  public List<Item> findItemsWithAmountLessThan(int amount) {
    return findItemsbyPredicate(item -> item.getAmount() < amount);
  }

  public List<Item> findItemsWithAmountMoreThan(int amount) {
    return findItemsbyPredicate(item -> item.getAmount() > amount);
  }

  public List<Item> getAllItems() {
    return getAllItemsSorted(SortOptions.Date, true);
  }

  private List<Item> sortItemsByComparator(Comparator<Item> c) {
    return items.values().stream().sorted(c).toList();
  }

  public List<Item> getItemsSortedAndFiltered(SortOptions options, boolean ascendingOrder, String filterText) {
    List<Item> items = getAllItemsSorted(options, ascendingOrder);
    return items
        .stream()
        .filter(item -> item.getName().toLowerCase().contains(filterText.toLowerCase()) || item.getBarcode().contains(filterText))
        .collect(Collectors.toList());
  }

  public List<Item> getAllItemsSorted(SortOptions options, boolean ascendingOrder) {
    Comparator<Item> comparator = switch (options) {
      case Name -> Comparator.comparing(Item::getName, String::compareToIgnoreCase);
      case Amount -> Comparator.comparingInt(Item::getAmount);
      case Price -> Comparator.comparing(Item::getCurrentPrice, Comparator.nullsLast(Comparator.naturalOrder()));
      case Weight -> Comparator.comparing(Item::getWeight, Comparator.nullsLast(Comparator.naturalOrder()));
      case Date -> Comparator.comparing(Item::getCreationDate);
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
    return new ArrayList<>(sortItemsByComparator(comparator));
  }

  public Map<String, Item> getAllItemsAsMap() {
    return new TreeMap<>(items);
  }

  private void notifyItemAdded(Item item) {
    for (WarehouseListener listener : listeners) {
      listener.itemAddedToWarehouse(item);
    }
    notifyItemsUpdated();
  }
  
  private void notifyItemsUpdated() {
    for (WarehouseListener listener : listeners) {
      listener.warehouseItemsUpdated();
    }
  }
  
  private void notifyItemRemoved(Item item) {
    for (WarehouseListener listener : listeners) {
      listener.itemRemovedFromWarehouse(item);
    }
    notifyItemsUpdated();
  }

  public void addListener(WarehouseListener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(WarehouseListener listener) {
    this.listeners.remove(listener);
  }
}
