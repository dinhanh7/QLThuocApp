package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.TaiKhoan;

import java.sql.*;

/**
 * TaiKhoanDAO.java
 * Chứa các phương thức truy vấn bảng TaiKhoan, bao gồm checkLogin và getByUsername.
 */
public class TaiKhoanDAO {

    /**
     * Kiểm tra username/password (cách cũ, chỉ trả về true/false).
     */
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return false;
    }

    /**
     * Lấy thông tin đầy đủ của một tài khoản (bao gồm idVT) theo username.
     * Trả về TaiKhoan nếu tìm thấy, ngược lại trả về null.
     */
    public TaiKhoan getByUsername(String username) {
        String sql = "SELECT idTK, username, password, idNV, idVT FROM TaiKhoan WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setIdTK(rs.getString("idTK"));
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setIdNV(rs.getString("idNV"));
                tk.setIdVT(rs.getString("idVT"));
                return tk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return null;
    }

    /**
     * Chèn mới tài khoản (nếu cần dùng từ Java).
     */
    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (idTK, username, password, idNV, idVT) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tk.getIdTK());
            stmt.setString(2, tk.getUsername());
            stmt.setString(3, tk.getPassword());
            stmt.setString(4, tk.getIdNV());
            stmt.setString(5, tk.getIdVT());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Bạn có thể thêm các phương thức update, delete nếu cần
}
