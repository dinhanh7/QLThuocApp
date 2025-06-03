package gui;

import controller.ThuocController;
import entities.Thuoc;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * ThuocPanel.java (đã bổ sung chức năng Tìm kiếm)
 *
 * Bố cục chính:
 *  - Dòng 1 (y = 10): Các nút chức năng (Thêm, Sửa, Xóa, Làm mới)
 *  - Dòng 2 (y = 50): Panel Tìm kiếm (IDThuoc, Tên thuốc, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel (ẩn/chỉ hiển thị khi Thêm/Sửa)
 *  - Dòng 4 (y = 200): JTable chứa dữ liệu, chiều cao 310 (đủ để hiển thị nhiều dòng)
 */
public class ThuocPanel extends JPanel {

    private JTable tblThuoc;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtId, txtTen, txtThanhPhan, txtIdDVT, txtIdDM,
                       txtIdXX, txtSoLuongTon, txtGiaNhap, txtDonGia;
    private JFormattedTextField txtHanSuDung;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdThuoc, txtSearchTenThuoc;
    private JButton   btnSearch;

    // nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private ThuocController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public ThuocPanel() {
        controller = new ThuocController();
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
                "IDThuoc", "Tên thuốc", "Thành phần", "IDDVT", "IDDM", "IDXX",
                "SL tồn", "Giá nhập", "Đơn giá", "Hạn sử dụng"
        });
        tblThuoc = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblThuoc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblThuoc.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - nhãn + txtSearchIdThuoc
     *  - nhãn + txtSearchTenThuoc
     *  - nút Tìm kiếm
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchId = new JLabel("IDThuoc:");
        lblSearchId.setBounds(0, 5, 60, 20);
        searchPanel.add(lblSearchId);

        txtSearchIdThuoc = new JTextField();
        txtSearchIdThuoc.setBounds(65, 3, 120, 25);
        searchPanel.add(txtSearchIdThuoc);

        JLabel lblSearchTen = new JLabel("Tên thuốc:");
        lblSearchTen.setBounds(200, 5, 80, 20);
        searchPanel.add(lblSearchTen);

        txtSearchTenThuoc = new JTextField();
        txtSearchTenThuoc.setBounds(270, 3, 150, 25);
        searchPanel.add(txtSearchTenThuoc);

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

        // IDThuoc
        JLabel lblId = new JLabel("IDThuoc:");
        lblId.setBounds(10, 10, 60, 25);
        inputPanel.add(lblId);
        txtId = new JTextField();
        txtId.setBounds(80, 10, 120, 25);
        inputPanel.add(txtId);

        // Tên thuốc
        JLabel lblTen = new JLabel("Tên thuốc:");
        lblTen.setBounds(220, 10, 80, 25);
        inputPanel.add(lblTen);
        txtTen = new JTextField();
        txtTen.setBounds(300, 10, 150, 25);
        inputPanel.add(txtTen);

        // Thành phần
        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setBounds(470, 10, 80, 25);
        inputPanel.add(lblThanhPhan);
        txtThanhPhan = new JTextField();
        txtThanhPhan.setBounds(550, 10, 150, 25);
        inputPanel.add(txtThanhPhan);

        // IDDVT
        JLabel lblIdDVT = new JLabel("ID DVT:");
        lblIdDVT.setBounds(10, 45, 60, 25);
        inputPanel.add(lblIdDVT);
        txtIdDVT = new JTextField();
        txtIdDVT.setBounds(80, 45, 120, 25);
        inputPanel.add(txtIdDVT);

        // IDDM
        JLabel lblIdDM = new JLabel("ID DM:");
        lblIdDM.setBounds(220, 45, 60, 25);
        inputPanel.add(lblIdDM);
        txtIdDM = new JTextField();
        txtIdDM.setBounds(300, 45, 150, 25);
        inputPanel.add(txtIdDM);

        // IDXX
        JLabel lblIdXX = new JLabel("ID XX:");
        lblIdXX.setBounds(470, 45, 60, 25);
        inputPanel.add(lblIdXX);
        txtIdXX = new JTextField();
        txtIdXX.setBounds(550, 45, 150, 25);
        inputPanel.add(txtIdXX);

        // Số lượng tồn
        JLabel lblSoLuongTon = new JLabel("SL tồn:");
        lblSoLuongTon.setBounds(720, 10, 50, 25);
        inputPanel.add(lblSoLuongTon);
        txtSoLuongTon = new JTextField();
        txtSoLuongTon.setBounds(770, 10, 70, 25);
        inputPanel.add(txtSoLuongTon);

        // Giá nhập
        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setBounds(720, 45, 60, 25);
        inputPanel.add(lblGiaNhap);
        txtGiaNhap = new JTextField();
        txtGiaNhap.setBounds(780, 45, 60, 25);
        inputPanel.add(txtGiaNhap);

        // Đơn giá
        JLabel lblDonGia = new JLabel("Đơn giá:");
        lblDonGia.setBounds(10, 75, 60, 25);
        inputPanel.add(lblDonGia);
        txtDonGia = new JTextField();
        txtDonGia.setBounds(80, 75, 100, 25);
        inputPanel.add(txtDonGia);

        // Hạn sử dụng
        JLabel lblHan = new JLabel("Hạn SD:");
        lblHan.setBounds(200, 75, 60, 25);
        inputPanel.add(lblHan);
        txtHanSuDung = new JFormattedTextField();
        txtHanSuDung.setBounds(260, 75, 120, 25);
        inputPanel.add(txtHanSuDung);

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
        List<Thuoc> list = controller.getAllThuoc();
        for (Thuoc t : list) {
            tblModel.addRow(new Object[]{
                    t.getIdThuoc(),
                    t.getTenThuoc(),
                    t.getThanhPhan(),
                    t.getIdDVT(),
                    t.getIdDM(),
                    t.getIdXX(),
                    t.getSoLuongTon(),
                    t.getGiaNhap(),
                    t.getDonGia(),
                    DateHelper.toString(t.getHanSuDung(), "dd/MM/yyyy")
            });
        }
    }

    /**
     * Khi người dùng bấm "Tìm kiếm".
     * Lấy giá trị trong txtSearchIdThuoc và txtSearchTenThuoc,
     * gọi controller.searchThuoc(...) để lấy kết quả,
     * rồi hiển thị lên JTable.
     * Nếu có kết quả, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idThuoc = txtSearchIdThuoc.getText().trim();
        String tenThuoc = txtSearchTenThuoc.getText().trim();

        List<Thuoc> results = controller.searchThuoc(idThuoc, tenThuoc);

        tblModel.setRowCount(0);
        for (Thuoc t : results) {
            tblModel.addRow(new Object[]{
                    t.getIdThuoc(),
                    t.getTenThuoc(),
                    t.getThanhPhan(),
                    t.getIdDVT(),
                    t.getIdDM(),
                    t.getIdXX(),
                    t.getSoLuongTon(),
                    t.getGiaNhap(),
                    t.getDonGia(),
                    DateHelper.toString(t.getHanSuDung(), "dd/MM/yyyy")
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblThuoc.setRowSelectionInterval(0, 0);
                tblThuoc.scrollRectToVisible(tblThuoc.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ JTable lên inputPanel (nếu currentMode == NONE).
     */
    private void populateInputFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtTen.setText((String) tblModel.getValueAt(row, 1));
        txtThanhPhan.setText((String) tblModel.getValueAt(row, 2));
        txtIdDVT.setText((String) tblModel.getValueAt(row, 3));
        txtIdDM.setText((String) tblModel.getValueAt(row, 4));
        txtIdXX.setText((String) tblModel.getValueAt(row, 5));
        txtSoLuongTon.setText(tblModel.getValueAt(row, 6).toString());
        txtGiaNhap.setText(tblModel.getValueAt(row, 7).toString());
        txtDonGia.setText(tblModel.getValueAt(row, 8).toString());
        txtHanSuDung.setText((String) tblModel.getValueAt(row, 9));
    }

    /**
     * Ẩn inputPanel và reset tất cả các trường, đồng thời enable lại
     * các nút chức năng, phần tìm kiếm và JTable.
     */
    private void hideInputPanel() {
        txtId.setText("");
        txtTen.setText("");
        txtThanhPhan.setText("");
        txtIdDVT.setText("");
        txtIdDM.setText("");
        txtIdXX.setText("");
        txtSoLuongTon.setText("");
        txtGiaNhap.setText("");
        txtDonGia.setText("");
        txtHanSuDung.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblThuoc.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdThuoc.setEnabled(true);
        txtSearchTenThuoc.setEnabled(true);
    }

    /**
     * Khi bấm "Thêm":
     *  - Hiển thị inputPanel
     *  - Reset trường
     *  - Disable các nút tìm kiếm, nút chức năng, JTable
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtId.setText("");
        txtTen.setText("");
        txtThanhPhan.setText("");
        txtIdDVT.setText("");
        txtIdDM.setText("");
        txtIdXX.setText("");
        txtSoLuongTon.setText("");
        txtGiaNhap.setText("");
        txtDonGia.setText("");
        txtHanSuDung.setText("");

        txtId.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblThuoc.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdThuoc.setEnabled(false);
        txtSearchTenThuoc.setEnabled(false);
    }

    /**
     * Khi bấm "Sửa":
     *  - Nếu chưa chọn dòng, hiện cảnh báo
     *  - Ngược lại, hiển thị inputPanel, điền dữ liệu vào, disable các thành phần khác
     */
    private void onEdit() {
        int row = tblThuoc.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn thuốc cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtId.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblThuoc.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdThuoc.setEnabled(false);
        txtSearchTenThuoc.setEnabled(false);
    }

    /**
     * Khi bấm "Xóa":
     *  - Nếu chưa chọn dòng, hiện cảnh báo
     *  - Ngược lại, hỏi confirm rồi xóa qua controller
     */
    private void onDelete() {
        int row = tblThuoc.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn thuốc cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa thuốc " + id + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteThuoc(id)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
            } else {
                MessageDialog.showError(this, "Xóa thất bại!", "Lỗi");
            }
        }
    }

    /**
     * Khi bấm "Làm mới":
     *  - Ẩn inputPanel (nếu đang hiển thị)
     *  - Load lại toàn bộ dữ liệu
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm "Lưu" trong inputPanel:
     *  - Kiểm tra dữ liệu hợp lệ
     *  - Nếu currentMode == ADDING, gọi addThuoc
     *  - Nếu currentMode == EDITING, gọi updateThuoc
     *  - Ẩn inputPanel và load lại dữ liệu
     */
    private void onSave() {
        if (!validateInput()) {
            return;
        }

        Thuoc t = new Thuoc();
        t.setIdThuoc(txtId.getText().trim());
        t.setTenThuoc(txtTen.getText().trim());
        t.setThanhPhan(txtThanhPhan.getText().trim());
        t.setIdDVT(txtIdDVT.getText().trim());
        t.setIdDM(txtIdDM.getText().trim());
        t.setIdXX(txtIdXX.getText().trim());
        t.setSoLuongTon(Integer.parseInt(txtSoLuongTon.getText().trim()));
        t.setGiaNhap(Double.parseDouble(txtGiaNhap.getText().trim()));
        t.setDonGia(Double.parseDouble(txtDonGia.getText().trim()));
        t.setHanSuDung(DateHelper.toDate(txtHanSuDung.getText().trim(), "dd/MM/yyyy"));

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addThuoc(t);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateThuoc(t);
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
     * Khi bấm "Hủy" trong inputPanel:
     *  - Chỉ cần ẩn inputPanel, không làm thay đổi dữ liệu
     */
    private void onCancel() {
        hideInputPanel();
    }

    /**
     * Kiểm tra dữ liệu trong các ô inputPanel trước khi Lưu.
     */
    private boolean validateInput() {
        if (Validator.isNullOrEmpty(txtId.getText())) {
            MessageDialog.showWarning(this, "IDThuoc không được để trống", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtTen.getText())) {
            MessageDialog.showWarning(this, "Tên thuốc không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isInteger(txtSoLuongTon.getText())) {
            MessageDialog.showWarning(this, "SL tồn phải là số nguyên", "Cảnh báo");
            return false;
        }
        if (!Validator.isDouble(txtGiaNhap.getText())) {
            MessageDialog.showWarning(this, "Giá nhập phải là số", "Cảnh báo");
            return false;
        }
        if (!Validator.isDouble(txtDonGia.getText())) {
            MessageDialog.showWarning(this, "Đơn giá phải là số", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtHanSuDung.getText(), "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Hạn sử dụng phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return false;
        }
        return true;
    }
}
