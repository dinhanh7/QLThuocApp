package gui;

import controller.NhanVienController;
import entities.NhanVien;
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
 * NhanVienPanel.java (đã sửa để hiển thị thêm cột luong và trangThai, đồng thời form Add/Edit có thêm hai ô nhập Luong và TrangThai)
 *
 * Bố cục:
 *  - Row 1 (y = 10): 4 nút chức năng Thêm, Sửa, Xóa, Làm mới
 *  - Row 2 (y = 50): Panel Tìm kiếm (IDNV, Họ tên, nút Tìm kiếm)
 *  - Row 3 (y = 90): inputPanel ẩn/hiện khi Add/Edit (có thêm 2 ô Luong, TrangThai)
 *  - Row 4 (y = 200): JTable (cao = 310) hiển thị toàn bộ cột mới
 */
public class NhanVienPanel extends JPanel {

    private JTable tblNhanVien;
    private DefaultTableModel tblModel;

    // Panel nhập liệu (ẩn khi currentMode == "NONE")
    private JPanel inputPanel;
    private JTextField txtIdNV, txtHoTen, txtSdt, txtGioiTinh, txtNamSinh, txtNgayVaoLam;
    private JTextField txtLuong, txtTrangThai;        // <-- mới
    private JTextField txtUsername, txtPassword;       // (vẫn giữ)
    private JButton btnSave, btnCancel;

    // Panel tìm kiếm
    private JTextField txtSearchIdNV, txtSearchHoTen;
    private JButton btnSearch;

