package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.PhieuDatHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PhieuDatHangDAO.java
 *
 * CRUD cơ bản cho bảng PhieuDatHang,
 * và bổ sung thêm phương thức search(String idPDH, String idKH).
 */
public class PhieuDatHangDAO {

    /**
     * Lấy toàn bộ danh sách PhieuDatHang.
     */
    public List<PhieuDatHang> getAll() {
        List<PhieuDatHang> list = new ArrayList<>();
        String sql = "SELECT idPDH, thoiGian, idKH, tongTien, diaChi, phuongThucThanhToan, trangThai FROM PhieuDatHang";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PhieuDatHang pdh = new PhieuDatHang();
                pdh.setIdPDH(rs.getString("idPDH"));
                pdh.setThoiGian(rs.getTimestamp("thoiGian"));
                pdh.setIdKH(rs.getString("idKH"));
                pdh.setTongTien(rs.getDouble("tongTien"));
                pdh.setDiaChi(rs.getString("diaChi"));
                pdh.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                pdh.setTrangThai(rs.getString("trangThai"));
                list.add(pdh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Thêm mới PhieuDatHang.
     */
    public boolean insert(PhieuDatHang pdh) {
        String sql = "INSERT INTO PhieuDatHang (idPDH, thoiGian, idKH, tongTien, diaChi, phuongThucThanhToan, trangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pdh.getIdPDH());
            stmt.setTimestamp(2, new Timestamp(pdh.getThoiGian().getTime()));
            stmt.setString(3, pdh.getIdKH());
            stmt.setDouble(4, pdh.getTongTien());
            stmt.setString(5, pdh.getDiaChi());
            stmt.setString(6, pdh.getPhuongThucThanhToan());
            stmt.setString(7, pdh.getTrangThai());
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
     * Cập nhật PhieuDatHang.
     */
    public boolean update(PhieuDatHang pdh) {
        String sql = "UPDATE PhieuDatHang SET thoiGian = ?, idKH = ?, tongTien = ?, diaChi = ?, phuongThucThanhToan = ?, trangThai = ? " +
                     "WHERE idPDH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(pdh.getThoiGian().getTime()));
            stmt.setString(2, pdh.getIdKH());
            stmt.setDouble(3, pdh.getTongTien());
            stmt.setString(4, pdh.getDiaChi());
            stmt.setString(5, pdh.getPhuongThucThanhToan());
            stmt.setString(6, pdh.getTrangThai());
            stmt.setString(7, pdh.getIdPDH());
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
     * Xóa PhieuDatHang theo idPDH.
     */
    public boolean delete(String idPDH) {
        String sql = "DELETE FROM PhieuDatHang WHERE idPDH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPDH);
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
     * Tìm kiếm PhieuDatHang theo idPDH hoặc idKH.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<PhieuDatHang> search(String idPDH, String idKH) {
        List<PhieuDatHang> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idPDH, thoiGian, idKH, tongTien, diaChi, phuongThucThanhToan, trangThai FROM PhieuDatHang WHERE 1=1"
        );

        if (idPDH != null && !idPDH.trim().isEmpty()) {
            sql.append(" AND idPDH LIKE ?");
        }
        if (idKH != null && !idKH.trim().isEmpty()) {
            sql.append(" AND idKH LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (idPDH != null && !idPDH.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idPDH.trim() + "%");
            }
            if (idKH != null && !idKH.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idKH.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhieuDatHang pdh = new PhieuDatHang();
                pdh.setIdPDH(rs.getString("idPDH"));
                pdh.setThoiGian(rs.getTimestamp("thoiGian"));
                pdh.setIdKH(rs.getString("idKH"));
                pdh.setTongTien(rs.getDouble("tongTien"));
                pdh.setDiaChi(rs.getString("diaChi"));
                pdh.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                pdh.setTrangThai(rs.getString("trangThai"));
                list.add(pdh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }
}
