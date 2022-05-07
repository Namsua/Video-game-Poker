module com.example.pokkergraafiline {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pokkergraafiline to javafx.fxml;
    exports com.example.pokkergraafiline;
}