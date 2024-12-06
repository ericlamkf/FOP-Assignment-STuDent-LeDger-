package com.example.studentledger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String name, int balance, int savings, int loans) {
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
                psInsert = connection.prepareStatement("INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
                psInsert.setString(1, name);
                psInsert.setString(2, email);
                psInsert.setString(3, password);
                state.setName(name);
                state.setBalance(0);
                state.setSavings(0);
                state.setLoans(0);
                psInsert.executeUpdate();

                changeScene(event, "home.fxml", "Welcome Back", name, 0, 0, 0 );
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

    public static void loginInUser(ActionEvent event, String name, String email, String password, int balance, int savings, int loans) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-fx-login", "root", "ERIClam.12");
            preparedStatement = connection.prepareStatement("SELECT password,balance, savings, loans FROM users WHERE name = ? AND email = ?");
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
                        balance = resultSet.getInt("balance");
                        savings = resultSet.getInt("savings");
                        loans = resultSet.getInt("loans");
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
}
