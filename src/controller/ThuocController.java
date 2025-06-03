package controller;

import dao.ThuocDAO;
import entities.Thuoc;

import java.util.List;

public class ThuocController {
    private ThuocDAO thuocDAO;

    public ThuocController() {
        thuocDAO = new ThuocDAO();
    }

    // Lấy danh sách tất cả thuốc
    public List<Thuoc> getAllThuoc() {
        return thuocDAO.getAll();
    }

    // Lấy thông tin một thuốc theo id
    public Thuoc getThuocById(String idThuoc) {
        return thuocDAO.getById(idThuoc);
    }

    // Thêm mới thuốc
    public boolean addThuoc(Thuoc t) {
        // Ví dụ: kiểm tra id không trống, tên không null...
        if (t.getIdThuoc() == null || t.getIdThuoc().trim().isEmpty()) {
            return false;
        }
        return thuocDAO.insert(t);
    }

    // Cập nhật thuốc
    public boolean updateThuoc(Thuoc t) {
        if (t.getIdThuoc() == null || t.getIdThuoc().trim().isEmpty()) {
            return false;
        }
        return thuocDAO.update(t);
    }

    // Xóa thuốc
    public boolean deleteThuoc(String idThuoc) {
        if (idThuoc == null || idThuoc.trim().isEmpty()) {
            return false;
        }
        return thuocDAO.delete(idThuoc);
    }
}
// ThuocController.java 
