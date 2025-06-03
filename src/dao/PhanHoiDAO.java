package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.PhanHoi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PhanHoiDAO.java
 *
 * CRUD cơ bản cho bảng PhanHoi,
 * và bổ sung thêm phương thức search(String idPH, String idKH).
 */
public class PhanHoiDAO {

    /**
     * Chèn mới 1 record vào PhanHoi.
     */
    public boolean insert(PhanHoi ph) {
        String sql = "INSERT INTO PhanHoi " +
                     "(idPH, idKH, idHD, idThuoc, noiDung, thoiGian, danhGia) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
            stmt.setTimestamp(6, new Timestamp(ph.getThoiGian().getTime()));
            stmt.setInt(7, ph.getDanhGia());
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
     * Cập nhật 1 phản hồi (dành cho admin/nhân viên).
     */
    public boolean update(PhanHoi ph) {
        String sql = "UPDATE PhanHoi SET idKH = ?, idHD = ?, idThuoc = ?, noiDung = ?, thoiGian = ?, danhGia = ? " +
                     "WHERE idPH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ph.getIdKH());
            stmt.setString(2, ph.getIdHD());
            stmt.setString(3, ph.getIdThuoc());
            stmt.setString(4, ph.getNoiDung());
            stmt.setTimestamp(5, new Timestamp(ph.getThoiGian().getTime()));
            stmt.setInt(6, ph.getDanhGia());
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

    /**
     * Xóa phản hồi theo idPH.
     */
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

    /**
     * Lấy toàn bộ danh sách PhanHoi.
     */
    public List<PhanHoi> getAll() {
        List<PhanHoi> list = new ArrayList<>();
        String sql = "SELECT idPH, idKH, idHD, idThuoc, noiDung, thoiGian, danhGia FROM PhanHoi";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PhanHoi ph = new PhanHoi();
                ph.setIdPH(rs.getString("idPH"));
                ph.setIdKH(rs.getString("idKH"));
                ph.setIdHD(rs.getString("idHD"));
                ph.setIdThuoc(rs.getString("idThuoc"));
                ph.setNoiDung(rs.getString("noiDung"));
                ph.setThoiGian(rs.getTimestamp("thoiGian"));
                ph.setDanhGia(rs.getInt("danhGia"));
                list.add(ph);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Tìm kiếm Phản hồi theo idPH hoặc idKH.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<PhanHoi> search(String idPH, String idKH) {
        List<PhanHoi> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idPH, idKH, idHD, idThuoc, noiDung, thoiGian, danhGia FROM PhanHoi WHERE 1=1"
        );

        if (idPH != null && !idPH.trim().isEmpty()) {
            sql.append(" AND idPH LIKE ?");
        }
        if (idKH != null && !idKH.trim().isEmpty()) {
            sql.append(" AND idKH LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (idPH != null && !idPH.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idPH.trim() + "%");
            }
            if (idKH != null && !idKH.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idKH.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhanHoi ph = new PhanHoi();
                ph.setIdPH(rs.getString("idPH"));
                ph.setIdKH(rs.getString("idKH"));
                ph.setIdHD(rs.getString("idHD"));
                ph.setIdThuoc(rs.getString("idThuoc"));
                ph.setNoiDung(rs.getString("noiDung"));
                ph.setThoiGian(rs.getTimestamp("thoiGian"));
                ph.setDanhGia(rs.getInt("danhGia"));
                list.add(ph);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }
}
