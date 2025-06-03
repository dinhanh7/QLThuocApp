package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.HopDong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HopDongDAO {

    // Lấy tất cả Hợp đồng
    public List<HopDong> getAll() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT idHDong, ngayBatDau, ngayKetThuc, noiDung, idNV, idNCC FROM HopDong";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setIdHDong(rs.getString("idHDong"));
                hd.setNgayBatDau(rs.getDate("ngayBatDau"));
                hd.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                hd.setNoiDung(rs.getString("noiDung"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdNCC(rs.getString("idNCC"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Lấy Hợp đồng theo ID
    public HopDong getById(String idHDong) {
        HopDong hd = null;
        String sql = "SELECT * FROM HopDong WHERE idHDong = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHDong);
            rs = stmt.executeQuery();
            if (rs.next()) {
                hd = new HopDong();
                hd.setIdHDong(rs.getString("idHDong"));
                hd.setNgayBatDau(rs.getDate("ngayBatDau"));
                hd.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                hd.setNoiDung(rs.getString("noiDung"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdNCC(rs.getString("idNCC"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return hd;
    }

    // Thêm mới Hợp đồng
    public boolean insert(HopDong hd) {
        String sql = "INSERT INTO HopDong (idHDong, ngayBatDau, ngayKetThuc, noiDung, idNV, idNCC) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, hd.getIdHDong());
            stmt.setDate(2, new java.sql.Date(hd.getNgayBatDau().getTime()));
            stmt.setDate(3, new java.sql.Date(hd.getNgayKetThuc().getTime()));
            stmt.setString(4, hd.getNoiDung());
            // Chỉ thiết lập một trong hai: idNV hoặc idNCC (theo ràng buộc CHECK)
            if (hd.getIdNV() != null) {
                stmt.setString(5, hd.getIdNV());
                stmt.setNull(6, Types.NVARCHAR);
            } else {
                stmt.setNull(5, Types.NVARCHAR);
                stmt.setString(6, hd.getIdNCC());
            }
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật Hợp đồng
    public boolean update(HopDong hd) {
        String sql = "UPDATE HopDong SET ngayBatDau = ?, ngayKetThuc = ?, noiDung = ?, idNV = ?, idNCC = ? WHERE idHDong = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(hd.getNgayBatDau().getTime()));
            stmt.setDate(2, new java.sql.Date(hd.getNgayKetThuc().getTime()));
            stmt.setString(3, hd.getNoiDung());
            if (hd.getIdNV() != null) {
                stmt.setString(4, hd.getIdNV());
                stmt.setNull(5, Types.NVARCHAR);
            } else {
                stmt.setNull(4, Types.NVARCHAR);
                stmt.setString(5, hd.getIdNCC());
            }
            stmt.setString(6, hd.getIdHDong());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa Hợp đồng
    public boolean delete(String idHDong) {
        String sql = "DELETE FROM HopDong WHERE idHDong = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHDong);
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
// HopDongDAO.java 
