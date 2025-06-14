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
    public Thuoc getById(String idThuoc) {
        return thuocDAO.getById(idThuoc);
    }
    public boolean addThuoc(Thuoc t) {
        return thuocDAO.insertThuoc(t);
    }

    public boolean updateThuoc(Thuoc t) {
        return thuocDAO.updateThuoc(t);
    }

    public boolean deleteThuoc(String idThuoc, StringBuilder errorMessage) {
        try {
            return thuocDAO.deleteThuoc(idThuoc);
        } catch (RuntimeException ex) {
            if (errorMessage != null) errorMessage.append(ex.getMessage());
            return false;
        }
    }
    public boolean giamSoLuongThuoc(String idThuoc, int soLuong) {
        // Trả về true nếu cập nhật thành công, false nếu không
        return new ThuocDAO().giamSoLuong(idThuoc, soLuong);
    }
    /**
     * Tìm kiếm Thuốc theo idThuoc hoặc tenThuoc.
     */
    public List<Thuoc> searchThuoc(String idThuoc, String tenThuoc) {
        return thuocDAO.searchThuoc(idThuoc, tenThuoc);
    }
}
