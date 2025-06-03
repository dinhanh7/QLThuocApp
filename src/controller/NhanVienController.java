package controller;

import dao.NhanVienDAO;
import entities.NhanVien;

import java.util.List;

/**
 * NhanVienController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllNhanVien, addNhanVien, updateNhanVien, deleteNhanVien),
 * và bổ sung thêm searchNhanVien(String idNV, String hoTen).
 */
public class NhanVienController {

    private NhanVienDAO nhanVienDAO;

    public NhanVienController() {
        nhanVienDAO = new NhanVienDAO();
    }

    public List<NhanVien> getAllNhanVien() {
        return nhanVienDAO.getAll();
    }

    public boolean addNhanVien(NhanVien nv) {
        return nhanVienDAO.insert(nv);
    }

    public boolean updateNhanVien(NhanVien nv) {
        return nhanVienDAO.update(nv);
    }

    public boolean deleteNhanVien(String idNV) {
        return nhanVienDAO.delete(idNV);
    }

    /**
     * Tìm kiếm Nhân viên theo idNV hoặc hoTen.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<NhanVien> searchNhanVien(String idNV, String hoTen) {
        return nhanVienDAO.search(idNV, hoTen);
    }
}
