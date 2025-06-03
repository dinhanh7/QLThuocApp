package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;

import java.sql.*;

/**
 * ChiTietHoaDonDAO.java
 * 
 * Chỉ chứa phương thức để tìm idThuoc đầu tiên của một hóa đơn (dùng cho chức năng khách).
 */
public class ChiTietHoaDonDAO {

    /**
     * Với một idHD, trả về idThuoc đầu tiên (theo thứ tự SQL trả về).
     * Nếu bảng ChiTietHoaDon không có dòng nào cho idHD đó hoặc idHD không tồn tại, trả về null.
     */
    public String getFirstIdThuocByHD(String idHD) {
        String sql = "SELECT TOP 1 idThuoc FROM ChiTietHoaDon WHERE idHD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHD);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("idThuoc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return null;
    }
}
