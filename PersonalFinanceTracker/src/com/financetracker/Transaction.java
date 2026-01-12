package com.financetracker;
import java.time.LocalDate;

public class Transaction {
    private double amount;
    private String category;
    private LocalDate date;
    private boolean isExpense;
    private String currency;

    public Transaction(double amount, String category, LocalDate date, boolean isExpense, String currency) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isExpense = isExpense;
        this.currency = currency;
    }

    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public boolean isExpense() { return isExpense; }
    public String getCurrency() { return currency; }
}