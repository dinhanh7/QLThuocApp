// PhieuDatHangDAO.java 
package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.PhieuDatHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatHangDAO {

    // Lấy tất cả Phiếu đặt hàng
    public List<PhieuDatHang> getAll() {
        List<PhieuDatHang> list = new ArrayList<>();
        String sql = "SELECT idPDH, thoiGian, idKH, tongTien, diaChi, phuongThucThanhToan, trangThai FROM PhieuDatHang";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
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

    // Lấy Phiếu đặt hàng theo ID
    public PhieuDatHang getById(String idPDH) {
        PhieuDatHang pdh = null;
        String sql = "SELECT * FROM PhieuDatHang WHERE idPDH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPDH);
            rs = stmt.executeQuery();
            if (rs.next()) {
                pdh = new PhieuDatHang();
                pdh.setIdPDH(rs.getString("idPDH"));
                pdh.setThoiGian(rs.getTimestamp("thoiGian"));
                pdh.setIdKH(rs.getString("idKH"));
                pdh.setTongTien(rs.getDouble("tongTien"));
                pdh.setDiaChi(rs.getString("diaChi"));
                pdh.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                pdh.setTrangThai(rs.getString("trangThai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return pdh;
    }

    // Thêm mới Phiếu đặt hàng
    public boolean insert(PhieuDatHang pdh) {
        String sql = "INSERT INTO PhieuDatHang (idPDH, thoiGian, idKH, tongTien, diaChi, phuongThucThanhToan, trangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pdh.getIdPDH());
            stmt.setTimestamp(2, new java.sql.Timestamp(pdh.getThoiGian().getTime()));
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

    // Cập nhật Phiếu đặt hàng
    public boolean update(PhieuDatHang pdh) {
        String sql = "UPDATE PhieuDatHang SET thoiGian = ?, idKH = ?, tongTien = ?, diaChi = ?, phuongThucThanhToan = ?, trangThai = ? "
                   + "WHERE idPDH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(pdh.getThoiGian().getTime()));
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

    // Xóa Phiếu đặt hàng
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
}
