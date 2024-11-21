package controller;

import model.CurrentUser;
import model.Database;
import model.Kru;
import model.Pengguna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    private Database database;

    public LoginController() {
        database = new Database();
    }

    public boolean login(String username, String password) {
        Connection connection = database.getConnection();

        try {
            String query = "SELECT id_user, nama_lengkap FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_user");
                String fullName = rs.getString("nama_lengkap");

                if (isKru(id)) {
                    CurrentUser.setInstance(new Kru(id, username, fullName, password));
                } else if (isPengguna(id)) {
                    CurrentUser.setInstance(new Pengguna(id, username, fullName, password));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Gagal login: " + e.getMessage());
        } finally {
            database.closeConnection();
        }
        return false;
    }

    private boolean isKru(int idUser) throws SQLException {
        String query = "SELECT id_user FROM kru WHERE id_user = ?";
        PreparedStatement statement = database.getConnection().prepareStatement(query);
        statement.setInt(1, idUser);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    private boolean isPengguna(int idUser) throws SQLException {
        String query = "SELECT id_user FROM pengguna WHERE id_user = ?";
        PreparedStatement statement = database.getConnection().prepareStatement(query);
        statement.setInt(1, idUser);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }
}
