package com.example.studentledger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class historyController implements Initializable {
    public TableView<Transaction> tableView;
    public TableColumn<Transaction, String> dateColumn;
    public TableColumn<Transaction, String> descriptionColumn;
    public TableColumn<Transaction, Double> debitColumn;
    public TableColumn<Transaction, Double> creditColumn;
    public TableColumn<Transaction, Double> balanceColumn;

    @FXML
    private Button button_back;
    @FXML
    private Button button_csv;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_piecharts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        debitColumn.setCellValueFactory(new PropertyValueFactory<>("debit"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        GlobalState state = GlobalState.getInstance();
        int user_id = state.getUser_id();

        ObservableList<Transaction> data = FXCollections.observableArrayList();

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement ps = connection.prepareStatement("SELECT date, description, transaction_type, amount, balance FROM transaction_history WHERE user_id = ?;");
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String date = rs.getString("date");
                String description = rs.getString("description");
                String transaction_type = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                double balance = rs.getDouble("balance");

                if(transaction_type.equals("Debit")){
                    data.add(new Transaction(date,description, amount,0,balance));
                }
                else if(transaction_type.equals("Credit")){
                    data.add(new Transaction(date,description, 0,amount,balance));
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        tableView.setItems(data);

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

        button_piecharts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "charts.fxml","PieCharts",null,0,0,0);
            }
        });

        button_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setContentText("Are you sure you want to delete everything?");
                alert.showAndWait();

                if(alert.getResult() == ButtonType.OK){
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
                        PreparedStatement ps = connection.prepareStatement("DELETE FROM transaction_history WHERE user_id = ?;");
                        ps.setInt(1, user_id);
                        int rowsAffected = ps.executeUpdate();

                        if(rowsAffected > 0){
                            data.clear();
                            showAlert("Delete Successful", "All history data has been deleted");
                        }else {
                            showAlert("Delete Failed", "No history data is found in this user");
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                        showAlert(e.getMessage(), "Delete Failed. An error occurred while deleting the data.");
                    }
                }
            }
        });

        button_csv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save as CSV File");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                java.io.File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

                if(file != null){
                    try (FileWriter fileWriter = new FileWriter(file)) {
                        fileWriter.write("Date,Description,Debit,Credit,Balance\n");

                        for (Transaction transaction : data) {
                            fileWriter.write(
                                    transaction.getDate() + "," +
                                            transaction.getDescription() + "," +
                                            transaction.getDebit() + "," +
                                            transaction.getCredit() + "," +
                                            transaction.getBalance() + "\n"
                            );
                        }
                        showAlert("Congratulations","Export Successful. The data has been exported to" + file.getAbsolutePath());
                    }catch(IOException e){
                        e.printStackTrace();
                        showAlert("Error","Export Failed. An error occurred while exporting the data.");
                    }
                }
            }
        });
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


