package controller;

import dao.HoaDonDAO;
import entities.HoaDon;

import java.util.List;

/**
 * HoaDonController.java
 *
 * Kết nối giữa GUI và DAO cho Hóa đơn.
 * ĐÃ SỬA: Hỗ trợ bắt lỗi từ DAO và truyền thông báo lỗi về GUI.
 */
public class HoaDonController {

    private HoaDonDAO hoaDonDAO;

    public HoaDonController() {
        hoaDonDAO = new HoaDonDAO();
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
    public boolean addHoaDon(HoaDon hd, StringBuilder errorMsg) {
        try {
            return hoaDonDAO.insertHoaDon(hd);
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
}
