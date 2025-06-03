package controller;

import dao.NhanVienDAO;
import entities.NhanVien;

import java.util.List;

public class NhanVienController {
    private NhanVienDAO nhanVienDAO;

    public NhanVienController() {
        nhanVienDAO = new NhanVienDAO();
    }

    // Lấy danh sách tất cả nhân viên
    public List<NhanVien> getAllNhanVien() {
        return nhanVienDAO.getAll();
    }

    // Lấy một nhân viên theo id
    public NhanVien getNhanVienById(String idNV) {
        return nhanVienDAO.getById(idNV);
    }

    // Thêm mới nhân viên
    public boolean addNhanVien(NhanVien nv) {
        if (nv.getIdNV() == null || nv.getIdNV().trim().isEmpty()) {
            return false;
        }
        return nhanVienDAO.insert(nv);
    }

    // Cập nhật thông tin nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        if (nv.getIdNV() == null || nv.getIdNV().trim().isEmpty()) {
            return false;
        }
        return nhanVienDAO.update(nv);
    }

    // Xóa nhân viên
    public boolean deleteNhanVien(String idNV) {
        if (idNV == null || idNV.trim().isEmpty()) {
            return false;
        }
        return nhanVienDAO.delete(idNV);
    }
}
// NhanVienController.java 
