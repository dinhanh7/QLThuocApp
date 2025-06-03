// ThuocDAO.java 
package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.Thuoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThuocDAO {

    // Lấy tất cả Thuốc
    public List<Thuoc> getAll() {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT idThuoc, tenThuoc, hinhAnh, thanhPhan, idDVT, idDM, idXX, soLuongTon, giaNhap, donGia, hanSuDung FROM Thuoc";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setIdThuoc(rs.getString("idThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setHinhAnh(rs.getBytes("hinhAnh"));
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

    // Lấy Thuốc theo ID
    public Thuoc getById(String idThuoc) {
        Thuoc t = null;
        String sql = "SELECT * FROM Thuoc WHERE idThuoc = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idThuoc);
            rs = stmt.executeQuery();
            if (rs.next()) {
                t = new Thuoc();
                t.setIdThuoc(rs.getString("idThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setHinhAnh(rs.getBytes("hinhAnh"));
                t.setThanhPhan(rs.getString("thanhPhan"));
                t.setIdDVT(rs.getString("idDVT"));
                t.setIdDM(rs.getString("idDM"));
                t.setIdXX(rs.getString("idXX"));
                t.setSoLuongTon(rs.getInt("soLuongTon"));
                t.setGiaNhap(rs.getDouble("giaNhap"));
                t.setDonGia(rs.getDouble("donGia"));
                t.setHanSuDung(rs.getDate("hanSuDung"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return t;
    }

    // Thêm mới Thuốc
    public boolean insert(Thuoc thuoc) {
        String sql = "INSERT INTO Thuoc (idThuoc, tenThuoc, hinhAnh, thanhPhan, idDVT, idDM, idXX, soLuongTon, giaNhap, donGia, hanSuDung) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, thuoc.getIdThuoc());
            stmt.setString(2, thuoc.getTenThuoc());
            stmt.setBytes(3, thuoc.getHinhAnh());
            stmt.setString(4, thuoc.getThanhPhan());
            stmt.setString(5, thuoc.getIdDVT());
            stmt.setString(6, thuoc.getIdDM());
            stmt.setString(7, thuoc.getIdXX());
            stmt.setInt(8, thuoc.getSoLuongTon());
            stmt.setDouble(9, thuoc.getGiaNhap());
            stmt.setDouble(10, thuoc.getDonGia());
            stmt.setDate(11, new java.sql.Date(thuoc.getHanSuDung().getTime()));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật Thuốc
    public boolean update(Thuoc thuoc) {
        String sql = "UPDATE Thuoc SET tenThuoc = ?, hinhAnh = ?, thanhPhan = ?, idDVT = ?, idDM = ?, idXX = ?, soLuongTon = ?, giaNhap = ?, donGia = ?, hanSuDung = ? "
                   + "WHERE idThuoc = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, thuoc.getTenThuoc());
            stmt.setBytes(2, thuoc.getHinhAnh());
            stmt.setString(3, thuoc.getThanhPhan());
            stmt.setString(4, thuoc.getIdDVT());
            stmt.setString(5, thuoc.getIdDM());
            stmt.setString(6, thuoc.getIdXX());
            stmt.setInt(7, thuoc.getSoLuongTon());
            stmt.setDouble(8, thuoc.getGiaNhap());
            stmt.setDouble(9, thuoc.getDonGia());
            stmt.setDate(10, new java.sql.Date(thuoc.getHanSuDung().getTime()));
            stmt.setString(11, thuoc.getIdThuoc());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa Thuốc
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
}
