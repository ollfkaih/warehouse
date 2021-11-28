package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.main.Item;
import core.main.User;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DataUtilsTest {
  @Test
  @DisplayName("Test that serialized data matches the JSON-schema")
  void testSerializeDeserialize() throws IOException, ValidationException {
    ObjectMapper mapper = DataUtils.getObjectMapper();
    
    Item item = new Item("Bok", 4);
    User user = new User("test87", "password", false);

    JSONObject itemJson = new JSONObject(
        new JSONTokener(mapper.writeValueAsString(item)));
    JSONObject userJson = new JSONObject(
        new JSONTokener(mapper.writeValueAsString(user)));

    JSONObject userSchemaJson = new JSONObject(
        new JSONTokener(this.getClass().getResourceAsStream("/UserFileFormat.json")));
    JSONObject itemSchemaJson = new JSONObject(
        new JSONTokener(this.getClass().getResourceAsStream("/ItemFileFormat.json")));

    Schema userSchema = SchemaLoader.load(userSchemaJson);
    Schema itemSchema = SchemaLoader.load(itemSchemaJson);
    userSchema.validate(userJson);
    itemSchema.validate(itemJson);
  }
}
