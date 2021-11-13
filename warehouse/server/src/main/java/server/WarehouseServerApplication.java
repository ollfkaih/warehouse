package server;

import com.fasterxml.jackson.core.type.TypeReference;
import core.Item;
import core.User;
import data.DataPersistence;
import data.FileSaver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring application. Config is changed here.
 */
@SpringBootApplication
public class WarehouseServerApplication {
  private final DataPersistence<Item> itemPersistence = new FileSaver<>(new TypeReference<>() {}, "spring-server-items");
  private final DataPersistence<User> userPersistence = new FileSaver<>(new TypeReference<>() {}, "spring-server-users");

  public static void main(String[] args) {
    SpringApplication.run(WarehouseServerApplication.class, args);
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
