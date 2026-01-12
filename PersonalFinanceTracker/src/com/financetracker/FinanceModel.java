package com.financetracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;

public class FinanceModel {
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    public double getTotalIncome() {
        return transactions.stream().filter(t -> !t.isExpense()).mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalExpenses() {
        return transactions.stream().filter(Transaction::isExpense).mapToDouble(Transaction::getAmount).sum();
    }
}