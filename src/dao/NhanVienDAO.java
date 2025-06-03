package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NhanVienDAO.java
 *
 * CRUD cơ bản cho bảng NhanVien,
 * và bổ sung thêm phương thức search(String idNV, String hoTen).
 */
public class NhanVienDAO {

    /**
     * Lấy toàn bộ danh sách NhanVien.
     */
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT idNV, hoTen, sdt, gioiTinh, namSinh, ngayVaoLam FROM NhanVien";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setIdNV(rs.getString("idNV"));
                nv.setHoTen(rs.getString("hoTen"));
                nv.setSdt(rs.getString("sdt"));
                nv.setGioiTinh(rs.getString("gioiTinh"));
                nv.setNamSinh(rs.getInt("namSinh"));
                nv.setNgayVaoLam(rs.getDate("ngayVaoLam"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Thêm mới Nhân viên.
     */
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (idNV, hoTen, sdt, gioiTinh, namSinh, ngayVaoLam) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nv.getIdNV());
            stmt.setString(2, nv.getHoTen());
            stmt.setString(3, nv.getSdt());
            stmt.setString(4, nv.getGioiTinh());
            stmt.setInt(5, nv.getNamSinh());
            stmt.setDate(6, new java.sql.Date(nv.getNgayVaoLam().getTime()));
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
     * Cập nhật Nhân viên.
     */
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET hoTen = ?, sdt = ?, gioiTinh = ?, namSinh = ?, ngayVaoLam = ? " +
                     "WHERE idNV = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nv.getHoTen());
            stmt.setString(2, nv.getSdt());
            stmt.setString(3, nv.getGioiTinh());
            stmt.setInt(4, nv.getNamSinh());
            stmt.setDate(5, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            stmt.setString(6, nv.getIdNV());
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
     * Xóa Nhân viên theo idNV.
     */
    public boolean delete(String idNV) {
        String sql = "DELETE FROM NhanVien WHERE idNV = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idNV);
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
     * Tìm kiếm Nhân viên theo idNV hoặc hoTen (hoặc cả hai).
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<NhanVien> search(String idNV, String hoTen) {
        List<NhanVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idNV, hoTen, sdt, gioiTinh, namSinh, ngayVaoLam FROM NhanVien WHERE 1=1"
        );

        if (idNV != null && !idNV.trim().isEmpty()) {
            sql.append(" AND idNV LIKE ?");
        }
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND hoTen LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (idNV != null && !idNV.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNV.trim() + "%");
            }
            if (hoTen != null && !hoTen.trim().isEmpty()) {
                stmt.setString(idx++, "%" + hoTen.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setIdNV(rs.getString("idNV"));
                nv.setHoTen(rs.getString("hoTen"));
                nv.setSdt(rs.getString("sdt"));
                nv.setGioiTinh(rs.getString("gioiTinh"));
                nv.setNamSinh(rs.getInt("namSinh"));
                nv.setNgayVaoLam(rs.getDate("ngayVaoLam"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }
}
