module warehouse.ui {
  requires warehouse.core;
  requires warehouse.data;
  requires java.sql;
  requires javafx.controls;
  requires javafx.fxml;

  opens ui to javafx.graphics, javafx.fxml;
}
