package controller;

import dao.PhieuDatHangDAO;
import entities.PhieuDatHang;

import java.util.List;

public class PhieuDatHangController {
    private PhieuDatHangDAO phieuDatHangDAO;

    public PhieuDatHangController() {
        phieuDatHangDAO = new PhieuDatHangDAO();
    }

    // Lấy danh sách tất cả phiếu đặt hàng
    public List<PhieuDatHang> getAllPhieuDatHang() {
        return phieuDatHangDAO.getAll();
    }

    // Lấy phiếu đặt hàng theo id
    public PhieuDatHang getPhieuDatHangById(String idPDH) {
        return phieuDatHangDAO.getById(idPDH);
    }

    // Thêm mới phiếu đặt hàng
    public boolean addPhieuDatHang(PhieuDatHang pdh) {
        if (pdh.getIdPDH() == null || pdh.getIdPDH().trim().isEmpty()) {
            return false;
        }
        return phieuDatHangDAO.insert(pdh);
    }

    // Cập nhật phiếu đặt hàng
    public boolean updatePhieuDatHang(PhieuDatHang pdh) {
        if (pdh.getIdPDH() == null || pdh.getIdPDH().trim().isEmpty()) {
            return false;
        }
        return phieuDatHangDAO.update(pdh);
    }

    // Xóa phiếu đặt hàng
    public boolean deletePhieuDatHang(String idPDH) {
        if (idPDH == null || idPDH.trim().isEmpty()) {
            return false;
        }
        return phieuDatHangDAO.delete(idPDH);
    }
}
// PhieuDatHangController.java 
