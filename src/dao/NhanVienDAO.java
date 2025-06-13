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
 * CRUD cho bảng NhanVien, bây giờ đã có thêm hai cột luong và trangThai.
 * Đồng thời vẫn quản lý luôn bảng TaiKhoan như trước (LEFT JOIN để lấy username/password).
 */
public class NhanVienDAO {

    /**
     * Lấy tất cả NhanVien (kèm cột luong, trangThai và LEFT JOIN TaiKhoan để lấy username/password).
     */
	public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT n.idNV, n.hoTen, n.sdt, n.gioiTinh, n.namSinh, n.ngayVaoLam, "
                   + "n.luong, n.trangThai, n.isDeleted, t.username, t.password "
                   + "FROM NhanVien n LEFT JOIN TaiKhoan t ON n.idNV = t.idNV "
                   + "WHERE (n.isDeleted IS NULL OR n.isDeleted = 0)";
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
                nv.setLuong(rs.getString("luong"));
                nv.setTrangThai(rs.getString("trangThai"));
                nv.setUsername(rs.getString("username"));
                nv.setPassword(rs.getString("password"));
                // Lấy luôn isDeleted nếu cần (không nhất thiết)
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
     * Tìm kiếm NhanVien theo idNV hoặc hoTen (kèm cả luong, trangThai và username/password).
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ.
     */
	public List<NhanVien> search(String idNV, String hoTen) {
        List<NhanVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT n.idNV, n.hoTen, n.sdt, n.gioiTinh, n.namSinh, n.ngayVaoLam, "
            + "n.luong, n.trangThai, n.isDeleted, t.username, t.password "
            + "FROM NhanVien n LEFT JOIN TaiKhoan t ON n.idNV = t.idNV "
            + "WHERE (n.isDeleted IS NULL OR n.isDeleted = 0)"
        );
        if (idNV != null && !idNV.trim().isEmpty()) {
            sql.append(" AND n.idNV LIKE ?");
        }
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND n.hoTen LIKE ?");
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
                nv.setLuong(rs.getString("luong"));
                nv.setTrangThai(rs.getString("trangThai"));
                nv.setUsername(rs.getString("username"));
                nv.setPassword(rs.getString("password"));
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
     * Thêm mới NhanVien (kèm cả hai cột luong và trangThai) và thêm TaiKhoan nếu có username.
     */
    public boolean insert(NhanVien nv) {
        Connection conn = null;
        PreparedStatement stmtNV = null;
        PreparedStatement stmtTK = null;
        String sqlNV = "INSERT INTO NhanVien " +
                       "(idNV, hoTen, sdt, gioiTinh, namSinh, ngayVaoLam, luong, trangThai) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlTK = "INSERT INTO TaiKhoan (idTK, username, password, idNV, idVT) " +
                       "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            stmtNV = conn.prepareStatement(sqlNV);
            stmtNV.setString(1, nv.getIdNV());
            stmtNV.setString(2, nv.getHoTen());
            stmtNV.setString(3, nv.getSdt());
            stmtNV.setString(4, nv.getGioiTinh());
            stmtNV.setInt(5, nv.getNamSinh());
            stmtNV.setDate(6, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            stmtNV.setString(7, nv.getLuong());
            stmtNV.setString(8, nv.getTrangThai());
            int rowsNV = stmtNV.executeUpdate();

            int rowsTK = 1;
            if (nv.getUsername() != null && !nv.getUsername().trim().isEmpty()) {
                stmtTK = conn.prepareStatement(sqlTK);
                String idTK = "TK" + nv.getIdNV();
                stmtTK.setString(1, idTK);
                stmtTK.setString(2, nv.getUsername());
                stmtTK.setString(3, nv.getPassword());
                stmtTK.setString(4, nv.getIdNV());
                stmtTK.setString(5, nv.getRoleId() != null ? nv.getRoleId() : "VT02");
                rowsTK = stmtTK.executeUpdate();
            }

            conn.commit();
            return rowsNV > 0 && rowsTK > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            DBCloseHelper.closeAll(stmtTK, null);
            DBCloseHelper.closeAll(null, stmtNV, conn);
        }
    }

