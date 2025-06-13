package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.Thuoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThuocDAO {

    // Lấy toàn bộ danh sách thuốc còn hiệu lực (chưa bị xóa mềm)
    public List<Thuoc> getAllThuoc() {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT idThuoc, tenThuoc, hinhAnh, thanhPhan, donViTinh, danhMuc, xuatXu, " +
                     "soLuongTon, giaNhap, donGia, hanSuDung " +
                     "FROM Thuoc WHERE isDeleted = 0";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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
        }
        return list;
    }

    // Tìm kiếm thuốc theo ID hoặc tên (lọc ra thuốc chưa bị xóa)
    public List<Thuoc> searchThuoc(String idThuoc, String tenThuoc) {
        List<Thuoc> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idThuoc, tenThuoc, hinhAnh, thanhPhan, donViTinh, danhMuc, xuatXu, " +
            "soLuongTon, giaNhap, donGia, hanSuDung " +
            "FROM Thuoc WHERE isDeleted = 0"
        );
        if (idThuoc != null && !idThuoc.trim().isEmpty()) {
            sql.append(" AND idThuoc LIKE ?");
        }
        if (tenThuoc != null && !tenThuoc.trim().isEmpty()) {
            sql.append(" AND tenThuoc LIKE ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (idThuoc != null && !idThuoc.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idThuoc.trim() + "%");
            }
            if (tenThuoc != null && !tenThuoc.trim().isEmpty()) {
                stmt.setString(idx++, "%" + tenThuoc.trim() + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm thuốc mới
    public boolean insertThuoc(Thuoc t) {
        String sql = "INSERT INTO Thuoc " +
                     "(idThuoc, tenThuoc, hinhAnh, thanhPhan, donViTinh, danhMuc, xuatXu, " +
                     "soLuongTon, giaNhap, donGia, hanSuDung, isDeleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thuốc
    public boolean updateThuoc(Thuoc t) {
        String sql = "UPDATE Thuoc SET " +
                     "tenThuoc = ?, hinhAnh = ?, thanhPhan = ?, donViTinh = ?, danhMuc = ?, xuatXu = ?, " +
                     "soLuongTon = ?, giaNhap = ?, donGia = ?, hanSuDung = ? " +
                     "WHERE idThuoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa mềm thuốc (isDeleted = 1)
    public boolean deleteThuoc(String idThuoc) {
        String sql = "UPDATE Thuoc SET isDeleted = 1 WHERE idThuoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idThuoc);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa mềm thuốc: " + e.getMessage());
        }
    }

    // Phục hồi thuốc đã bị xóa mềm (isDeleted = 0)
    public boolean restoreThuoc(String idThuoc) {
        String sql = "UPDATE Thuoc SET isDeleted = 0 WHERE idThuoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idThuoc);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}