module PersonalFinanceTracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; 
    requires org.slf4j;
    opens com.financetracker to javafx.fxml;
    exports com.financetracker;
}