    /**
     * Cập nhật NhanVien (kèm luong, trangThai) và TaiKhoan nếu có thay đổi.
     */
    public boolean update(NhanVien nv) {
        Connection conn = null;
        PreparedStatement stmtNV = null;
        PreparedStatement stmtTKcheck = null;
        PreparedStatement stmtTKup = null;
        PreparedStatement stmtTKin = null;

        String sqlNV = "UPDATE NhanVien SET " +
                       "hoTen = ?, sdt = ?, gioiTinh = ?, namSinh = ?, ngayVaoLam = ?, " +
                       "luong = ?, trangThai = ? " +
                       "WHERE idNV = ?";
        String sqlTKcheck = "SELECT idTK FROM TaiKhoan WHERE idNV = ?";
        String sqlTKup = "UPDATE TaiKhoan SET username = ?, password = ?, idVT = ? WHERE idNV = ?";
        String sqlTKin = "INSERT INTO TaiKhoan (idTK, username, password, idNV, idVT) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1) Cập nhật NhanVien
            stmtNV = conn.prepareStatement(sqlNV);
            stmtNV.setString(1, nv.getHoTen());
            stmtNV.setString(2, nv.getSdt());
            stmtNV.setString(3, nv.getGioiTinh());
            stmtNV.setInt(4, nv.getNamSinh());
            stmtNV.setDate(5, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            stmtNV.setString(6, nv.getLuong());
            stmtNV.setString(7, nv.getTrangThai());
            stmtNV.setString(8, nv.getIdNV());
            int rowsNV = stmtNV.executeUpdate();

            // 2) Kiểm tra TaiKhoan
            stmtTKcheck = conn.prepareStatement(sqlTKcheck);
            stmtTKcheck.setString(1, nv.getIdNV());
            ResultSet rs = stmtTKcheck.executeQuery();
            boolean existsTK = rs.next();
            rs.close();

            int rowsTK = 0;
            if (nv.getUsername() != null && !nv.getUsername().trim().isEmpty()) {
                if (existsTK) {
                    stmtTKup = conn.prepareStatement(sqlTKup);
                    stmtTKup.setString(1, nv.getUsername());
                    stmtTKup.setString(2, nv.getPassword());
                    stmtTKup.setString(3, nv.getRoleId() != null ? nv.getRoleId() : "VT02");
                    stmtTKup.setString(4, nv.getIdNV());
                    rowsTK = stmtTKup.executeUpdate();
                } else {
                    stmtTKin = conn.prepareStatement(sqlTKin);
                    String idTK = "TK" + nv.getIdNV();
                    stmtTKin.setString(1, idTK);
                    stmtTKin.setString(2, nv.getUsername());
                    stmtTKin.setString(3, nv.getPassword());
                    stmtTKin.setString(4, nv.getIdNV());
                    stmtTKin.setString(5, nv.getRoleId() != null ? nv.getRoleId() : "VT02");
                    rowsTK = stmtTKin.executeUpdate();
                }
            } else {
                // Nếu xóa username (để trống), xóa luôn TaiKhoan nếu có
                if (existsTK) {
                    stmtTKup = conn.prepareStatement("DELETE FROM TaiKhoan WHERE idNV = ?");
                    stmtTKup.setString(1, nv.getIdNV());
                    rowsTK = stmtTKup.executeUpdate();
                } else {
                    rowsTK = 1; // không có tài khoản, coi như thành công
                }
            }

            conn.commit();
            return rowsNV > 0 && rowsTK >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            DBCloseHelper.closeAll(null, stmtTKin, null);
            DBCloseHelper.closeAll(null, stmtTKup, null);
            DBCloseHelper.closeAll(null, stmtTKcheck, null);
            DBCloseHelper.closeAll(null, stmtNV, conn);
        }
    }

    /**
     * Xóa NhanVien (và luôn xóa TaiKhoan nếu có).
     */
    public boolean delete(String idNV) {
        String sql = "UPDATE NhanVien SET isDeleted = 1 WHERE idNV = ?";
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
}
