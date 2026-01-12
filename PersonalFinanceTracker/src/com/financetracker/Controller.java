package com.financetracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller {
    @FXML private ListView<String> dbListView;
    @FXML private TabPane mainTabPane;
    @FXML private TextField dbNameField, amountField, otherCategoryField;
    @FXML private Label statusLabel;
    @FXML private ComboBox<String> categoryBox, currencyBox;
    @FXML private DatePicker datePicker;
    @FXML private Button addButton;
    @FXML private PieChart expenseChart;
    @FXML private TableView<Transaction> expenseTable;
    @FXML private TableColumn<Transaction, LocalDate> colDate;
    @FXML private TableColumn<Transaction, String> colCategory;
    @FXML private TableColumn<Transaction, Double> colAmount;

    private final ObservableList<Transaction> data = FXCollections.observableArrayList();
    private String currentDbUrl = "";
    private final String folderPath = System.getProperty("user.home");

    @FXML
    public void initialize() {
        currencyBox.getItems().addAll("$", "€", "£", "₹", "¥");
        currencyBox.getSelectionModel().selectFirst();
        categoryBox.getItems().addAll("Food", "Rent", "Salary", "Shopping", "Health", "Other");

        amountField.setTextFormatter(new TextFormatter<>(change -> 
            change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null));

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        colAmount.setCellFactory(column -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Transaction trans = getTableView().getItems().get(getIndex());
                    setText(trans.getCurrency() + " " + String.format("%.2f", item));
                }
            }
        });

        expenseTable.setItems(data);
        refreshDbList();
        dbListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) { dbNameField.setText(newV.replace(".db", "")); handleConnectDB(); }
        });
    }

    @FXML protected void handleConnectDB() {
        String dbName = dbNameField.getText().trim();
        if (dbName.isEmpty()) return;
        currentDbUrl = "jdbc:sqlite:" + folderPath + "/" + dbName + ".db";
        try (Connection conn = DriverManager.getConnection(currentDbUrl)) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS expenses (amount REAL, category TEXT, date TEXT, isExpense INTEGER, currency TEXT)");
            try {
                stmt.executeQuery("SELECT currency FROM expenses LIMIT 1");
            } catch (SQLException e) {
                stmt.execute("ALTER TABLE expenses ADD COLUMN currency TEXT DEFAULT '$'");
            }
            statusLabel.setText("ACTIVE: " + dbName.toUpperCase());
            statusLabel.setTextFill(javafx.scene.paint.Color.web("#27ae60"));
            addButton.setDisable(false);
            loadDataFromDB();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML public void loadDataFromDB() {
        if (currentDbUrl.isEmpty()) return;
        data.clear();
        // UPDATED QUERY: Order by date descending (Newest first)
        String query = "SELECT * FROM expenses ORDER BY date DESC";
        try (Connection conn = DriverManager.getConnection(currentDbUrl);
             ResultSet rs = conn.createStatement().executeQuery(query)) {
            while (rs.next()) {
                String curr = rs.getString("currency");
                data.add(new Transaction(rs.getDouble("amount"), rs.getString("category"),
                    LocalDate.parse(rs.getString("date")), rs.getInt("isExpense") == 1, curr != null ? curr : "$"));
            }
            updateChart();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML protected void handleAddTransaction() {
        try {
            if (amountField.getText().isEmpty() || datePicker.getValue() == null || categoryBox.getValue() == null) return;
            double amount = Double.parseDouble(amountField.getText());
            String cat = "Other".equals(categoryBox.getValue()) ? otherCategoryField.getText() : categoryBox.getValue();
            if (cat == null || cat.trim().isEmpty()) return;
            
            String symbol = currencyBox.getValue();

            try (Connection conn = DriverManager.getConnection(currentDbUrl)) {
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO expenses VALUES(?,?,?,?,?)");
                pstmt.setDouble(1, amount); 
                pstmt.setString(2, cat);
                pstmt.setString(3, datePicker.getValue().toString()); 
                pstmt.setInt(4, cat.equalsIgnoreCase("Salary") ? 0 : 1);
                pstmt.setString(5, symbol);
                pstmt.executeUpdate();
            }
            loadDataFromDB();
            handleResetFields();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML protected void handleDelete() {
        Transaction selected = expenseTable.getSelectionModel().getSelectedItem();
        if (selected == null || currentDbUrl.isEmpty()) return;
        try (Connection conn = DriverManager.getConnection(currentDbUrl)) {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM expenses WHERE amount=? AND category=? AND date=?");
            pstmt.setDouble(1, selected.getAmount()); 
            pstmt.setString(2, selected.getCategory());
            pstmt.setString(3, selected.getDate().toString()); 
            pstmt.executeUpdate();
            data.remove(selected); 
            updateChart();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML protected void handleEdit() {
        Transaction selected = expenseTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        amountField.setText(String.valueOf(selected.getAmount()));
        currencyBox.setValue(selected.getCurrency());
        categoryBox.setValue(selected.getCategory());
        datePicker.setValue(selected.getDate());
        
        handleDelete();
        mainTabPane.getSelectionModel().select(0);
    }

    @FXML protected void handleDeleteDB() {
        String selected = dbListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            new File(folderPath + "/" + selected).delete();
            handleGoBack();
            refreshDbList();
        }
    }

    @FXML public void refreshDbList() {
        File[] files = new File(folderPath).listFiles((d, n) -> n.endsWith(".db"));
        ObservableList<String> names = FXCollections.observableArrayList();
        if (files != null) for (File f : files) names.add(f.getName());
        dbListView.setItems(names);
    }

    @FXML protected void handleResetFields() {
        amountField.clear(); categoryBox.setValue(null); datePicker.setValue(null);
        otherCategoryField.setVisible(false); otherCategoryField.setManaged(false);
    }

    @FXML protected void handleGoBack() {
        currentDbUrl = ""; data.clear(); statusLabel.setText("Disconnected"); addButton.setDisable(true);
    }

    @FXML private void handleCategoryAction() {
        boolean isOther = "Other".equals(categoryBox.getValue());
        otherCategoryField.setVisible(isOther);
        otherCategoryField.setManaged(isOther);
    }

    private void updateChart() {
        // Shows all data in the chart so it never looks empty
        Map<String, Double> summary = data.stream()
            .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
        
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        summary.forEach((k, v) -> chartData.add(new PieChart.Data(k, v)));
        expenseChart.setData(chartData);
    }
}