package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.PhanHoi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhanHoiDAO {

    // Lấy tất cả Phản hồi
    public List<PhanHoi> getAll() {
        List<PhanHoi> list = new ArrayList<>();
        String sql = "SELECT idPH, idKH, idHD, idThuoc, noiDung, thoiGian, danhGia FROM PhanHoi";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhanHoi ph = new PhanHoi();
                ph.setIdPH(rs.getString("idPH"));
                ph.setIdKH(rs.getString("idKH"));
                ph.setIdHD(rs.getString("idHD"));
                ph.setIdThuoc(rs.getString("idThuoc"));
                ph.setNoiDung(rs.getString("noiDung"));
                ph.setThoiGian(rs.getTimestamp("thoiGian"));
                int dg = rs.getInt("danhGia");
                ph.setDanhGia(rs.wasNull() ? null : dg);
                list.add(ph);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Lấy Phản hồi theo ID
    public PhanHoi getById(String idPH) {
        PhanHoi ph = null;
        String sql = "SELECT * FROM PhanHoi WHERE idPH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPH);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ph = new PhanHoi();
                ph.setIdPH(rs.getString("idPH"));
                ph.setIdKH(rs.getString("idKH"));
                ph.setIdHD(rs.getString("idHD"));
                ph.setIdThuoc(rs.getString("idThuoc"));
                ph.setNoiDung(rs.getString("noiDung"));
                ph.setThoiGian(rs.getTimestamp("thoiGian"));
                int dg = rs.getInt("danhGia");
                ph.setDanhGia(rs.wasNull() ? null : dg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return ph;
    }

    // Thêm mới Phản hồi
    public boolean insert(PhanHoi ph) {
        String sql = "INSERT INTO PhanHoi (idPH, idKH, idHD, idThuoc, noiDung, thoiGian, danhGia) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ph.getIdPH());
            stmt.setString(2, ph.getIdKH());
            stmt.setString(3, ph.getIdHD());
            stmt.setString(4, ph.getIdThuoc());
            stmt.setString(5, ph.getNoiDung());
            stmt.setTimestamp(6, new java.sql.Timestamp(ph.getThoiGian().getTime()));
            if (ph.getDanhGia() != null) {
                stmt.setInt(7, ph.getDanhGia());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật Phản hồi
    public boolean update(PhanHoi ph) {
        String sql = "UPDATE PhanHoi SET idKH = ?, idHD = ?, idThuoc = ?, noiDung = ?, thoiGian = ?, danhGia = ? WHERE idPH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ph.getIdKH());
            stmt.setString(2, ph.getIdHD());
            stmt.setString(3, ph.getIdThuoc());
            stmt.setString(4, ph.getNoiDung());
            stmt.setTimestamp(5, new java.sql.Timestamp(ph.getThoiGian().getTime()));
            if (ph.getDanhGia() != null) {
                stmt.setInt(6, ph.getDanhGia());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setString(7, ph.getIdPH());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Xóa Phản hồi
    public boolean delete(String idPH) {
        String sql = "DELETE FROM PhanHoi WHERE idPH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPH);
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
// PhanHoiDAO.java 
