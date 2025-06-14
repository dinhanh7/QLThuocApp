package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.PhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PhieuNhapDAO.java
 *
 * CRUD cơ bản cho bảng PhieuNhap,
 * và bổ sung thêm phương thức search(String idPN, String idNV, String idNCC).
 */
public class PhieuNhapDAO {

    /**
     * Lấy toàn bộ danh sách PhieuNhap.
     */
    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT idPN, thoiGian, idNV, idNCC, tongTien FROM PhieuNhap WHERE (isDeleted IS NULL OR isDeleted = 0)";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
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

    /**
     * Thêm mới PhieuNhap.
     */
    public boolean insert(PhieuNhap pn) {
        String sql = "INSERT INTO PhieuNhap (idPN, thoiGian, idNV, idNCC, tongTien) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pn.getIdPN());
            stmt.setTimestamp(2, new Timestamp(pn.getThoiGian().getTime()));
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

    /**
     * Cập nhật PhieuNhap.
     */
    public boolean update(PhieuNhap pn) {
        String sql = "UPDATE PhieuNhap SET thoiGian = ?, idNV = ?, idNCC = ?, tongTien = ? WHERE idPN = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(pn.getThoiGian().getTime()));
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

    /**
     * Xóa PhieuNhap theo idPN.
     */
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

    /**
     * Tìm kiếm PhieuNhap theo idPN hoặc idNV hoặc idNCC.
     * Nếu cả ba tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<PhieuNhap> search(String idPN, String idNV, String idNCC) {
        List<PhieuNhap> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idPN, thoiGian, idNV, idNCC, tongTien FROM PhieuNhap WHERE 1=1"
        );

        if (idPN != null && !idPN.trim().isEmpty()) {
            sql.append(" AND idPN LIKE ?");
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
            if (idPN != null && !idPN.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idPN.trim() + "%");
            }
            if (idNV != null && !idNV.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNV.trim() + "%");
            }
            if (idNCC != null && !idNCC.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNCC.trim() + "%");
            }
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
    
    //them ham xoa mem
    public boolean deletePhieuNhap(String idPN) {
        String sql = "UPDATE PhieuNhap SET isDeleted = 1 WHERE idPN = ?";
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
    
    //ham Tung
 // setup for trash
    public List<PhieuNhap> getDeleted() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap WHERE isDeleted = 1";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setIdPN(rs.getString("idPN"));
                pn.setThoiGian(rs.getTimestamp("thoiGian"));
                pn.setIdNV(rs.getString("idNV"));
                pn.setIdNCC(rs.getString("idNCC"));
                pn.setTongTien(rs.getDouble("tongTien"));
                list.add(pn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public boolean restore(String id) {
        String sql = "UPDATE PhieuNhap SET isDeleted = 0 WHERE idPN = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
        ) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean deleteForever(String id) {
        String sql = "DELETE FROM PhieuNhap WHERE idPN = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
        ) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
