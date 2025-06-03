package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    // Lấy tất cả khách hàng
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT idKH, hoTen, sdt, gioiTinh, ngayThamGia FROM KhachHang";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setIdKH(rs.getString("idKH"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSdt(rs.getString("sdt"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNgayThamGia(rs.getDate("ngayThamGia"));
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Lấy khách hàng theo ID
    public KhachHang getById(String idKH) {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE idKH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idKH);
            rs = stmt.executeQuery();
            if (rs.next()) {
                kh = new KhachHang();
                kh.setIdKH(rs.getString("idKH"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSdt(rs.getString("sdt"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNgayThamGia(rs.getDate("ngayThamGia"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return kh;
    }

    // Thêm mới khách hàng
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (idKH, hoTen, sdt, gioiTinh, ngayThamGia) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, kh.getIdKH());
            stmt.setString(2, kh.getHoTen());
            stmt.setString(3, kh.getSdt());
            stmt.setString(4, kh.getGioiTinh());
            stmt.setDate(5, new java.sql.Date(kh.getNgayThamGia().getTime()));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật khách hàng
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET hoTen = ?, sdt = ?, gioiTinh = ?, ngayThamGia = ? WHERE idKH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, kh.getHoTen());
            stmt.setString(2, kh.getSdt());
            stmt.setString(3, kh.getGioiTinh());
            stmt.setDate(4, new java.sql.Date(kh.getNgayThamGia().getTime()));
            stmt.setString(5, kh.getIdKH());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa khách hàng
    public boolean delete(String idKH) {
        String sql = "DELETE FROM KhachHang WHERE idKH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idKH);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }
}
