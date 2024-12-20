package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class credit_loanController implements Initializable {

    @FXML
    private Button button_back;
    @FXML
    private Button button_repay;
    @FXML
    private Button button_apply;

    @FXML
    private TextField tf_principalAmount;
    @FXML
    private TextField tf_interest_rate;
    @FXML
    private TextField tf_period;

    @FXML
    private RadioButton radio_monthly;
    @FXML
    private RadioButton radio_annually;

    @FXML
    private Label label_loan;
    @FXML
    private Label label_dateline;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        radio_monthly.setToggleGroup(toggleGroup);
        radio_annually.setToggleGroup(toggleGroup);

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

        button_apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GlobalState state = GlobalState.getInstance();
                String period_type="";
                if(radio_monthly.isSelected()){
                    period_type = "monthly";
                }
                else if(radio_annually.isSelected()){
                    period_type = "annually";
                }
                else{
                    showAlert(Alert.AlertType.ERROR,"ERROR", "You have to choose a whether to apply an interest rate or annually.");
                }
                if(state.getLoans()==0){
                    DBUtils.loans(Double.parseDouble(tf_principalAmount.getText()),Double.parseDouble(tf_interest_rate.getText()),Double.parseDouble(tf_period.getText()),period_type);
                    DBUtils.changeScene(event,"credit_loan.fxml", "Credit Loan Applied",null,0,0,state.getLoans());
                }else{
                    showAlert(Alert.AlertType.INFORMATION,"You can't apply for another loan.","You have to repay the loans first before applying another loan.");
                }
            }
        });

        button_repay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GlobalState state = GlobalState.getInstance();
                double loans = state.getLoans();
                int user_id = state.getUser_id();
                if (loans == 0) {
                    showAlert(Alert.AlertType.ERROR, "Loans Paid.", "You have zero loans in your account.");
                }
                else{
                    showAlert(Alert.AlertType.CONFIRMATION, "Repay", "Are you sure you want to repay this credit loan?");
                    try {
                        Connection connection = null;
                        PreparedStatement psLoan = null;
                        PreparedStatement psFetch = null;
                        PreparedStatement psInsert = null;
                        ResultSet rs = null;
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");

                        psInsert = connection.prepareStatement("INSERT INTO loans_repaid (user_id,repaid_amount) VALUES (?,?)");
                        psInsert.setInt(1, user_id);
                        psInsert.setDouble(2, loans);
                        psInsert.executeUpdate();

                        psLoan = connection.prepareStatement("UPDATE users SET loans = 0 WHERE user_id = ? ");
                        psLoan.setDouble(1, user_id);
                        psLoan.executeUpdate();

                        psFetch = connection.prepareStatement("SELECT loans FROM users WHERE user_id = ? ");
                        psFetch.setInt(1, user_id);
                        rs = psFetch.executeQuery();

                        while(rs.next()){
                            loans = rs.getDouble("loans");
                            state.setLoans(loans);
                        }

                        state.setDateline(null);
                        DBUtils.changeScene(event,"credit_loan.fxml", "Credit Repaid !", null, 0,0,state.getLoans());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

        GlobalState state = GlobalState.getInstance();
        state.getCurrentTime();
        label_loan.setText(String.format("LOANS : RM %.2f" , state.getLoans()));
        if(state.getDateline()!=null){
            label_dateline.setText("Dateline     " + state.getDateline());
        }else
            label_dateline.setText("");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
