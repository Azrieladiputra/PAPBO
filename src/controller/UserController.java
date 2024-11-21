package controller;

import model.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class UserController {
    private Database database;

    public UserController() {
        database = new Database();
    }

    public void loadUsers(DefaultTableModel tableModel) {
        String query = "SELECT u.nama_lengkap, p.kontak, p.domisili, u.username, u.password "
                     + "FROM users u "
                     + "JOIN pengguna p ON u.id_user = p.id_user";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nama_lengkap"),
                    rs.getString("kontak"),
                    rs.getString("domisili"),
                    rs.getString("username"),
                    rs.getString("password")
                });
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat data masyarakat: " + e.getMessage());
        }
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus pengguna: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUser(String username, String fullName, String kontak, String domisili) {
        String query = "UPDATE users u "
                     + "JOIN pengguna p ON u.id_user = p.id_user "
                     + "SET u.nama_lengkap = ?, p.kontak = ?, p.domisili = ? "
                     + "WHERE u.username = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, fullName);
            statement.setString(2, kontak);
            statement.setString(3, domisili);
            statement.setString(4, username);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui pengguna: " + e.getMessage());
        }
        return false;
    }
}
