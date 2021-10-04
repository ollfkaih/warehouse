package core;

import core.CoreConst.SortOptions;

import java.util.ArrayList;
import java.util.Collections;
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

  private List<Item> sortingMethod(Comparator<Item> c) {
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
    List<Item> sortedItems;
    switch (options) {
      case Name -> {
        sortedItems = sortingMethod((i1, i2) ->
            i1.getName().compareTo(i2.getName()));
      }
      case Amount -> {
        sortedItems = sortingMethod((i1, i2) -> {
          if (i1.getAmount() != i2.getAmount()) {
            return Integer.compare(i1.getAmount(), (i2.getAmount()));
          } else {
            return i1.getName().compareTo(i2.getName());
          }
        });
      }
      case Price -> {
          sortedItems = sortingMethod((i1, i2) -> {
            if (i1.getPrice() != i2.getPrice()) {
              return Double.compare(i1.getPrice(), (i2.getPrice()));
            } else {
              return i1.getName().compareTo(i2.getName());
            }
          });
      }
      case Weight -> {
        sortedItems = sortingMethod((i1, i2) -> {
          if (i1.getWeight() != i2.getWeight()) {
            return Double.compare(i1.getWeight(), (i2.getWeight()));
          } else {
            return i1.getName().compareTo(i2.getName());
          }
        });
      }
      case Date -> {
        sortedItems = sortingMethod((i1, i2) ->
            i2.getCreationDate().compareTo(i1.getCreationDate()));
      }
      //TODO: Add case for status[gitlab.assigne=eikhr]
      default -> {
        sortedItems = getAllItems();
      }
    }
    if (!ascendingOrder && sortedItems.size() > 0) {
      List<Item> descendingList = new ArrayList<>(sortedItems);
      Collections.reverse(descendingList);
      return descendingList;
    }
    return new ArrayList<>(sortedItems);
  }

  public Map<String, Item> getAllItemsAsMap() {
    return new TreeMap<>(items);
  }
}