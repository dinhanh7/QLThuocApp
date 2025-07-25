package gui;

import controller.HoaDonController;


import entities.HoaDon;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import gui.AddHoaDonDialog;
import entities.HoaDon;
import entities.ChiTietHoaDon;
import java.util.List;
import java.util.Map;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.text.SimpleDateFormat;


public class HoaDonPanel extends JPanel {
	private HoaDonController controller;
    private JTable tblHoaDon;
    private DefaultTableModel tblModel;
    private JTextField txtSearchIdHD, txtSearchIdNV, txtSearchIdKH;
    private JButton btnSearch;
    private JButton btnTinhDoanhThu, btnDoanhThuTheoThang;
    private JButton btnAdd, btnEdit, btnDelete, btnViewDetail, btnRefresh;

    public HoaDonPanel() {
        controller = new HoaDonController();
        initComponents();
        initSearchPanel();
        loadDataToTable();
    }

    private void initComponents() {
    	setPreferredSize(new Dimension(1600,1200));
        setLayout(null);

        btnAdd = new JButton("     Thêm");
        btnAdd.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/hoaDonAdd.png")));
        btnAdd.setBounds(10, 10, 155, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> onAdd());

        btnEdit = new JButton("     Sửa");
        btnEdit.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/chungEdit.png")));
        btnEdit.setBounds(186, 10, 147, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        btnDelete = new JButton("     Xóa");
        btnDelete.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/chungDelete.png")));
        btnDelete.setBounds(355, 10, 185, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> onDelete());

        btnViewDetail = new JButton("  Xem chi tiết");
        btnViewDetail.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/chungDetail.png")));
        btnViewDetail.setBounds(10, 50, 155, 30);
        add(btnViewDetail);
        btnViewDetail.addActionListener(e -> onViewDetail());

        btnRefresh = new JButton("   Làm mới");
        btnRefresh.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/chungRefresh.png")));
        btnRefresh.setBounds(186, 50, 147, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> onRefresh());

	// Tinh doanh thu theo ngay
	btnTinhDoanhThu = new JButton("   Doanh thu ngày");
	btnTinhDoanhThu.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/chart-671.png")));
        btnTinhDoanhThu.setBounds(355, 50, 185, 30);
        add(btnTinhDoanhThu);
        btnTinhDoanhThu.addActionListener(e -> {
            JTextField fromField = new JTextField();
            JTextField toField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Từ ngày (yyyy-MM-dd):"));
            panel.add(fromField);
            panel.add(new JLabel("Đến ngày (yyyy-MM-dd):"));
            panel.add(toField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Chọn khoảng ngày", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String fromDate = fromField.getText();
                String toDate = toField.getText();

                Map<String, Integer> data = HoaDonController.tinhDoanhThuTheoNgay(fromDate, toDate);
                JFrame chartFrame = new JFrame("Biểu đồ Doanh thu");
                chartFrame.setSize(600, 400);
                chartFrame.getContentPane().add(new LineChartPanel(data));
                chartFrame.setVisible(true);
            }
        });
     // Tinh doanh thu theo thang
        btnDoanhThuTheoThang = new JButton("   Doanh thu tháng");
        btnDoanhThuTheoThang.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/chart-671.png")));
        btnDoanhThuTheoThang.setBounds(559, 50, 200, 30);  // Tuỳ chỉnh vị trí và kích thước
        add(btnDoanhThuTheoThang);

        btnDoanhThuTheoThang.addActionListener(e -> {
            String yearStr = JOptionPane.showInputDialog(this, "Nhập năm (VD: 2024):", "Chọn năm", JOptionPane.PLAIN_MESSAGE);
            if (yearStr == null || yearStr.trim().isEmpty()) return;

            try {
                int year = Integer.parseInt(yearStr.trim());
                Map<String, Integer> doanhThuTheoThang = HoaDonController.tinhDoanhThuTheoThang(year);

                int tong = doanhThuTheoThang.values().stream().mapToInt(Integer::intValue).sum();
                int trungBinh = doanhThuTheoThang.isEmpty() ? 0 : tong / doanhThuTheoThang.size();

                String title = "Biểu đồ doanh thu theo tháng - " + year;
                String tongText = "Tổng doanh thu: " + String.format("%,d", tong) + " VNĐ";
                String tbText = "Trung bình: " + String.format("%,d", trungBinh) + " VNĐ/tháng";

                LineChartPanel2 chartPanel = new LineChartPanel2(doanhThuTheoThang, title, tongText, tbText);
                JFrame frame = new JFrame(title);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.getContentPane().add(chartPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (NumberFormatException e11) {
                JOptionPane.showMessageDialog(this, "Năm không hợp lệ!");
            }
        });

	
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[]{
                "IDHD", "Thời gian", "IDNV", "IDKH", "Tổng tiền", "PT Thanh toán", "Trạng thái"
        });
        tblHoaDon = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblHoaDon.getSelectedRow();
            }
        });
    }

    
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 112, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdHD = new JLabel("IDHD:");
        lblSearchIdHD.setBounds(0, 5, 40, 20);
        searchPanel.add(lblSearchIdHD);

        txtSearchIdHD = new JTextField();
        txtSearchIdHD.setBounds(45, 3, 120, 25);
        searchPanel.add(txtSearchIdHD);

        JLabel lblSearchIdNV = new JLabel("IDNV:");
        lblSearchIdNV.setBounds(180, 5, 40, 20);
        searchPanel.add(lblSearchIdNV);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(225, 3, 120, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchIdKH = new JLabel("IDKH:");
        lblSearchIdKH.setBounds(360, 5, 40, 20);
        searchPanel.add(lblSearchIdKH);

        txtSearchIdKH = new JTextField();
        txtSearchIdKH.setBounds(405, 3, 120, 25);
        searchPanel.add(txtSearchIdKH);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setIcon(new ImageIcon(HoaDonPanel.class.getResource("/icon/chungSearch.png")));
        btnSearch.setBounds(556, 3, 130, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    
    

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<HoaDon> list = controller.getAllHoaDon();
        for (HoaDon hd : list) {
            tblModel.addRow(new Object[]{
                    hd.getIdHD(),
                    DateHelper.toString(hd.getThoiGian(), "dd/MM/yyyy HH:mm:ss"),
                    hd.getIdNV(),
                    hd.getIdKH(),
                    String.format("%.1f", hd.getTongTien()),
                    hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan() : "",
                    hd.getTrangThaiDonHang()
            });
        }
    }

    private void onSearch() {
        String idHD = txtSearchIdHD.getText().trim();
        String idNV = txtSearchIdNV.getText().trim();
        String idKH = txtSearchIdKH.getText().trim();

        List<HoaDon> results = controller.searchHoaDon(idHD, idNV, idKH);

        tblModel.setRowCount(0);
        for (HoaDon hd : results) {
            tblModel.addRow(new Object[]{
                    hd.getIdHD(),
                    DateHelper.toString(hd.getThoiGian(), "dd/MM/yyyy HH:mm:ss"),
                    hd.getIdNV(),
                    hd.getIdKH(),
                    String.format("%.1f", hd.getTongTien()),
                    hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan() : "",
                    hd.getTrangThaiDonHang()
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblHoaDon.setRowSelectionInterval(0, 0);
                tblHoaDon.scrollRectToVisible(tblHoaDon.getCellRect(0, 0, true));
            });
        }
    }
    // ======================= XỬ LÝ CHÍNH ===========================

    private void onAdd() {
        AddHoaDonDialog dialog = new AddHoaDonDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            HoaDon hd = dialog.getHoaDon();
            List<ChiTietHoaDon> chiTietList = dialog.getChiTietHoaDonList();

            StringBuilder errorMsg = new StringBuilder();
            boolean ok = controller.addHoaDon(hd, chiTietList, errorMsg);

            // XỬ LÝ TÍCH/ TRỪ ĐIỂM
            int diemSuDung = dialog.getDiemSuDung();
            double thanhToanSauGiam = dialog.getThanhTienSauGiam();

            if (ok) {
                // Trừ điểm cho khách nếu có sử dụng
                if (diemSuDung > 0) {
                    controller.truDiem(hd.getIdKH(), diemSuDung);
                }
                // Cộng điểm thưởng sau mua (VD: mỗi 100k được 1 điểm)
                int diemCong = (int) (thanhToanSauGiam / 100000);
                if (diemCong > 0) {
                    controller.congDiem(hd.getIdKH(), diemCong);
                }
                MessageDialog.showInfo(this, "Thêm hóa đơn thành công!", "Thông báo");
                reloadHoaDonTable();
            } else {
                MessageDialog.showError(this, "Lỗi: " + errorMsg.toString(), "Thêm hóa đơn thất bại");
            }
        }
    }


    private void onEdit() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hóa đơn cần sửa!", "Cảnh báo");
            return;
        }
        // Lấy ID hóa đơn từ bảng
        String idHD = tblModel.getValueAt(selectedRow, 0).toString();

        // Lấy thông tin hóa đơn (cần có hàm getHoaDonById)
        HoaDon hoaDon = controller.getHoaDonById(idHD);
        // Lấy danh sách chi tiết hóa đơn (cần có DAO hoặc Controller lấy danh sách này)
        List<ChiTietHoaDon> chiTietList = new dao.ChiTietHoaDonDAO().getByIdHD(idHD);

        // Tạo dialog sửa
        EditHoaDonDialog dialog = new EditHoaDonDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            hoaDon, 
            chiTietList
        );
        dialog.setVisible(true);

        // Nếu cập nhật thành công thì reload lại bảng
        if (dialog.isUpdated()) {
            loadDataToTable(); // Hoặc reloadHoaDonTable() nếu có
        }
    }

    private void onDelete() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hóa đơn cần xóa!", "Cảnh báo");
            return;
        }
        String idHD = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this,
                "Bạn có chắc muốn xóa hóa đơn " + idHD + "?", "Xác nhận");
        if (confirm) {
            StringBuilder errorMsg = new StringBuilder();
            if (controller.deleteHoaDon(idHD, errorMsg)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
            } else {
                MessageDialog.showError(this, errorMsg.length() > 0 ? errorMsg.toString() : "Xóa thất bại!", "Lỗi");
            }
        }
    }

    private void onViewDetail() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hóa đơn để xem chi tiết!", "Cảnh báo");
            return;
        }
        String idHD = (String) tblModel.getValueAt(row, 0);
        // Lấy đối tượng HoaDon từ controller (hoặc lấy từ model nếu đã lưu trong biến)
        List<HoaDon> all = controller.getAllHoaDon();
        HoaDon hd = null;
        for (HoaDon h : all) if (h.getIdHD().equals(idHD)) { hd = h; break; }
        if (hd == null) {
            MessageDialog.showError(this, "Không tìm thấy hóa đơn!", "Lỗi");
            return;
        }
        ViewHoaDonDetailDialog dialog = new ViewHoaDonDetailDialog(SwingUtilities.getWindowAncestor(this), hd);
        dialog.setVisible(true);
    }

    private void onRefresh() {
        loadDataToTable();
    }

    private void reloadHoaDonTable() {
        // Lấy danh sách hóa đơn mới nhất từ controller
        List<HoaDon> ds = controller.getAllHoaDon();

        // Xóa hết dữ liệu cũ trên bảng
        tblModel.setRowCount(0);

        // Thêm lại từng dòng hóa đơn mới
        for (HoaDon hd : ds) {
            Object[] rowData = {
                hd.getIdHD(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(hd.getThoiGian()),
                hd.getIdNV(),
                hd.getIdKH(),
                hd.getTongTien(),
                hd.getPhuongThucThanhToan(),
                hd.getTrangThaiDonHang()
            };
            tblModel.addRow(rowData);
        }
    }

}
