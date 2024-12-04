package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;
    @FXML
    private Label label_balance;
    @FXML
    private Label label_savings;
    @FXML
    private Label label_loans;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Log In", null,0,0,0);
            }
        });

    }

    public void setUserInformation(String name) {
        label_welcome.setText("Welcome "+ name + " ! ");
    }

    public void setBalance(int balance, int savings, int loans) {
        label_balance.setText("Balance: RM " + balance);
        label_savings.setText("Savings: RM " + savings);
        label_loans.setText("Loans: RM " + loans);
    }
}
