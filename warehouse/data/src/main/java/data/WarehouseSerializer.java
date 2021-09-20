package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import core.Warehouse;
import core.Item;

public class WarehouseSerializer {
    public static void warehouseToJson(Warehouse warehouse, OutputStream outputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, warehouse.getAllItems());
    }

    public static Warehouse jsonToWarehouse(InputStream json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Item> warehouseItems = objectMapper.readValue(json, new TypeReference<List<Item>>(){});
        Warehouse warehouse = new Warehouse();
        for (Item item : warehouseItems) {
            warehouse.addItem(item);
        }
        return warehouse;
    }
}
