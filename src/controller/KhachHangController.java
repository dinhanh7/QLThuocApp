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
}
