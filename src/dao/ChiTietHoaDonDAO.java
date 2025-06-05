package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.ChiTietHoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ChiTietHoaDonDAO.java
 *
 * DAO cho bảng dbo.ChiTietHoaDon, đã sửa để join với bảng Thuoc lấy thêm tên thuốc.
 */
public class ChiTietHoaDonDAO {

    /**
     * Lấy danh sách ChiTietHoaDon (kèm tên thuốc) theo idHD.
     *
     * SQL:
     *   SELECT ct.idHD, ct.idThuoc, t.tenThuoc, ct.soLuong, ct.donGia
     *     FROM ChiTietHoaDon ct
     *     JOIN Thuoc t ON ct.idThuoc = t.idThuoc
     *    WHERE ct.idHD = ?
     *
     * @param idHD Mã hóa đơn cần lấy chi tiết.
     * @return Danh sách ChiTietHoaDon có tenThuoc.
     */
    public List<ChiTietHoaDon> getByIdHD(String idHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = ""
            + "SELECT ct.idHD, ct.idThuoc, t.tenThuoc, ct.soLuong, ct.donGia "
            + "FROM ChiTietHoaDon ct "
            + "JOIN Thuoc t ON ct.idThuoc = t.idThuoc "
            + "WHERE ct.idHD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHD);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setIdHD(rs.getString("idHD"));
                ct.setIdThuoc(rs.getString("idThuoc"));
                ct.setTenThuoc(rs.getString("tenThuoc")); // Lấy tên thuốc
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setDonGia(rs.getDouble("donGia"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return list;
    }
}
