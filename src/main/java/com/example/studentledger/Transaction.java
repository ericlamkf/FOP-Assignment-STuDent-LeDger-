package com.example.studentledger;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.SimpleTimeZone;

public class Transaction {
    private String date;
    private String description;
    private double debit;
    private double credit;
    private double balance;

    // Constructor
    public Transaction(String date, String description, double debit, double credit, double balance) {
        this.date = date;
        this.description = description;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getDebit() {
        return debit;
    }

    public double getCredit() {
        return credit;
    }

    public double getBalance() {
        return balance;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDebit(double debit) {
        this.debit = debit;
    }
    public void setCredit(double credit) {
        this.credit = credit;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}

