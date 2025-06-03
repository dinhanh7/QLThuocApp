package gui;

import controller.HoaDonController;
import entities.HoaDon;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * HoaDonPanel.java (đã bổ sung chức năng Tìm kiếm)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (IDHD, IDNV, IDKH, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/chỉ hiển thị khi Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310)
 */
public class HoaDonPanel extends JPanel {

    private JTable tblHoaDon;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtIdHD, txtIdNV, txtIdKH, txtTongTien;
    private JFormattedTextField txtThoiGian;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdHD, txtSearchIdNV, txtSearchIdKH;
    private JButton   btnSearch;

    // nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private HoaDonController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public HoaDonPanel() {
        controller = new HoaDonController();
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
            "IDHD", "Thời gian", "IDNV", "IDKH", "Tổng tiền"
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

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - txtSearchIdHD, txtSearchIdNV, txtSearchIdKH, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdHD = new JLabel("IDHD:");
        lblSearchIdHD.setBounds(0, 5, 50, 20);
        searchPanel.add(lblSearchIdHD);

        txtSearchIdHD = new JTextField();
        txtSearchIdHD.setBounds(55, 3, 100, 25);
        searchPanel.add(txtSearchIdHD);

        JLabel lblSearchIdNV = new JLabel("IDNV:");
        lblSearchIdNV.setBounds(170, 5, 50, 20);
        searchPanel.add(lblSearchIdNV);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(225, 3, 100, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchIdKH = new JLabel("IDKH:");
        lblSearchIdKH.setBounds(340, 5, 50, 20);
        searchPanel.add(lblSearchIdKH);

        txtSearchIdKH = new JTextField();
        txtSearchIdKH.setBounds(395, 3, 100, 25);
        searchPanel.add(txtSearchIdKH);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(530, 3, 100, 25);
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

        // IDHD
        JLabel lblIdHD = new JLabel("IDHD:");
        lblIdHD.setBounds(10, 10, 50, 25);
        inputPanel.add(lblIdHD);
        txtIdHD = new JTextField();
        txtIdHD.setBounds(70, 10, 120, 25);
        inputPanel.add(txtIdHD);

        // Thời gian
        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(220, 10, 70, 25);
        inputPanel.add(lblThoiGian);
        txtThoiGian = new JFormattedTextField();
        txtThoiGian.setBounds(300, 10, 150, 25);
        inputPanel.add(txtThoiGian);

        // IDNV
        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(470, 10, 50, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(530, 10, 100, 25);
        inputPanel.add(txtIdNV);

        // IDKH
        JLabel lblIdKH = new JLabel("IDKH:");
        lblIdKH.setBounds(650, 10, 50, 25);
        inputPanel.add(lblIdKH);
        txtIdKH = new JTextField();
        txtIdKH.setBounds(710, 10, 100, 25);
        inputPanel.add(txtIdKH);

        // Tổng tiền
        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setBounds(10, 45, 70, 25);
        inputPanel.add(lblTongTien);
        txtTongTien = new JTextField();
        txtTongTien.setBounds(90, 45, 120, 25);
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
     * Tải toàn bộ dữ liệu vào JTable (khi khởi động hoặc khi làm mới).
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<HoaDon> list = controller.getAllHoaDon();
        for (HoaDon hd : list) {
            tblModel.addRow(new Object[]{
                hd.getIdHD(),
                DateHelper.toString(hd.getThoiGian(), "dd/MM/yyyy HH:mm"),
                hd.getIdNV(),
                hd.getIdKH(),
                hd.getTongTien()
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idHD, idNV, idKH, gọi controller.searchHoaDon(...),
     * hiển thị kết quả, nếu có ít nhất 1 dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idHD = txtSearchIdHD.getText().trim();
        String idNV = txtSearchIdNV.getText().trim();
        String idKH = txtSearchIdKH.getText().trim();

        List<HoaDon> results = controller.searchHoaDon(idHD, idNV, idKH);

        tblModel.setRowCount(0);
        for (HoaDon hd : results) {
            tblModel.addRow(new Object[]{
                hd.getIdHD(),
                DateHelper.toString(hd.getThoiGian(), "dd/MM/yyyy HH:mm"),
                hd.getIdNV(),
                hd.getIdKH(),
                hd.getTongTien()
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblHoaDon.setRowSelectionInterval(0, 0);
                tblHoaDon.scrollRectToVisible(tblHoaDon.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == NONE).
     */
    private void populateInputFromTable(int row) {
        txtIdHD.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdNV.setText((String) tblModel.getValueAt(row, 2));
        txtIdKH.setText((String) tblModel.getValueAt(row, 3));
        txtTongTien.setText(tblModel.getValueAt(row, 4).toString());
    }

    /**
     * Ẩn inputPanel và reset fields, enable lại phần tìm kiếm, bảng và các nút.
     */
    private void hideInputPanel() {
        txtIdHD.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblHoaDon.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdHD.setEnabled(true);
        txtSearchIdNV.setEnabled(true);
        txtSearchIdKH.setEnabled(true);
    }

    /**
     * Khi bấm “Thêm”: hiện inputPanel, reset ô, disable các thành phần còn lại.
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdHD.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");

        txtIdHD.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblHoaDon.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdHD.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
    }

    /**
     * Khi bấm “Sửa”: phải có dòng được chọn, điền dữ liệu vào inputPanel, disable các thành phần khác.
     */
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
        btnRefresh.setEnabled(false);
        tblHoaDon.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdHD.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”: phải có dòng được chọn, xác nhận, gọi controller.deleteHoaDon(idHD).
     */
    private void onDelete() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hóa đơn cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa hóa đơn " + id + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteHoaDon(id)) {
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
     *  - nếu ADDING, gọi addHoaDon,
     *    nếu EDITING, gọi updateHoaDon,
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
        HoaDon hd = new HoaDon();
        hd.setIdHD(txtIdHD.getText().trim());
        hd.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        hd.setIdNV(txtIdNV.getText().trim());
        hd.setIdKH(txtIdKH.getText().trim());
        hd.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addHoaDon(hd);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateHoaDon(hd);
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
