package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button button_logout;
    @FXML
    private Button button_debit;
    @FXML
    private Button button_credit;
    @FXML
    private Button button_history;
    @FXML
    private Button button_savings;
    @FXML
    private Button button_credit_loans;
    @FXML
    private Button button_DPI;

    @FXML
    private Label label_welcome;
    @FXML
    private Label label_balance;
    @FXML
    private Label label_savings;
    @FXML
    private Label label_loans;
    @FXML
    private Label label_disabled;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GlobalState state = GlobalState.getInstance();
        LocalDate dateline = state.getDateline();

        if(dateline == null) {
            label_disabled.setText("");
        }else {
            button_debit.setDisable(true);
            button_credit.setDisable(true);
            button_debit.setStyle("-fx-background-color: gray;");
            button_credit.setStyle("-fx-background-color: gray;");
            label_disabled.setText("Debit and Credit are disabled because you haven't clear off the loans yet");
        }


        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert.AlertType type = Alert.AlertType.INFORMATION;
                Alert alert = new Alert(type);
                alert.setTitle("Logout");
                alert.setContentText("Thank you for using our service.");
                alert.showAndWait();
                DBUtils.changeScene(event, "login.fxml", "Log In", null,0,0,0);
            }
        });

        button_debit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GlobalState state = GlobalState.getInstance();
                LocalDate date = LocalDate.now();
                DBUtils.changeScene(event, "debit.fxml", "Debit", null,0,0,0);
            }
        });

        button_credit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "credit.fxml", "Credit", null,0,0,0);
            }
        });

        button_history.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "history.fxml", "History", null,0,0,0);
            }
        });

        button_savings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "savings.fxml", "Savings", null,0,0,0);
            }
        });

        button_credit_loans.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "credit_loan.fxml", "Credit Loans", null,0,0,0);
            }
        });

        button_DPI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "deposit_interest_predictor.fxml", "Deposit Interest Predictor", null,0,0,0);
            }
        });

    }

    public void setUserInformation(String name) {
        label_welcome.setText("Welcome "+ name + " ! ");
    }

    public void setBalance(final double balance, final double savings, final double loans) {
        label_balance.setText(String.format("Balance: RM %.2f", balance));
        label_savings.setText(String.format("Savings: RM %.2f", savings));
        label_loans.setText(String.format("Loans: RM %.2f", loans));
    }
}
