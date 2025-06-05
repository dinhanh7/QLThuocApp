package controller;

import dao.ThuocDAO;
import entities.Thuoc;

import java.util.List;

/**
 * ThuocController.java
 *
 * Kết nối giữa GUI và DAO cho Thuốc.
 */
public class ThuocController {

    private ThuocDAO thuocDAO;

    public ThuocController() {
        thuocDAO = new ThuocDAO();
    }

    public List<Thuoc> getAllThuoc() {
        return thuocDAO.getAllThuoc();
    }

    public boolean addThuoc(Thuoc t) {
        return thuocDAO.insertThuoc(t);
    }

    public boolean updateThuoc(Thuoc t) {
        return thuocDAO.updateThuoc(t);
    }

    public boolean deleteThuoc(String idThuoc) {
        return thuocDAO.deleteThuoc(idThuoc);
    }

    /**
     * Tìm kiếm Thuốc theo idThuoc hoặc tenThuoc.
     */
    public List<Thuoc> searchThuoc(String idThuoc, String tenThuoc) {
        return thuocDAO.searchThuoc(idThuoc, tenThuoc);
    }
}
