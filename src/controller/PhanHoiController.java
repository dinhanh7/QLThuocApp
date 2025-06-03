package controller;

import dao.PhanHoiDAO;
import entities.PhanHoi;

import java.util.List;

public class PhanHoiController {
    private PhanHoiDAO phanHoiDAO;

    public PhanHoiController() {
        phanHoiDAO = new PhanHoiDAO();
    }

    // Lấy danh sách tất cả phản hồi
    public List<PhanHoi> getAllPhanHoi() {
        return phanHoiDAO.getAll();
    }

    // Lấy phản hồi theo id
    public PhanHoi getPhanHoiById(String idPH) {
        return phanHoiDAO.getById(idPH);
    }

    // Thêm mới phản hồi
    public boolean addPhanHoi(PhanHoi ph) {
        if (ph.getIdPH() == null || ph.getIdPH().trim().isEmpty()) {
            return false;
        }
        return phanHoiDAO.insert(ph);
    }

    // Cập nhật phản hồi
    public boolean updatePhanHoi(PhanHoi ph) {
        if (ph.getIdPH() == null || ph.getIdPH().trim().isEmpty()) {
            return false;
        }
        return phanHoiDAO.update(ph);
    }

    // Xóa phản hồi
    public boolean deletePhanHoi(String idPH) {
        if (idPH == null || idPH.trim().isEmpty()) {
            return false;
        }
        return phanHoiDAO.delete(idPH);
    }
}
// PhanHoiController.java 
