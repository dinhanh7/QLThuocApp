package controller;

import dao.PhanHoiDAO;
import entities.PhanHoi;

import java.util.List;

/**
 * PhanHoiController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllPhanHoi, addPhanHoi, updatePhanHoi, deletePhanHoi),
 * và bổ sung thêm searchPhanHoi(String idPH, String idKH).
 */
public class PhanHoiController {

    private PhanHoiDAO phanHoiDAO;

    public PhanHoiController() {
        phanHoiDAO = new PhanHoiDAO();
    }

    public List<PhanHoi> getAllPhanHoi() {
        return phanHoiDAO.getAll();
    }

    public boolean addPhanHoi(PhanHoi ph) {
        return phanHoiDAO.insert(ph);
    }

    public boolean updatePhanHoi(PhanHoi ph) {
        return phanHoiDAO.update(ph);
    }

    public boolean deletePhanHoi(String idPH) {
        return phanHoiDAO.delete(idPH);
    }

    /**
     * Tìm kiếm Phản hồi theo idPH hoặc idKH.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<PhanHoi> searchPhanHoi(String idPH, String idKH) {
        return phanHoiDAO.search(idPH, idKH);
    }
}
