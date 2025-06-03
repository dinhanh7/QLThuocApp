package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.PhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    // Lấy tất cả Phiếu nhập
    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT idPN, thoiGian, idNV, idNCC, tongTien FROM PhieuNhap";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setIdPN(rs.getString("idPN"));
                pn.setThoiGian(rs.getTimestamp("thoiGian"));
                pn.setIdNV(rs.getString("idNV"));
                pn.setIdNCC(rs.getString("idNCC"));
                pn.setTongTien(rs.getDouble("tongTien"));
                list.add(pn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Lấy Phiếu nhập theo ID
    public PhieuNhap getById(String idPN) {
        PhieuNhap pn = null;
        String sql = "SELECT * FROM PhieuNhap WHERE idPN = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPN);
            rs = stmt.executeQuery();
            if (rs.next()) {
                pn = new PhieuNhap();
                pn.setIdPN(rs.getString("idPN"));
                pn.setThoiGian(rs.getTimestamp("thoiGian"));
                pn.setIdNV(rs.getString("idNV"));
                pn.setIdNCC(rs.getString("idNCC"));
                pn.setTongTien(rs.getDouble("tongTien"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return pn;
    }

    // Thêm mới Phiếu nhập
    public boolean insert(PhieuNhap pn) {
        String sql = "INSERT INTO PhieuNhap (idPN, thoiGian, idNV, idNCC, tongTien) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pn.getIdPN());
            stmt.setTimestamp(2, new java.sql.Timestamp(pn.getThoiGian().getTime()));
            stmt.setString(3, pn.getIdNV());
            stmt.setString(4, pn.getIdNCC());
            stmt.setDouble(5, pn.getTongTien());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật Phiếu nhập
    public boolean update(PhieuNhap pn) {
        String sql = "UPDATE PhieuNhap SET thoiGian = ?, idNV = ?, idNCC = ?, tongTien = ? WHERE idPN = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(pn.getThoiGian().getTime()));
            stmt.setString(2, pn.getIdNV());
            stmt.setString(3, pn.getIdNCC());
            stmt.setDouble(4, pn.getTongTien());
            stmt.setString(5, pn.getIdPN());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa Phiếu nhập
    public boolean delete(String idPN) {
        String sql = "DELETE FROM PhieuNhap WHERE idPN = ?";
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
}
