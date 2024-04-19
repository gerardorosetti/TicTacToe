module ve.ula.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;


    opens ve.ula.tictactoe to javafx.fxml;
    exports ve.ula.tictactoe;
}