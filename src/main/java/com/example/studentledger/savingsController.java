package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class savingsController implements Initializable {

    @FXML
    private Button button_back;
    @FXML
    private Button button_savepercentage;

    @FXML
    private RadioButton yes_radiobutton;
    @FXML
    private RadioButton no_radiobutton;

    @FXML
    private TextField tf_percentage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        yes_radiobutton.setToggleGroup(toggleGroup);
        no_radiobutton.setToggleGroup(toggleGroup);

        //DEFAULT SET TO NO
        no_radiobutton.setSelected(true);

        tf_percentage.setDisable(true);

        yes_radiobutton.setOnAction(e -> handleRadioSelection(true));
        no_radiobutton.setOnAction(e -> handleRadioSelection(false));

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

        button_savepercentage.setOnAction(e -> saveSettings());
    }

    private void handleRadioSelection(boolean isYesSelected) {
        GlobalState state = GlobalState.getInstance();
        if(isYesSelected) {
            showAlert("Savings mode is activated.", "SAVING MODE ACTIVATED");
            state.setSavingMode("SAVING : ON");
            state.setIsON(true);
            tf_percentage.setDisable(false);
        }
        else {
            state.setSavingMode("SAVING : OFF");
            state.setIsON(false);
            tf_percentage.setDisable(true);
            tf_percentage.clear();
        }
    }

    private void saveSettings() {
        if(yes_radiobutton.isSelected()) {
            try {
                GlobalState state = GlobalState.getInstance();
                double percentage = Double.parseDouble(tf_percentage.getText());
                if(percentage < 0 || percentage > 100) {
                    showAlert("Enter only 0-100 for percentage. Try again !", "Please enter a valid percentage");
                }else {
                    state.setPercentage(percentage);
                    System.out.println("Percentage: " + state.getPercentage());
                    showAlert("Savings completed successfully.", "Savings complete");
                }
            }catch (NumberFormatException e) {
                showAlert("Invalid input. Enter only number.", "Error");
            }
        } else if (no_radiobutton.isSelected()) {
            showAlert("Savings mode is deactivated.", "SAVING MODE DEACTIVATED");
        }
        else{
            showAlert("Selection is required.", "Yes OR No ?");
        }
    }

    private void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
