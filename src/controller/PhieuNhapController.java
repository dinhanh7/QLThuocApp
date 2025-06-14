package controller;

import dao.PhieuNhapDAO;
import entities.PhieuNhap;

import java.util.List;

/**
 * PhieuNhapController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllPhieuNhap, addPhieuNhap, updatePhieuNhap, deletePhieuNhap),
 * và bổ sung thêm searchPhieuNhap(String idPN, String idNV, String idNCC).
 */
public class PhieuNhapController {

    private PhieuNhapDAO phieuNhapDAO;

    public PhieuNhapController() {
        phieuNhapDAO = new PhieuNhapDAO();
    }

    public List<PhieuNhap> getAllPhieuNhap() {
        return phieuNhapDAO.getAll();
    }

    public boolean addPhieuNhap(PhieuNhap pn) {
        return phieuNhapDAO.insert(pn);
    }

    public boolean updatePhieuNhap(PhieuNhap pn) {
        return phieuNhapDAO.update(pn);
    }

//    public boolean deletePhieuNhap(String idPN) {
//        return phieuNhapDAO.delete(idPN);
//    }

    /**
     * Tìm kiếm Phiếu Nhập theo idPN hoặc idNV hoặc idNCC.
     * Nếu cả ba tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<PhieuNhap> searchPhieuNhap(String idPN, String idNV, String idNCC) {
        return phieuNhapDAO.search(idPN, idNV, idNCC);
    }
    public boolean deletePhieuNhap(String idPN) {
        return phieuNhapDAO.deletePhieuNhap(idPN);
    }

}
