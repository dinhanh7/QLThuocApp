package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HoaDonDAO.java
 *
 * CRUD cho bảng dbo.HoaDon (đã bổ sung các trường phuongThucThanhToan và trangThaiDonHang).
 */
public class HoaDonDAO {

    /**
     * Lấy toàn bộ danh sách Hóa đơn, kèm cả hai cột mới.
     */
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT idHD, thoiGian, idNV, idKH, tongTien, phuongThucThanhToan, trangThaiDonHang " +
                     "  FROM HoaDon";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setIdHD(rs.getString("idHD"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdKH(rs.getString("idKH"));
                hd.setTongTien(rs.getDouble("tongTien"));
                hd.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                hd.setTrangThaiDonHang(rs.getString("trangThaiDonHang"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Tìm kiếm Hóa đơn theo idHD, idNV hoặc idKH.
     * Nếu cả ba tham số đều rỗng, trả về toàn bộ.
     */
    public List<HoaDon> searchHoaDon(String idHD, String idNV, String idKH) {
        List<HoaDon> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idHD, thoiGian, idNV, idKH, tongTien, phuongThucThanhToan, trangThaiDonHang " +
            "  FROM HoaDon WHERE 1=1"
        );
        if (idHD != null && !idHD.trim().isEmpty()) {
            sql.append(" AND idHD LIKE ?");
        }
        if (idNV != null && !idNV.trim().isEmpty()) {
            sql.append(" AND idNV LIKE ?");
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
            if (idHD != null && !idHD.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idHD.trim() + "%");
            }
            if (idNV != null && !idNV.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNV.trim() + "%");
            }
            if (idKH != null && !idKH.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idKH.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setIdHD(rs.getString("idHD"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdKH(rs.getString("idKH"));
                hd.setTongTien(rs.getDouble("tongTien"));
                hd.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                hd.setTrangThaiDonHang(rs.getString("trangThaiDonHang"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Thêm mới một Hóa đơn (kèm 2 trường mới).
     */
    public boolean insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon " +
                     "(idHD, thoiGian, idNV, idKH, tongTien, phuongThucThanhToan, trangThaiDonHang) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, hd.getIdHD());
            stmt.setTimestamp(2, new java.sql.Timestamp(hd.getThoiGian().getTime()));
            stmt.setString(3, hd.getIdNV());
            stmt.setString(4, hd.getIdKH());
            stmt.setDouble(5, hd.getTongTien());
            if (hd.getPhuongThucThanhToan() != null) {
                stmt.setString(6, hd.getPhuongThucThanhToan());
            } else {
                stmt.setNull(6, Types.NVARCHAR);
            }
            stmt.setString(7, hd.getTrangThaiDonHang());
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
     * Cập nhật Hóa đơn (theo idHD), bao gồm 2 trường mới.
     */
    public boolean updateHoaDon(HoaDon hd) {
        String sql = "UPDATE HoaDon SET " +
                     "thoiGian = ?, idNV = ?, idKH = ?, tongTien = ?, " +
                     "phuongThucThanhToan = ?, trangThaiDonHang = ? " +
                     "WHERE idHD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(hd.getThoiGian().getTime()));
            stmt.setString(2, hd.getIdNV());
            stmt.setString(3, hd.getIdKH());
            stmt.setDouble(4, hd.getTongTien());
            if (hd.getPhuongThucThanhToan() != null) {
                stmt.setString(5, hd.getPhuongThucThanhToan());
            } else {
                stmt.setNull(5, Types.NVARCHAR);
            }
            stmt.setString(6, hd.getTrangThaiDonHang());
            stmt.setString(7, hd.getIdHD());
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
     * Xóa Hóa đơn theo idHD.
     */
    public boolean deleteHoaDon(String idHD) {
        String sql = "DELETE FROM HoaDon WHERE idHD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHD);
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
