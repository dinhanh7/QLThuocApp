package gui;

import controller.NhanVienController;
import entities.NhanVien;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * NhanVienPanel.java (đã bổ sung chức năng Tìm kiếm)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (IDNV, Họ tên, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/chỉ hiển thị khi Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310)
 */
public class NhanVienPanel extends JPanel {

    private JTable tblNhanVien;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtIdNV, txtHoTen, txtSDT, txtGioiTinh, txtNamSinh;
    private JFormattedTextField txtNgayVaoLam;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdNV, txtSearchHoTen;
    private JButton   btnSearch;

    // nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private NhanVienController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public NhanVienPanel() {
        controller = new NhanVienController();
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
                "IDNV", "Họ tên", "SĐT", "Giới tính", "Năm sinh", "Ngày vào làm"
        });
        tblNhanVien = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - txtSearchIdNV, txtSearchHoTen, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchId = new JLabel("IDNV:");
        lblSearchId.setBounds(0, 5, 50, 20);
        searchPanel.add(lblSearchId);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(55, 3, 120, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchHoTen = new JLabel("Họ tên:");
        lblSearchHoTen.setBounds(200, 5, 60, 20);
        searchPanel.add(lblSearchHoTen);

        txtSearchHoTen = new JTextField();
        txtSearchHoTen.setBounds(260, 3, 150, 25);
        searchPanel.add(txtSearchHoTen);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(440, 3, 100, 25);
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

        // IDNV
        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(10, 10, 50, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(70, 10, 120, 25);
        inputPanel.add(txtIdNV);

        // Họ tên
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setBounds(220, 10, 60, 25);
        inputPanel.add(lblHoTen);
        txtHoTen = new JTextField();
        txtHoTen.setBounds(280, 10, 150, 25);
        inputPanel.add(txtHoTen);

        // SĐT
        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setBounds(450, 10, 40, 25);
        inputPanel.add(lblSDT);
        txtSDT = new JTextField();
        txtSDT.setBounds(500, 10, 120, 25);
        inputPanel.add(txtSDT);

        // Giới tính
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setBounds(630, 10, 60, 25);
        inputPanel.add(lblGioiTinh);
        txtGioiTinh = new JTextField();
        txtGioiTinh.setBounds(700, 10, 100, 25);
        inputPanel.add(txtGioiTinh);

        // Năm sinh
        JLabel lblNamSinh = new JLabel("Năm sinh:");
        lblNamSinh.setBounds(10, 45, 60, 25);
        inputPanel.add(lblNamSinh);
        txtNamSinh = new JTextField();
        txtNamSinh.setBounds(70, 45, 100, 25);
        inputPanel.add(txtNamSinh);

        // Ngày vào làm
        JLabel lblNgayVaoLam = new JLabel("Ngày vào:");
        lblNgayVaoLam.setBounds(200, 45, 70, 25);
        inputPanel.add(lblNgayVaoLam);
        txtNgayVaoLam = new JFormattedTextField();
        txtNgayVaoLam.setBounds(280, 45, 120, 25);
        inputPanel.add(txtNgayVaoLam);

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
        List<NhanVien> list = controller.getAllNhanVien();
        for (NhanVien nv : list) {
            tblModel.addRow(new Object[]{
                nv.getIdNV(),
                nv.getHoTen(),
                nv.getSdt(),
                nv.getGioiTinh(),
                nv.getNamSinh(),
                DateHelper.toString(nv.getNgayVaoLam(), "dd/MM/yyyy")
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idNV và hoTen từ 2 ô, gọi controller.searchNhanVien(...),
     * hiển thị kết quả, nếu có ít nhất 1 dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idNV = txtSearchIdNV.getText().trim();
        String hoTen = txtSearchHoTen.getText().trim();

        List<NhanVien> results = controller.searchNhanVien(idNV, hoTen);

        tblModel.setRowCount(0);
        for (NhanVien nv : results) {
            tblModel.addRow(new Object[]{
                nv.getIdNV(),
                nv.getHoTen(),
                nv.getSdt(),
                nv.getGioiTinh(),
                nv.getNamSinh(),
                DateHelper.toString(nv.getNgayVaoLam(), "dd/MM/yyyy")
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblNhanVien.setRowSelectionInterval(0, 0);
                tblNhanVien.scrollRectToVisible(tblNhanVien.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == NONE).
     */
    private void populateInputFromTable(int row) {
        txtIdNV.setText((String) tblModel.getValueAt(row, 0));
        txtHoTen.setText((String) tblModel.getValueAt(row, 1));
        txtSDT.setText((String) tblModel.getValueAt(row, 2));
        txtGioiTinh.setText((String) tblModel.getValueAt(row, 3));
        txtNamSinh.setText(tblModel.getValueAt(row, 4).toString());
        txtNgayVaoLam.setText((String) tblModel.getValueAt(row, 5));
    }

    /**
     * Ẩn inputPanel và reset fields, enable lại phần tìm kiếm, bảng và các nút.
     */
    private void hideInputPanel() {
        txtIdNV.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtGioiTinh.setText("");
        txtNamSinh.setText("");
        txtNgayVaoLam.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblNhanVien.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdNV.setEnabled(true);
        txtSearchHoTen.setEnabled(true);
    }

    /**
     * Khi bấm “Thêm”: hiện inputPanel, reset ô, disable các thành phần còn lại.
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdNV.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtGioiTinh.setText("");
        txtNamSinh.setText("");
        txtNgayVaoLam.setText("");

        txtIdNV.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblNhanVien.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchHoTen.setEnabled(false);
    }

    /**
     * Khi bấm “Sửa”: phải có dòng được chọn, điền dữ liệu vào inputPanel, disable các thành phần khác.
     */
    private void onEdit() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn nhân viên cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtIdNV.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblNhanVien.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchHoTen.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”: phải có dòng được chọn, xác nhận, gọi controller.deleteNhanVien(idNV).
     */
    private void onDelete() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn nhân viên cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa nhân viên " + id + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteNhanVien(id)) {
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
     *  - nếu ADDING, gọi addNhanVien,
     *    nếu EDITING, gọi updateNhanVien,
     *  - ẩn inputPanel, load lại dữ liệu.
     */
    private void onSave() {
        if (!Validator.isNullOrEmpty(txtIdNV.getText()) && !Validator.isInteger(txtNamSinh.getText())) {
            MessageDialog.showWarning(this, "Năm sinh phải là số", "Cảnh báo");
            return;
        }
        if (!Validator.isDate(txtNgayVaoLam.getText(), "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Ngày vào làm phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return;
        }
        NhanVien nv = new NhanVien();
        nv.setIdNV(txtIdNV.getText().trim());
        nv.setHoTen(txtHoTen.getText().trim());
        nv.setSdt(txtSDT.getText().trim());
        nv.setGioiTinh(txtGioiTinh.getText().trim());
        nv.setNamSinh(Integer.parseInt(txtNamSinh.getText().trim()));
        nv.setNgayVaoLam(DateHelper.toDate(txtNgayVaoLam.getText().trim(), "dd/MM/yyyy"));

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addNhanVien(nv);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateNhanVien(nv);
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
