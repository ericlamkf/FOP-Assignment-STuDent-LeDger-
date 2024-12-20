package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import jdk.jfr.Category;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class chartsController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button button_back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "history.fxml", "History", null, 0,0,0);
            }
        });
    }

    @FXML
    private void handleSpendingTrendsCredit(ActionEvent event) {
        GlobalState state = GlobalState.getInstance();
        Connection connection = null;
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time ");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Spending Amount ( RM ) ");

        BarChart barchart = new BarChart(xAxis, yAxis);

        XYChart.Series data = new XYChart.Series();
        data.setName("Spending Trends (Credit)");

        //Provide Data
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement pstmt = connection.prepareStatement("SELECT amount, date FROM transaction_history WHERE user_id = ? AND transaction_type = 'Credit'");
            pstmt.setInt(1,state.getUser_id());
            ResultSet rs = pstmt.executeQuery();

            Map<LocalDate,Double> dailyCreditSpending = new HashMap<>();
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                double amount = rs.getDouble("amount");

                dailyCreditSpending.put(date, dailyCreditSpending.getOrDefault(date, 0.0) + amount);
            }
            List<Map.Entry<LocalDate,Double>> sortedData = dailyCreditSpending.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());

            for(Map.Entry<LocalDate,Double> entry : sortedData) {
                data.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }
            barchart.getData().add(data);

            //Add Barchart to BorderPane
            borderPane.setCenter(barchart);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSpendingTrendsDebit(ActionEvent event) {
        GlobalState state = GlobalState.getInstance();
        Connection connection = null;
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time ");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Spending Amount ( RM ) ");

        BarChart barchart = new BarChart(xAxis, yAxis);

        XYChart.Series data = new XYChart.Series();
        data.setName("Spending Trends (Debit)");

        //Provide Data
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement pstmt = connection.prepareStatement("SELECT amount, date FROM transaction_history WHERE user_id = ? AND transaction_type = 'Debit'");
            pstmt.setInt(1,state.getUser_id());
            ResultSet rs = pstmt.executeQuery();

            Map<LocalDate,Double> dailyDebitSpending = new HashMap<>();
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                double amount = rs.getDouble("amount");

                dailyDebitSpending.put(date, dailyDebitSpending.getOrDefault(date, 0.0) + amount);
            }
            List<Map.Entry<LocalDate,Double>> sortedData = dailyDebitSpending.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());


            for(Map.Entry<LocalDate,Double> entry : sortedData) {
                data.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }
            barchart.getData().add(data);

            //Add Barchart to BorderPane
            borderPane.setCenter(barchart);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSavingsGrowth(ActionEvent event) {
        GlobalState state = GlobalState.getInstance();
        Connection connection = null;
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Saving Amount ( RM ) ");

        BarChart barchart = new BarChart(xAxis, yAxis);
        XYChart.Series data = new XYChart.Series();
        data.setName("Saving Growth");

        //Provide Data
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement pstmt = connection.prepareStatement("SELECT amount, date FROM savings WHERE user_id = ? ");
            pstmt.setInt(1,state.getUser_id());
            ResultSet rs = pstmt.executeQuery();

            Map<LocalDate,Double> dailySavings = new HashMap<>();
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                double amount = rs.getDouble("amount");

                dailySavings.put(date, dailySavings.getOrDefault(date, 0.0) + amount);
            }
            List<Map.Entry<LocalDate,Double>> sortedData = dailySavings.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());


            for(Map.Entry<LocalDate,Double> entry : sortedData) {
                data.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }
            barchart.getData().add(data);

            //Add Barchart to BorderPane
            borderPane.setCenter(barchart);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoansRepayment(ActionEvent event) {
        GlobalState state = GlobalState.getInstance();
        Connection connection = null;
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Loan Repayment ( RM ) ");

        BarChart barchart = new BarChart(xAxis, yAxis);
        XYChart.Series data = new XYChart.Series();
        data.setName("Loan Repayment");

        //Provide Data
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement pstmt = connection.prepareStatement("SELECT repaid_amount, date FROM loans_repaid WHERE user_id = ? ");
            pstmt.setInt(1,state.getUser_id());
            ResultSet rs = pstmt.executeQuery();

            Map<LocalDate,Double> dailyRepayment = new HashMap<>();
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                double amount = rs.getDouble("repaid_amount");

                dailyRepayment.put(date, dailyRepayment.getOrDefault(date, 0.0) + amount);
            }
            List<Map.Entry<LocalDate,Double>> sortedData = dailyRepayment.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());


            for(Map.Entry<LocalDate,Double> entry : sortedData) {
                data.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }
            barchart.getData().add(data);

            //Add Barchart to BorderPane
            borderPane.setCenter(barchart);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
