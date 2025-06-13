package controller;

import dao.KhachHangDAO;

import entities.KhachHang;

import java.util.List;

/**
 * KhachHangController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllKhachHang, addKhachHang, updateKhachHang, deleteKhachHang),
 * và bổ sung thêm searchKhachHang(String hoTen, String sdt).
 */
public class KhachHangController {

    private KhachHangDAO khachHangDAO;

    public KhachHangController() {
        khachHangDAO = new KhachHangDAO();
    }

    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.getAll();
    }

    public boolean addKhachHang(KhachHang kh, StringBuilder errorMsg) {
        try {
            return khachHangDAO.insert(kh);
        } catch (RuntimeException ex) {
            if (errorMsg != null) errorMsg.append(ex.getMessage());
            return false;
        }
    }

    public boolean updateKhachHang(KhachHang kh, StringBuilder errorMsg) {
        try {
            return khachHangDAO.update(kh);
        } catch (RuntimeException ex) {
            if (errorMsg != null) errorMsg.append(ex.getMessage());
            return false;
        }
    }

    public boolean deleteKhachHang(String idKH, StringBuilder errorMsg) {
        try {
            return khachHangDAO.delete(idKH);
        } catch (RuntimeException ex) {
            if (errorMsg != null) errorMsg.append(ex.getMessage());
            return false;
        }
    }

    /**
     * Tìm kiếm Khách hàng theo hoTen hoặc sdt.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<KhachHang> searchKhachHang(String hoTen, String sdt) {
        return khachHangDAO.search(hoTen, sdt);
    }

    public KhachHang getBySDT(String sdt) {
        return khachHangDAO.getBySDT(sdt);
    }
    public boolean congDiem(String idKH, int soDiemCong) {
        return khachHangDAO.congDiem(idKH, soDiemCong);
    }

    // Trừ điểm (chỉ cho phép trừ khi đủ điểm)
    public boolean truDiem(String idKH, int soDiemTru) {
        KhachHang kh = khachHangDAO.getById(idKH);
        if (kh == null) return false;
        if (kh.getDiemTichLuy() < soDiemTru) return false;
        return khachHangDAO.truDiem(idKH, soDiemTru);
    }

    // Cập nhật điểm về giá trị cụ thể (nếu cần)
    public boolean updateDiemTichLuy(String idKH, int diemMoi) {
        return khachHangDAO.updateDiemTichLuy(idKH, diemMoi);
    }

    // Lấy điểm hiện tại
    public int getDiemHienTai(String idKH) {
        KhachHang kh = khachHangDAO.getById(idKH);
        if (kh != null) {
            return kh.getDiemTichLuy();
        }
        return 0;
    }
}