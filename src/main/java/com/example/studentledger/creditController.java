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
                    else if(!tf_description.getText().isEmpty()){
                        String name = state.getName();
                        DBUtils.credit(Double.parseDouble(tf_amount.getText()));
                        double balance = state.getBalance();
                        double savings = state.getSavings();
                        double loans = state.getLoans();
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
