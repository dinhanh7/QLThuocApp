package gui;

import controller.HoaDonController;


import entities.HoaDon;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import gui.AddHoaDonDialog;
import entities.HoaDon;
import entities.ChiTietHoaDon;
import java.util.List;
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

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 10, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> onAdd());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 10, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 10, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> onDelete());

        btnViewDetail = new JButton("Xem chi tiết");
        btnViewDetail.setBounds(280, 10, 110, 30);
        add(btnViewDetail);
        btnViewDetail.addActionListener(e -> onViewDetail());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(400, 10, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> onRefresh());

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
        searchPanel.setBounds(10, 50, 860, 30);
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
        btnSearch.setBounds(550, 3, 100, 25);
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
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hóa đơn cần sửa!", "Cảnh báo");
            return;
        }
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnViewDetail.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblHoaDon.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdHD.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
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
        // Mở dialog chi tiết hóa đơn (bạn cần có ViewChiTietHDDialog, không nằm trong code này)
        // ViewChiTietHDDialog dialog = new ViewChiTietHDDialog(SwingUtilities.getWindowAncestor(this), idHD);
        // dialog.setVisible(true);
        MessageDialog.showInfo(this, "Chức năng xem chi tiết hóa đơn đang được phát triển!", "Thông báo");
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
