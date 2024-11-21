package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Regis {
    private Database database;

    public Regis() {
        database = new Database();
    }

    public boolean register(String username, String password, String fullName, String domisili, String kontak) {
        Connection connection = database.getConnection();

        try {
            connection.setAutoCommit(false);

            // Tambahkan ke tabel users
            String userQuery = "INSERT INTO users (username, password, nama_lengkap) VALUES (?, ?, ?)";
            PreparedStatement userStatement = connection.prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, username);
            userStatement.setString(2, password);
            userStatement.setString(3, fullName);
            userStatement.executeUpdate();

            // Dapatkan id_user yang baru ditambahkan
            ResultSet rs = userStatement.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            // Tambahkan ke tabel pengguna
            String penggunaQuery = "INSERT INTO pengguna (id_user, domisili, kontak) VALUES (?, ?, ?)";
            PreparedStatement penggunaStatement = connection.prepareStatement(penggunaQuery);
            penggunaStatement.setInt(1, userId);
            penggunaStatement.setString(2, domisili);
            penggunaStatement.setString(3, kontak);
            penggunaStatement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.err.println("Gagal registrasi: " + e.getMessage());
            return false;
        } finally {
            database.closeConnection();
        }
    }

    public boolean registerUser(String username, String password, String namaLengkap, String domisili, Integer kontakInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
