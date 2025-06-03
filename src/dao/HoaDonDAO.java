package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // Lấy tất cả Hóa đơn
    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT idHD, thoiGian, idNV, idKH, tongTien FROM HoaDon";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setIdHD(rs.getString("idHD"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdKH(rs.getString("idKH"));
                hd.setTongTien(rs.getDouble("tongTien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Lấy Hóa đơn theo ID
    public HoaDon getById(String idHD) {
        HoaDon hd = null;
        String sql = "SELECT * FROM HoaDon WHERE idHD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHD);
            rs = stmt.executeQuery();
            if (rs.next()) {
                hd = new HoaDon();
                hd.setIdHD(rs.getString("idHD"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdKH(rs.getString("idKH"));
                hd.setTongTien(rs.getDouble("tongTien"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return hd;
    }

    // Thêm mới Hóa đơn
    public boolean insert(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (idHD, thoiGian, idNV, idKH, tongTien) VALUES (?, ?, ?, ?, ?)";
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
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật Hóa đơn
    public boolean update(HoaDon hd) {
        String sql = "UPDATE HoaDon SET thoiGian = ?, idNV = ?, idKH = ?, tongTien = ? WHERE idHD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(hd.getThoiGian().getTime()));
            stmt.setString(2, hd.getIdNV());
            stmt.setString(3, hd.getIdKH());
            stmt.setDouble(4, hd.getTongTien());
            stmt.setString(5, hd.getIdHD());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa Hóa đơn
    public boolean delete(String idHD) {
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
