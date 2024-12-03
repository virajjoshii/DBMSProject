package org.example;
import java.util.Collections;
import java.sql.*;

public class DbFunctions {

    public Connection conn(String dbName, String userName, String password){
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, userName, password);
            if(conn!= null)
                System.out.println("Connection established successfully");
        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }

    public void createTbl(Connection conn, String tblNm, String clmNm){
        Statement statement;
        try {
            String query = String.format("CREATE TABLE %s(%s);", tblNm, clmNm);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created successfully");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void Insert(Connection conn, String tblNm, String clmNm, String[] values){
//        Statement statement;
//        try {
//            String query = String.format("INSERT INTO %s(%s) VALUES ('%s');", tblNm, clmNm, values);
//            statement = conn.createStatement();
//            statement.executeUpdate(query);
//            System.out.println("Table created successfully");
//        }catch (Exception e){
//            System.out.println(e);
//        }
        PreparedStatement preparedStatement = null;
        try {
            // Dynamically create placeholders for the VALUES clause
            String placeholders = String.join(", ", Collections.nCopies(values.length, "?"));;
            String query = String.format("INSERT INTO %s (%s) VALUES (%s)", tblNm, clmNm, placeholders);
            preparedStatement = conn.prepareStatement(query);

            // Set the values dynamically
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }

            preparedStatement.executeUpdate();
            System.out.println("Row inserted successfully");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void drop(Connection conn, String tblNm){
        Statement statement;
        try{
            String query = String.format("DROP TABLE %s", tblNm);
            statement =conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Dropped Successfully");

        } catch (Exception e) {
            System.out.println(e);

        }
    }
    public void delete(Connection conn, String tblNm,String var, String value){
        Statement statement;
        try{
            String query = String.format("DELETE FROM %s WHERE %s = '%s';",tblNm, var, value);
            statement =conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Deleted successfully");

        } catch (Exception e) {
            System.out.println(e);

        }
    }

}
