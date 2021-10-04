package core;

import core.CoreConst.SortOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Warehouse {
  private Map<String, Item> items;

  public Warehouse() {
    items = new TreeMap<>();
  }

  public void addItem(Item item) {
    if (items.containsKey(item.getId())) {
      throw new IllegalArgumentException("Item cannot be added because id is taken");
    }
    if (!item.getName().isEmpty()) {
      items.putIfAbsent(item.getId(), item);
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
    items.remove(id);
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
    return items.values().stream().sorted((c)).toList();
  }

  public List<Item> getItemsSortedAndFiltered(SortOptions options, boolean ascendingOrder, String filterText) {
    List<Item> items = getAllItemsSorted(options, ascendingOrder);
    return items
        .stream()
        .filter(item -> item.getName().toLowerCase().contains(filterText.toLowerCase()) || item.getBarcode().contains(filterText))
        .collect(Collectors.toList());
  }

  public List<Item> getAllItemsSorted(SortOptions options, boolean ascendingOrder) {
    final Comparator<Item> comparatorType = switch (options) {
      case Name -> Item.nameComparator;
      case Amount -> Item.amountComparator;
      case Price -> Item.priceComparator;
      case Weight -> Item.weightComparator;
      case Date -> Item.createdDateComparator;
      default -> Item.nameComparator;
    };
    Comparator<Item> comparator = (i1, i2) -> compareItems(i1, i2, comparatorType, ascendingOrder);
    List<Item> sortedItems = sortItemsByComparator(comparator);
    return new ArrayList<>(sortedItems);
  }

  private int compareItems(Item i1, Item i2, Comparator<Item> comparator, boolean ascendingOrder) {
    if (!ascendingOrder) {
      comparator = comparator.reversed();
    }
    int comparison = comparator.compare(i1, i2);
    if (comparison == 0) {
      return ascendingOrder ? Item.nameComparator.compare(i1, i2) : Item.nameComparator.reversed().compare(i1, i2);
    }
    return comparison;
  }

  public Map<String, Item> getAllItemsAsMap() {
    return new TreeMap<>(items);
  }
}