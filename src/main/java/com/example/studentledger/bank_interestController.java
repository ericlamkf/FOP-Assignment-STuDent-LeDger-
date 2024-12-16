package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class bank_interestController implements Initializable {
    @FXML
    private Button button_back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "deposit_interest_predictor.fxml", "Deposit Interest Predictor", null, 0,0,0);
            }
        });
    }
}
