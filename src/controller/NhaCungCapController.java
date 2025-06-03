package controller;

import dao.NhaCungCapDAO;
import entities.NhaCungCap;

import java.util.List;

/**
 * NhaCungCapController.java
 *
 * Giúp gọi tới các phương thức của DAO, đồng thời có thể bổ sung logic nghiệp vụ nếu cần.
 */
public class NhaCungCapController {

    private NhaCungCapDAO nccDAO;

    public NhaCungCapController() {
        nccDAO = new NhaCungCapDAO();
    }

    public List<NhaCungCap> getAllNhaCungCap() {
        return nccDAO.getAll();
    }

    public boolean addNhaCungCap(NhaCungCap ncc) {
        return nccDAO.insert(ncc);
    }

    public boolean updateNhaCungCap(NhaCungCap ncc) {
        return nccDAO.update(ncc);
    }

    public boolean deleteNhaCungCap(String idNCC) {
        return nccDAO.delete(idNCC);
    }

    /**
     * Tìm kiếm Nhà cung cấp theo idNCC hoặc tenNCC.
     */
    public List<NhaCungCap> searchNhaCungCap(String idNCC, String tenNCC) {
        return nccDAO.search(idNCC, tenNCC);
    }
}
