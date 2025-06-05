package controller;

import dao.HoaDonDAO;
import entities.HoaDon;

import java.util.List;

/**
 * HoaDonController.java
 *
 * Kết nối giữa GUI và DAO cho Hóa đơn.
 */
public class HoaDonController {

    private HoaDonDAO hoaDonDAO;

    public HoaDonController() {
        hoaDonDAO = new HoaDonDAO();
    }

    public List<HoaDon> getAllHoaDon() {
        return hoaDonDAO.getAllHoaDon();
    }

    public boolean addHoaDon(HoaDon hd) {
        return hoaDonDAO.insertHoaDon(hd);
    }

    public boolean updateHoaDon(HoaDon hd) {
        return hoaDonDAO.updateHoaDon(hd);
    }

    public boolean deleteHoaDon(String idHD) {
        return hoaDonDAO.deleteHoaDon(idHD);
    }

    /**
     * Tìm kiếm Hóa đơn theo idHD, idNV hoặc idKH.
     */
    public List<HoaDon> searchHoaDon(String idHD, String idNV, String idKH) {
        return hoaDonDAO.searchHoaDon(idHD, idNV, idKH);
    }
}
