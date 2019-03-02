/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite;

/**
 *
 * @author Valerij
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBController {

    public static final DBController dbcontroller = new DBController();
    public static Connection connection;
    private final String DB_PATH = "jdbc:sqlite:.\\src\\main\\java\\sqlite\\settings.sqlite";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }

    private DBController() {
    }

    public static DBController getInstance() {
        return dbcontroller;
    }

    public void initDBConnection() {
        try {
            if (connection != null) {
                return;
            }
            System.out.println("Creating Connection to Database...");
            connection = DriverManager.getConnection(DB_PATH);
            if (!connection.isClosed()) {
                System.out.println("...Connection established");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (!connection.isClosed() && connection != null) {
                        connection.close();
                        if (connection.isClosed()) {
                            System.out.println("Connection to Database closed");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
     public List<String> handleSpecificRowsGetDB(String table ,String ... row) {
        List<String> resultList = new ArrayList();
       String rows = String.join(",", row);
       
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + rows + " FROM " + table);
            while (rs.next()) {
                for(String r : row){
                resultList.add(rs.getString(r)); 
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
        return resultList;
    }
    public List<String> handleAnyRowsGetDB(String row, int rowAmount, String table) {
        List<String> resultList = new ArrayList();
        if(row.equals("")){
            row = "*";
        }
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + row + " FROM " + table);
            while (rs.next()) {
                for(int i = 1; i<= rowAmount; i++){
                resultList.add(rs.getString(i)); 
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
        return resultList;
    }

    public String handleGetDB(String row, String table) {
        String result = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + row + " FROM " + table);
            while (rs.next()) {
                result = rs.getString(row); 
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
        return result;
    }

    public void handleUpdateDB(String data, String row, String table) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("UPDATE "+table+"\n"
                    + "  SET  "+row+"= \""+data+"\"");
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Execute");
            e.printStackTrace();
        }
    }
}
