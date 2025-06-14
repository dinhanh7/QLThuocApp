package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.ChiTietHoaDon;
import entities.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HoaDonDAO.java
 *
 * CRUD cho bảng dbo.HoaDon (bổ sung trường phuongThucThanhToan và trangThaiDonHang).
 * ĐÃ SỬA: Trả về lỗi chi tiết khi vi phạm ràng buộc, lỗi SQL cho Controller xử lý.
 */
public class HoaDonDAO {

    /**
     * Lấy toàn bộ danh sách Hóa đơn.
     */
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT idHD, thoiGian, idNV, idKH, tongTien, phuongThucThanhToan, trangThaiDonHang FROM HoaDon";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setIdHD(rs.getString("idHD"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdKH(rs.getString("idKH"));
                hd.setTongTien(rs.getDouble("tongTien"));
                hd.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                hd.setTrangThaiDonHang(rs.getString("trangThaiDonHang"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Thêm mới một Hóa đơn (kèm trường mới).
     * Báo lỗi nếu trùng khóa hoặc thiếu khóa ngoại.
     */
    public boolean insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (idHD, thoiGian, idNV, idKH, tongTien, phuongThucThanhToan, trangThaiDonHang) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, hd.getIdHD());
            stmt.setTimestamp(2, new java.sql.Timestamp(hd.getThoiGian().getTime()));
            stmt.setString(3, hd.getIdNV());
            stmt.setString(4, hd.getIdKH());
            stmt.setDouble(5, hd.getTongTien());
            if (hd.getPhuongThucThanhToan() != null) {
                stmt.setString(6, hd.getPhuongThucThanhToan());
            } else {
                stmt.setNull(6, Types.NVARCHAR);
            }
            stmt.setString(7, hd.getTrangThaiDonHang());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) { // Trùng PK
                throw new RuntimeException("ID hóa đơn đã tồn tại!");
            }
            if (e.getErrorCode() == 547) { // FK constraint failed
                throw new RuntimeException("ID nhân viên hoặc khách hàng không tồn tại!");
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi thêm hóa đơn: " + e.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    /**
     * Cập nhật Hóa đơn (theo idHD).
     */
    public boolean updateHoaDon(HoaDon hd) {
        String sql = "UPDATE HoaDon SET thoiGian=?, idNV=?, idKH=?, tongTien=?, phuongThucThanhToan=?, trangThaiDonHang=? WHERE idHD=?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(hd.getThoiGian().getTime()));
            stmt.setString(2, hd.getIdNV());
            stmt.setString(3, hd.getIdKH());
            stmt.setDouble(4, hd.getTongTien());
            if (hd.getPhuongThucThanhToan() != null) {
                stmt.setString(5, hd.getPhuongThucThanhToan());
            } else {
                stmt.setNull(5, Types.NVARCHAR);
            }
            stmt.setString(6, hd.getTrangThaiDonHang());
            stmt.setString(7, hd.getIdHD());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) {
                throw new RuntimeException("ID nhân viên hoặc khách hàng không tồn tại!");
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi cập nhật hóa đơn: " + e.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    /**
     * Xóa Hóa đơn theo idHD.
     * Báo lỗi nếu còn chi tiết hóa đơn hoặc phản hồi liên quan.
     */
    public boolean deleteHoaDon(String idHD) {
        String sql = "DELETE FROM HoaDon WHERE idHD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHD);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) { // FK constraint failed
                throw new RuntimeException("Không thể xóa hóa đơn này vì đã có chi tiết hóa đơn hoặc phản hồi liên quan!");
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi SQL khi xóa hóa đơn: " + e.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmt, conn);
        }
    }

    /**
     * Tìm kiếm Hóa đơn theo idHD, idNV hoặc idKH.
     * Nếu cả ba tham số đều rỗng, trả về toàn bộ.
     */
    public List<HoaDon> searchHoaDon(String idHD, String idNV, String idKH) {
        List<HoaDon> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT idHD, thoiGian, idNV, idKH, tongTien, phuongThucThanhToan, trangThaiDonHang FROM HoaDon WHERE 1=1"
        );
        if (idHD != null && !idHD.trim().isEmpty()) {
            sql.append(" AND idHD LIKE ?");
        }
        if (idNV != null && !idNV.trim().isEmpty()) {
            sql.append(" AND idNV LIKE ?");
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
            if (idHD != null && !idHD.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idHD.trim() + "%");
            }
            if (idNV != null && !idNV.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idNV.trim() + "%");
            }
            if (idKH != null && !idKH.trim().isEmpty()) {
                stmt.setString(idx++, "%" + idKH.trim() + "%");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setIdHD(rs.getString("idHD"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdKH(rs.getString("idKH"));
                hd.setTongTien(rs.getDouble("tongTien"));
                hd.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                hd.setTrangThaiDonHang(rs.getString("trangThaiDonHang"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }
    public boolean insertHoaDonWithDetails(HoaDon hd, List<ChiTietHoaDon> chiTietList) {
        Connection conn = null;
        PreparedStatement stmtHD = null, stmtCT = null;
        String sqlHD = "INSERT INTO HoaDon (idHD, thoiGian, idNV, idKH, tongTien, phuongThucThanhToan, trangThaiDonHang) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlCT = "INSERT INTO ChiTietHoaDon (idHD, idThuoc, soLuong, donGia) VALUES (?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Thêm hóa đơn
            stmtHD = conn.prepareStatement(sqlHD);
            stmtHD.setString(1, hd.getIdHD());
            stmtHD.setTimestamp(2, new java.sql.Timestamp(hd.getThoiGian().getTime()));
            stmtHD.setString(3, hd.getIdNV());
            stmtHD.setString(4, hd.getIdKH());
            stmtHD.setDouble(5, hd.getTongTien());
            stmtHD.setString(6, hd.getPhuongThucThanhToan());
            stmtHD.setString(7, hd.getTrangThaiDonHang());
            stmtHD.executeUpdate();

            // Thêm từng chi tiết hóa đơn
            stmtCT = conn.prepareStatement(sqlCT);
            for (ChiTietHoaDon ct : chiTietList) {
                stmtCT.setString(1, hd.getIdHD());
                stmtCT.setString(2, ct.getIdThuoc());
                stmtCT.setInt(3, ct.getSoLuong());
                stmtCT.setDouble(4, ct.getDonGia());
                stmtCT.addBatch();
            }
            stmtCT.executeBatch();

            conn.commit();
            return true;
        } catch (Exception ex) {
            if (conn != null) try { conn.rollback(); } catch (Exception ignore) {}
            ex.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm hóa đơn và chi tiết hóa đơn: " + ex.getMessage());
        } finally {
            DBCloseHelper.closeAll(stmtCT, null);
            DBCloseHelper.closeAll(stmtHD, conn);
        }
    }
    //thêm hàm từ đây 246 đến 277
    public String getKhachHangIdByHoaDonId(String idHD) {
        String sql = "SELECT idKH FROM HoaDon WHERE idHD = ?";
        try (Connection conn = DriverManager.getConnection(
        	    "jdbc:sqlserver://localhost:1433;databaseName=QLTHUOC;encrypt=false;", "sa", "sa");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idHD);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("idKH");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // không tìm thấy
    }
    public boolean exists(String idHD) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE idHD = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idHD);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //setup trash
    public List<HoaDon> getDeleted() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE isDeleted = 1";

        try (
        	Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setIdHD(rs.getString("idHD"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setIdNV(rs.getString("idNV"));
                hd.setIdKH(rs.getString("idKH"));
                hd.setTongTien(rs.getDouble("tongTien"));
                hd.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                hd.setTrangThaiDonHang(rs.getString("trangThaiDonHang"));
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list; // ⬅️ ĐÂY LÀ DÒNG QUAN TRỌNG KHÔNG ĐƯỢC THIẾU
    }

    public boolean restore(String idHD) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DBConnection.getConnection();
            String sql = "UPDATE HoaDon SET isDeleted = 0 WHERE idHD = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, idHD);

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
    public boolean deleteForever(String idHD) {
        String sql = "DELETE FROM HoaDon WHERE idHD = ?";
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
        ) {
            stmt.setString(1, idHD);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
