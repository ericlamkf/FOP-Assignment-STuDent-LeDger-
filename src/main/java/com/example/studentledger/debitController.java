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
    private Label label_savingMode;

    @FXML
    private TextField tf_amount;
    @FXML
    private TextField tf_description;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        label_savingMode.setText(savingMode());

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
                double amount = Double.parseDouble(tf_amount.getText());
                try {
                    if(amount < 0 || amount > 99999999) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("Please enter a positive amount or less than 99999999.");
                        alert.showAndWait();
                    }
                    else if(tf_description.getText().trim().length() > 100){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("Please enter a description of less than 100 characters.");
                        alert.showAndWait();
                    }
                    else if(!tf_description.getText().isEmpty()) {

                        DBUtils.debit(amount);

                        int user_id = state.getUser_id();
                        double balance = state.getBalance();
                        DBUtils.insertData(user_id, tf_description.getText(),"Debit",amount, balance);

                        if(state.getIsON()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Congratulations !");
                            alert.setContentText(String.format("RM %.2f of debit has been recorded and %.2f of debit amount is saved into savings successfully.", amount*(100 - state.getPercentage())/100, state.getPercentage()));
                            alert.showAndWait();}
                        else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Congratulations !");
                            alert.setContentText(String.format("RM %.2f of debit has been recorded successfully.", amount));
                            alert.showAndWait();
                        }

                        String name = state.getName();
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
    private String savingMode() {
        GlobalState state = GlobalState.getInstance();
        if(state.getIsON()){
            state.setSavingMode("SAVING : ON");
        }else {
            state.setSavingMode("SAVING : OFF");
        }
        return state.getSavingMode();
    }
}
