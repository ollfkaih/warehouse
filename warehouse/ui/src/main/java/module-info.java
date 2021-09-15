module warehouse.ui {
    requires warehouse.core;
    requires javafx.controls;
    requires javafx.fxml;

    opens ui to javafx.graphics, javafx.fxml;
}
