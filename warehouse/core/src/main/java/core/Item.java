package core;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private int id;
    private String name;
    private int amount;
    private Date creationDate;

    public Item(
            @JsonProperty("id") int id, 
            @JsonProperty("name") String name, 
            @JsonProperty("amount") int amount,
            @JsonProperty("creationdate") Date creationDate
        ) {
        setName(name);
        setId(id);
        setAmount(amount);    
        setCreationDate(creationDate);
}

    private void setCreationDate(Date date) {
        if (new Date().compareTo(date) > 0) {
            return;
        }
        this.creationDate = date;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Item(int id, String name) {
        this(id, name, 0, new Date());
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
        if (id < 0) {
            throw new IllegalArgumentException();
        } 
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount < CoreConst.MINAMOUNT) {
            throw new IllegalArgumentException();
        }
        this.amount = amount;
    }

    public void incrementAmount() {
        if (getAmount() >= CoreConst.MAXAMOUNT) {
            return;
        }
        setAmount(getAmount() + 1);
    }

    public void decrementAmount() {
        if (getAmount() > CoreConst.MINAMOUNT + 1) {
            setAmount(getAmount() - 1);
        }
    }
}
