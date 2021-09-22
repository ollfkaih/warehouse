package data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.Warehouse;
import core.Item;

public class WarehouseSerializerTest {
    @Test
    @DisplayName("Test that a Warehouse that is serialized and then deserialized is equal")
    public void testSerializeDeserialize() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.addItem(new Item("Bok", 4));
        warehouse.addItem(new Item("Laks"));
        warehouse.addItem(new Item("Kaffe", 100));

        ByteArrayOutputStream jsonOutput = new ByteArrayOutputStream();
        WarehouseSerializer.warehouseToJson(warehouse, jsonOutput);

        ByteArrayInputStream jsonInput = new ByteArrayInputStream(jsonOutput.toByteArray());
        Warehouse newWarehouse = WarehouseSerializer.jsonToWarehouse(jsonInput);

        assertEquals(warehouse.getAllItems().size(), newWarehouse.getAllItems().size());
        for (Item item : warehouse.getAllItems()) {
            Item newItem = newWarehouse.findItem(item.getId());
            assertEquals(item.getId(), newItem.getId());
            assertEquals(item.getName(), newItem.getName());
            assertEquals(item.getAmount(), newItem.getAmount());
        }
    }
}
