package controller;

import dao.PhieuNhapDAO;
import entities.PhieuNhap;

import java.util.List;

public class PhieuNhapController {
    private PhieuNhapDAO phieuNhapDAO;

    public PhieuNhapController() {
        phieuNhapDAO = new PhieuNhapDAO();
    }

    // Lấy danh sách tất cả phiếu nhập
    public List<PhieuNhap> getAllPhieuNhap() {
        return phieuNhapDAO.getAll();
    }

    // Lấy phiếu nhập theo id
    public PhieuNhap getPhieuNhapById(String idPN) {
        return phieuNhapDAO.getById(idPN);
    }

    // Thêm mới phiếu nhập
    public boolean addPhieuNhap(PhieuNhap pn) {
        if (pn.getIdPN() == null || pn.getIdPN().trim().isEmpty()) {
            return false;
        }
        return phieuNhapDAO.insert(pn);
    }

    // Cập nhật phiếu nhập
    public boolean updatePhieuNhap(PhieuNhap pn) {
        if (pn.getIdPN() == null || pn.getIdPN().trim().isEmpty()) {
            return false;
        }
        return phieuNhapDAO.update(pn);
    }

    // Xóa phiếu nhập
    public boolean deletePhieuNhap(String idPN) {
        if (idPN == null || idPN.trim().isEmpty()) {
            return false;
        }
        return phieuNhapDAO.delete(idPN);
    }
}
// PhieuNhapController.java 
