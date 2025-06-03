package controller;

import dao.HoaDonDAO;
import entities.HoaDon;

import java.util.List;

/**
 * HoaDonController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllHoaDon, addHoaDon, updateHoaDon, deleteHoaDon),
 * và bổ sung thêm searchHoaDon(String idHD, String idNV, String idKH).
 */
public class HoaDonController {

    private HoaDonDAO hoaDonDAO;

    public HoaDonController() {
        hoaDonDAO = new HoaDonDAO();
    }

    public List<HoaDon> getAllHoaDon() {
        return hoaDonDAO.getAll();
    }

    public boolean addHoaDon(HoaDon hd) {
        return hoaDonDAO.insert(hd);
    }

    public boolean updateHoaDon(HoaDon hd) {
        return hoaDonDAO.update(hd);
    }

    public boolean deleteHoaDon(String idHD) {
        return hoaDonDAO.delete(idHD);
    }

    /**
     * Tìm kiếm HoaDon theo idHD hoặc idNV hoặc idKH.
     * Nếu cả ba tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<HoaDon> searchHoaDon(String idHD, String idNV, String idKH) {
        return hoaDonDAO.search(idHD, idNV, idKH);
    }
}
