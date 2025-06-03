package gui;

import controller.HopDongController;
import entities.HopDong;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * HopDongPanel.java (đã bổ sung chức năng Tìm kiếm)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (IDHDong, IDNV, IDNCC, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/chỉ hiển thị khi Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310)
 */
public class HopDongPanel extends JPanel {

    private JTable tblHopDong;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtIdHDong, txtNoiDung, txtIdNV, txtIdNCC;
    private JFormattedTextField txtNgayBatDau, txtNgayKetThuc;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdHDong, txtSearchIdNV, txtSearchIdNCC;
    private JButton   btnSearch;

    // nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private HopDongController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public HopDongPanel() {
        controller = new HopDongController();
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
            "IDHDong", "Ngày bắt đầu", "Ngày kết thúc", "Nội dung", "IDNV", "IDNCC"
        });
        tblHopDong = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblHopDong);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblHopDong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblHopDong.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - txtSearchIdHDong, txtSearchIdNV, txtSearchIdNCC, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdHDong = new JLabel("IDHDong:");
        lblSearchIdHDong.setBounds(0, 5, 70, 20);
        searchPanel.add(lblSearchIdHDong);

        txtSearchIdHDong = new JTextField();
        txtSearchIdHDong.setBounds(75, 3, 100, 25);
        searchPanel.add(txtSearchIdHDong);

        JLabel lblSearchIdNV = new JLabel("IDNV:");
        lblSearchIdNV.setBounds(190, 5, 50, 20);
        searchPanel.add(lblSearchIdNV);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(245, 3, 100, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchIdNCC = new JLabel("IDNCC:");
        lblSearchIdNCC.setBounds(360, 5, 60, 20);
        searchPanel.add(lblSearchIdNCC);

        txtSearchIdNCC = new JTextField();
        txtSearchIdNCC.setBounds(425, 3, 100, 25);
        searchPanel.add(txtSearchIdNCC);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(560, 3, 100, 25);
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

        // IDHDong
        JLabel lblIdHDong = new JLabel("IDHDong:");
        lblIdHDong.setBounds(10, 10, 70, 25);
        inputPanel.add(lblIdHDong);
        txtIdHDong = new JTextField();
        txtIdHDong.setBounds(90, 10, 120, 25);
        inputPanel.add(txtIdHDong);

        // Ngày bắt đầu
        JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu:");
        lblNgayBatDau.setBounds(220, 10, 90, 25);
        inputPanel.add(lblNgayBatDau);
        txtNgayBatDau = new JFormattedTextField();
        txtNgayBatDau.setBounds(320, 10, 120, 25);
        inputPanel.add(txtNgayBatDau);

        // Ngày kết thúc
        JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        lblNgayKetThuc.setBounds(460, 10, 90, 25);
        inputPanel.add(lblNgayKetThuc);
        txtNgayKetThuc = new JFormattedTextField();
        txtNgayKetThuc.setBounds(560, 10, 120, 25);
        inputPanel.add(txtNgayKetThuc);

        // Nội dung
        JLabel lblNoiDung = new JLabel("Nội dung:");
        lblNoiDung.setBounds(10, 45, 60, 25);
        inputPanel.add(lblNoiDung);
        txtNoiDung = new JTextField();
        txtNoiDung.setBounds(80, 45, 300, 25);
        inputPanel.add(txtNoiDung);

        // IDNV
        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(400, 45, 50, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(450, 45, 100, 25);
        inputPanel.add(txtIdNV);

        // IDNCC
        JLabel lblIdNCC = new JLabel("IDNCC:");
        lblIdNCC.setBounds(580, 45, 60, 25);
        inputPanel.add(lblIdNCC);
        txtIdNCC = new JTextField();
        txtIdNCC.setBounds(640, 45, 100, 25);
        inputPanel.add(txtIdNCC);

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
        List<HopDong> list = controller.getAllHopDong();
        for (HopDong hd : list) {
            tblModel.addRow(new Object[]{
                hd.getIdHDong(),
                DateHelper.toString(hd.getNgayBatDau(), "dd/MM/yyyy"),
                DateHelper.toString(hd.getNgayKetThuc(), "dd/MM/yyyy"),
                hd.getNoiDung(),
                hd.getIdNV(),
                hd.getIdNCC()
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idHDong, idNV, idNCC, gọi controller.searchHopDong(...),
     * hiển thị kết quả, nếu có ít nhất 1 dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idHDong = txtSearchIdHDong.getText().trim();
        String idNV = txtSearchIdNV.getText().trim();
        String idNCC = txtSearchIdNCC.getText().trim();

        List<HopDong> results = controller.searchHopDong(idHDong, idNV, idNCC);

        tblModel.setRowCount(0);
        for (HopDong hd : results) {
            tblModel.addRow(new Object[]{
                hd.getIdHDong(),
                DateHelper.toString(hd.getNgayBatDau(), "dd/MM/yyyy"),
                DateHelper.toString(hd.getNgayKetThuc(), "dd/MM/yyyy"),
                hd.getNoiDung(),
                hd.getIdNV(),
                hd.getIdNCC()
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblHopDong.setRowSelectionInterval(0, 0);
                tblHopDong.scrollRectToVisible(tblHopDong.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == NONE).
     */
    private void populateInputFromTable(int row) {
        txtIdHDong.setText((String) tblModel.getValueAt(row, 0));
        txtNgayBatDau.setText((String) tblModel.getValueAt(row, 1));
        txtNgayKetThuc.setText((String) tblModel.getValueAt(row, 2));
        txtNoiDung.setText((String) tblModel.getValueAt(row, 3));
        txtIdNV.setText((String) tblModel.getValueAt(row, 4));
        txtIdNCC.setText((String) tblModel.getValueAt(row, 5));
    }

    /**
     * Ẩn inputPanel và reset fields, enable lại phần tìm kiếm, bảng và các nút.
     */
    private void hideInputPanel() {
        txtIdHDong.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtNoiDung.setText("");
        txtIdNV.setText("");
        txtIdNCC.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblHopDong.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdHDong.setEnabled(true);
        txtSearchIdNV.setEnabled(true);
        txtSearchIdNCC.setEnabled(true);
    }

    /**
     * Khi bấm “Thêm”: hiện inputPanel, reset ô, disable các thành phần còn lại.
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdHDong.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtNoiDung.setText("");
        txtIdNV.setText("");
        txtIdNCC.setText("");

        txtIdHDong.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblHopDong.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdHDong.setEnabled(true);
        txtSearchIdNV.setEnabled(true);
        txtSearchIdNCC.setEnabled(true);
    }

    /**
     * Khi bấm “Sửa”: phải có dòng được chọn, điền dữ liệu vào inputPanel, disable các thành phần khác.
     */
    private void onEdit() {
        int row = tblHopDong.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hợp đồng cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtIdHDong.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblHopDong.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdHDong.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdNCC.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”: phải có dòng được chọn, xác nhận, gọi controller.deleteHopDong(idHDong).
     */
    private void onDelete() {
        int row = tblHopDong.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hợp đồng cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa hợp đồng " + id + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteHopDong(id)) {
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
     *  - nếu ADDING, gọi addHopDong,
     *    nếu EDITING, gọi updateHopDong,
     *  - ẩn inputPanel, load lại dữ liệu.
     */
    private void onSave() {
        if (!Validator.isDate(txtNgayBatDau.getText(), "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Ngày bắt đầu phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return;
        }
        if (!Validator.isDate(txtNgayKetThuc.getText(), "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Ngày kết thúc phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return;
        }
        HopDong hd = new HopDong();
        hd.setIdHDong(txtIdHDong.getText().trim());
        hd.setNgayBatDau(DateHelper.toDate(txtNgayBatDau.getText().trim(), "dd/MM/yyyy"));
        hd.setNgayKetThuc(DateHelper.toDate(txtNgayKetThuc.getText().trim(), "dd/MM/yyyy"));
        hd.setNoiDung(txtNoiDung.getText().trim());
        hd.setIdNV(txtIdNV.getText().trim());
        hd.setIdNCC(txtIdNCC.getText().trim());

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addHopDong(hd);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateHopDong(hd);
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
