package controller;

import dao.HoaDonDAO;
import entities.HoaDon;

import java.util.List;

public class HoaDonController {
    private HoaDonDAO hoaDonDAO;

    public HoaDonController() {
        hoaDonDAO = new HoaDonDAO();
    }

    // Lấy danh sách tất cả hóa đơn
    public List<HoaDon> getAllHoaDon() {
        return hoaDonDAO.getAll();
    }

    // Lấy hóa đơn theo id
    public HoaDon getHoaDonById(String idHD) {
        return hoaDonDAO.getById(idHD);
    }

    // Thêm mới hóa đơn
    public boolean addHoaDon(HoaDon hd) {
        if (hd.getIdHD() == null || hd.getIdHD().trim().isEmpty()) {
            return false;
        }
        return hoaDonDAO.insert(hd);
    }

    // Cập nhật hóa đơn
    public boolean updateHoaDon(HoaDon hd) {
        if (hd.getIdHD() == null || hd.getIdHD().trim().isEmpty()) {
            return false;
        }
        return hoaDonDAO.update(hd);
    }

    // Xóa hóa đơn
    public boolean deleteHoaDon(String idHD) {
        if (idHD == null || idHD.trim().isEmpty()) {
            return false;
        }
        return hoaDonDAO.delete(idHD);
    }
}
// HoaDonController.java 
