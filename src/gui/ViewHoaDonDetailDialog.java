package gui;

import entities.HoaDon;
import entities.ChiTietHoaDon;
import controller.HoaDonController;
import dao.ChiTietHoaDonDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ViewHoaDonDetailDialog extends JDialog {
    public ViewHoaDonDetailDialog(Window parent, HoaDon hoaDon) {
        super(parent, "Chi tiết hóa đơn " + hoaDon.getIdHD(), ModalityType.APPLICATION_MODAL);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Panel thông tin chung hóa đơn
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        infoPanel.add(new JLabel("Mã hóa đơn:"));
        infoPanel.add(new JLabel(hoaDon.getIdHD()));
        infoPanel.add(new JLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(hoaDon.getThoiGian())));
        infoPanel.add(new JLabel("ID Nhân viên:"));
        infoPanel.add(new JLabel(hoaDon.getIdNV()));
        infoPanel.add(new JLabel("ID Khách hàng:"));
        infoPanel.add(new JLabel(hoaDon.getIdKH()));
        infoPanel.add(new JLabel("Tổng tiền:"));
        infoPanel.add(new JLabel(String.format("%.1f", hoaDon.getTongTien())));
        infoPanel.add(new JLabel("PT Thanh toán:"));
        infoPanel.add(new JLabel(hoaDon.getPhuongThucThanhToan() != null ? hoaDon.getPhuongThucThanhToan() : ""));
        infoPanel.add(new JLabel("Trạng thái:"));
        infoPanel.add(new JLabel(hoaDon.getTrangThaiDonHang()));

        add(infoPanel, BorderLayout.NORTH);

        // Bảng chi tiết thuốc
        String[] colNames = {"ID thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0);
        JTable tbl = new JTable(model);
        JScrollPane scroll = new JScrollPane(tbl);
        add(scroll, BorderLayout.CENTER);

        // Lấy danh sách chi tiết thuốc
        ChiTietHoaDonDAO ctdao = new ChiTietHoaDonDAO();
        List<ChiTietHoaDon> ctList = ctdao.getByIdHD(hoaDon.getIdHD());
        for (ChiTietHoaDon ct : ctList) {
            model.addRow(new Object[]{
                ct.getIdThuoc(),
                ct.getTenThuoc(),
                ct.getSoLuong(),
                ct.getDonGia(),
                ct.getSoLuong() * ct.getDonGia()
            });
        }

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.add(btnClose);
        add(pnlBtn, BorderLayout.SOUTH);
    }
}
