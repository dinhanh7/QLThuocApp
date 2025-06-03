package gui;

import controller.NhaCungCapController;
import entities.NhaCungCap;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * NhaCungCapPanel.java
 *
 * - Có 4 nút: Thêm, Sửa, Xóa, Làm mới (y = 10)
 * - Dưới đó (y = 50): panel Tìm kiếm (IDNCC, TenNCC, nút Tìm kiếm)
 * - Tiếp theo (y = 90): panel nhập liệu (ẩn khi khởi tạo)
 * - Cuối cùng (y = 200): JTable (cao khoảng 310) hiển thị kết quả.
 */
public class NhaCungCapPanel extends JPanel {

    private JTable tblNCC;
    private DefaultTableModel tblModel;

    // Panel nhập liệu (ẩn khi currentMode == "NONE")
    private JPanel inputPanel;
    private JTextField txtIdNCC, txtTenNCC, txtSdt, txtDiaChi;
    private JButton btnSave, btnCancel;

    // Panel tìm kiếm
    private JTextField txtSearchIdNCC, txtSearchTenNCC;
    private JButton btnSearch;

    // 4 nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private NhaCungCapController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public NhaCungCapPanel() {
        controller = new NhaCungCapController();
        initComponents();
        initSearchPanel();
        initInputPanel(false);
        loadDataToTable();
    }

    /**
     * Khởi tạo 4 nút chức năng (y = 10) và JTable (y = 200, cao = 310).
     */
    private void initComponents() {
        setLayout(null);

        // --- Nút Thêm ---
        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 10, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> onAdd());

        // --- Nút Sửa ---
        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 10, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        // --- Nút Xóa ---
        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 10, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> onDelete());

        // --- Nút Làm Mới ---
        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 10, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> onRefresh());

        // --- JTable hiển thị dữ liệu NhaCungCap (y = 200, cao = 310) ---
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[]{
            "IDNCC", "Tên NCC", "SĐT", "Địa chỉ"
        });
        tblNCC = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblNCC);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblNCC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNCC.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (IDNCC, TenNCC, nút Tìm kiếm) (y = 50, cao = 30).
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdNCC = new JLabel("IDNCC:");
        lblSearchIdNCC.setBounds(0, 5, 60, 20);
        searchPanel.add(lblSearchIdNCC);

        txtSearchIdNCC = new JTextField();
        txtSearchIdNCC.setBounds(65, 3, 120, 25);
        searchPanel.add(txtSearchIdNCC);

        JLabel lblSearchTenNCC = new JLabel("Tên NCC:");
        lblSearchTenNCC.setBounds(200, 5, 70, 20);
        searchPanel.add(lblSearchTenNCC);

        txtSearchTenNCC = new JTextField();
        txtSearchTenNCC.setBounds(275, 3, 150, 25);
        searchPanel.add(txtSearchTenNCC);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(450, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (ẩn khi visible = false) (y = 90, cao = 100):
     *  - txtIdNCC, txtTenNCC, txtSdt, txtDiaChi, btnSave, btnCancel.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        // IDNCC
        JLabel lblIdNCC = new JLabel("IDNCC:");
        lblIdNCC.setBounds(10, 10, 60, 25);
        inputPanel.add(lblIdNCC);
        txtIdNCC = new JTextField();
        txtIdNCC.setBounds(80, 10, 120, 25);
        inputPanel.add(txtIdNCC);

        // Tên NCC
        JLabel lblTenNCC = new JLabel("Tên NCC:");
        lblTenNCC.setBounds(220, 10, 70, 25);
        inputPanel.add(lblTenNCC);
        txtTenNCC = new JTextField();
        txtTenNCC.setBounds(300, 10, 200, 25);
        inputPanel.add(txtTenNCC);

        // SĐT
        JLabel lblSdt = new JLabel("SĐT:");
        lblSdt.setBounds(520, 10, 40, 25);
        inputPanel.add(lblSdt);
        txtSdt = new JTextField();
        txtSdt.setBounds(570, 10, 120, 25);
        inputPanel.add(txtSdt);

        // Địa chỉ
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(10, 45, 60, 25);
        inputPanel.add(lblDiaChi);
        txtDiaChi = new JTextField();
        txtDiaChi.setBounds(80, 45, 400, 25);
        inputPanel.add(txtDiaChi);

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
     * Load toàn bộ dữ liệu Nhà cung cấp vào JTable.
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<NhaCungCap> list = controller.getAllNhaCungCap();
        for (NhaCungCap ncc : list) {
            tblModel.addRow(new Object[]{
                ncc.getIdNCC(),
                ncc.getTenNCC(),
                ncc.getSdt(),
                ncc.getDiaChi()
            });
        }
    }

    /**
     * Xử lý khi nhấn nút “Tìm kiếm”:
     *  - Lấy giá trị từ txtSearchIdNCC, txtSearchTenNCC,
     *  - Gọi controller.searchNhaCungCap(...),
     *  - Hiển thị kết quả lên table; nếu có dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idNCC = txtSearchIdNCC.getText().trim();
        String tenNCC = txtSearchTenNCC.getText().trim();

        List<NhaCungCap> results = controller.searchNhaCungCap(idNCC, tenNCC);

        tblModel.setRowCount(0);
        for (NhaCungCap ncc : results) {
            tblModel.addRow(new Object[]{
                ncc.getIdNCC(),
                ncc.getTenNCC(),
                ncc.getSdt(),
                ncc.getDiaChi()
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblNCC.setRowSelectionInterval(0, 0);
                tblNCC.scrollRectToVisible(tblNCC.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ hàng table được chọn lên các ô input (nếu currentMode == "NONE").
     */
    private void populateInputFromTable(int row) {
        txtIdNCC.setText((String) tblModel.getValueAt(row, 0));
        txtTenNCC.setText((String) tblModel.getValueAt(row, 1));
        txtSdt.setText((String) tblModel.getValueAt(row, 2));
        txtDiaChi.setText((String) tblModel.getValueAt(row, 3));
    }

    /**
     * Ẩn inputPanel và reset các ô, enable lại các thành phần tìm kiếm và table.
     */
    private void hideInputPanel() {
        txtIdNCC.setText("");
        txtTenNCC.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblNCC.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdNCC.setEnabled(true);
        txtSearchTenNCC.setEnabled(true);
    }

    /**
     * Khi bấm “Thêm”:
     *  - Hiện inputPanel (rỗng),
     *  - Disable các nút khác, bảng và panel tìm kiếm,
     *  - currentMode = "ADDING".
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdNCC.setText("");
        txtTenNCC.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");

        txtIdNCC.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblNCC.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdNCC.setEnabled(false);
        txtSearchTenNCC.setEnabled(false);
    }

    /**
     * Khi bấm “Sửa”:
     *  - Phải có hàng được chọn trên table,
     *  - Điền dữ liệu lên inputPanel,
     *  - Disable các nút khác, bảng và panel tìm kiếm,
     *  - currentMode = "EDITING".
     */
    private void onEdit() {
        int row = tblNCC.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn Nhà cung cấp cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtIdNCC.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblNCC.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdNCC.setEnabled(false);
        txtSearchTenNCC.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”:
     *  - Phải có hàng được chọn,
     *  - Yêu cầu xác nhận trước khi xóa,
     *  - Gọi controller.deleteNhaCungCap(idNCC),
     *  - Nếu thành công, load lại table.
     */
    private void onDelete() {
        int row = tblNCC.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn Nhà cung cấp cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa Nhà cung cấp " + id + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteNhaCungCap(id)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
            } else {
                MessageDialog.showError(this, "Xóa thất bại!", "Lỗi");
            }
        }
    }

    /**
     * Khi bấm “Làm mới”:
     *  - Ẩn inputPanel nếu đang hiển thị,
     *  - Load lại toàn bộ dữ liệu.
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Lưu” trong inputPanel:
     *  - Kiểm tra dữ liệu hợp lệ (ID không được rỗng,
     *    Tên không được rỗng, SĐT phải đúng định dạng, v.v.),
     *  - Nếu currentMode == "ADDING": gọi addNhaCungCap,
     *    nếu currentMode == "EDITING": gọi updateNhaCungCap,
     *  - Ẩn inputPanel, reload table.
     */
    private void onSave() {
        String idNCC = txtIdNCC.getText().trim();
        String tenNCC = txtTenNCC.getText().trim();
        String sdt = txtSdt.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        if (idNCC.isEmpty()) {
            MessageDialog.showWarning(this, "IDNCC không được để trống!", "Cảnh báo");
            return;
        }
        if (tenNCC.isEmpty()) {
            MessageDialog.showWarning(this, "Tên Nhà cung cấp không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isPhone(sdt)) {
            MessageDialog.showWarning(this, "SĐT không hợp lệ!", "Cảnh báo");
            return;
        }
        // Địa chỉ có thể để trống hoặc kiểm tra tùy nhu cầu.

        NhaCungCap ncc = new NhaCungCap();
        ncc.setIdNCC(idNCC);
        ncc.setTenNCC(tenNCC);
        ncc.setSdt(sdt);
        ncc.setDiaChi(diaChi);

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addNhaCungCap(ncc);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại! Kiểm tra lại ID hoặc kết nối DB.", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateNhaCungCap(ncc);
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
     *  - Chỉ cần ẩn inputPanel, không thay đổi dữ liệu.
     */
    private void onCancel() {
        hideInputPanel();
    }
}
