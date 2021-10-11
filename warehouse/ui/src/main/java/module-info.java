module warehouse.ui {
  requires warehouse.core;
  requires warehouse.data;
  requires java.sql;
  requires javafx.controls;
  requires javafx.fxml;
  requires barbecue;

  opens ui to javafx.graphics, javafx.fxml;
}
