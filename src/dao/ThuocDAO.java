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
 * Chứa các phương thức CRUD cơ bản cho bảng Thuoc,
 * và bổ sung thêm phương thức search() để tìm thuốc theo id hoặc tên.
 */
public class ThuocDAO {

    /**
     * Lấy toàn bộ danh sách Thuoc.
     */
    public List<Thuoc> getAll() {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT idThuoc, tenThuoc, thanhPhan, idDVT, idDM, idXX, soLuongTon, giaNhap, donGia, hanSuDung " +
                     "FROM Thuoc";
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
                t.setThanhPhan(rs.getString("thanhPhan"));
                t.setIdDVT(rs.getString("idDVT"));
                t.setIdDM(rs.getString("idDM"));
                t.setIdXX(rs.getString("idXX"));
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
     * Thêm mới Thuoc.
     */
    public boolean insert(Thuoc t) {
        String sql = "INSERT INTO Thuoc " +
                     "(idThuoc, tenThuoc, thanhPhan, idDVT, idDM, idXX, soLuongTon, giaNhap, donGia, hanSuDung) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, t.getIdThuoc());
            stmt.setString(2, t.getTenThuoc());
            stmt.setString(3, t.getThanhPhan());
            stmt.setString(4, t.getIdDVT());
            stmt.setString(5, t.getIdDM());
            stmt.setString(6, t.getIdXX());
            stmt.setInt(7, t.getSoLuongTon());
            stmt.setDouble(8, t.getGiaNhap());
            stmt.setDouble(9, t.getDonGia());
            stmt.setDate(10, new java.sql.Date(t.getHanSuDung().getTime()));
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
     * Cập nhật Thuoc.
     */
    public boolean update(Thuoc t) {
        String sql = "UPDATE Thuoc SET tenThuoc = ?, thanhPhan = ?, idDVT = ?, idDM = ?, idXX = ?, " +
                     "soLuongTon = ?, giaNhap = ?, donGia = ?, hanSuDung = ? " +
                     "WHERE idThuoc = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, t.getTenThuoc());
            stmt.setString(2, t.getThanhPhan());
            stmt.setString(3, t.getIdDVT());
            stmt.setString(4, t.getIdDM());
            stmt.setString(5, t.getIdXX());
            stmt.setInt(6, t.getSoLuongTon());
            stmt.setDouble(7, t.getGiaNhap());
            stmt.setDouble(8, t.getDonGia());
            stmt.setDate(9, new java.sql.Date(t.getHanSuDung().getTime()));
            stmt.setString(10, t.getIdThuoc());
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
     * Xóa Thuoc theo idThuoc.
     */
    public boolean delete(String idThuoc) {
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
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    /**
     * Tìm kiếm Thuoc theo idThuoc hoặc tenThuoc (khi 1 trong 2 trường có giá trị).
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách (tương đương getAll()).
     */
    public List<Thuoc> search(String idThuoc, String tenThuoc) {
        List<Thuoc> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT idThuoc, tenThuoc, thanhPhan, idDVT, idDM, idXX, soLuongTon, giaNhap, donGia, hanSuDung " +
                "FROM Thuoc WHERE 1=1"
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

            int paramIndex = 1;
            if (idThuoc != null && !idThuoc.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + idThuoc.trim() + "%");
            }
            if (tenThuoc != null && !tenThuoc.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + tenThuoc.trim() + "%");
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setIdThuoc(rs.getString("idThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setThanhPhan(rs.getString("thanhPhan"));
                t.setIdDVT(rs.getString("idDVT"));
                t.setIdDM(rs.getString("idDM"));
                t.setIdXX(rs.getString("idXX"));
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
}
