package core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private int id;
    private String name;
    private int quantity;

    public Item(
            @JsonProperty("id") int id, 
            @JsonProperty("name") String name, 
            @JsonProperty("quantity") int quantity
        ) {
        setName(name);
        setId(id);
        setQuantity(quantity);    
}

    public Item(int id, String name) {
        this(id, name, 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
    }
}
