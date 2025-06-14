package controller;

import dao.ChiTietPhieuNhapDAO;
import entities.ChiTietPhieuNhap;

public class ChiTietPhieuNhapController {
    private ChiTietPhieuNhapDAO chiTietPhieuNhapDAO = new ChiTietPhieuNhapDAO();

    public boolean addChiTietPhieuNhap(ChiTietPhieuNhap ct) {
        return chiTietPhieuNhapDAO.insert(ct);
    }
    public void deleteByPhieuNhapAndThuoc(String idPN, String idThuoc) {
        chiTietPhieuNhapDAO.deleteByPhieuNhapAndThuoc(idPN, idThuoc);
    }
    // Bạn có thể thêm các phương thức khác nếu cần
}
