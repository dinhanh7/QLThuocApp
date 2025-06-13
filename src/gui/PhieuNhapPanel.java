package gui;

import controller.PhieuNhapController;
import entities.PhieuNhap;
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
 * PhieuNhapPanel.java
 *
 * Đã chỉnh:
 * 1. Cột "Tổng tiền" hiển thị dưới dạng String.format("%.1f", value) để không bị chuyển thành dạng khoa học (E).
 * 2. Thêm nút "Xem chi tiết" cạnh nút "Xóa". Khi bấm, sẽ mở dialog chi tiết (ViewChiTietPNDialog).
 */
public class PhieuNhapPanel extends JPanel {

    private JTable tblPhieuNhap;
    private DefaultTableModel tblModel;

    // Panel nhập liệu ẩn/hiện khi ADD/EDIT
    private JPanel inputPanel;
    private JTextField txtIdPN, txtThoiGian, txtIdNV, txtIdNCC, txtTongTien;
    private JButton btnSave, btnCancel;

    // Panel tìm kiếm
    private JTextField txtSearchIdPN, txtSearchIdNV, txtSearchIdNCC;
    private JButton btnSearch;

    // 5 nút chức năng: Thêm, Sửa, Xóa, Xem chi tiết, Làm mới
    private JButton btnAdd, btnEdit, btnDelete, btnViewDetail, btnRefresh;

    private PhieuNhapController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public PhieuNhapPanel() {
        controller = new PhieuNhapController();
        initComponents();
        initSearchPanel();
        initInputPanel(false);
        loadDataToTable();
    }

    /**
     * Khởi tạo các thành phần chính:
     *  - Nút Thêm, Sửa, Xóa, Xem chi tiết, Làm mới (y = 10)
     *  - JTable (y = 200, cao = 310) hiển thị 5 cột: IDPN, Thời gian, IDNV, IDNCC, Tổng tiền (định dạng chuẩn)
     */
    private void initComponents() {
    	setPreferredSize(new Dimension(1600, 1200));
        setLayout(null);

        // --- Nút chức năng (y = 10) --- //
        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 10, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> {
            AddPhieuNhapDialog dlg = new AddPhieuNhapDialog((JFrame)SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
            loadDataToTable(); // reload lại bảng khi đóng dialog
        });

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 10, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 10, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> onDelete());

        // **Nút "Xem chi tiết" (mới)**
        btnViewDetail = new JButton("Xem chi tiết");
        btnViewDetail.setBounds(280, 10, 110, 30);
        add(btnViewDetail);
        btnViewDetail.addActionListener(e -> onViewDetail());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(400, 10, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> onRefresh());

