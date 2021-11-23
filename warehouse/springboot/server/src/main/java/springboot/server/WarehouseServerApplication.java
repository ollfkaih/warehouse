package springboot.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Item;
import core.User;
import data.DataPersistence;
import data.DataUtils;
import data.FileSaver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Spring application. Config is changed here.
 */
@SpringBootApplication
public class WarehouseServerApplication {
  private final DataPersistence<Item> itemPersistence = new FileSaver<>(new TypeReference<>() {}, "spring-server-items");
  private final DataPersistence<User> userPersistence = new FileSaver<>(new TypeReference<>() {}, "spring-server-users");
  private final ObjectMapper objectMapper = DataUtils.createObjectMapper();

  public static void main(String[] args) {
    SpringApplication.run(WarehouseServerApplication.class, args);
  }

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    return objectMapper;
  }

  @Bean
  public DataPersistence<Item> itemPersistence() {
    return itemPersistence;
  }

  @Bean
  public DataPersistence<User> userPersistence() {
    return userPersistence;
  }
}
