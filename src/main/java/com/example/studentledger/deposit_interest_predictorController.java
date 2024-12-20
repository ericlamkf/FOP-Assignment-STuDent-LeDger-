package com.example.studentledger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class deposit_interest_predictorController implements Initializable {

    @FXML
    private Button button_back;
    @FXML
    private Button click_me;

    @FXML
    private ComboBox<String> dropdown_bank_name;

    @FXML
    private Button button_calculate;

    @FXML
    private RadioButton daily_button;
    @FXML
    private RadioButton annually_button;
    @FXML
    private RadioButton monthly_button;

    @FXML
    private TextField tf_deposit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> bank_name = FXCollections.observableArrayList("RHB", "Maybank", "Hong Leong", "Alliance", "AmBank", "Standard Chartered");
        dropdown_bank_name.setItems(bank_name);

        ToggleGroup toggleGroup = new ToggleGroup();
        daily_button.setToggleGroup(toggleGroup);
        annually_button.setToggleGroup(toggleGroup);
        monthly_button.setToggleGroup(toggleGroup);

        //DEFAULT SELECTION
        monthly_button.setSelected(true);

        click_me.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"bank_interest.fxml", "Bank Interest Rates", null, 0,0,0);
            }
        });

        button_calculate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(!tf_deposit.getText().isEmpty()){
                        String selectedFrequency = "";

                        if(daily_button.isSelected()){
                            selectedFrequency = "Daily";
                        }
                        else if(annually_button.isSelected()){
                            selectedFrequency = "Annually";
                        }
                        else if(monthly_button.isSelected()){
                            selectedFrequency = "Monthly";
                        }

                        if(dropdown_bank_name.getValue() != null && !dropdown_bank_name.getValue().isEmpty()){
                            try {
                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
                                PreparedStatement ps = connection.prepareStatement("SELECT interest_rate from bank_interest WHERE bank_name=?");
                                ps.setString(1, dropdown_bank_name.getValue());
                                ResultSet rs = ps.executeQuery();

                                if(rs.next()){
                                    double interestRate = rs.getDouble("interest_rate");
                                    double interest = calculateInterest(Double.parseDouble(tf_deposit.getText()), interestRate,selectedFrequency);
                                    showAlert(Alert.AlertType.INFORMATION, "Interest Calculated", String.format("Interest Amount %s is RM %.2f", selectedFrequency, interest));
                                }

                            }catch (Exception e){
                                showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong");
                            }
                        }else{
                            showAlert(Alert.AlertType.ERROR, "Error", "Please select a bank name");
                        }

                    }else{
                        showAlert(Alert.AlertType.ERROR,"Error", "Please enter a deposit amount. Deposit amount must be filled.");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR,"Error", "Please enter a valid deposit amount.");
                }
            }
        });

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
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private double calculateInterest(double deposit, double interest_rate, String frequency) {
        switch (frequency){
            case "Monthly":
                return (interest_rate/100 * deposit)/12;
            case "Daily":
                return (interest_rate/100 * deposit)/365;
            case "Annually":
                return (interest_rate/100 * deposit);
            default:
                return 0;
        }
    }
}
