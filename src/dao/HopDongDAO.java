package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.HopDong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HopDongDAO {

    // Lấy toàn bộ hợp đồng chưa bị xóa
    public List<HopDong> getAllHopDong() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT idHDong, ngayBatDau, ngayKetThuc, noiDung, idNV, idNCC, trangThai " +
                     "FROM HopDong WHERE (isDeleted IS NULL OR isDeleted = 0)";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setIdHDong(rs.getString("idHDong"));
                hd.setNgayBatDau(rs.getDate("ngayBatDau"));
                hd.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                hd.setNoiDung(rs.getString("noiDung"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdNCC(rs.getString("idNCC"));
                hd.setTrangThai(rs.getString("trangThai"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Tìm kiếm hợp đồng (chỉ hiện chưa xóa)
    public List<HopDong> searchHopDong(String idHDong, String idNV, String idNCC) {
        List<HopDong> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idHDong, ngayBatDau, ngayKetThuc, noiDung, idNV, idNCC, trangThai " +
            "FROM HopDong WHERE (isDeleted IS NULL OR isDeleted = 0)"
        );
        if (idHDong != null && !idHDong.trim().isEmpty()) {
            sql.append(" AND idHDong LIKE ?");
        }
        if (idNV != null && !idNV.trim().isEmpty()) {
            sql.append(" AND idNV LIKE ?");
        }
        if (idNCC != null && !idNCC.trim().isEmpty()) {
            sql.append(" AND idNCC LIKE ?");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (idHDong != null && !idHDong.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idHDong.trim() + "%");
            }
            if (idNV != null && !idNV.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNV.trim() + "%");
            }
            if (idNCC != null && !idNCC.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNCC.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setIdHDong(rs.getString("idHDong"));
                hd.setNgayBatDau(rs.getDate("ngayBatDau"));
                hd.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                hd.setNoiDung(rs.getString("noiDung"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdNCC(rs.getString("idNCC"));
                hd.setTrangThai(rs.getString("trangThai"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    // Thêm mới hợp đồng (isDeleted = 0)
    public boolean insertHopDong(HopDong hd) {
        String sql = "INSERT INTO HopDong (idHDong, ngayBatDau, ngayKetThuc, noiDung, idNV, idNCC, trangThai, isDeleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, hd.getIdHDong());
            stmt.setDate(2, new java.sql.Date(hd.getNgayBatDau().getTime()));
            stmt.setDate(3, new java.sql.Date(hd.getNgayKetThuc().getTime()));
            if (hd.getNoiDung() != null) {
                stmt.setString(4, hd.getNoiDung());
            } else {
                stmt.setNull(4, Types.NVARCHAR);
            }
            if (hd.getIdNV() != null) {
                stmt.setString(5, hd.getIdNV());
            } else {
                stmt.setNull(5, Types.NVARCHAR);
            }
            if (hd.getIdNCC() != null) {
                stmt.setString(6, hd.getIdNCC());
            } else {
                stmt.setNull(6, Types.NVARCHAR);
            }
            stmt.setString(7, hd.getTrangThai());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // Cập nhật hợp đồng (không đổi isDeleted)
    public boolean updateHopDong(HopDong hd) {
        String sql = "UPDATE HopDong SET ngayBatDau = ?, ngayKetThuc = ?, noiDung = ?, idNV = ?, idNCC = ?, trangThai = ? WHERE idHDong = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(hd.getNgayBatDau().getTime()));
            stmt.setDate(2, new java.sql.Date(hd.getNgayKetThuc().getTime()));
            if (hd.getNoiDung() != null) {
                stmt.setString(3, hd.getNoiDung());
            } else {
                stmt.setNull(3, Types.NVARCHAR);
            }
            if (hd.getIdNV() != null) {
                stmt.setString(4, hd.getIdNV());
            } else {
                stmt.setNull(4, Types.NVARCHAR);
            }
            if (hd.getIdNCC() != null) {
                stmt.setString(5, hd.getIdNCC());
            } else {
                stmt.setNull(5, Types.NVARCHAR);
            }
            stmt.setString(6, hd.getTrangThai());
            stmt.setString(7, hd.getIdHDong());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    // XÓA MỀM: chỉ cập nhật isDeleted = 1
    public boolean deleteHopDong(String idHDong) {
        String sql = "UPDATE HopDong SET isDeleted = 1 WHERE idHDong = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHDong);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }
    //setup trash
    public List<HopDong> getDeleted() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT * FROM HopDong WHERE isDeleted = 1";

        try (
        	Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setIdHDong(rs.getString("idHDong"));
                hd.setNgayBatDau(rs.getDate("ngayBatDau"));
                hd.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                hd.setNoiDung(rs.getString("noiDung"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdNCC(rs.getString("idNCC"));
                hd.setTrangThai(rs.getString("trangThai"));
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list; // ⬅️ ĐÂY LÀ DÒNG QUAN TRỌNG KHÔNG ĐƯỢC THIẾU
    }

    public boolean restore(String idHDong) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE HopDong SET isDeleted = 0 WHERE idHDong = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, idHDong);

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
    public boolean deleteForever(String idHDong) {
        String sql = "DELETE FROM HopDong WHERE idHDong = ?";
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
        ) {
            stmt.setString(1, idHDong);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
