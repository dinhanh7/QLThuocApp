package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.HopDong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HopDongDAO.java
 *
 * CRUD cơ bản cho bảng HopDong,
 * và bổ sung thêm phương thức search(String idHDong, String idNV, String idNCC).
 */
public class HopDongDAO {

    /**
     * Lấy toàn bộ danh sách HopDong.
     */
    public List<HopDong> getAll() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT idHDong, ngayBatDau, ngayKetThuc, noiDung, idNV, idNCC FROM HopDong";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
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

    /**
     * Thêm mới HopDong.
     */
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
            stmt.setString(5, hd.getIdNV());
            stmt.setString(6, hd.getIdNCC());
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
     * Cập nhật HopDong.
     */
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
            stmt.setString(4, hd.getIdNV());
            stmt.setString(5, hd.getIdNCC());
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

    /**
     * Xóa HopDong theo idHDong.
     */
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

    /**
     * Tìm kiếm HopDong theo idHDong hoặc idNV hoặc idNCC.
     * Nếu cả ba tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<HopDong> search(String idHDong, String idNV, String idNCC) {
        List<HopDong> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idHDong, ngayBatDau, ngayKetThuc, noiDung, idNV, idNCC FROM HopDong WHERE 1=1"
        );

        if (idHDong != null && !idHDong.trim().isEmpty()) {
            sql.append(" AND idHDong LIKE ?");
        }
        if (idNV != null && !idNV.trim().isEmpty()) {
            sql.append(" AND idNV LIKE ?");
        }
        if (idNCC != null && !idNCC.trim().isEmpty()) {
            sql.append(" AND idNCC LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (idHDong != null && !idHDong.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idHDong.trim() + "%");
            }
            if (idNV != null && !idNV.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNV.trim() + "%");
            }
            if (idNCC != null && !idNCC.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNCC.trim() + "%");
            }
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
}
