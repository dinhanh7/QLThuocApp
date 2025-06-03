package controller;

import dao.ThuocDAO;
import entities.Thuoc;

import java.util.List;

/**
 * ThuocController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllThuoc, addThuoc, updateThuoc, deleteThuoc),
 * và bổ sung thêm searchThuoc() để tìm theo id hoặc tên.
 */
public class ThuocController {

    private ThuocDAO thuocDAO;

    public ThuocController() {
        thuocDAO = new ThuocDAO();
    }

    /**
     * Lấy toàn bộ danh sách Thuoc.
     */
    public List<Thuoc> getAllThuoc() {
        return thuocDAO.getAll();
    }

    /**
     * Thêm Thuoc mới.
     */
    public boolean addThuoc(Thuoc t) {
        return thuocDAO.insert(t);
    }

    /**
     * Cập nhật Thuoc.
     */
    public boolean updateThuoc(Thuoc t) {
        return thuocDAO.update(t);
    }

    /**
     * Xóa Thuoc theo idThuoc.
     */
    public boolean deleteThuoc(String idThuoc) {
        return thuocDAO.delete(idThuoc);
    }

    /**
     * Tìm kiếm Thuoc theo idThuoc hoặc tenThuoc.
     * Nếu cả hai tham số đều rỗng, sẽ trả về toàn bộ danh sách.
     */
    public List<Thuoc> searchThuoc(String idThuoc, String tenThuoc) {
        return thuocDAO.search(idThuoc, tenThuoc);
    }
}
