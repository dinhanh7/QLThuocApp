package controller;

import dao.HopDongDAO;
import entities.HopDong;

import java.util.List;

/**
 * HopDongController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllHopDong, addHopDong, updateHopDong, deleteHopDong),
 * và bổ sung thêm searchHopDong(String idHDong, String idNV, String idNCC).
 */
public class HopDongController {

    private HopDongDAO hopDongDAO;

    public HopDongController() {
        hopDongDAO = new HopDongDAO();
    }

    public List<HopDong> getAllHopDong() {
        return hopDongDAO.getAll();
    }

    public boolean addHopDong(HopDong hd) {
        return hopDongDAO.insert(hd);
    }

    public boolean updateHopDong(HopDong hd) {
        return hopDongDAO.update(hd);
    }

    public boolean deleteHopDong(String idHDong) {
        return hopDongDAO.delete(idHDong);
    }

    /**
     * Tìm kiếm Hợp đồng theo idHDong hoặc idNV hoặc idNCC.
     * Nếu cả ba tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<HopDong> searchHopDong(String idHDong, String idNV, String idNCC) {
        return hopDongDAO.search(idHDong, idNV, idNCC);
    }
}
