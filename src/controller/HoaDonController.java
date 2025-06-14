package controller;

import dao.HoaDonDAO;
import entities.ChiTietHoaDon;
import controller.KhachHangController;
import entities.HoaDon;
import entities.HoaDon;
import entities.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import connectDB.DBCloseHelper;
import connectDB.DBConnection;

import java.util.List;

public class HoaDonController {

    private HoaDonDAO hoaDonDAO;
    private KhachHangController khachHangController; // <-- Khai báo ở đây, TRƯỚC constructor

    public HoaDonController() {
        hoaDonDAO = new HoaDonDAO();
        khachHangController = new KhachHangController(); // Khởi tạo trong constructor
    }

    /**
     * Lấy tất cả hóa đơn.
     */
    public List<HoaDon> getAllHoaDon() {
        return hoaDonDAO.getAllHoaDon();
    }

    /**
     * Thêm mới hóa đơn, trả về true nếu thành công, false nếu lỗi.
     * @param hd        Đối tượng hóa đơn cần thêm.
     * @param errorMsg  Nếu có lỗi sẽ nhận được message lỗi để GUI hiển thị.
     */
    public boolean addHoaDon(HoaDon hd, List<ChiTietHoaDon> chiTietList, StringBuilder errorMsg) {
        try {
            // Gọi DAO để insert master-detail (cả hóa đơn và chi tiết)
            return hoaDonDAO.insertHoaDonWithDetails(hd, chiTietList);
        } catch (RuntimeException ex) {
            if (errorMsg != null) errorMsg.append(ex.getMessage());
            return false;
        }
    }


    /**
     * Cập nhật hóa đơn, trả về true nếu thành công, false nếu lỗi.
     * @param hd        Đối tượng hóa đơn cần sửa.
     * @param errorMsg  Nếu có lỗi sẽ nhận được message lỗi để GUI hiển thị.
     */
    public boolean updateHoaDon(HoaDon hd, StringBuilder errorMsg) {
        try {
            return hoaDonDAO.updateHoaDon(hd);
        } catch (RuntimeException ex) {
            if (errorMsg != null) errorMsg.append(ex.getMessage());
            return false;
        }
    }

    /**
     * Xóa hóa đơn theo idHD.
     * @param idHD      Mã hóa đơn cần xóa.
     * @param errorMsg  Nếu có lỗi sẽ nhận được message lỗi để GUI hiển thị.
     */
    public boolean deleteHoaDon(String idHD, StringBuilder errorMsg) {
        try {
            return hoaDonDAO.deleteHoaDon(idHD);
        } catch (RuntimeException ex) {
            if (errorMsg != null) errorMsg.append(ex.getMessage());
            return false;
        }
    }

    /**
     * Tìm kiếm hóa đơn theo idHD, idNV, idKH.
     * Nếu tham số rỗng thì trả về toàn bộ danh sách.
     */
    public List<HoaDon> searchHoaDon(String idHD, String idNV, String idKH) {
        return hoaDonDAO.searchHoaDon(idHD, idNV, idKH);
    }


    public int getDiemHienTai(String idKH) {
        return khachHangController.getDiemHienTai(idKH);
    }
    public boolean truDiem(String idKH, int soDiemTru) {
        return khachHangController.truDiem(idKH, soDiemTru);
    }
    public boolean congDiem(String idKH, int soDiemCong) {
        return khachHangController.congDiem(idKH, soDiemCong);
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
    public String getNextHoaDonId() {
        // Lấy tất cả hóa đơn hiện tại
        List<HoaDon> ds = getAllHoaDon();
        int max = 0;
        for (HoaDon hd : ds) {
            String id = hd.getIdHD();
            if (id.startsWith("HD")) {
                try {
                    int num = Integer.parseInt(id.substring(2));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("HD%03d", max + 1); // HD001, HD002,...
    }
    public static Map<String, Integer> tinhDoanhThuTheoNgay(String fromDate, String toDate) {
        Map<String, Integer> doanhThuMap = new LinkedHashMap<>();
        String query = "SELECT CONVERT(date, thoiGian) AS ngay, SUM(tongTien) AS doanhThu " +
                "FROM HoaDon " +
                "WHERE thoiGian >= ? AND thoiGian < DATEADD(DAY, 1, ?) " +
                "GROUP BY CONVERT(date, thoiGian) " +
                "ORDER BY ngay";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fromDate);
            stmt.setString(2, toDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ngay = rs.getString("ngay");
                int doanhThu = rs.getInt("doanhThu");
                doanhThuMap.put(ngay, doanhThu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doanhThuMap;
    }
        public HoaDon getHoaDonById(String idHD) {
        return hoaDonDAO.getById(idHD); // Viết thêm hàm này nếu chưa có
    }
    public boolean updateHoaDonWithDetails(HoaDon hd, List<ChiTietHoaDon> chiTietList) {
        return hoaDonDAO.updateWithDetails(hd, chiTietList);
    }

}
