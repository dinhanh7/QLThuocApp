package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * KhachHangDAO.java
 *
 * CRUD cơ bản cho bảng KhachHang,
 * và bổ sung thêm phương thức search(String hoTen, String sdt).
 */
public class KhachHangDAO {
    // sửa từ 19 - 202
    /**
     * Lấy toàn bộ danh sách KhachHang.
     */
	public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT idKH, hoTen, sdt, gioiTinh, ngayThamGia FROM KhachHang WHERE (isDeleted IS NULL OR isDeleted = 0)";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setIdKH(rs.getString("idKH"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSdt(rs.getString("sdt"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNgayThamGia(rs.getDate("ngayThamGia"));
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Thêm mới KhachHang.
     */
	public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (idKH, hoTen, sdt, gioiTinh, ngayThamGia, isDeleted) VALUES (?, ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, kh.getIdKH());
            stmt.setString(2, kh.getHoTen());
            stmt.setString(3, kh.getSdt());
            stmt.setString(4, kh.getGioiTinh());
            stmt.setDate(5, new java.sql.Date(kh.getNgayThamGia().getTime()));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw new RuntimeException("ID khách hàng đã tồn tại!");
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi thêm khách hàng: " + e.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    /**
     * Cập nhật KhachHang.
     */
	public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET hoTen = ?, sdt = ?, gioiTinh = ?, ngayThamGia = ? WHERE idKH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, kh.getHoTen());
            stmt.setString(2, kh.getSdt());
            stmt.setString(3, kh.getGioiTinh());
            stmt.setDate(4, new java.sql.Date(kh.getNgayThamGia().getTime()));
            stmt.setString(5, kh.getIdKH());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi cập nhật khách hàng: " + e.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    /**
     * Xóa KhachHang theo idKH.
     */
	public boolean delete(String idKH) {
        String sql = "UPDATE KhachHang SET isDeleted = 1 WHERE idKH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idKH);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) {
                throw new RuntimeException("Không thể xóa vì khách hàng đã có hóa đơn liên quan!");
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi xóa khách hàng: " + e.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    /**
     * Tìm kiếm Khách hàng theo hoTen hoặc sdt (hoặc cả hai).
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
	public List<KhachHang> search(String hoTen, String sdt) {
        List<KhachHang> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idKH, hoTen, sdt, gioiTinh, ngayThamGia FROM KhachHang WHERE (isDeleted IS NULL OR isDeleted = 0)"
        );
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND hoTen LIKE ?");
        }
        if (sdt != null && !sdt.trim().isEmpty()) {
            sql.append(" AND sdt LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (hoTen != null && !hoTen.trim().isEmpty()) {
                stmt.setString(idx++, "%" + hoTen.trim() + "%");
            }
            if (sdt != null && !sdt.trim().isEmpty()) {
                stmt.setString(idx++, "%" + sdt.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setIdKH(rs.getString("idKH"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSdt(rs.getString("sdt"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNgayThamGia(rs.getDate("ngayThamGia"));
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }


    /**
     * Lấy KhachHang theo SĐT (đã có sẵn):
     */
	public KhachHang getBySDT(String sdt) {
        String sql = "SELECT idKH, hoTen, sdt, gioiTinh, ngayThamGia FROM KhachHang WHERE sdt = ? AND (isDeleted IS NULL OR isDeleted = 0)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sdt);
            rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setIdKH(rs.getString("idKH"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSdt(rs.getString("sdt"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setNgayThamGia(rs.getDate("ngayThamGia"));
                return kh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return null;
    }
    public boolean updateDiemTichLuy(String idKH, int diemMoi) {
        String sql = "UPDATE KhachHang SET diemTichLuy = ? WHERE idKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, diemMoi);
            stmt.setString(2, idKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean congDiem(String idKH, int soDiemCong) {
        String sql = "UPDATE KhachHang SET diemTichLuy = diemTichLuy + ? WHERE idKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, soDiemCong);
            stmt.setString(2, idKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean truDiem(String idKH, int soDiemTru) {
        String sql = "UPDATE KhachHang SET diemTichLuy = diemTichLuy - ? WHERE idKH = ? AND diemTichLuy >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, soDiemTru);
            stmt.setString(2, idKH);
            stmt.setInt(3, soDiemTru);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public KhachHang getById(String idKH) {
        String sql = "SELECT * FROM KhachHang WHERE idKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idKH);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setIdKH(rs.getString("idKH"));
                    kh.setHoTen(rs.getString("hoTen"));
                    kh.setSdt(rs.getString("sdt"));
                    // ... các trường khác của KhachHang ...
                    kh.setDiemTichLuy(rs.getInt("diemTichLuy")); // nhớ lấy trường điểm tích lũy!
                    return kh;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
