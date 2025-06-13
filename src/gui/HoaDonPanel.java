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

    private JTable tblHoaDon;
    private DefaultTableModel tblModel;

    private JPanel inputPanel;
    private JTextField txtIdHD, txtThoiGian, txtIdNV, txtIdKH, txtTongTien;
    private JTextField txtPhuongThucThanhToan, txtTrangThaiDonHang;
    private JLabel lblDiemHienTaiValue;
    private JTextField txtDiemSuDung;
    private JLabel lblThanhTienValue;
    private JButton btnSave, btnCancel;

    private JTextField txtSearchIdHD, txtSearchIdNV, txtSearchIdKH;
    private JButton btnSearch;

    private JButton btnAdd, btnEdit, btnDelete, btnViewDetail, btnRefresh;

    private HoaDonController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public HoaDonPanel() {
        controller = new HoaDonController();
        initComponents();
        initSearchPanel();
        initInputPanel(false);
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
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
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

    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        JLabel lblIdHD = new JLabel("IDHD:");
        lblIdHD.setBounds(10, 10, 40, 25);
        inputPanel.add(lblIdHD);
        txtIdHD = new JTextField();
        txtIdHD.setBounds(60, 10, 100, 25);
        inputPanel.add(txtIdHD);

        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(180, 10, 70, 25);
        inputPanel.add(lblThoiGian);
        txtThoiGian = new JTextField();
        txtThoiGian.setBounds(262, 10, 150, 25);
        inputPanel.add(txtThoiGian);

        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(420, 10, 40, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(475, 10, 100, 25);
        inputPanel.add(txtIdNV);

        JLabel lblIdKH = new JLabel("IDKH:");
        lblIdKH.setBounds(626, 10, 40, 25);
        inputPanel.add(lblIdKH);
        txtIdKH = new JTextField();
        txtIdKH.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String idKH = txtIdKH.getText().trim();
                if (idKH.isEmpty()) {
                	lblDiemHienTaiValue.setText("");
                    return;
                }
                // Gọi sang controller lấy điểm tích lũy hiện tại
                int diemHienCo = controller.getDiemHienTai(idKH);
                lblDiemHienTaiValue.setText("");
                lblDiemHienTaiValue.setText(String.valueOf(diemHienCo));
                lblDiemHienTaiValue.repaint();
                capNhatThanhTienSauGiam(); // Cập nhật luôn số tiền phải trả nếu có thay đổi điểm
            }
        });
        txtIdKH.setBounds(663, 10, 100, 25);
        inputPanel.add(txtIdKH);

        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setBounds(10, 40, 70, 25);
        inputPanel.add(lblTongTien);
        txtTongTien = new JTextField();
        txtTongTien.setBounds(60, 40, 100, 25);
        inputPanel.add(txtTongTien);

        JLabel lblPTTT = new JLabel("PT Thanh toán:");
        lblPTTT.setBounds(180, 40, 100, 25);
        inputPanel.add(lblPTTT);
        txtPhuongThucThanhToan = new JTextField();
        txtPhuongThucThanhToan.setBounds(262, 40, 150, 25);
        inputPanel.add(txtPhuongThucThanhToan);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(420, 40, 70, 25);
        inputPanel.add(lblTrangThai);
        txtTrangThaiDonHang = new JTextField();
        txtTrangThaiDonHang.setBounds(475, 40, 100, 25);
        inputPanel.add(txtTrangThaiDonHang);
        JLabel lblDiemHienTai = new JLabel("Điểm hiện có:");
        lblDiemHienTai.setBounds(10, 70, 80, 25);
        inputPanel.add(lblDiemHienTai);

        lblDiemHienTaiValue = new JLabel("0");
        lblDiemHienTaiValue.setBounds(90, 70, 100, 25);
        inputPanel.add(lblDiemHienTaiValue);

        JLabel lblDiemSuDung = new JLabel("Sử dụng điểm:");
        lblDiemSuDung.setBounds(180, 70, 80, 25);
        inputPanel.add(lblDiemSuDung);

        txtDiemSuDung = new JTextField("0");
        txtDiemSuDung.setBounds(260, 70, 50, 25);
        inputPanel.add(txtDiemSuDung);

        JLabel lblThanhTienSauGiam = new JLabel("Thanh toán:");
        lblThanhTienSauGiam.setBounds(340, 70, 80, 25);
        inputPanel.add(lblThanhTienSauGiam);

        lblThanhTienValue = new JLabel("0");
        lblThanhTienValue.setBounds(420, 70, 100, 25);
        inputPanel.add(lblThanhTienValue);

        btnSave = new JButton("Lưu");
        btnSave.setBounds(800, 10, 60, 30);
        inputPanel.add(btnSave);
        btnSave.addActionListener(e -> onSave());

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(800, 50, 60, 30);
        inputPanel.add(btnCancel);
        btnCancel.addActionListener(e -> onCancel());

        inputPanel.setVisible(visible);
     // Thêm vào cuối các trường nhập ở inputPanel (sau dòng txtTrangThaiDonHang)


        txtDiemSuDung.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                capNhatThanhTienSauGiam();
            }
        });
        txtTongTien.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                capNhatThanhTienSauGiam();
            }
        });
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

    private void populateInputFromTable(int row) {
        txtIdHD.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdNV.setText((String) tblModel.getValueAt(row, 2));
        txtIdKH.setText((String) tblModel.getValueAt(row, 3));
        txtTongTien.setText((String) tblModel.getValueAt(row, 4));
        txtPhuongThucThanhToan.setText((String) tblModel.getValueAt(row, 5));
        txtTrangThaiDonHang.setText((String) tblModel.getValueAt(row, 6));
    }

    private void hideInputPanel() {
        txtIdHD.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");
        txtPhuongThucThanhToan.setText("");
        txtTrangThaiDonHang.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnViewDetail.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblHoaDon.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdHD.setEnabled(true);
        txtSearchIdNV.setEnabled(true);
        txtSearchIdKH.setEnabled(true);
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
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);
        txtIdHD.setEditable(false);

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
        hideInputPanel();
        loadDataToTable();
    }

    private void onSave() {
        String idHD = txtIdHD.getText().trim();
        String thoiGianStr = txtThoiGian.getText().trim();
        String idNV = txtIdNV.getText().trim();
        String idKH = txtIdKH.getText().trim();
        String tongTienStr = txtTongTien.getText().trim();
        String phuongThuc = txtPhuongThucThanhToan.getText().trim();
        String trangThai = txtTrangThaiDonHang.getText().trim();

        if (idHD.isEmpty()) {
            MessageDialog.showWarning(this, "IDHD không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isDateTime(thoiGianStr, "dd/MM/yyyy HH:mm:ss")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm:ss!", "Cảnh báo");
            return;
        }
        if (idNV.isEmpty()) {
            MessageDialog.showWarning(this, "IDNV không được để trống!", "Cảnh báo");
            return;
        }
        if (idKH.isEmpty()) {
            MessageDialog.showWarning(this, "IDKH không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isDouble(tongTienStr)) {
            MessageDialog.showWarning(this, "Tổng tiền phải là số!", "Cảnh báo");
            return;
        }
        if (trangThai.isEmpty()) {
            MessageDialog.showWarning(this, "Trạng thái đơn hàng không được để trống!", "Cảnh báo");
            return;
        }

        // Lấy số điểm sử dụng để trừ
        int diemSuDung = 0;
        try {
            diemSuDung = Integer.parseInt(txtDiemSuDung.getText().trim());
            if (diemSuDung < 0) diemSuDung = 0;
        } catch (Exception ex) {
            diemSuDung = 0;
        }

        // Lấy số tiền đã trừ điểm
        double tongTienSauGiam = 0;
        try {
            tongTienSauGiam = Double.parseDouble(lblThanhTienValue.getText().trim());
        } catch (Exception ex) {
            tongTienSauGiam = 0;
        }

        HoaDon hd = new HoaDon();
        hd.setIdHD(idHD);
        hd.setThoiGian(DateHelper.toDateTime(thoiGianStr, "dd/MM/yyyy HH:mm:ss"));
        hd.setIdNV(idNV);
        hd.setIdKH(idKH);
        hd.setTongTien(tongTienSauGiam);  // LƯU TIỀN ĐÃ TRỪ ĐIỂM
        hd.setPhuongThucThanhToan(phuongThuc.isEmpty() ? null : phuongThuc);
        hd.setTrangThaiDonHang(trangThai);

        boolean success;
        StringBuilder errorMsg = new StringBuilder();
        if (currentMode.equals("EDITING")) {
            success = controller.updateHoaDon(hd, errorMsg);
            if (success) {
                MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, errorMsg.length() > 0 ? errorMsg.toString() : "Cập nhật thất bại!", "Lỗi");
                return;
            }
        }
        // Nếu vẫn muốn ADDING, hãy thông báo chuyển sang dùng nút Thêm
        else if (currentMode.equals("ADDING")) {
            MessageDialog.showWarning(this, "Vui lòng bấm nút Thêm để nhập hóa đơn mới!", "Thông báo");
            return;
        }

        hideInputPanel();
        loadDataToTable();
    }


    private void onCancel() {
        hideInputPanel();
    }
    private void capNhatThanhTienSauGiam() {
        try {
            double tongTien = Double.parseDouble(txtTongTien.getText().trim());
            int diemHienCo = Integer.parseInt(lblDiemHienTaiValue.getText());
            int diemSuDung = Integer.parseInt(txtDiemSuDung.getText().trim());
            if (diemSuDung > diemHienCo) diemSuDung = diemHienCo;
            if (diemSuDung < 0) diemSuDung = 0;
            double thanhTien = tongTien - diemSuDung * 1000; // 1 điểm = 1000đ, tuỳ bạn quy đổi
            if (thanhTien < 0) thanhTien = 0;
            lblThanhTienValue.setText(String.format("%.0f", thanhTien));
        } catch (Exception ex) {
            lblThanhTienValue.setText("0");
        }
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