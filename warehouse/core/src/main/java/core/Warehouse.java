package core;

import core.CoreConst.SortOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

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
        if (! items.containsKey(id)) {
            throw new IllegalArgumentException("Item id does not exist in warehouse");
        }
        Item item = items.get(id);
        items.remove(id);
        return item;
    }

    public Item findItem(String id) {

        if (! items.containsKey(id)) {
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

    public List<Item> findItemsWithAmountLessThan(int amount) {
        return findItemsbyPredicate(item -> item.getAmount() < amount);
    }
    
    public List<Item> findItemsWithAmountMoreThan(int amount) {
        return findItemsbyPredicate(item -> item.getAmount() > amount);
    }

    public List<Item> findItemsbyName(String name) {
        return findItemsbyPredicate(item -> item.getName().equals(name));
    }

    public List<Item> getAllItems() {
        return getAllItemsSorted(SortOptions.Date, true);
    }

    private List<Item> sortingMethod(Comparator<Item> c) {
        return items.values().stream().sorted((c)).toList();
    }

    public List<Item> getAllItemsSorted(SortOptions options, boolean ascendingOrder) {
        List<Item> sortedItems;
        switch (options) {
            case Date -> {
                sortedItems = sortingMethod((i1, i2) -> 
                    i2.getCreationDate().compareTo(i1.getCreationDate()));
            }
            case Amount -> {
                //TODO: sort by name if amount is equal
                sortedItems = sortingMethod((i1, i2) ->
                    Integer.compare(i1.getAmount(), (i2.getAmount())));
            }
            case Name -> {
                sortedItems = sortingMethod((i1, i2) ->
                    i1.getName().compareTo(i2.getName()));
            }
            //TODO: Add case for status[gitlab.assigne=eikhr]
            default -> {
                sortedItems = getAllItems();
            }
        }
        if (! ascendingOrder && sortedItems.size() > 0) {
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
