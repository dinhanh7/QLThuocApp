package controller;

import dao.HopDongDAO;
import entities.HopDong;

import java.util.List;

/**
 * HopDongController.java
 *
 * Kết nối giữa GUI và DAO cho HopDong.
 */
public class HopDongController {

    private HopDongDAO hopDongDAO;

    public HopDongController() {
        hopDongDAO = new HopDongDAO();
    }

    public List<HopDong> getAllHopDong() {
        return hopDongDAO.getAllHopDong();
    }

    public boolean addHopDong(HopDong hd) {
        return hopDongDAO.insertHopDong(hd);
    }

    public boolean updateHopDong(HopDong hd) {
        return hopDongDAO.updateHopDong(hd);
    }

    public boolean deleteHopDong(String idHDong) {
        return hopDongDAO.deleteHopDong(idHDong);
    }

    /**
     * Tìm kiếm HopDong theo idHDong, idNV hoặc idNCC.
     */
    public List<HopDong> searchHopDong(String idHDong, String idNV, String idNCC) {
        return hopDongDAO.searchHopDong(idHDong, idNV, idNCC);
    }
}
