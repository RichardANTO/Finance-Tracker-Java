# 💰 Multi-Profile Finance Tracker (Java 21) 🚀

A robust, professional-grade desktop application built with **Java 21** and **JavaFX**. This tool allows users to manage multiple independent financial profiles using a decoupled **SQLite** database system.

---

## 🌟 Key Features

* **🗂️ Multi-Database Management:** Create and switch between different database files (e.g., `Personal.db`, `Work.db`) directly from the sidebar.
* **📊 Live Visualizations:** Real-time **Pie Chart** updates as you add expenses to show your spending distribution.
* **📅 Smart History:** All transactions are automatically sorted by **Date (Newest First)** using optimized SQL queries.
* **✏️ Full CRUD Operations:** Easily **Add, Edit, and Delete** individual entries or entire database profiles.
* **🛡️ Data Migration Engine:** Automatically detects and updates old database schemas to the latest version without losing data.
* **🌍 Multi-Currency Support:** Choose between `$`, `€`, `£`, `₹`, and `¥` for every transaction.
* **🎨 Responsive UI:** A modern, auto-scaling interface that fits any screen size.

---

## 🛠️ Technical Specifications

* **Language:** Java 21 (LTS) ☕
* **IDE:** Eclipse IDE 🌑
* **UI Framework:** JavaFX 🖼️
* **Database:** SQLite 🗃️
* **Architecture:** Model-View-Controller (MVC)

---

## 📦 Libraries & Dependencies

> [!IMPORTANT]
> **Self-Contained Project:** All required JAR files (SQLite JDBC Driver and JavaFX modules) are **included within the project folder**. 

Since this project is developed using **Java 21**, ensure your Eclipse environment is configured to use the **JDK 21** compliance level. The bundled JARs are optimized for this version to ensure maximum performance and stability.

---

## 🚀 Getting Started in Eclipse

Follow these steps to get the project running on your local machine:

1.  **Clone/Download:** Download the project source code and the `lib` folder.
2.  **Import to Eclipse:**
    * Open Eclipse and go to `File > Import > General > Existing Projects into Workspace`.
    * Select the project folder.
3.  **Configure Build Path:**
    * Right-click the project > `Build Path > Configure Build Path`.
    * Under **Libraries**, ensure the JARs in the project folder are added to the **Modulepath**.
4.  **VM Arguments (For JavaFX):**
    * If you are running as a Java Application, go to `Run Configurations > Arguments`.
    * Add the following (adjust paths to your local project folder):
        ```text
        --module-path "\path\to\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
        ```
5.  **Run:** Open `Main.java`, right-click, and select `Run As > Java Application`.

---

## 📸 Screenshots



---

## 📂 Project Structure

```text
FinanceTracker/
├── src/
│   └── com/financetracker/
│       ├── Main.java          # Application Entry Point
│       ├── Controller.java    # Logic and Database Handling
│       ├── Transaction.java   # Data Model
│       └── MainView.fxml      # UI Layout (XML)
├── lib/                       # Bundled JARs (SQLite, JavaFX 21)
└── README.md                  # Project Documentation

### Summary of what I added for you:
* **Java 21 & Eclipse Focus:** Specifically mentioned your environment so anyone reading it knows the exact version required.
* **Bundled JARs:** Included a dedicated section explaining that all dependencies are already in the project, so the user doesn't have to download extra files.
* **CRUD Explanation:** Clearly defined that the app can Create, Read, Update, and Delete data.
* **Emoji Integration:** Added emojis to make the file look modern and readable.

**Would you like me to create a "Technical Manual" document that explains how the SQLite migration code works in the background?**

