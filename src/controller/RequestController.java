package controller;

import model.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RequestController {
    private Database database;

    public RequestController() {
        database = new Database();
    }

    public void loadRequests(DefaultTableModel tableModel) {
        String query = "SELECT r.id_request, u.nama_lengkap, p.kontak, p.domisili, s.jenis_sampah, "
                     + "r.jam_masuk, r.jam_jemput, r.status "
                     + "FROM request r "
                     + "JOIN users u ON r.users_id_user = u.id_user "
                     + "JOIN pengguna p ON u.id_user = p.id_user "
                     + "JOIN sampah s ON r.sampah_id_sampah = s.id_sampah";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nama_lengkap"),
                    rs.getString("kontak"),
                    rs.getString("domisili"),
                    rs.getString("jenis_sampah"),
                    rs.getString("jam_masuk"),
                    rs.getString("jam_jemput"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat data request: " + e.getMessage());
        }
    }

    public boolean deleteRequest(int requestId) {
        String query = "DELETE FROM request WHERE id_request = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, requestId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus request: " + e.getMessage());
        }
        return false;
    }

    public boolean updateRequestStatus(int requestId, String newStatus) {
        String query = "UPDATE request SET status = ? WHERE id_request = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newStatus);
            statement.setInt(2, requestId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui status request: " + e.getMessage());
        }
        return false;
    }
    public boolean saveRequest(int userId, String sampahId) {
    String query = "INSERT INTO request (jam_masuk, users_id_user, sampah_id_sampah) VALUES (NOW(), ?, ?)";
    
    try (Connection connection = database.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, userId);
        statement.setString(2, sampahId);
        return statement.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Gagal menyimpan request: " + e.getMessage());
    }
    return false;
    
    }
    public void loadUserDataMasyarakat(DefaultTableModel tableModel) {
    String query = "SELECT u.nama_lengkap, u.username, u.password, p.kontak, p.domisili "
                 + "FROM users u "
                 + "LEFT JOIN pengguna p ON u.id_user = p.id_user "
                 + "LEFT JOIN kru k ON u.id_user = k.id_user "
                 + "WHERE k.id_user IS NULL";



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
        System.err.println("Gagal memuat data pengguna: " + e.getMessage());
    }
    }
    
       public void loadRequestMasyarakat(DefaultTableModel tableModel) {
           try (Connection conn = Database.getInstance().getConnection()) {
        // Query untuk mengambil data dari tabel request beserta relasinya
        String query = "SELECT r.id_request, " +
                       "       u.nama_lengkap, " +
                       "       p.kontak, " +
                       "       p.domisili, " +
                       "       s.jenis_sampah, " +
                       "       r.jam_masuk, " +
                       "       r.jam_jemput, " +
                       "       r.status, " +
                       "       r.kru_penjemput " +
                       "FROM request r " +
                       "INNER JOIN users u ON r.users_id_user = u.id_user " +
                       "INNER JOIN pengguna p ON u.id_user = p.id_user " +
                       "INNER JOIN sampah s ON r.sampah_id_sampah = s.id_sampah";

        // Eksekusi query
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            // Iterasi hasil query dan tambahkan ke tabel model
            while (rs.next()) {
                Object[] rowData = {
                    rs.getString("id_request"),       // ID Request
                    rs.getString("nama_lengkap"),    // Nama Lengkap
                    rs.getString("kontak"),          // Kontak
                    rs.getString("domisili"),        // Domisili
                    rs.getString("jenis_sampah"),    // Jenis Sampah
                    rs.getTimestamp("jam_masuk"),    // Jam Masuk
                    rs.getString("jam_jemput"),      // Jam Jemput
                    rs.getString("status"),          // Status
                    rs.getString("kru_penjemput")    // Kru Penjemput
                };
                tableModel.addRow(rowData); // Tambahkan data ke tabel model
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

}
    public void deleteUser(int userId) {
    String query = "DELETE FROM users WHERE id_user = ?";

    try (Connection connection = database.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, userId); // Set id_user yang ingin dihapus
        int rowsDeleted = statement.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("Data pengguna berhasil dihapus.");
        } else {
            System.out.println("Data pengguna tidak ditemukan.");
        }

    } catch (SQLException e) {
        System.err.println("Gagal menghapus data pengguna: " + e.getMessage());
    }
}
    public static boolean updateUserData(String currentUsername, String newUsername, String newPassword, String newKontak, String newDomisili) throws SQLException {
        String updateQuery = "UPDATE users u "
                           + "JOIN pengguna p ON u.id_user = p.id_user "
                           + "SET u.username = ?, u.password = ?, p.kontak = ?, p.domisili = ? "
                           + "WHERE u.username = ?";

        try (Connection conn = Database.getInstance().getConnection()) {
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Koneksi database gagal.");
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);
                preparedStatement.setString(3, newKontak);
                preparedStatement.setString(4, newDomisili);
                preparedStatement.setString(5, currentUsername);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        }
    }
    
    
    public static boolean deleteUser(String username) throws SQLException {
        String deletePenggunaQuery = "DELETE FROM pengguna WHERE id_user = (SELECT id_user FROM users WHERE username = ?)";
        String deleteUsersQuery = "DELETE FROM users WHERE username = ?";

        try (Connection conn = Database.getInstance().getConnection()) {
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Koneksi database gagal.");
            }

            try {
                // Mulai transaksi
                conn.setAutoCommit(false);

                // Hapus data dari tabel `pengguna`
                try (PreparedStatement stmtPengguna = conn.prepareStatement(deletePenggunaQuery)) {
                    stmtPengguna.setString(1, username);
                    stmtPengguna.executeUpdate();
                }

                // Hapus data dari tabel `users`
                try (PreparedStatement stmtUsers = conn.prepareStatement(deleteUsersQuery)) {
                    stmtUsers.setString(1, username);
                    int rowsAffected = stmtUsers.executeUpdate();

                    if (rowsAffected > 0) {
                        // Commit jika semua operasi berhasil
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback(); // Batalkan transaksi jika tidak ada baris yang dihapus
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); // Batalkan transaksi jika terjadi kesalahan
                throw e;
            } finally {
                conn.setAutoCommit(true); // Kembalikan mode autocommit
            }
        }
    }
    public boolean updateRequestData(String idRequest, String jamJemput, String kruPenjemput, String status) throws SQLException {
    try (Connection conn = Database.getInstance().getConnection()) {
        // Query untuk update tabel recycle_request
        String updateQuery = "UPDATE request r " +
                             "SET jam_jemput = ?, " +
                             "    kru_penjemput = ?, " +
                             "    status = ? " +
                             "WHERE id_request = ?";

        // Menggunakan PreparedStatement untuk mencegah SQL Injection
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            // Mengisi parameter dalam query
            ps.setString(1, jamJemput);      // Nilai baru untuk kolom jam_jemput
            ps.setString(2, kruPenjemput);  // Nilai baru untuk kolom kru_penjemput
            ps.setString(3, status);        // Nilai baru untuk kolom status
            ps.setString(4, idRequest);     // ID request untuk baris yang akan diperbarui

            // Eksekusi query dan cek jumlah baris yang terpengaruh
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Mengembalikan true jika ada baris yang diperbarui
        }
    }

}
}









