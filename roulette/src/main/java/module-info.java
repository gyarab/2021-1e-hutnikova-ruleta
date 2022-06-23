module com.example.roulette {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.roulette to javafx.fxml;
    exports com.example.roulette;
}