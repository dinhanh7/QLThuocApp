package gui;

import entities.ChiTietHoaDon;
import dao.ChiTietHoaDonDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewChiTietHDDialog.java
 *
 * Hiển thị chi tiết Hóa đơn (idHD) trong JDialog dạng bảng.
 * Cột: "Tên Thuốc", "Số lượng", "Đơn giá".
 */
public class ViewChiTietHDDialog extends JDialog {
    private JTable tblChiTiet;
    private DefaultTableModel tblModel;

    /**
     * @param parent Cửa sổ cha (MainFrame hoặc Panel chứa nút Xem chi tiết).
     * @param idHD   Mã hóa đơn cần xem chi tiết.
     */
    public ViewChiTietHDDialog(Window parent, String idHD) {
        super(parent, "Chi tiết Hóa đơn: " + idHD, ModalityType.APPLICATION_MODAL);
        setSize(500, 300);
        setLocationRelativeTo(parent);

        initComponents();
        loadData(idHD);
    }

    /**
     * Khởi tạo giao diện: chỉ có một JTable với 3 cột:
     *   "Tên Thuốc", "Số lượng", "Đơn giá"
     */
    private void initComponents() {
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[]{"Tên thuốc", "Số lượng", "Đơn giá"});
        tblChiTiet = new JTable(tblModel);

        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Lấy dữ liệu từ DAO và đổ vào JTable.
     *
     * @param idHD Mã hóa đơn cần lấy chi tiết.
     */
    private void loadData(String idHD) {
        ChiTietHoaDonDAO dao = new ChiTietHoaDonDAO();
        List<ChiTietHoaDon> list = dao.getByIdHD(idHD);
        tblModel.setRowCount(0);

        for (ChiTietHoaDon ct : list) {
            tblModel.addRow(new Object[]{
                ct.getTenThuoc(),
                ct.getSoLuong(),
                // Hiển thị donGia kiểu bình thường, ví dụ 20000.0
                String.format("%.1f", ct.getDonGia())
            });
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Không tìm thấy chi tiết cho Hóa đơn: " + idHD,
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
