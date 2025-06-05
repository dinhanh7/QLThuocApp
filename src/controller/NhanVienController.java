package controller;

import dao.NhanVienDAO;
import entities.NhanVien;

import java.util.List;

/**
 * NhanVienController.java
 *
 * Kết nối giữa GUI và DAO cho NhanVien.
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
     * Tìm kiếm NhanVien theo idNV hoặc hoTen.
     */
    public List<NhanVien> searchNhanVien(String idNV, String hoTen) {
        return nhanVienDAO.search(idNV, hoTen);
    }
}
