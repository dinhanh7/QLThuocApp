package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.PhanHoi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhanHoiDAO {

    // Lấy tất cả phản hồi chưa bị xóa mềm
    public List<PhanHoi> getAll() {
        List<PhanHoi> list = new ArrayList<>();
        String sql = "SELECT idPH, idKH, idHD, noiDung, thoiGian, danhGia FROM PhanHoi WHERE (isDeleted IS NULL OR isDeleted = 0)";
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

    // Thêm phản hồi mới (set mặc định isDeleted = 0)
    public boolean insert(PhanHoi ph) {
        String sql = "INSERT INTO PhanHoi (idPH, idKH, idHD, noiDung, thoiGian, danhGia, isDeleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ph.getIdPH());
            stmt.setString(2, ph.getIdKH());
            stmt.setString(3, ph.getIdHD());
            stmt.setString(4, ph.getNoiDung());
            stmt.setTimestamp(5, new java.sql.Timestamp(ph.getThoiGian().getTime()));
            stmt.setInt(6, ph.getDanhGia());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật phản hồi (không cho sửa isDeleted)
    public boolean update(PhanHoi ph) {
        String sql = "UPDATE PhanHoi SET idKH = ?, idHD = ?, noiDung = ?, thoiGian = ?, danhGia = ? WHERE idPH = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ph.getIdKH());
            stmt.setString(2, ph.getIdHD());
            stmt.setString(3, ph.getNoiDung());
            stmt.setTimestamp(4, new java.sql.Timestamp(ph.getThoiGian().getTime()));
            stmt.setInt(5, ph.getDanhGia());
            stmt.setString(6, ph.getIdPH());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // XÓA MỀM: cập nhật isDeleted = 1 thay vì xóa thật
    public boolean delete(String idPH) {
        String sql = "UPDATE PhanHoi SET isDeleted = 1 WHERE idPH = ?";
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

    // Tìm kiếm phản hồi (lọc chưa xóa)
    public List<PhanHoi> search(String idPH, String idKH) {
        List<PhanHoi> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idPH, idKH, idHD, noiDung, thoiGian, danhGia FROM PhanHoi WHERE (isDeleted IS NULL OR isDeleted = 0)"
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
    //setup trash
    public List<PhanHoi> getDeleted() {
        List<PhanHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM PhanHoi WHERE isDeleted = 1";

        try (
        	Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                PhanHoi p = new PhanHoi();
                p.setIdPH(rs.getString("idPH"));
                p.setIdKH(rs.getString("idKH"));
                p.setIdHD(rs.getString("idHD"));
                p.setNoiDung(rs.getString("noiDung"));
                p.setThoiGian(rs.getTimestamp("thoiGian"));
                p.setDanhGia(rs.getInt("danhGia"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list; // ⬅️ ĐÂY LÀ DÒNG QUAN TRỌNG KHÔNG ĐƯỢC THIẾU
    }

    public boolean restore(String idPH) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE PhanHoi SET isDeleted = 0 WHERE idPH = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, idPH);

            int rows = stmt.executeUpdate();
            return rows > 0; // trả về true nếu có dòng bị ảnh hưởng

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false; // nếu có lỗi hoặc không cập nhật gì thì trả false
    }
    public boolean deleteForever(String idPH) {
        String sql = "DELETE FROM PhanHoi WHERE idPH = ?";
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
        ) {
            stmt.setString(1, idPH);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
