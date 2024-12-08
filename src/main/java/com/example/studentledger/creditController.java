package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class creditController implements Initializable {

    @FXML
    private Button button_back;
    @FXML
    private Button button_credit;

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
                DBUtils.changeScene(event, "home.fxml", "Welcome Back", name, balance,savings,loans);
            }
        });

        button_credit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GlobalState state = GlobalState.getInstance();
                try{
                    if(Double.parseDouble(tf_amount.getText()) > state.getBalance()){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("You do not have enough balance.");
                        alert.showAndWait();
                    }
                    else if(Double.parseDouble(tf_amount.getText())<0 || Double.parseDouble(tf_amount.getText()) > 99999999){
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
                    else if(!tf_description.getText().isEmpty()){
                        DBUtils.credit(Double.parseDouble(tf_amount.getText()));
                        String name = state.getName();
                        double balance = state.getBalance();
                        double savings = state.getSavings();
                        double loans = state.getLoans();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Congratulations");
                        alert.setContentText(String.format("RM %.2f of credit has been recorded successfully.", Double.parseDouble(tf_amount.getText())));
                        alert.show();

                        DBUtils.changeScene(event, "home.fxml", "Welcome Back", name, balance,savings,loans);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("Please enter the description of your credit amount to credit.");
                        alert.showAndWait();
                    }
                }catch(NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Invalid amount entered. Please try again.");
                    alert.showAndWait();
                    e.printStackTrace();
                }

            }
        });

    }
}
