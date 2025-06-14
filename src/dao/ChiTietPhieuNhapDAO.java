package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.ChiTietPhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhapDAO {

    // Lấy danh sách chi tiết phiếu nhập theo idPN (dùng cho chức năng xem chi tiết)
    public List<ChiTietPhieuNhap> getByIdPN(String idPN) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT ct.idPN, ct.idThuoc, t.tenThuoc, ct.soLuong, ct.giaNhap "
                   + "FROM ChiTietPhieuNhap ct "
                   + "JOIN Thuoc t ON ct.idThuoc = t.idThuoc "
                   + "WHERE ct.idPN = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPN);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
                ct.setIdPN(rs.getString("idPN"));
                ct.setIdThuoc(rs.getString("idThuoc"));
                ct.setTenThuoc(rs.getString("tenThuoc"));
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setGiaNhap(rs.getDouble("giaNhap")); // Đúng field
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Thêm một chi tiết phiếu nhập mới
    public boolean insert(ChiTietPhieuNhap ct) {
        String sql = "INSERT INTO ChiTietPhieuNhap (idPN, idThuoc, soLuong, giaNhap) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ct.getIdPN());
            stmt.setString(2, ct.getIdThuoc());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setDouble(4, ct.getGiaNhap()); // Đúng field
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa tất cả chi tiết theo idPN (ít dùng)
    public boolean deleteByPhieuNhap(String idPN) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE idPN = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPN);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật số lượng, giá nhập chi tiết phiếu nhập
    public boolean update(ChiTietPhieuNhap ct) {
        String sql = "UPDATE ChiTietPhieuNhap SET soLuong = ?, giaNhap = ? WHERE idPN = ? AND idThuoc = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ct.getSoLuong());
            stmt.setDouble(2, ct.getGiaNhap()); // Đúng field
            stmt.setString(3, ct.getIdPN());
            stmt.setString(4, ct.getIdThuoc());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }
    
    //them
    public void deleteByPhieuNhapAndThuoc(String idPN, String idThuoc) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE idPN=? AND idThuoc=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPN);
            ps.setString(2, idThuoc);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