        // --- Bảng dữ liệu (y = 200, cao = 310) --- //
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[]{
            "IDPN", "Thời gian", "IDNV", "IDNCC", "Tổng tiền"
        });
        tblPhieuNhap = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblPhieuNhap);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblPhieuNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuNhap.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - txtSearchIdPN, txtSearchIdNV, txtSearchIdNCC, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdPN = new JLabel("IDPN:");
        lblSearchIdPN.setBounds(0, 5, 50, 20);
        searchPanel.add(lblSearchIdPN);

        txtSearchIdPN = new JTextField();
        txtSearchIdPN.setBounds(55, 3, 120, 25);
        searchPanel.add(txtSearchIdPN);

        JLabel lblSearchIdNV = new JLabel("IDNV:");
        lblSearchIdNV.setBounds(200, 5, 50, 20);
        searchPanel.add(lblSearchIdNV);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(255, 3, 120, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchIdNCC = new JLabel("IDNCC:");
        lblSearchIdNCC.setBounds(400, 5, 50, 20);
        searchPanel.add(lblSearchIdNCC);

        txtSearchIdNCC = new JTextField();
        txtSearchIdNCC.setBounds(455, 3, 120, 25);
        searchPanel.add(txtSearchIdNCC);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(600, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (y = 90, cao = 100), ẩn khi visible = false.
     * Gồm các ô: IDPN, ThoiGian, IDNV, IDNCC, TongTien
     * và 2 nút Lưu, Hủy.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        // IDPN
        JLabel lblIdPN = new JLabel("IDPN:");
        lblIdPN.setBounds(10, 10, 50, 25);
        inputPanel.add(lblIdPN);
        txtIdPN = new JTextField();
        txtIdPN.setBounds(70, 10, 100, 25);
        inputPanel.add(txtIdPN);

        // Thời gian
        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(200, 10, 70, 25);
        inputPanel.add(lblThoiGian);
        txtThoiGian = new JTextField();
        txtThoiGian.setBounds(280, 10, 100, 25);
        inputPanel.add(txtThoiGian);

        // IDNV
        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(210, 40, 40, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(280, 40, 100, 25);
        inputPanel.add(txtIdNV);

        // IDNCC
        JLabel lblIdNCC = new JLabel("IDNCC:");
        lblIdNCC.setBounds(444, 10, 50, 25);
        inputPanel.add(lblIdNCC);
        txtIdNCC = new JTextField();
        txtIdNCC.setBounds(487, 10, 100, 25);
        inputPanel.add(txtIdNCC);

        // Tổng tiền
        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setBounds(10, 40, 70, 25);
        inputPanel.add(lblTongTien);
        txtTongTien = new JTextField();
        txtTongTien.setBounds(70, 40, 100, 25);
        inputPanel.add(txtTongTien);

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
     * Load dữ liệu PhieuNhap vào JTable, gồm 5 cột:
     *  IDPN, Thời gian, IDNV, IDNCC, Tổng tiền (đã formatted).
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<PhieuNhap> list = controller.getAllPhieuNhap();
        for (PhieuNhap pn : list) {
            tblModel.addRow(new Object[]{
                pn.getIdPN(),
                DateHelper.toString(pn.getThoiGian(), "dd/MM/yyyy HH:mm"),
                pn.getIdNV(),
                pn.getIdNCC(),
                // Hiển thị Tổng tiền ở dạng 123000.0, không để E
                String.format("%.1f", pn.getTongTien())
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idPN, idNV, idNCC, gọi controller.searchPhieuNhap(...),
     * hiển thị kết quả lên table, chọn tự động dòng đầu tiên nếu có.
     */
    private void onSearch() {
        String idPN = txtSearchIdPN.getText().trim();
        String idNV = txtSearchIdNV.getText().trim();
        String idNCC = txtSearchIdNCC.getText().trim();

        List<PhieuNhap> results = controller.searchPhieuNhap(idPN, idNV, idNCC);

        tblModel.setRowCount(0);
        for (PhieuNhap pn : results) {
            tblModel.addRow(new Object[]{
                pn.getIdPN(),
                DateHelper.toString(pn.getThoiGian(), "dd/MM/yyyy HH:mm"),
                pn.getIdNV(),
                pn.getIdNCC(),
                String.format("%.1f", pn.getTongTien())
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblPhieuNhap.setRowSelectionInterval(0, 0);
                tblPhieuNhap.scrollRectToVisible(tblPhieuNhap.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ hàng bảng lên inputPanel (nếu currentMode == "NONE").
     */
    private void populateInputFromTable(int row) {
        txtIdPN.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdNV.setText((String) tblModel.getValueAt(row, 2));
        txtIdNCC.setText((String) tblModel.getValueAt(row, 3));
        txtTongTien.setText((String) tblModel.getValueAt(row, 4));
    }

    /**
     * Ẩn inputPanel và reset các ô, đồng thời enable lại các thành phần khác.
     */
    private void hideInputPanel() {
        txtIdPN.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdNCC.setText("");
        txtTongTien.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnViewDetail.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblPhieuNhap.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdPN.setEnabled(true);
        txtSearchIdNV.setEnabled(true);
        txtSearchIdNCC.setEnabled(true);
    }

    // ========================================
    // ========== Các phương thức xử lý ========
    // ========================================

    /**
     * Khi bấm “Thêm”:
     *  - Hiện inputPanel (rỗng),
     *  - Disable các thành phần khác,
     *  - currentMode = "ADDING".
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdPN.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdNCC.setText("");
        txtTongTien.setText("");

        txtIdPN.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnViewDetail.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblPhieuNhap.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdPN.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdNCC.setEnabled(false);
    }

    /**
     * Khi bấm “Sửa”:
     *  - Phải có dòng được chọn,
     *  - Điền dữ liệu lên inputPanel,
     *  - Disable các thành phần khác,
     *  - currentMode = "EDITING".
     */
    private void onEdit() {
        int row = tblPhieuNhap.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn phiếu nhập cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtIdPN.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnViewDetail.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblPhieuNhap.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdPN.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdNCC.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”:
     *  - Phải có dòng được chọn,
     *  - Xác nhận trước khi xóa,
     *  - Gọi controller.deletePhieuNhap(idPN), nếu thành công reload bảng.
     */
    private void onDelete() {
        int row = tblPhieuNhap.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn phiếu nhập cần xóa!", "Cảnh báo");
            return;
        }
        String idPN = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this,
                "Bạn có chắc muốn xóa Phiếu nhập " + idPN + "?", "Xác nhận");
        if (confirm) {
            if (controller.deletePhieuNhap(idPN)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
            } else {
                MessageDialog.showError(this, "Xóa thất bại!", "Lỗi");
            }
        }
    }

    /**
     * Khi bấm “Xem chi tiết”:
     *  - Phải có dòng được chọn,
     *  - Lấy idPN, mở dialog ViewChiTietPNDialog để hiển thị chi tiết.
     */
    private void onViewDetail() {
        int row = tblPhieuNhap.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn phiếu nhập để xem chi tiết!", "Cảnh báo");
            return;
        }
        String idPN = (String) tblModel.getValueAt(row, 0);
        // Mở dialog chi tiết
        ViewChiTietPNDialog dialog = new ViewChiTietPNDialog(SwingUtilities.getWindowAncestor(this), idPN);
        dialog.setVisible(true);
    }

    /**
     * Khi bấm “Làm mới”:
     *  - Ẩn inputPanel nếu đang hiển thị,
     *  - Reload lại dữ liệu bảng.
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Lưu” trong inputPanel:
     *  - Validate: IDPN, Thời gian, IDNV, IDNCC, Tổng tiền (phải đúng kiểu).
     *  - Nếu currentMode == "ADDING": gọi controller.addPhieuNhap(pn)
     *    Nếu currentMode == "EDITING": gọi controller.updatePhieuNhap(pn)
     *  - Ẩn inputPanel và reload bảng nếu thành công.
     */
    private void onSave() {
        String idPN = txtIdPN.getText().trim();
        String thoiGianStr = txtThoiGian.getText().trim();
        String idNV = txtIdNV.getText().trim();
        String idNCC = txtIdNCC.getText().trim();
        String tongTienStr = txtTongTien.getText().trim();

        if (idPN.isEmpty()) {
            MessageDialog.showWarning(this, "IDPN không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isDateTime(thoiGianStr, "dd/MM/yyyy HH:mm")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm!", "Cảnh báo");
            return;
        }
        if (idNV.isEmpty()) {
            MessageDialog.showWarning(this, "IDNV không được để trống!", "Cảnh báo");
            return;
        }
        if (idNCC.isEmpty()) {
            MessageDialog.showWarning(this, "IDNCC không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isDouble(tongTienStr)) {
            MessageDialog.showWarning(this, "Tổng tiền phải là số!", "Cảnh báo");
            return;
        }

        PhieuNhap pn = new PhieuNhap();
        pn.setIdPN(idPN);
        pn.setThoiGian(DateHelper.toDateTime(thoiGianStr, "dd/MM/yyyy HH:mm"));
        pn.setIdNV(idNV);
        pn.setIdNCC(idNCC);
        pn.setTongTien(Double.parseDouble(tongTienStr));

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addPhieuNhap(pn);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại! Kiểm tra IDPN hoặc kết nối DB.", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updatePhieuNhap(pn);
            if (success) {
                MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Cập nhật thất bại! Kiểm tra lại dữ liệu.", "Lỗi");
                return;
            }
        }

        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Hủy” trong inputPanel:
     *  - Chỉ cần ẩn inputPanel, không lưu.
     */
    private void onCancel() {
        hideInputPanel();
    }
}
