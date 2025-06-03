package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NhaCungCapDAO.java
 *
 * CRUD cho bảng NhaCungCap, kèm phương thức search(idNCC, tenNCC).
 */
public class NhaCungCapDAO {

    /**
     * Lấy danh sách toàn bộ Nhà cung cấp.
     */
    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT idNCC, tenNCC, sdt, diaChi FROM NhaCungCap";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setIdNCC(rs.getString("idNCC"));
                ncc.setTenNCC(rs.getString("tenNCC"));
                ncc.setSdt(rs.getString("sdt"));
                ncc.setDiaChi(rs.getString("diaChi"));
                list.add(ncc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Thêm mới một Nhà cung cấp.
     */
    public boolean insert(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap (idNCC, tenNCC, sdt, diaChi) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ncc.getIdNCC());
            stmt.setString(2, ncc.getTenNCC());
            stmt.setString(3, ncc.getSdt());
            stmt.setString(4, ncc.getDiaChi());
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
     * Cập nhật dữ liệu Nhà cung cấp theo idNCC.
     */
    public boolean update(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET tenNCC = ?, sdt = ?, diaChi = ? WHERE idNCC = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ncc.getTenNCC());
            stmt.setString(2, ncc.getSdt());
            stmt.setString(3, ncc.getDiaChi());
            stmt.setString(4, ncc.getIdNCC());
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
     * Xóa Nhà cung cấp theo idNCC.
     */
    public boolean delete(String idNCC) {
        String sql = "DELETE FROM NhaCungCap WHERE idNCC = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idNCC);
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
     * Tìm kiếm Nhà cung cấp theo idNCC hoặc tenNCC.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<NhaCungCap> search(String idNCC, String tenNCC) {
        List<NhaCungCap> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idNCC, tenNCC, sdt, diaChi FROM NhaCungCap WHERE 1=1"
        );

        if (idNCC != null && !idNCC.trim().isEmpty()) {
            sql.append(" AND idNCC LIKE ?");
        }
        if (tenNCC != null && !tenNCC.trim().isEmpty()) {
            sql.append(" AND tenNCC LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (idNCC != null && !idNCC.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNCC.trim() + "%");
            }
            if (tenNCC != null && !tenNCC.trim().isEmpty()) {
                stmt.setString(idx++, "%" + tenNCC.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setIdNCC(rs.getString("idNCC"));
                ncc.setTenNCC(rs.getString("tenNCC"));
                ncc.setSdt(rs.getString("sdt"));
                ncc.setDiaChi(rs.getString("diaChi"));
                list.add(ncc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }
}