    // 4 nút chức năng
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
     *  - JTable (y = 200, cao = 310) hiển thị 10 cột: IDNV, Họ tên, SĐT, Giới tính, Năm sinh, Ngày vào làm, Luong, TrangThai, Tài khoản, Mật khẩu
     */
    private void initComponents() {
    	setPreferredSize(new Dimension(1600, 1200));
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
            "IDNV", "Họ tên", "SĐT", "Giới tính", "Năm sinh", "Ngày vào làm",
            "Lương", "Trạng thái", "Tài khoản", "Mật khẩu"
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

        JLabel lblSearchIdNV = new JLabel("IDNV:");
        lblSearchIdNV.setBounds(0, 5, 50, 20);
        searchPanel.add(lblSearchIdNV);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(55, 3, 120, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchHoTen = new JLabel("Họ tên:");
        lblSearchHoTen.setBounds(200, 5, 60, 20);
        searchPanel.add(lblSearchHoTen);

        txtSearchHoTen = new JTextField();
        txtSearchHoTen.setBounds(265, 3, 150, 25);
        searchPanel.add(txtSearchHoTen);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(450, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (y = 90, cao = 100), ẩn khi visible = false.
     * Gồm các ô: IDNV, HoTen, Sdt, GioiTinh, NamSinh, NgayVaoLam, Luong, TrangThai, Username, Password,
     * và 2 nút Lưu, Hủy.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        // IDNV
        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(10, 10, 50, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(80, 10, 80, 25);
        inputPanel.add(txtIdNV);

        // Họ tên
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setBounds(200, 10, 60, 25);
        inputPanel.add(lblHoTen);
        txtHoTen = new JTextField();
        txtHoTen.setBounds(260, 10, 200, 25);
        inputPanel.add(txtHoTen);

        // SĐT
        JLabel lblSdt = new JLabel("SĐT:");
        lblSdt.setBounds(480, 10, 40, 25);
        inputPanel.add(lblSdt);
        txtSdt = new JTextField();
        txtSdt.setBounds(568, 10, 120, 25);
        inputPanel.add(txtSdt);

        // Giới tính
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setBounds(10, 40, 60, 25);
        inputPanel.add(lblGioiTinh);
        txtGioiTinh = new JTextField();
        txtGioiTinh.setBounds(80, 40, 80, 25);
        inputPanel.add(txtGioiTinh);

        // Năm sinh
        JLabel lblNamSinh = new JLabel("Năm sinh:");
        lblNamSinh.setBounds(623, 70, 65, 25);
        inputPanel.add(lblNamSinh);
        txtNamSinh = new JTextField();
        txtNamSinh.setBounds(691, 70, 80, 25);
        inputPanel.add(txtNamSinh);

        // Ngày vào làm
        JLabel lblNgayVaoLam = new JLabel("Ngày vào:");
        lblNgayVaoLam.setBounds(200, 40, 70, 25);
        inputPanel.add(lblNgayVaoLam);
        txtNgayVaoLam = new JTextField();
        txtNgayVaoLam.setBounds(260, 40, 120, 25);
        inputPanel.add(txtNgayVaoLam);

        // Lương
        JLabel lblLuong = new JLabel("Lương:");
        lblLuong.setBounds(440, 70, 60, 25);
        inputPanel.add(lblLuong);
        txtLuong = new JTextField();
        txtLuong.setBounds(505, 70, 97, 25);
        inputPanel.add(txtLuong);

        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(480, 45, 70, 25);
        inputPanel.add(lblTrangThai);
        txtTrangThai = new JTextField();
        txtTrangThai.setBounds(568, 40, 130, 25);
        inputPanel.add(txtTrangThai);

        // Tài khoản
        JLabel lblUsername = new JLabel("Tài khoản:");
        lblUsername.setBounds(10, 70, 70, 25);
        inputPanel.add(lblUsername);
        txtUsername = new JTextField();
        txtUsername.setBounds(79, 70, 103, 25);
        inputPanel.add(txtUsername);

        // Mật khẩu
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setBounds(200, 70, 60, 25);
        inputPanel.add(lblPassword);
        txtPassword = new JTextField();
        txtPassword.setBounds(260, 70, 150, 25);
        inputPanel.add(txtPassword);

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
     * Load dữ liệu NhanVien vào JTable, gồm các cột:
     *  IDNV, Họ tên, SĐT, Giới tính, Năm sinh, Ngày vào làm, Lương, Trạng thái, Tài khoản, Mật khẩu.
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
                DateHelper.toString(nv.getNgayVaoLam(), "dd/MM/yyyy"),
                nv.getLuong(),
                nv.getTrangThai(),
                nv.getUsername() != null ? nv.getUsername() : "",
                nv.getPassword() != null ? nv.getPassword() : ""
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idNV, hoTen, gọi controller.searchNhanVien(...),
     * hiển thị kết quả, chọn tự động dòng đầu tiên nếu có.
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
                DateHelper.toString(nv.getNgayVaoLam(), "dd/MM/yyyy"),
                nv.getLuong(),
                nv.getTrangThai(),
                nv.getUsername() != null ? nv.getUsername() : "",
                nv.getPassword() != null ? nv.getPassword() : ""
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
     * Điền dữ liệu từ hàng bảng lên inputPanel (nếu currentMode == "NONE").
     * Gồm cả hai cột mới Luong và TrangThai.
     */
    private void populateInputFromTable(int row) {
        txtIdNV.setText((String) tblModel.getValueAt(row, 0));
        txtHoTen.setText((String) tblModel.getValueAt(row, 1));
        txtSdt.setText((String) tblModel.getValueAt(row, 2));
        txtGioiTinh.setText((String) tblModel.getValueAt(row, 3));
        txtNamSinh.setText(tblModel.getValueAt(row, 4).toString());
        txtNgayVaoLam.setText((String) tblModel.getValueAt(row, 5));
        txtLuong.setText((String) tblModel.getValueAt(row, 6));
        txtTrangThai.setText((String) tblModel.getValueAt(row, 7));
        txtUsername.setText((String) tblModel.getValueAt(row, 8));
        txtPassword.setText((String) tblModel.getValueAt(row, 9));
    }

    /**
     * Ẩn inputPanel và reset giá trị các ô, đồng thời enable lại mọi thành phần khác.
     */
    private void hideInputPanel() {
        txtIdNV.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtGioiTinh.setText("");
        txtNamSinh.setText("");
        txtNgayVaoLam.setText("");
        txtLuong.setText("");
        txtTrangThai.setText("");
        txtUsername.setText("");
        txtPassword.setText("");

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
     * Khi bấm “Thêm”:
     *  - Hiện inputPanel (rỗng),
     *  - Disable các nút khác, bảng và panel tìm kiếm,
     *  - currentMode = "ADDING".
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdNV.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtGioiTinh.setText("");
        txtNamSinh.setText("");
        txtNgayVaoLam.setText("");
        txtLuong.setText("");
        txtTrangThai.setText("");
        txtUsername.setText("");
        txtPassword.setText("");

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
     * Khi bấm “Sửa”:
     *  - Phải có hàng được chọn trên bảng,
     *  - Điền dữ liệu lên inputPanel,
     *  - Disable các nút khác, bảng và panel tìm kiếm,
     *  - currentMode = "EDITING".
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
     * Khi bấm “Xóa”:
     *  - Phải có hàng được chọn,
     *  - Xác nhận trước khi xóa,
     *  - Gọi controller.deleteNhanVien(idNV), nếu thành công reload bảng.
     */
    private void onDelete() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn nhân viên cần xóa!", "Cảnh báo");
            return;
        }
        String idNV = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this,
                "Bạn có chắc muốn xóa nhân viên " + idNV + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteNhanVien(idNV)) {
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
     *  - Reload toàn bộ dữ liệu.
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Lưu” trong inputPanel:
     *  - Validate dữ liệu: IDNV, HoTen, Số điện thoại, Giới tính, Năm sinh, Ngày vào làm, Lương, Trang thái
     *  - Nếu currentMode == "ADDING": gọi controller.addNhanVien(nv),
     *    nếu currentMode == "EDITING": gọi controller.updateNhanVien(nv),
     *  - Ẩn inputPanel và reload bảng nếu thành công.
     */
    private void onSave() {
        String idNV      = txtIdNV.getText().trim();
        String hoTen     = txtHoTen.getText().trim();
        String sdt       = txtSdt.getText().trim();
        String gioiTinh  = txtGioiTinh.getText().trim();
        String namSinhStr = txtNamSinh.getText().trim();
        String ngayVaoLamStr = txtNgayVaoLam.getText().trim();
        String luong     = txtLuong.getText().trim();
        String trangThai = txtTrangThai.getText().trim();
        String username  = txtUsername.getText().trim();
        String password  = txtPassword.getText().trim();

        if (idNV.isEmpty()) {
            MessageDialog.showWarning(this, "IDNV không được để trống!", "Cảnh báo");
            return;
        }
        if (hoTen.isEmpty()) {
            MessageDialog.showWarning(this, "Họ tên không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isPhone(sdt)) {
            MessageDialog.showWarning(this, "SĐT phải là 10–11 chữ số!", "Cảnh báo");
            return;
        }
        if (gioiTinh.isEmpty()) {
            MessageDialog.showWarning(this, "Giới tính không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isInteger(namSinhStr)) {
            MessageDialog.showWarning(this, "Năm sinh phải là số!", "Cảnh báo");
            return;
        }
        if (!Validator.isDate(ngayVaoLamStr, "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Ngày vào làm phải đúng định dạng dd/MM/yyyy!", "Cảnh báo");
            return;
        }
        if (luong.isEmpty()) {
            MessageDialog.showWarning(this, "Lương không được để trống!", "Cảnh báo");
            return;
        }
        if (trangThai.isEmpty()) {
            MessageDialog.showWarning(this, "Trạng thái không được để trống!", "Cảnh báo");
            return;
        }
        // Username/Password có thể để trống, DAO sẽ tự xử lý

        NhanVien nv = new NhanVien();
        nv.setIdNV(idNV);
        nv.setHoTen(hoTen);
        nv.setSdt(sdt);
        nv.setGioiTinh(gioiTinh);
        nv.setNamSinh(Integer.parseInt(namSinhStr));
        nv.setNgayVaoLam(DateHelper.toDate(ngayVaoLamStr, "dd/MM/yyyy"));
        nv.setLuong(luong);
        nv.setTrangThai(trangThai);
        nv.setUsername(username);
        nv.setPassword(password);
        nv.setRoleId("VT02"); // giả sử mặc định là nhân viên

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addNhanVien(nv);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại! Kiểm tra lại IDNV hoặc tài khoản.", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateNhanVien(nv);
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
     *  - Chỉ cần ẩn inputPanel mà không thực hiện lưu hay thay đổi.
     */
    private void onCancel() {
        hideInputPanel();
    }
}
