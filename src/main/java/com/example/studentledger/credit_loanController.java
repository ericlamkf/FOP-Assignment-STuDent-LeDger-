package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class credit_loanController implements Initializable {

    @FXML
    private Button button_back;
    @FXML
    private Button button_repay;

    @FXML
    private Label label_loan;

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

        button_repay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GlobalState state = GlobalState.getInstance();
                double balance = state.getBalance();
                double loans = state.getLoans();
                int user_id = state.getUser_id();
                if (loans == 0) {
                    showAlert(Alert.AlertType.ERROR, "Loans Paid.", "You have zero loans in your account.");
                }
                else if(balance > loans){
                    showAlert(Alert.AlertType.CONFIRMATION, "Repay", "Are you sure you want to repay this credit loan?");
                    try {
                        Connection connection = null;
                        PreparedStatement psLoan = null;
                        PreparedStatement psBalance = null;
                        PreparedStatement psFetch = null;
                        ResultSet rs = null;
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");

                        psBalance = connection.prepareStatement("UPDATE users SET balance = balance - ? WHERE user_id = ? ");
                        psBalance.setDouble(1, loans);
                        psBalance.setInt(2, user_id);
                        psBalance.executeUpdate();

                        psLoan = connection.prepareStatement("UPDATE users SET loans = 0 WHERE user_id = ? ");
                        psLoan.setDouble(1, user_id);
                        psLoan.executeUpdate();

                        psFetch = connection.prepareStatement("SELECT balance, loans FROM users WHERE user_id = ? ");
                        psFetch.setInt(1, user_id);
                        rs = psFetch.executeQuery();

                        while(rs.next()){
                            balance = rs.getDouble("balance");
                            loans = rs.getDouble("loans");
                            state.setBalance(balance);
                            state.setLoans(loans);
                        }

                        DBUtils.changeScene(event,"credit_loan.fxml", "Credit Repaid !", null, 0,0,state.getLoans());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    showAlert(Alert.AlertType.ERROR,"Error", "You don't have enough balance to repay.");
                }
            }
        });

        GlobalState state = GlobalState.getInstance();
        label_loan.setText(String.format("LOANS : RM %.2f" , state.getLoans()));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
