package gui;

import controller.PhanHoiController;
import entities.PhanHoi;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * PhanHoiPanel.java (đã bổ sung chức năng Tìm kiếm)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (IDPH, IDKH, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/chỉ hiển thị khi Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310)
 */
public class PhanHoiPanel extends JPanel {

    private JTable tblPhanHoi;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtIdPH, txtIdKH, txtIdHD, txtIdThuoc, txtNoiDung, txtDanhGia;
    private JFormattedTextField txtThoiGian;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdPH, txtSearchIdKH;
    private JButton   btnSearch;

    // nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private PhanHoiController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public PhanHoiPanel() {
        controller = new PhanHoiController();
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
            "IDPH", "IDKH", "IDHD", "IDThuoc", "Nội dung", "Thời gian", "Đánh giá"
        });
        tblPhanHoi = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblPhanHoi);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblPhanHoi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhanHoi.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - txtSearchIdPH, txtSearchIdKH, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdPH = new JLabel("IDPH:");
        lblSearchIdPH.setBounds(0, 5, 50, 20);
        searchPanel.add(lblSearchIdPH);

        txtSearchIdPH = new JTextField();
        txtSearchIdPH.setBounds(55, 3, 120, 25);
        searchPanel.add(txtSearchIdPH);

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

        // IDPH
        JLabel lblIdPH = new JLabel("IDPH:");
        lblIdPH.setBounds(10, 10, 50, 25);
        inputPanel.add(lblIdPH);
        txtIdPH = new JTextField();
        txtIdPH.setBounds(70, 10, 120, 25);
        inputPanel.add(txtIdPH);

        // IDKH
        JLabel lblIdKH = new JLabel("IDKH:");
        lblIdKH.setBounds(220, 10, 50, 25);
        inputPanel.add(lblIdKH);
        txtIdKH = new JTextField();
        txtIdKH.setBounds(280, 10, 100, 25);
        inputPanel.add(txtIdKH);

        // IDHD
        JLabel lblIdHD = new JLabel("IDHD:");
        lblIdHD.setBounds(400, 10, 50, 25);
        inputPanel.add(lblIdHD);
        txtIdHD = new JTextField();
        txtIdHD.setBounds(460, 10, 100, 25);
        inputPanel.add(txtIdHD);

        // IDThuoc
        JLabel lblIdThuoc = new JLabel("IDThuoc:");
        lblIdThuoc.setBounds(580, 10, 60, 25);
        inputPanel.add(lblIdThuoc);
        txtIdThuoc = new JTextField();
        txtIdThuoc.setBounds(650, 10, 100, 25);
        inputPanel.add(txtIdThuoc);

        // Nội dung
        JLabel lblNoiDung = new JLabel("Nội dung:");
        lblNoiDung.setBounds(10, 45, 60, 25);
        inputPanel.add(lblNoiDung);
        txtNoiDung = new JTextField();
        txtNoiDung.setBounds(80, 45, 300, 25);
        inputPanel.add(txtNoiDung);

        // Thời gian
        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(400, 45, 70, 25);
        inputPanel.add(lblThoiGian);
        txtThoiGian = new JFormattedTextField();
        txtThoiGian.setBounds(480, 45, 150, 25);
        inputPanel.add(txtThoiGian);

        // Đánh giá
        JLabel lblDanhGia = new JLabel("Đánh giá:");
        lblDanhGia.setBounds(650, 45, 60, 25);
        inputPanel.add(lblDanhGia);
        txtDanhGia = new JTextField();
        txtDanhGia.setBounds(720, 45, 50, 25);
        inputPanel.add(txtDanhGia);

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
        List<PhanHoi> list = controller.getAllPhanHoi();
        for (PhanHoi ph : list) {
            tblModel.addRow(new Object[]{
                ph.getIdPH(),
                ph.getIdKH(),
                ph.getIdHD(),
                ph.getIdThuoc(),
                ph.getNoiDung(),
                DateHelper.toString(ph.getThoiGian(), "dd/MM/yyyy HH:mm"),
                ph.getDanhGia()
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idPH, idKH, gọi controller.searchPhanHoi(...),
     * hiển thị kết quả, nếu có ít nhất 1 dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idPH = txtSearchIdPH.getText().trim();
        String idKH = txtSearchIdKH.getText().trim();

        List<PhanHoi> results = controller.searchPhanHoi(idPH, idKH);

        tblModel.setRowCount(0);
        for (PhanHoi ph : results) {
            tblModel.addRow(new Object[]{
                ph.getIdPH(),
                ph.getIdKH(),
                ph.getIdHD(),
                ph.getIdThuoc(),
                ph.getNoiDung(),
                DateHelper.toString(ph.getThoiGian(), "dd/MM/yyyy HH:mm"),
                ph.getDanhGia()
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblPhanHoi.setRowSelectionInterval(0, 0);
                tblPhanHoi.scrollRectToVisible(tblPhanHoi.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == NONE).
     */
    private void populateInputFromTable(int row) {
        txtIdPH.setText((String) tblModel.getValueAt(row, 0));
        txtIdKH.setText((String) tblModel.getValueAt(row, 1));
        txtIdHD.setText((String) tblModel.getValueAt(row, 2));
        txtIdThuoc.setText((String) tblModel.getValueAt(row, 3));
        txtNoiDung.setText((String) tblModel.getValueAt(row, 4));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 5));
        txtDanhGia.setText(tblModel.getValueAt(row, 6).toString());
    }

    /**
     * Ẩn inputPanel và reset fields, enable lại phần tìm kiếm, bảng và các nút.
     */
    private void hideInputPanel() {
        txtIdPH.setText("");
        txtIdKH.setText("");
        txtIdHD.setText("");
        txtIdThuoc.setText("");
        txtNoiDung.setText("");
        txtThoiGian.setText("");
        txtDanhGia.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblPhanHoi.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdPH.setEnabled(true);
        txtSearchIdKH.setEnabled(true);
    }

    /**
     * Khi bấm “Thêm”: hiện inputPanel, reset ô, disable các thành phần còn lại.
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdPH.setText("");
        txtIdKH.setText("");
        txtIdHD.setText("");
        txtIdThuoc.setText("");
        txtNoiDung.setText("");
        txtThoiGian.setText("");
        txtDanhGia.setText("");

        txtIdPH.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblPhanHoi.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdPH.setEnabled(true);
        txtSearchIdKH.setEnabled(true);
    }

    /**
     * Khi bấm “Sửa”: phải có dòng được chọn, điền dữ liệu vào inputPanel, disable các thành phần khác.
     */
    private void onEdit() {
        int row = tblPhanHoi.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn phản hồi cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtIdPH.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblPhanHoi.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdPH.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”: phải có dòng được chọn, xác nhận, gọi controller.deletePhanHoi(idPH).
     */
    private void onDelete() {
        int row = tblPhanHoi.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn phản hồi cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa phản hồi " + id + "?", "Xác nhận");
        if (confirm) {
            if (controller.deletePhanHoi(id)) {
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
     *  - nếu ADDING, gọi addPhanHoi,
     *    nếu EDITING, gọi updatePhanHoi,
     *  - ẩn inputPanel, load lại dữ liệu.
     */
    private void onSave() {
        if (!Validator.isDate(txtThoiGian.getText(), "dd/MM/yyyy HH:mm")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm", "Cảnh báo");
            return;
        }
        if (!Validator.isInteger(txtDanhGia.getText())) {
            MessageDialog.showWarning(this, "Đánh giá phải là số nguyên", "Cảnh báo");
            return;
        }
        PhanHoi ph = new PhanHoi();
        ph.setIdPH(txtIdPH.getText().trim());
        ph.setIdKH(txtIdKH.getText().trim());
        ph.setIdHD(txtIdHD.getText().trim());
        ph.setIdThuoc(txtIdThuoc.getText().trim());
        ph.setNoiDung(txtNoiDung.getText().trim());
        ph.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        ph.setDanhGia(Integer.parseInt(txtDanhGia.getText().trim()));

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addPhanHoi(ph);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updatePhanHoi(ph);
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
