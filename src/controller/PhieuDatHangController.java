package controller;

import dao.PhieuDatHangDAO;
import entities.PhieuDatHang;

import java.util.List;

/**
 * PhieuDatHangController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllPhieuDatHang, addPhieuDatHang, updatePhieuDatHang, deletePhieuDatHang),
 * và bổ sung thêm searchPhieuDatHang(String idPDH, String idKH).
 */
public class PhieuDatHangController {

    private PhieuDatHangDAO phieuDatHangDAO;

    public PhieuDatHangController() {
        phieuDatHangDAO = new PhieuDatHangDAO();
    }

    public List<PhieuDatHang> getAllPhieuDatHang() {
        return phieuDatHangDAO.getAll();
    }

    public boolean addPhieuDatHang(PhieuDatHang pdh) {
        return phieuDatHangDAO.insert(pdh);
    }

    public boolean updatePhieuDatHang(PhieuDatHang pdh) {
        return phieuDatHangDAO.update(pdh);
    }

    public boolean deletePhieuDatHang(String idPDH) {
        return phieuDatHangDAO.delete(idPDH);
    }

    /**
     * Tìm kiếm Phiếu Đặt hàng theo idPDH hoặc idKH.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<PhieuDatHang> searchPhieuDatHang(String idPDH, String idKH) {
        return phieuDatHangDAO.search(idPDH, idKH);
    }
}
