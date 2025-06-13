package controller;

import dao.ChiTietPhieuNhapDAO;
import entities.ChiTietPhieuNhap;

public class ChiTietPhieuNhapController {
    private ChiTietPhieuNhapDAO chiTietPhieuNhapDAO = new ChiTietPhieuNhapDAO();

    public boolean addChiTietPhieuNhap(ChiTietPhieuNhap ct) {
        return chiTietPhieuNhapDAO.insert(ct);
    }

    // Bạn có thể thêm các phương thức khác nếu cần
}
