package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Database instance;
    private static final String URL = "jdbc:mysql://localhost:3306/recycle";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi berhasil");
        } catch (SQLException e) {
            System.err.println("Koneksi database gagal: " + e.getMessage());
        }
    }

    // Singleton instance retrieval with double-checked locking
    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    // Method to get connection, recreates connection if closed
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Koneksi berhasil diperbarui");
            }
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui koneksi database: " + e.getMessage());
        }
        return connection;
    }

    // Method to close the connection manually (optional)
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup");
            }
        } catch (SQLException e) {
            System.err.println("Gagal menutup koneksi database: " + e.getMessage());
        }
    }
}