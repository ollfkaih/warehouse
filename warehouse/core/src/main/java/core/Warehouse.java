package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Warehouse{
    
    private Map<Integer, Item> items;
    
    public Warehouse() {
        items = new HashMap<>();
    }

    public void addItem(Item item) {
        if (items.containsKey(item.getId())) {
            throw new IllegalArgumentException("Item cannot be added because id is taken");
        }
        items.putIfAbsent(item.getId(), item);
    }

    public Item removeItem(Item item) {
        if (! items.containsKey(item.getId())) {
            throw new IllegalArgumentException("Item id does not exist in warehouse");
        }
        items.remove(item.getId());
        return item;
    }

    public Item findItem(int id) {
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

    public List<Item> findItemswithQuantitylessthan(int quantity) {
        return findItemsbyPredicate(item -> item.getQuantity() < quantity);
    }
    
    public List<Item> findItemswithQuantitymorethan(int quantity) {
        return findItemsbyPredicate(item -> item.getQuantity() > quantity);
    }

    public List<Item> findItemsbyName(String name) {
        return findItemsbyPredicate(item -> item.getName().equals(name));
    }

    public List<Item> getAllItems() {
        return items.values().stream().toList();
    }

    public Map<Integer, Item> getAllItemsAsMap() {
        return new HashMap<>(items);
    }

}