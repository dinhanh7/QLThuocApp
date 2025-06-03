package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    // Lấy tất cả tài khoản
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT idTK, username, password, idNV, idVT FROM TaiKhoan";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setIdTK(rs.getString("idTK"));
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setIdNV(rs.getString("idNV"));
                tk.setIdVT(rs.getString("idVT"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Lấy tài khoản theo ID
    public TaiKhoan getById(String idTK) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TaiKhoan WHERE idTK = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idTK);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tk = new TaiKhoan();
                tk.setIdTK(rs.getString("idTK"));
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setIdNV(rs.getString("idNV"));
                tk.setIdVT(rs.getString("idVT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return tk;
    }

    // Thêm mới tài khoản
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

    // Cập nhật tài khoản
    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET username = ?, password = ?, idNV = ?, idVT = ? WHERE idTK = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tk.getUsername());
            stmt.setString(2, tk.getPassword());
            stmt.setString(3, tk.getIdNV());
            stmt.setString(4, tk.getIdVT());
            stmt.setString(5, tk.getIdTK());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa tài khoản
    public boolean delete(String idTK) {
        String sql = "DELETE FROM TaiKhoan WHERE idTK = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idTK);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Kiểm tra đăng nhập (ví dụ cơ bản)
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
    
}
// TaiKhoanDAO.java 
