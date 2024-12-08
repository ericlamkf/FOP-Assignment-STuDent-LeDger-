package com.example.studentledger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class historyController implements Initializable {

    @FXML
    private Button button_back;


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
    }
}


