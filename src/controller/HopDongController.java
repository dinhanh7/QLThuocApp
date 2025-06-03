package controller;

import dao.HopDongDAO;
import entities.HopDong;

import java.util.List;

public class HopDongController {
    private HopDongDAO hopDongDAO;

    public HopDongController() {
        hopDongDAO = new HopDongDAO();
    }

    // Lấy danh sách tất cả hợp đồng
    public List<HopDong> getAllHopDong() {
        return hopDongDAO.getAll();
    }

    // Lấy hợp đồng theo id
    public HopDong getHopDongById(String idHDong) {
        return hopDongDAO.getById(idHDong);
    }

    // Thêm mới hợp đồng
    public boolean addHopDong(HopDong hd) {
        if (hd.getIdHDong() == null || hd.getIdHDong().trim().isEmpty()) {
            return false;
        }
        return hopDongDAO.insert(hd);
    }

    // Cập nhật hợp đồng
    public boolean updateHopDong(HopDong hd) {
        if (hd.getIdHDong() == null || hd.getIdHDong().trim().isEmpty()) {
            return false;
        }
        return hopDongDAO.update(hd);
    }

    // Xóa hợp đồng
    public boolean deleteHopDong(String idHDong) {
        if (idHDong == null || idHDong.trim().isEmpty()) {
            return false;
        }
        return hopDongDAO.delete(idHDong);
    }
}
// HopDongController.java 
