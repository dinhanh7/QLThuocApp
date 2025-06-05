package gui;

import controller.PhieuDatHangController;
import entities.PhieuDatHang;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.Dimension;

/**
 * PhieuDatHangPanel.java (đã bổ sung chức năng Tìm kiếm)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (IDPDH, IDKH, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/chỉ hiển thị khi Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310)
 */
public class PhieuDatHangPanel extends JPanel {

    private JTable tblPhieuDatHang;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtIdPDH, txtIdKH, txtDiaChi, txtPTThanhToan, txtTrangThai, txtTongTien;
    private JFormattedTextField txtThoiGian;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdPDH, txtSearchIdKH;
    private JButton   btnSearch;

    // nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private PhieuDatHangController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public PhieuDatHangPanel() {
        controller = new PhieuDatHangController();
        initComponents();
        initSearchPanel();
        initInputPanel(false);
        loadDataToTable();
    }

    /**
     * Khởi tạo các thành phần chính:
     *  - Nút Thêm, Sửa, Xóa, Làm mới (y = 10)
     *  - JTable (y = 200, cao = 310)
     */
    private void initComponents() {
    	//setPreferredSize(new Dimension(1600, 800));
        setLayout(null);

        // --- Nút chức năng (y = 10) --- //
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

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 10, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> onRefresh());

        // --- Bảng dữ liệu (y = 200, cao = 310) --- //
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[]{
            "IDPDH", "Thời gian", "IDKH", "Tổng tiền", "Địa chỉ", "PTTT", "Trạng thái"
        });
        tblPhieuDatHang = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblPhieuDatHang);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblPhieuDatHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuDatHang.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - txtSearchIdPDH, txtSearchIdKH, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdPDH = new JLabel("IDPDH:");
        lblSearchIdPDH.setBounds(0, 5, 60, 20);
        searchPanel.add(lblSearchIdPDH);

        txtSearchIdPDH = new JTextField();
        txtSearchIdPDH.setBounds(65, 3, 120, 25);
        searchPanel.add(txtSearchIdPDH);

        JLabel lblSearchIdKH = new JLabel("IDKH:");
        lblSearchIdKH.setBounds(200, 5, 50, 20);
        searchPanel.add(lblSearchIdKH);

        txtSearchIdKH = new JTextField();
        txtSearchIdKH.setBounds(255, 3, 120, 25);
        searchPanel.add(txtSearchIdKH);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(400, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (y = 90, cao = 100), ẩn khi visible = false.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        // IDPDH
        JLabel lblIdPDH = new JLabel("IDPDH:");
        lblIdPDH.setBounds(10, 10, 60, 25);
        inputPanel.add(lblIdPDH);
        txtIdPDH = new JTextField();
        txtIdPDH.setBounds(80, 10, 120, 25);
        inputPanel.add(txtIdPDH);

        // Thời gian
        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(220, 10, 70, 25);
        inputPanel.add(lblThoiGian);
        txtThoiGian = new JFormattedTextField();
        txtThoiGian.setBounds(275, 10, 150, 25);
        inputPanel.add(txtThoiGian);

        // IDKH
        JLabel lblIdKH = new JLabel("IDKH:");
        lblIdKH.setBounds(449, 10, 50, 25);
        inputPanel.add(lblIdKH);
        txtIdKH = new JTextField();
        txtIdKH.setBounds(491, 10, 60, 25);
        inputPanel.add(txtIdKH);

        // Tổng tiền
        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setBounds(575, 10, 70, 25);
        inputPanel.add(lblTongTien);
        txtTongTien = new JTextField();
        txtTongTien.setBounds(653, 10, 100, 25);
        inputPanel.add(txtTongTien);

        // Địa chỉ
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(10, 45, 60, 25);
        inputPanel.add(lblDiaChi);
        txtDiaChi = new JTextField();
        txtDiaChi.setBounds(80, 45, 200, 25);
        inputPanel.add(txtDiaChi);

        // Phương thức thanh toán
        JLabel lblPTTT = new JLabel("PTTT:");
        lblPTTT.setBounds(288, 45, 50, 25);
        inputPanel.add(lblPTTT);
        txtPTThanhToan = new JTextField();
        txtPTThanhToan.setBounds(322, 45, 100, 25);
        inputPanel.add(txtPTThanhToan);

        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(449, 45, 70, 25);
        inputPanel.add(lblTrangThai);
        txtTrangThai = new JTextField();
        txtTrangThai.setBounds(530, 45, 100, 25);
        inputPanel.add(txtTrangThai);

        // Nút Lưu
        btnSave = new JButton("Lưu");
        btnSave.setBounds(800, 10, 60, 30);
        inputPanel.add(btnSave);
        btnSave.addActionListener(e -> onSave());

        // Nút Hủy
        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(800, 50, 60, 30);
        inputPanel.add(btnCancel);
        btnCancel.addActionListener(e -> onCancel());

        inputPanel.setVisible(visible);
    }

    /**
     * Tải toàn bộ dữ liệu vào JTable (khi khởi động hoặc khi làm mới).
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<PhieuDatHang> list = controller.getAllPhieuDatHang();
        for (PhieuDatHang pdh : list) {
            tblModel.addRow(new Object[]{
                pdh.getIdPDH(),
                DateHelper.toString(pdh.getThoiGian(), "dd/MM/yyyy HH:mm"),
                pdh.getIdKH(),
                pdh.getTongTien(),
                pdh.getDiaChi(),
                pdh.getPhuongThucThanhToan(),
                pdh.getTrangThai()
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idPDH, idKH, gọi controller.searchPhieuDatHang(...),
     * hiển thị kết quả, nếu có ít nhất 1 dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idPDH = txtSearchIdPDH.getText().trim();
        String idKH = txtSearchIdKH.getText().trim();

        List<PhieuDatHang> results = controller.searchPhieuDatHang(idPDH, idKH);

        tblModel.setRowCount(0);
        for (PhieuDatHang pdh : results) {
            tblModel.addRow(new Object[]{
                pdh.getIdPDH(),
                DateHelper.toString(pdh.getThoiGian(), "dd/MM/yyyy HH:mm"),
                pdh.getIdKH(),
                pdh.getTongTien(),
                pdh.getDiaChi(),
                pdh.getPhuongThucThanhToan(),
                pdh.getTrangThai()
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblPhieuDatHang.setRowSelectionInterval(0, 0);
                tblPhieuDatHang.scrollRectToVisible(tblPhieuDatHang.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == NONE).
     */
    private void populateInputFromTable(int row) {
        txtIdPDH.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdKH.setText((String) tblModel.getValueAt(row, 2));
        txtTongTien.setText(tblModel.getValueAt(row, 3).toString());
        txtDiaChi.setText((String) tblModel.getValueAt(row, 4));
        txtPTThanhToan.setText((String) tblModel.getValueAt(row, 5));
        txtTrangThai.setText((String) tblModel.getValueAt(row, 6));
    }

    /**
     * Ẩn inputPanel và reset fields, enable lại phần tìm kiếm, bảng và các nút.
     */
    private void hideInputPanel() {
        txtIdPDH.setText("");
        txtThoiGian.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");
        txtDiaChi.setText("");
        txtPTThanhToan.setText("");
        txtTrangThai.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblPhieuDatHang.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdPDH.setEnabled(true);
        txtSearchIdKH.setEnabled(true);
    }

    /**
     * Khi bấm “Thêm”: hiện inputPanel, reset ô, disable các thành phần còn lại.
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdPDH.setText("");
        txtThoiGian.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");
        txtDiaChi.setText("");
        txtPTThanhToan.setText("");
        txtTrangThai.setText("");

        txtIdPDH.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblPhieuDatHang.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdPDH.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
    }

    /**
     * Khi bấm “Sửa”: phải có dòng được chọn, điền dữ liệu vào inputPanel, disable các thành phần khác.
     */
    private void onEdit() {
        int row = tblPhieuDatHang.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn phiếu đặt hàng cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtIdPDH.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblPhieuDatHang.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdPDH.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
    }

    /** 
     * Khi bấm “Xóa”: phải có dòng được chọn, xác nhận, gọi controller.deletePhieuDatHang(idPDH).
     */
    private void onDelete() {
        int row = tblPhieuDatHang.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn phiếu đặt hàng cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa phiếu đặt hàng " + id + "?", "Xác nhận");
        if (confirm) {
            if (controller.deletePhieuDatHang(id)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
            } else {
                MessageDialog.showError(this, "Xóa thất bại!", "Lỗi");
            }
        }
    }

    /**
     * Khi bấm “Làm mới”: ẩn inputPanel (nếu đang hiển thị) và load lại danh sách.
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Lưu” trong inputPanel:
     *  - validate dữ liệu,
     *  - nếu ADDING, gọi addPhieuDatHang,
     *    nếu EDITING, gọi updatePhieuDatHang,
     *  - ẩn inputPanel, load lại dữ liệu.
     */
    private void onSave() {
        if (!Validator.isDate(txtThoiGian.getText(), "dd/MM/yyyy HH:mm")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm", "Cảnh báo");
            return;
        }
        if (!Validator.isDouble(txtTongTien.getText())) {
            MessageDialog.showWarning(this, "Tổng tiền phải là số", "Cảnh báo");
            return;
        }
        PhieuDatHang pdh = new PhieuDatHang();
        pdh.setIdPDH(txtIdPDH.getText().trim());
        pdh.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        pdh.setIdKH(txtIdKH.getText().trim());
        pdh.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));
        pdh.setDiaChi(txtDiaChi.getText().trim());
        pdh.setPhuongThucThanhToan(txtPTThanhToan.getText().trim());
        pdh.setTrangThai(txtTrangThai.getText().trim());

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addPhieuDatHang(pdh);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updatePhieuDatHang(pdh);
            if (success) {
                MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
                return;
            }
        }

        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Hủy” trong inputPanel: chỉ cần ẩn inputPanel.
     */
    private void onCancel() {
        hideInputPanel();
    }
}
