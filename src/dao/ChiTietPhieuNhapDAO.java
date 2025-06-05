package dao;

import connectDB.DBConnection;
import connectDB.DBCloseHelper;
import entities.ChiTietPhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ChiTietPhieuNhapDAO.java
 *
 * Đã chỉnh sửa truy vấn trong getByIdPN(...) để join với bảng Thuoc
 * và lấy thêm cột tenThuoc (tên thuốc).
 */
public class ChiTietPhieuNhapDAO {

    /**
     * Trả về danh sách ChiTietPhieuNhap (kèm cả tên thuốc) theo idPN.
     * 
     * SELECT ct.idThuoc, t.tenThuoc, ct.soLuong, ct.donGia
     *   FROM ChiTietPhieuNhap ct
     *   JOIN Thuoc t ON ct.idThuoc = t.idThuoc
     *  WHERE ct.idPN = ?
     */
    public List<ChiTietPhieuNhap> getByIdPN(String idPN) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = ""
            + "SELECT ct.idPN, ct.idThuoc, t.tenThuoc, ct.soLuong, ct.donGia "
            + "FROM ChiTietPhieuNhap ct "
            + "JOIN Thuoc t ON ct.idThuoc = t.idThuoc "
            + "WHERE ct.idPN = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPN);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
                ct.setIdPN(rs.getString("idPN"));
                ct.setIdThuoc(rs.getString("idThuoc"));
                ct.setTenThuoc(rs.getString("tenThuoc")); // Tên thuốc mới
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
