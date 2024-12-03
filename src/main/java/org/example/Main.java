package org.example;
import java.sql.*;
//import java.util.*;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection db = conn();
            System.out.println("Connection Established Successfully");
        }catch (Exception e){                       //establishing db connection.
            System.out.println(e);
        }

//        printTable("prod");
//        printTable("depot");
//        printTable("stock");

        //deleteProduct("p1");

        //deleteDepot("d1");

//        insertProd("p1", "cable", 20);
//        insertDepot("d1", "New York",9000);
//        insertStock("p1", "d1", 100);

        //updateProduct("p1", "pp1");


        //updateDepot("d1", "dd1");

        //addProduct("p100", "cd", 5,"d2", 50);

        addDepot("d100", "Chicago", 100, "p1", 100);




    } //end of main method

    public static Connection conn(){                        //connection method begin
        Connection conn = null;
        final String url = "jdbc:postgresql://localhost:5432/postgres";
        final String user = "postgres";
        final String pass = "root123";
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, pass);
        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }//connection method end.

    public static void printTable(String tblNm) {           //printTable method begin
        String query = String.format("SELECT * FROM %s", tblNm);

        try (Connection conn = conn();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            // Get column metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print table headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", metaData.getColumnName(i)); // Column names
            }
            System.out.println();

            // Print separator
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("--------------------");
            }
            System.out.println();

            // Iterate through the result set and print each row
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", resultSet.getString(i)); // Column values
                }
                System.out.println();
            }
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//printTable method end

    //Transaction 1 begins
    public static void deleteProduct(String productId) {
        String checkProductQuery = String.format("SELECT COUNT(*) FROM prod WHERE prodId = '%s';",productId);
        String deleteProductQuery = String.format("DELETE FROM Prod WHERE prodId = '%s';",productId);
        String deleteStockQuery = String.format("DELETE FROM Stock WHERE pCode = '%s';",productId);

        try (Connection conn = conn()) {
            conn.setAutoCommit(false); // Start transaction
            try(PreparedStatement checkStmt = conn.prepareStatement(checkProductQuery)){
                try(ResultSet resultSet = checkStmt.executeQuery()){
                    if(resultSet.next() && resultSet.getInt(1) == 0)
                        System.out.println("The ProdId given is incorrect, please confirm the ID");
                    else {
                        System.out.println("tables before query execution");
                        printTable("prod");
                        printTable("stock");

                        try (PreparedStatement productStmt = conn.prepareStatement(deleteProductQuery);
                             PreparedStatement stockStmt = conn.prepareStatement(deleteStockQuery)) {


                            productStmt.executeUpdate();
                            stockStmt.executeUpdate();

                            conn.commit(); // Commit transaction
                            System.out.println("Product and associated stock deleted successfully, below are the updated result.");
                            System.out.println("tables after query execution");
                            printTable("prod");
                            printTable("stock");
                        }
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction in case of error
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//Transaction 1 end.

    public static void deleteDepot(String depotId) { //Transaction 2 begins
        String checkDepotQuery = String.format("SELECT COUNT(*) FROM Depot WHERE depID = '%s';", depotId);
        String deleteDepotQuery = String.format("DELETE FROM Depot WHERE depId = '%s';",depotId);
        String deleteStockQuery = String.format("DELETE FROM Stock WHERE dCode = '%s';", depotId);

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkDepotQuery)) {

                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) == 0)
                        System.out.println("Depot with ID " + depotId + " does not exist.");

                    else{
                        System.out.println("tables before the query execution");
                        printTable("depot");
                        printTable("stock");

                        try (PreparedStatement depotStmt = conn.prepareStatement(deleteDepotQuery);
                             PreparedStatement stockStmt = conn.prepareStatement(deleteStockQuery)) {

                            depotStmt.executeUpdate();
                            stockStmt.executeUpdate();

                            conn.commit();
                            System.out.println("Depot and associated stock deleted successfully.");
                            System.out.println("Tables after the query execution");
                            printTable("depot");
                            printTable("stock");
                        }
                    }
                }


            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//Transaction 2 ends.

    //Transaction 3 starts.
    public static void updateProduct(String oldName, String newName) {
        String updateProductQuery = String.format("UPDATE Prod SET prodId = '%s' WHERE prodId = '%s';", newName, oldName);
        String updateStockQuery = String.format("UPDATE Stock SET pCode = '%s' WHERE pCode = '%s';", newName, oldName);

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);

            System.out.println("tables before the result");
            printTable("prod");
            printTable("stock");

            try (PreparedStatement productStmt = conn.prepareStatement(updateProductQuery);
                 PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {


                productStmt.executeUpdate();
                stockStmt.executeUpdate();

                conn.commit();
                System.out.println("Product renamed successfully, updated tables below.");
                System.out.println("tables after the result");
                printTable("prod");
                printTable("stock");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// End of Transaction 3.

    //Transaction 4 starts.
    public static void updateDepot(String oldName, String newName) {
        String updateDepotQuery = String.format("UPDATE Depot SET depId = '%s' WHERE depId = '%s';", newName, oldName);
        String updateStockQuery = String.format("UPDATE Stock SET dCode = '%s' WHERE dCode = '%s';", newName, oldName);

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);
            printTable("depot");
            printTable("Stock");

            try (PreparedStatement depotStmt = conn.prepareStatement(updateDepotQuery);
                 PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {

                depotStmt.executeUpdate();
                stockStmt.executeUpdate();

                conn.commit();
                System.out.println("Depot Id renamed successfully, below are the updated tables.");
                printTable("depot");
                printTable("Stock");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//Transactions 4 end.

    //Transaction 5 starts.
    public static void addProduct(String prodId, String prodName, int price, String depotId, int quantity) {
        String insertProductQuery = "INSERT INTO Prod (prodId, pName, price) VALUES (?, ?, ?)";
        String insertStockQuery = "INSERT INTO Stock (pCode, dCode, quantity) VALUES (?, ?, ?)";

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);

            System.out.println("tables before the result");
            printTable("prod");
            printTable("stock");

            try (PreparedStatement productStmt = conn.prepareStatement(insertProductQuery);
                 PreparedStatement stockStmt = conn.prepareStatement(insertStockQuery)) {
                productStmt.setString(1, prodId);
                productStmt.setString(2, prodName);
                productStmt.setInt(3, price);

                stockStmt.setString(1, prodId);
                stockStmt.setString(2, depotId);
                stockStmt.setInt(3, quantity);

                productStmt.executeUpdate();
                stockStmt.executeUpdate();

                conn.commit();
                System.out.println("Product and stock added successfully, below are the updated tables.");
                printTable("prod");
                printTable("stock");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//Transaction 5 starts.

    //Transaction 6 starts.
    public static void addDepot(String depotId, String location, int capacity, String prodId, int quantity) {
        insertProd("p1", "cable", 20);
        String insertDepotQuery = "INSERT INTO Depot (depId, addr, volume) VALUES (?, ?, ?)";
        String insertStockQuery = "INSERT INTO Stock (pCode, dCode, quantity) VALUES (?, ?, ?)";

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);

            System.out.println("tables before the result");
            printTable("depot");
            printTable("stock");

            try (PreparedStatement depotStmt = conn.prepareStatement(insertDepotQuery);
                 PreparedStatement stockStmt = conn.prepareStatement(insertStockQuery)) {
                depotStmt.setString(1, depotId);
                depotStmt.setString(2, location);
                depotStmt.setInt(3, capacity);

                stockStmt.setString(1, prodId);
                stockStmt.setString(2, depotId);
                stockStmt.setInt(3, quantity);

                depotStmt.executeUpdate();
                stockStmt.executeUpdate();

                conn.commit();
                System.out.println("Depot and stock added successfully, below are the updated tables.");
                System.out.println("Tables after the result");
                printTable("depot");
                printTable("stock");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//Transaction 6 end.


    public static void insertProd(String prodID, String pName, int price){
        String updateProductQuery = String.format("INSERT INTO prod(prodId, pName, price) VALUES ('%s', '%s', '%d')",prodID, pName, price);

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);

            System.out.println("tables before the result");
            printTable("prod");

            try (PreparedStatement productStmt = conn.prepareStatement(updateProductQuery)){
                productStmt.executeUpdate();
                conn.commit();
                System.out.println("Product added successfully, updated tables below.");
                System.out.println("tables after the result");
                printTable("prod");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertDepot(String depID, String addr, int volume ){
        String updateDepotQuery = String.format("INSERT INTO depot(depId, addr, volume) VALUES ('%s', '%s', '%d')",depID, addr, volume);

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);

            System.out.println("tables before the result");
            printTable("depot");

            try (PreparedStatement depotStmt = conn.prepareStatement(updateDepotQuery)){
                depotStmt.executeUpdate();
                conn.commit();
                System.out.println("Depot info inserted successfully, updated tables below.");
                System.out.println("tables after the result");
                printTable("depot");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertStock(String pCode, String dCode, int quantity){
        String updateStockQuery = String.format("INSERT INTO Stock(pCode, dCode, quantity) VALUES ('%s', '%s', '%d')",pCode, dCode, quantity);

        try (Connection conn = conn()) {
            conn.setAutoCommit(false);

            System.out.println("tables before the result");
            printTable("Stock");

            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)){
                stockStmt.executeUpdate();
                conn.commit();
                System.out.println("Stock info inserted successfully, updated tables below.");
                System.out.println("tables after the result");
                printTable("stock");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Rollback achieved gracefully. Please find detailed error analysis below");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

