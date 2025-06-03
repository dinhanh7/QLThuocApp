package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * KhachHangDAO.java
 *
 * CRUD cơ bản cho bảng KhachHang,
 * và bổ sung thêm phương thức search(String hoTen, String sdt).
 */
public class KhachHangDAO {

    /**
     * Lấy toàn bộ danh sách KhachHang.
     */
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT idKH, hoTen, sdt, gioiTinh, ngayThamGia FROM KhachHang";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
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

    /**
     * Thêm mới KhachHang.
     */
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (idKH, hoTen, sdt, gioiTinh, ngayThamGia) " +
                     "VALUES (?, ?, ?, ?, ?)";
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

    /**
     * Cập nhật KhachHang.
     */
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET hoTen = ?, sdt = ?, gioiTinh = ?, ngayThamGia = ? " +
                     "WHERE idKH = ?";
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

    /**
     * Xóa KhachHang theo idKH.
     */
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

    /**
     * Tìm kiếm Khách hàng theo hoTen hoặc sdt (hoặc cả hai).
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<KhachHang> search(String hoTen, String sdt) {
        List<KhachHang> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idKH, hoTen, sdt, gioiTinh, ngayThamGia FROM KhachHang WHERE 1=1"
        );

        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND hoTen LIKE ?");
        }
        if (sdt != null && !sdt.trim().isEmpty()) {
            sql.append(" AND sdt LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (hoTen != null && !hoTen.trim().isEmpty()) {
                stmt.setString(idx++, "%" + hoTen.trim() + "%");
            }
            if (sdt != null && !sdt.trim().isEmpty()) {
                stmt.setString(idx++, "%" + sdt.trim() + "%");
            }
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

    /**
     * Lấy KhachHang theo SĐT (đã có sẵn):
     */
    public KhachHang getBySDT(String sdt) {
        String sql = "SELECT idKH, hoTen, sdt, gioiTinh, ngayThamGia FROM KhachHang WHERE sdt = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sdt);
            rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setIdKH(rs.getString("idKH"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSdt(rs.getString("sdt"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNgayThamGia(rs.getDate("ngayThamGia"));
                return kh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return null;
    }
}
