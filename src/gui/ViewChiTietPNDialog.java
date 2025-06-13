package gui;

import entities.ChiTietPhieuNhap;
import dao.ChiTietPhieuNhapDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewChiTietPNDialog.java
 *
 * Hiển thị chi tiết Phiếu nhập (idPN) trong JDialog.
 * Bảng giờ sẽ có 3 cột: "Tên thuốc", "Số lượng", "Đơn giá".
 */
public class ViewChiTietPNDialog extends JDialog {
    private JTable tblChiTiet;
    private DefaultTableModel tblModel;

    /**
     * @param parent Cửa sổ cha (MainFrame hoặc Panel)
     * @param idPN   Mã Phiếu nhập cần xem chi tiết
     */
    public ViewChiTietPNDialog(Window parent, String idPN) {
        super(parent, "Chi tiết Phiếu nhập: " + idPN, ModalityType.APPLICATION_MODAL);
        setSize(500, 300);
        setLocationRelativeTo(parent);

        initComponents();
        loadData(idPN);
    }

    /**
     * Khởi tạo giao diện: chỉ chứa JTable với 3 cột:
     *   "Tên thuốc", "Số lượng", "Đơn giá".
     */
    private void initComponents() {
        tblModel = new DefaultTableModel();
        // Đổi tên cột đầu thành "Tên Thuốc"
        tblModel.setColumnIdentifiers(new String[]{"ID Thuốc","Tên Thuốc", "Số lượng","Giá nhập"});
        tblChiTiet = new JTable(tblModel);

        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Lấy dữ liệu chi tiết từ DAO và hiển thị vào bảng.
     * DAO giờ trả về danh sách ChiTietPhieuNhap có sẵn tenThuoc.
     */
    private void loadData(String idPN) {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        List<ChiTietPhieuNhap> list = dao.getByIdPN(idPN);
        tblModel.setRowCount(0);

        for (ChiTietPhieuNhap ct : list) {
            tblModel.addRow(new Object[]{
            		ct.getIdThuoc(),  
                ct.getTenThuoc(), 
                ct.getSoLuong(),
                // Hiển thị DonGia với 1 chữ số thập phân (ví dụ 2000.0)
                String.format("%.1f", ct.getGiaNhap())
            });
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Phiếu nhập " + idPN + " không có chi tiết hoặc không tồn tại!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
