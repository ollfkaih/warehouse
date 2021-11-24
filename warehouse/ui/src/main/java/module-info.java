module warehouse.ui {
  requires warehouse.core.client;
  requires warehouse.data;
  requires warehouse.localserver;
  requires java.sql;
  requires javafx.controls;
  requires javafx.fxml;
  requires barbecue;
  requires java.net.http;

  opens ui to javafx.graphics, javafx.fxml;
  opens ui.validators;
}
