package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class debitController implements Initializable {

    @FXML
    private Button button_back;
    @FXML
    private Button button_debit;

    @FXML
    private TextField tf_amount;
    @FXML
    private TextField tf_description;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GlobalState state = GlobalState.getInstance();
                String name = state.getName();
                double balance = state.getBalance();
                double savings = state.getSavings();
                double loans = state.getLoans();
                DBUtils.changeScene(event, "home.fxml", "Welcome Back", name,balance,savings,loans);
            }
        });

        button_debit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GlobalState state = GlobalState.getInstance();
                try {
                    if(!tf_description.getText().isEmpty()) {
                        double amount = Double.parseDouble(tf_amount.getText());
                        DBUtils.debit(amount);
                        String name = state.getName();
                        double balance = state.getBalance();
                        double savings = state.getSavings();
                        double loans = state.getLoans();
                        DBUtils.changeScene(event, "home.fxml", "Welcome Back", name,balance,savings,loans);
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("Please enter the description of your debit amount to debit.");
                        alert.showAndWait();
                    }

                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Invalid amount entered. Please try again.");
                    alert.showAndWait();
                    throw new RuntimeException(e);
                }

            }
        });

    }
}
