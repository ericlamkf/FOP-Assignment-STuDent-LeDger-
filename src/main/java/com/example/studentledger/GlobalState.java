package com.example.studentledger;

//used to handle global variables

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GlobalState {
    private static GlobalState instance;
    private int user_id;
    private String name;
    private String email;
    private double balance;
    private double savings;
    private double loans;
    private String transaction_type;
    private double percentage;
    private String savingMode;
    private boolean isON;
    private LocalDateTime currentTime;
    private LocalDate dateline;

    private GlobalState() {}

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }
    public LocalDateTime getCurrentTime() {
        return currentTime;
    }
    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }
    public LocalDate getDateline() {
        return dateline;
    }
    public void setDateline(LocalDate dateline) {
        this.dateline = dateline;
    }

    public void updateTime() {
        currentTime = LocalDateTime.now();
    }

    public boolean getIsON() {
        return isON;
    }
    public void setIsON(boolean isON) {
        this.isON = isON;
    }
    public String getSavingMode(){
        return savingMode;
    }
    public void setSavingMode(String savingMode) {
        this.savingMode = savingMode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTransaction_type() {
        return transaction_type;
    }
    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }
    public double getPercentage() {
        return percentage;
    }
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    public int getUser_id() {return user_id;}
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public double getSavings() {
        return savings;
    }
    public void setSavings(double savings) {
        this.savings = savings;
    }
    public double getLoans() {
        return loans;
    }
    public void setLoans(double loans) {
        this.loans = loans;
    }


}
