package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label label_empty;
    @FXML
    private Label label_click;

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

    @FXML
    private void handlePieChart(ActionEvent event) {
        GlobalState state = GlobalState.getInstance();
        Connection connection = null;

        label_click.setText("");
        label_empty.setText("");

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Spending Distribution Pie Chart");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT description, SUM(amount) AS total_amount FROM transaction_history WHERE user_id = ? GROUP BY description"
            );
            pstmt.setInt(1, state.getUser_id());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String description = rs.getString("description");
                double amount = rs.getDouble("total_amount");

                // Add data to PieChart
                PieChart.Data slice = new PieChart.Data(description + " (RM " + amount + ")", amount);
                pieChart.getData().add(slice);
            }

            // Add PieChart to BorderPane
            borderPane.setCenter(pieChart);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBarChart(ActionEvent event) {
        GlobalState state = GlobalState.getInstance();
        Connection connection = null;

        // Set up axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (RM)");

        // Create BarChart
        BarChart<String, Number> barchart = new BarChart<>(xAxis, yAxis);
        barchart.setTitle("Spending Distribution");

        // Create data series
        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.setName("Spending Distribution");

        try {
            // Database connection and query
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT description, SUM(amount) AS total_amount FROM transaction_history WHERE user_id = ? GROUP BY description"
            );
            pstmt.setInt(1, state.getUser_id());
            ResultSet rs = pstmt.executeQuery();

            // Populate data
            while (rs.next()) {
                String description = rs.getString("description");
                double amount = rs.getDouble("total_amount");
                data.getData().add(new XYChart.Data<>(description, amount));
            }

            // Add data to BarChart
            barchart.getData().add(data);

            // Add BarChart to BorderPane
            borderPane.setCenter(barchart);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
