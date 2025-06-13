package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.Thuoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ThuocDAO.java
 *
 * CRUD cho bảng dbo.Thuoc (đã sửa để dùng trực tiếp các trường donViTinh, danhMuc, xuatXu).
 */
public class ThuocDAO {

    /**
     * Lấy toàn bộ danh sách Thuốc.
     */
    public List<Thuoc> getAllThuoc() {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT idThuoc, tenThuoc, hinhAnh, thanhPhan, donViTinh, danhMuc, xuatXu, " +
                     "       soLuongTon, giaNhap, donGia, hanSuDung " +
                     "  FROM Thuoc";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setIdThuoc(rs.getString("idThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setHinhAnh(rs.getBytes("hinhAnh"));        // có thể null
                t.setThanhPhan(rs.getString("thanhPhan"));    // có thể null
                t.setDonViTinh(rs.getString("donViTinh"));
                t.setDanhMuc(rs.getString("danhMuc"));
                t.setXuatXu(rs.getString("xuatXu"));
                t.setSoLuongTon(rs.getInt("soLuongTon"));
                t.setGiaNhap(rs.getDouble("giaNhap"));
                t.setDonGia(rs.getDouble("donGia"));
                t.setHanSuDung(rs.getDate("hanSuDung"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Tìm kiếm Thuốc theo idThuoc hoặc tenThuoc.
     * Nếu cả hai tham số rỗng, trả về toàn bộ.
     */
    public List<Thuoc> searchThuoc(String idThuoc, String tenThuoc) {
        List<Thuoc> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idThuoc, tenThuoc, hinhAnh, thanhPhan, donViTinh, danhMuc, xuatXu, " +
            "       soLuongTon, giaNhap, donGia, hanSuDung " +
            "  FROM Thuoc " +
            " WHERE 1=1"
        );
        if (idThuoc != null && !idThuoc.trim().isEmpty()) {
            sql.append(" AND idThuoc LIKE ?");
        }
        if (tenThuoc != null && !tenThuoc.trim().isEmpty()) {
            sql.append(" AND tenThuoc LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (idThuoc != null && !idThuoc.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idThuoc.trim() + "%");
            }
            if (tenThuoc != null && !tenThuoc.trim().isEmpty()) {
                stmt.setString(idx++, "%" + tenThuoc.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setIdThuoc(rs.getString("idThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setHinhAnh(rs.getBytes("hinhAnh"));
                t.setThanhPhan(rs.getString("thanhPhan"));
                t.setDonViTinh(rs.getString("donViTinh"));
                t.setDanhMuc(rs.getString("danhMuc"));
                t.setXuatXu(rs.getString("xuatXu"));
                t.setSoLuongTon(rs.getInt("soLuongTon"));
                t.setGiaNhap(rs.getDouble("giaNhap"));
                t.setDonGia(rs.getDouble("donGia"));
                t.setHanSuDung(rs.getDate("hanSuDung"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Thêm mới một Thuốc.
     */
    public boolean insertThuoc(Thuoc t) {
        String sql = "INSERT INTO Thuoc " +
                     "(idThuoc, tenThuoc, hinhAnh, thanhPhan, donViTinh, danhMuc, xuatXu, " +
                     " soLuongTon, giaNhap, donGia, hanSuDung) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, t.getIdThuoc());
            stmt.setString(2, t.getTenThuoc());
            if (t.getHinhAnh() != null) {
                stmt.setBytes(3, t.getHinhAnh());
            } else {
                stmt.setNull(3, Types.VARBINARY);
            }
            if (t.getThanhPhan() != null) {
                stmt.setString(4, t.getThanhPhan());
            } else {
                stmt.setNull(4, Types.NVARCHAR);
            }
            stmt.setString(5, t.getDonViTinh());
            stmt.setString(6, t.getDanhMuc());
            stmt.setString(7, t.getXuatXu());
            stmt.setInt(8, t.getSoLuongTon());
            stmt.setDouble(9, t.getGiaNhap());
            stmt.setDouble(10, t.getDonGia());
            stmt.setDate(11, new java.sql.Date(t.getHanSuDung().getTime()));

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
     * Cập nhật dữ liệu cho Thuốc (theo idThuoc).
     */
    public boolean updateThuoc(Thuoc t) {
        String sql = "UPDATE Thuoc SET " +
                     "tenThuoc = ?, hinhAnh = ?, thanhPhan = ?, donViTinh = ?, danhMuc = ?, xuatXu = ?, " +
                     "soLuongTon = ?, giaNhap = ?, donGia = ?, hanSuDung = ? " +
                     "WHERE idThuoc = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, t.getTenThuoc());
            if (t.getHinhAnh() != null) {
                stmt.setBytes(2, t.getHinhAnh());
            } else {
                stmt.setNull(2, Types.VARBINARY);
            }
            if (t.getThanhPhan() != null) {
                stmt.setString(3, t.getThanhPhan());
            } else {
                stmt.setNull(3, Types.NVARCHAR);
            }
            stmt.setString(4, t.getDonViTinh());
            stmt.setString(5, t.getDanhMuc());
            stmt.setString(6, t.getXuatXu());
            stmt.setInt(7, t.getSoLuongTon());
            stmt.setDouble(8, t.getGiaNhap());
            stmt.setDouble(9, t.getDonGia());
            stmt.setDate(10, new java.sql.Date(t.getHanSuDung().getTime()));
            stmt.setString(11, t.getIdThuoc());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }


    private boolean isThuocReferenced(String idThuoc) {
        String sql1 = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE idThuoc = ?";
        String sql2 = "SELECT COUNT(*) FROM ChiTietPhieuNhap WHERE idThuoc = ?";
        String sql3 = "SELECT COUNT(*) FROM PhanHoi WHERE idThuoc = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();

            stmt = conn.prepareStatement(sql1);
            stmt.setString(1, idThuoc);
            rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return true;
            rs.close();

            stmt = conn.prepareStatement(sql2);
            stmt.setString(1, idThuoc);
            rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return true;
            rs.close();

            stmt = conn.prepareStatement(sql3);
            stmt.setString(1, idThuoc);
            rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Nếu có lỗi thì giả định có liên kết để không xóa nhầm
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return false;
    }

    // HÀM XÓA THUỐC
    public boolean deleteThuoc(String idThuoc) {
        if (isThuocReferenced(idThuoc)) {
            // Trả về false, báo lỗi ở Controller/GUI
            throw new RuntimeException("Không thể xóa vì thuốc đã có dữ liệu liên quan (Hóa đơn, Phiếu nhập hoặc Phản hồi)!");
        }

        String sql = "DELETE FROM Thuoc WHERE idThuoc = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idThuoc);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi xóa thuốc: " + e.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

}
