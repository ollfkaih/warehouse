package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.module.SimpleModule;
import core.Warehouse;
import core.Item;
import data.mapper.LocalDateTimeDeserializer;
import data.mapper.LocalDateTimeSerializer;

public class WarehouseSerializer {
    public static void warehouseToJson(Warehouse warehouse, OutputStream outputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(getLocalDateTimeModule());

        objectMapper.writeValue(outputStream, warehouse.getAllItems());
    }

    public static Warehouse jsonToWarehouse(InputStream json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(getLocalDateTimeModule());

        List<Item> warehouseItems = objectMapper.readValue(json, new TypeReference<List<Item>>(){});
        Warehouse warehouse = new Warehouse();
        for (Item item : warehouseItems) {
            warehouse.addItem(item);
        }
        return warehouse;
    }

    private static SimpleModule getLocalDateTimeModule() {
        SimpleModule localDateTimeModule = new SimpleModule();
        localDateTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        localDateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        return localDateTimeModule;
    }
}
