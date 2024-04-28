module ve.ula.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.graphics;


    opens ve.ula.tictactoe to javafx.fxml;
    exports ve.ula.tictactoe;
    exports ve.ula.tictactoe.controllers;
    opens ve.ula.tictactoe.controllers to javafx.fxml;
}