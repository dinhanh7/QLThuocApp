package controller;

import dao.PhanHoiDAO;
import entities.PhanHoi;

import java.util.List;

/**
 * PhanHoiController.java
 *
 * Giữ nguyên các phương thức CRUD cơ bản (getAllPhanHoi, addPhanHoi, updatePhanHoi, deletePhanHoi),
 * và bổ sung thêm searchPhanHoi(String idPH, String idKH).
 */
public class PhanHoiController {

    private PhanHoiDAO phanHoiDAO;

    public PhanHoiController() {
        phanHoiDAO = new PhanHoiDAO();
    }

    public List<PhanHoi> getAllPhanHoi() {
        return phanHoiDAO.getAll();
    }

    public boolean addPhanHoi(PhanHoi ph) {
        return phanHoiDAO.insert(ph);
    }

    public boolean updatePhanHoi(PhanHoi ph) {
        return phanHoiDAO.update(ph);
    }

    public boolean deletePhanHoi(String idPH) {
        return phanHoiDAO.delete(idPH);
    }

    /**
     * Tìm kiếm Phản hồi theo idPH hoặc idKH.
     * Nếu cả hai tham số đều rỗng, trả về toàn bộ danh sách.
     */
    public List<PhanHoi> searchPhanHoi(String idPH, String idKH) {
        return phanHoiDAO.search(idPH, idKH);
    }
    //thêm chức năng 
    /**
     * Gửi phản hồi ở chế độ khách.
     * Nếu số điện thoại đã có thì dùng idKH cũ, nếu chưa có thì thêm mới khách hàng với idKH = KH0xxx.
     * Mỗi phản hồi có idPH = PHxxx tăng dần.
     */
    public boolean addPhanHoiGuest(String idHD, String sdt, String noiDung, int danhGia) {
        if (!hoaDonDAO.exists(idHD)) {
            System.out.println("Hóa đơn không tồn tại!");
            return false;
        }

        // Lấy idKH từ hóa đơn
        String idKH = hoaDonDAO.getKhachHangIdByHoaDonId(idHD); // <-- phương thức bạn cần tạo

        if (idKH == null) {
            System.out.println("Không tìm thấy khách hàng cho hóa đơn này!");
            return false;
        }

        String idPH = generateNextPhanHoiId();
        Date thoiGian = new Date();

        PhanHoi ph = new PhanHoi(idPH, idKH, idHD, noiDung, thoiGian, danhGia);
        return phanHoiDAO.insert(ph);
    }



    /**
     * Sinh mã idKH cho khách mới (dạng KH001, KH002, ...)
     */
    private String generateNextGuestIdKH() {
        List<KhachHang> list = new KhachHangDAO().getAll();
        int max = 0;
        for (KhachHang kh : list) {
            if (kh.getIdKH().startsWith("KH0")) {
                try {
                    int num = Integer.parseInt(kh.getIdKH().substring(3));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("KH0%03d", max + 1);
    }

    /**
     * Sinh mã idPH cho phản hồi mới (dạng PH001, PH002, ...)
     */
    private String generateNextPhanHoiId() {
        List<PhanHoi> list = phanHoiDAO.getAll();
        int max = 0;
        for (PhanHoi ph : list) {
            if (ph.getIdPH().startsWith("PH")) {
                try {
                    int num = Integer.parseInt(ph.getIdPH().substring(2));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("PH%03d", max + 1);
    }
}
