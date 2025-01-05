package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.PipedReader;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String name, double balance, double savings, double loans) {
        Parent root = null;

        if(name!=null) {
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                HomeController homeController = loader.getController();
                homeController.setUserInformation(name);
                homeController.setBalance(balance, savings, loans);
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }


    public static void signUpUser(ActionEvent event, String name, String email, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            String validAnot = validInputs(name, email, password);
            if(!validAnot.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(validAnot);
                alert.show();
                return;
            }

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            psCheckUserExists.setString(1, email);
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()){
                System.out.println("User already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This email already exists");
                alert.show();
            }else {
                GlobalState state = GlobalState.getInstance();
                psInsert = connection.prepareStatement("INSERT INTO users (name, email, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                psInsert.setString(1, name);
                psInsert.setString(2, email);
                psInsert.setString(3, password);
                state.setName(name);
                state.setBalance(0);
                state.setSavings(0);
                state.setLoans(0);
                psInsert.executeUpdate();

                ResultSet resultSet1 = psInsert.getGeneratedKeys();
                if (resultSet1.next()) {
                    int generatedKey = resultSet1.getInt(1);
                    state.setUser_id(generatedKey);
                } else {
                    throw new SQLException("No ID obtained.");
                }

                changeScene(event, "home.fxml", "Welcome Back", name, state.getBalance(), state.getSavings(), state.getLoans() );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psInsert!=null){
                try{
                    psInsert.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists!=null){
                try{
                    psCheckUserExists.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static String validInputs(String name, String email, String password) {
        //CHECK for name
        if(!name.matches("[a-zA-Z0-9 ]+"))
            return "Name must be alphanumeric and cannot contain special characters.";

        //CHECK for email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if(!Pattern.matches(emailRegex, email))
            return "Invalid email format. Please enter a valid email (e.g., name@example.com).";

        //CHECK for password
        if(password.length()<=8)
            return "Password must be at least 8 characters long.";
        if(!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
            return "Password must contains at least one special character.";
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number.";
        }

        return "";//一个错都没有
    }

    public static void loginInUser(ActionEvent event, String name, String email, String password, double balance, double savings, double loans) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            preparedStatement = connection.prepareStatement("SELECT user_id,password,balance, savings, loans FROM users WHERE name = ? AND email = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("User does not exist in database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credential are incorrect");
                alert.show();
            }else{
                while(resultSet.next()){
                    String retrivedPassword = resultSet.getString("password");

                    if(retrivedPassword.equals(password)){
                        GlobalState state = GlobalState.getInstance();
                        balance = resultSet.getDouble("balance");
                        savings = resultSet.getDouble("savings");
                        loans = resultSet.getDouble("loans");
                        state.setUser_id(resultSet.getInt("user_id"));
                        state.setName(name);
                        state.setBalance(balance);
                        state.setSavings(savings);
                        state.setLoans(loans);
                        changeScene(event, "home.fxml", "Welcome Back", name, balance, savings, loans);
                    }else{
                        System.out.println("Incorrect password");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }
            }

        }catch(SQLException e){
        e.printStackTrace();
        }finally {
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try{
                    preparedStatement.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void debit(double amount){
        Connection connection = null;
        PreparedStatement psAlter = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psSavings = null;
        PreparedStatement psDatabase = null;
        ResultSet resultSet = null;

        try{
            GlobalState state = GlobalState.getInstance();
            String name = state.getName();
            int user_id = state.getUser_id();
            double percentage = state.getPercentage();
            boolean YesOrNo = state.getIsON();
            double savings = 0;

            //TO PERFORM SAVINGS CALCULATION
            if(YesOrNo){
                savings = amount * percentage/100;
                amount = amount * ((100-percentage)/100);
            }

            //TO PERFORM CALCULATION
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            psAlter = connection.prepareStatement("UPDATE users SET balance = balance + ? WHERE name = ?");
            psAlter.setDouble(1, amount);
            psAlter.setString(2, name);
            psAlter.executeUpdate();

            //ADD THE SAVED AMOUNT TO THE SAVINGS DATABASE
            psDatabase = connection.prepareStatement("INSERT INTO savings (user_id, amount) VALUES (?,?)");
            psDatabase.setInt(1, user_id);
            psDatabase.setDouble(2, savings);
            psDatabase.executeUpdate();

            //ADD THE SAVED AMOUNT TO THE SAVING
            psSavings = connection.prepareStatement("UPDATE users SET savings = savings + ? WHERE name = ?");
            psSavings.setDouble(1, savings);
            psSavings.setString(2, name);
            psSavings.executeUpdate();

            //TO UPDATE THE LATEST VALUE TO THE GLOBAL STATE
            psUpdate = connection.prepareStatement("SELECT balance, savings FROM users WHERE name = ?");
            psUpdate.setString(1, name);
            resultSet = psUpdate.executeQuery();

            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                state.setBalance(balance);
                savings = resultSet.getInt("savings");
                state.setSavings(savings);
            }


        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if(psAlter!=null){
                try {
                    psAlter.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void credit(double amount){
        Connection connection = null;
        PreparedStatement psAlter = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;

        try{
            GlobalState state = GlobalState.getInstance();
            String name = state.getName();

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            psAlter = connection.prepareStatement("UPDATE users SET balance = balance - ? WHERE name = ?");
            psAlter.setDouble(1, amount);
            psAlter.setString(2, name);
            psAlter.executeUpdate();

            psUpdate = connection.prepareStatement("SELECT balance FROM users WHERE name = ?");
            psUpdate.setString(1, name);
            resultSet = psUpdate.executeQuery();

            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                state.setBalance(balance);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psAlter!=null){
                try {
                    psAlter.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loans(double amount, double rate, double period, String period_type){
        GlobalState state = GlobalState.getInstance();
        int user_id = state.getUser_id();
        double nt, repayment_amount;
        if (amount <= 0 || rate <= 0 || period <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a positive number");
            alert.showAndWait();
        }
        if(period_type.equals("monthly")){
            nt = 12 * period;
            repayment_amount = (amount * (rate/1200))/(1-Math.pow((1+(rate/1200)),-nt));
            state.setLoans(repayment_amount);
        }else if(period_type.equals("annually")){
            repayment_amount = (amount * (rate/100))/(1-Math.pow((1+(rate/100)),-period));
            state.setLoans(repayment_amount);
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            PreparedStatement psUpdate = connection.prepareStatement("UPDATE users SET loans = ? WHERE user_id = ? ");
            psUpdate.setDouble(1,state.getLoans());
            psUpdate.setInt(2,state.getUser_id());
            psUpdate.executeUpdate();

            PreparedStatement psInsert = connection.prepareStatement("INSERT INTO credit_loan (user_id, principal_amount,interest_rate,repayment_period) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            psInsert.setInt(1,state.getUser_id());
            psInsert.setDouble(2,amount);
            psInsert.setDouble(3,rate);
            psInsert.setDouble(4,period);
            psInsert.executeUpdate();

            ResultSet rs = psInsert.getGeneratedKeys();

            if(rs.next()){
                if(period_type.equals("monthly")){
                    PreparedStatement psDateline = connection.prepareStatement("UPDATE credit_loan SET dateline = DATE_ADD(created_at, INTERVAL 1 MONTH) WHERE loan_id = ?\n");
                    psDateline.setInt(1,rs.getInt(1));
                    psDateline.executeUpdate();}
                else if(period_type.equals("annually")){
                    PreparedStatement psDateline = connection.prepareStatement("UPDATE credit_loan SET dateline = DATE_ADD(created_at, INTERVAL 1 YEAR) WHERE loan_id = ?\n");
                    psDateline.setInt(1,rs.getInt(1));
                    psDateline.executeUpdate();
                }
            }

            PreparedStatement psRetrived = connection.prepareStatement("SELECT dateline FROM credit_loan WHERE loan_id = ?");
            psRetrived.setInt(1,rs.getInt(1));
            ResultSet rsRetrived = psRetrived.executeQuery();
            if(rsRetrived.next()){
                Timestamp timestamp = rsRetrived.getTimestamp("dateline");
                if(timestamp != null){
                    LocalDate dateline = timestamp.toLocalDateTime().toLocalDate();
                    state.setDateline(dateline);
                }

            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Your transaction has been successfully loaned.");
            alert.showAndWait();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(int user_id, String description, String transaction_type, double amount, double balance){
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            psInsert = connection.prepareStatement("INSERT INTO transaction_history (user_id, description, transaction_type, amount, balance) VALUES (?,?,?,?,?)");
            psInsert.setInt(1, user_id);
            psInsert.setString(2, description);
            psInsert.setString(3, transaction_type);
            psInsert.setDouble(4, amount);
            psInsert.setDouble(5, balance);
            psInsert.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(psInsert!=null){
                try {
                    psInsert.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
