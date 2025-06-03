package controller;

import dao.KhachHangDAO;
import entities.KhachHang;

import java.util.List;

/**
 * KhachHangController.java
 * Quản lý luồng xử lý nghiệp vụ cho Khách hàng (CRUD).
 */
public class KhachHangController {
    private KhachHangDAO khachHangDAO;

    public KhachHangController() {
        khachHangDAO = new KhachHangDAO();
    }

    /**
     * Lấy danh sách tất cả Khách hàng.
     * @return List<KhachHang>
     */
    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.getAll();
    }

    /**
     * Lấy thông tin một Khách hàng theo ID.
     * @param idKH mã khách hàng
     * @return KhachHang hoặc null nếu không tìm thấy
     */
    public KhachHang getKhachHangById(String idKH) {
        if (idKH == null || idKH.trim().isEmpty()) {
            return null;
        }
        return khachHangDAO.getById(idKH);
    }

    /**
     * Thêm mới một Khách hàng.
     * @param kh đối tượng KhachHang cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addKhachHang(KhachHang kh) {
        if (kh == null) {
            return false;
        }
        // Kiểm tra idKH không để trống
        if (kh.getIdKH() == null || kh.getIdKH().trim().isEmpty()) {
            return false;
        }
        // Kiểm tra họ tên không để trống
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            return false;
        }
        // Số điện thoại có thể kiểm tra thêm định dạng nếu cần

        return khachHangDAO.insert(kh);
    }

    /**
     * Cập nhật thông tin một Khách hàng.
     * @param kh đối tượng KhachHang đã được sửa
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateKhachHang(KhachHang kh) {
        if (kh == null) {
            return false;
        }
        if (kh.getIdKH() == null || kh.getIdKH().trim().isEmpty()) {
            return false;
        }
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            return false;
        }
        return khachHangDAO.update(kh);
    }

    /**
     * Xóa một Khách hàng theo ID.
     * @param idKH mã khách hàng cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteKhachHang(String idKH) {
        if (idKH == null || idKH.trim().isEmpty()) {
            return false;
        }
        return khachHangDAO.delete(idKH);
    }
}
