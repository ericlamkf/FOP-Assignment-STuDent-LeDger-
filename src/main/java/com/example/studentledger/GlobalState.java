package com.example.studentledger;

//used to handle global variables

public class GlobalState {
    private static GlobalState instance;
    private int user_id;
    private String name;
    private String email;
    private double balance;
    private double savings;
    private double loans;
    private String date;
    private String description;
    private String transaction_type;
    private double debit;
    private double credit;
    private double amount;
    private double balance_history;

    private GlobalState() {}

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getUser_id() {
        return user_id;
    }
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTransaction_type() {
        return transaction_type;
    }
    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }
    public double getDebit() {
        return debit;
    }
    public void setDebit(double debit) {
        this.debit = debit;
    }
    public double getCredit() {
        return credit;
    }
    public void setCredit(double credit) {
        this.credit = credit;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setBalance_history(double balance_history) {
        this.balance_history = balance_history;
    }
    public double getBalance_history() {
        return balance_history;
    }

}
