package gui;

import controller.ThuocController;
import entities.Thuoc;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;
import connectDB.DBConnection;
import connectDB.DBCloseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.List;

/**
 * ThuocPanel.java (đã sửa để hiển thị tên DonViTinh, DanhMuc, XuatXu cạnh ID)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (IDThuoc, TenThuoc, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/chỉ hiển thị khi Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310)
 */
public class ThuocPanel extends JPanel {

    private JTable tblThuoc;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtIdThuoc, txtTenThuoc, txtThanhPhan, txtIdDVT, txtIdDM, txtIdXX, txtSoLuongTon, txtGiaNhap, txtDonGia, txtHanSuDung;
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
            "IDThuoc", "Tên thuốc", "Thành phần",
            "IDDVT - Tên DVT", "IDDM - Tên DM", "IDXX - Tên XX",
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
     *  - txtSearchIdThuoc, txtSearchTenThuoc, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchIdThuoc = new JLabel("IDThuoc:");
        lblSearchIdThuoc.setBounds(0, 5, 60, 20);
        searchPanel.add(lblSearchIdThuoc);

        txtSearchIdThuoc = new JTextField();
        txtSearchIdThuoc.setBounds(65, 3, 120, 25);
        searchPanel.add(txtSearchIdThuoc);

        JLabel lblSearchTenThuoc = new JLabel("Tên thuốc:");
        lblSearchTenThuoc.setBounds(200, 5, 70, 20);
        searchPanel.add(lblSearchTenThuoc);

        txtSearchTenThuoc = new JTextField();
        txtSearchTenThuoc.setBounds(275, 3, 150, 25);
        searchPanel.add(txtSearchTenThuoc);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(450, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (y = 90, cao = 100), ẩn khi visible = false.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        // IDThuoc
        JLabel lblIdThuoc = new JLabel("IDThuoc:");
        lblIdThuoc.setBounds(10, 10, 60, 25);
        inputPanel.add(lblIdThuoc);
        txtIdThuoc = new JTextField();
        txtIdThuoc.setBounds(80, 10, 120, 25);
        inputPanel.add(txtIdThuoc);

        // Tên Thuốc
        JLabel lblTenThuoc = new JLabel("Tên thuốc:");
        lblTenThuoc.setBounds(220, 10, 70, 25);
        inputPanel.add(lblTenThuoc);
        txtTenThuoc = new JTextField();
        txtTenThuoc.setBounds(300, 10, 200, 25);
        inputPanel.add(txtTenThuoc);

        // Thành phần
        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setBounds(520, 10, 80, 25);
        inputPanel.add(lblThanhPhan);
        txtThanhPhan = new JTextField();
        txtThanhPhan.setBounds(600, 10, 250, 25);
        inputPanel.add(txtThanhPhan);

        // IDDVT
        JLabel lblIdDVT = new JLabel("IDDVT:");
        lblIdDVT.setBounds(10, 45, 50, 25);
        inputPanel.add(lblIdDVT);
        txtIdDVT = new JTextField();
        txtIdDVT.setBounds(70, 45, 100, 25);
        inputPanel.add(txtIdDVT);

        // IDDM
        JLabel lblIdDM = new JLabel("IDDM:");
        lblIdDM.setBounds(190, 45, 50, 25);
        inputPanel.add(lblIdDM);
        txtIdDM = new JTextField();
        txtIdDM.setBounds(250, 45, 100, 25);
        inputPanel.add(txtIdDM);

        // IDXX
        JLabel lblIdXX = new JLabel("IDXX:");
        lblIdXX.setBounds(370, 45, 50, 25);
        inputPanel.add(lblIdXX);
        txtIdXX = new JTextField();
        txtIdXX.setBounds(430, 45, 100, 25);
        inputPanel.add(txtIdXX);

        // Số lượng tồn
        JLabel lblSoLuongTon = new JLabel("SL tồn:");
        lblSoLuongTon.setBounds(550, 45, 50, 25);
        inputPanel.add(lblSoLuongTon);
        txtSoLuongTon = new JTextField();
        txtSoLuongTon.setBounds(610, 45, 80, 25);
        inputPanel.add(txtSoLuongTon);

        // Giá nhập
        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setBounds(700, 45, 60, 25);
        inputPanel.add(lblGiaNhap);
        txtGiaNhap = new JTextField();
        txtGiaNhap.setBounds(760, 45, 90, 25);
        inputPanel.add(txtGiaNhap);

        // Đơn giá
        JLabel lblDonGia = new JLabel("Đơn giá:");
        lblDonGia.setBounds(10, 70, 60, 25);
        inputPanel.add(lblDonGia);
        txtDonGia = new JTextField();
        txtDonGia.setBounds(80, 70, 100, 25);
        inputPanel.add(txtDonGia);

        // Hạn sử dụng
        JLabel lblHanSuDung = new JLabel("Hạn sử dụng:");
        lblHanSuDung.setBounds(200, 70, 80, 25);
        inputPanel.add(lblHanSuDung);
        txtHanSuDung = new JTextField();
        txtHanSuDung.setBounds(290, 70, 100, 25);
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
     * Load toàn bộ dữ liệu Thuốc vào JTable, lần lượt lấy tên DVT, DM, XX để hiển thị cùng ID.
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<Thuoc> list = controller.getAllThuoc();
        for (Thuoc t : list) {
            String tenDVT = fetchTenDVT(t.getIdDVT());
            String tenDM  = fetchTenDM(t.getIdDM());
            String tenXX  = fetchTenXX(t.getIdXX());

            tblModel.addRow(new Object[]{
                t.getIdThuoc(),
                t.getTenThuoc(),
                t.getThanhPhan(),
                t.getIdDVT() + " - " + tenDVT,
                t.getIdDM()  + " - " + tenDM,
                t.getIdXX()  + " - " + tenXX,
                t.getSoLuongTon(),
                t.getGiaNhap(),
                t.getDonGia(),
                DateHelper.toString(t.getHanSuDung(), "dd/MM/yyyy")
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idThuoc, tenThuoc, gọi controller.searchThuoc(...),
     * hiển thị kết quả, nếu có ít nhất 1 dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String idThuoc  = txtSearchIdThuoc.getText().trim();
        String tenThuoc = txtSearchTenThuoc.getText().trim();

        List<Thuoc> results = controller.searchThuoc(idThuoc, tenThuoc);

        tblModel.setRowCount(0);
        for (Thuoc t : results) {
            String tenDVT = fetchTenDVT(t.getIdDVT());
            String tenDM  = fetchTenDM(t.getIdDM());
            String tenXX  = fetchTenXX(t.getIdXX());

            tblModel.addRow(new Object[]{
                t.getIdThuoc(),
                t.getTenThuoc(),
                t.getThanhPhan(),
                t.getIdDVT() + " - " + tenDVT,
                t.getIdDM()  + " - " + tenDM,
                t.getIdXX()  + " - " + tenXX,
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
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == NONE).
     * Khi click vào hàng bảng, phân tách chuỗi "ID - Tên" để lấy ID cho việc sửa.
     */
    private void populateInputFromTable(int row) {
        txtIdThuoc.setText((String) tblModel.getValueAt(row, 0));
        txtTenThuoc.setText((String) tblModel.getValueAt(row, 1));
        txtThanhPhan.setText((String) tblModel.getValueAt(row, 2));

        // Cột DVT: giá trị dạng "IDDVT - TênDVT"
        String dvtCell = (String) tblModel.getValueAt(row, 3);
        String idDVT = dvtCell.split(" - ")[0];
        txtIdDVT.setText(idDVT);

        // Cột DM: "IDDM - TênDM"
        String dmCell = (String) tblModel.getValueAt(row, 4);
        String idDM = dmCell.split(" - ")[0];
        txtIdDM.setText(idDM);

        // Cột XX: "IDXX - TênXX"
        String xxCell = (String) tblModel.getValueAt(row, 5);
        String idXX = xxCell.split(" - ")[0];
        txtIdXX.setText(idXX);

        txtSoLuongTon.setText(tblModel.getValueAt(row, 6).toString());
        txtGiaNhap.setText(tblModel.getValueAt(row, 7).toString());
        txtDonGia.setText(tblModel.getValueAt(row, 8).toString());
        txtHanSuDung.setText((String) tblModel.getValueAt(row, 9));
    }

    /**
     * Ẩn inputPanel và reset fields, enable lại phần tìm kiếm, bảng và các nút.
     */
    private void hideInputPanel() {
        txtIdThuoc.setText("");
        txtTenThuoc.setText("");
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
     * Khi bấm “Thêm”: hiện inputPanel, reset ô, disable các thành phần còn lại.
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdThuoc.setText("");
        txtTenThuoc.setText("");
        txtThanhPhan.setText("");
        txtIdDVT.setText("");
        txtIdDM.setText("");
        txtIdXX.setText("");
        txtSoLuongTon.setText("");
        txtGiaNhap.setText("");
        txtDonGia.setText("");
        txtHanSuDung.setText("");

        txtIdThuoc.setEditable(true);

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
     * Khi bấm “Sửa”: phải có dòng được chọn, điền dữ liệu vào inputPanel, disable các thành phần khác.
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

        txtIdThuoc.setEditable(false);

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
     * Khi bấm “Xóa”: phải có dòng được chọn, xác nhận, gọi controller.deleteThuoc(idThuoc).
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
     * Khi bấm “Làm mới”: ẩn inputPanel (nếu đang hiển thị) và load lại danh sách.
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Lưu” trong inputPanel:
     *  - validate dữ liệu,
     *  - nếu ADDING, gọi controller.addThuoc,
     *    nếu EDITING, gọi controller.updateThuoc,
     *  - ẩn inputPanel, load lại dữ liệu.
     */
    private void onSave() {
        // Kiểm tra dữ liệu
        if (txtIdThuoc.getText().trim().isEmpty() || txtTenThuoc.getText().trim().isEmpty()) {
            MessageDialog.showWarning(this, "ID thuốc và Tên thuốc không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isInteger(txtSoLuongTon.getText())) {
            MessageDialog.showWarning(this, "Số lượng tồn phải là số nguyên!", "Cảnh báo");
            return;
        }
        if (!Validator.isDouble(txtGiaNhap.getText()) || !Validator.isDouble(txtDonGia.getText())) {
            MessageDialog.showWarning(this, "Giá nhập và Đơn giá phải là số!", "Cảnh báo");
            return;
        }
        if (!Validator.isDate(txtHanSuDung.getText(), "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Hạn sử dụng phải đúng định dạng dd/MM/yyyy!", "Cảnh báo");
            return;
        }

        Thuoc t = new Thuoc();
        t.setIdThuoc(txtIdThuoc.getText().trim());
        t.setTenThuoc(txtTenThuoc.getText().trim());
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
     * Khi bấm “Hủy” trong inputPanel: chỉ cần ẩn inputPanel.
     */
    private void onCancel() {
        hideInputPanel();
    }

    // ----------------------------------------------------------------------------------
    // Các phương thức tiện ích để truy vấn tên từ bảng DonViTinh, DanhMuc, XuatXu
    // ----------------------------------------------------------------------------------

    /**
     * Lấy tên DonViTinh dựa trên idDVT.
     */
    private String fetchTenDVT(String idDVT) {
        if (idDVT == null || idDVT.trim().isEmpty()) {
            return "";
        }
        String result = "";
        String sql = "SELECT ten FROM DonViTinh WHERE idDVT = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idDVT);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("ten");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return result != null ? result : "";
    }

    /**
     * Lấy tên DanhMuc dựa trên idDM.
     */
    private String fetchTenDM(String idDM) {
        if (idDM == null || idDM.trim().isEmpty()) {
            return "";
        }
        String result = "";
        String sql = "SELECT ten FROM DanhMuc WHERE idDM = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idDM);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("ten");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return result != null ? result : "";
    }

    /**
     * Lấy tên XuatXu dựa trên idXX.
     */
    private String fetchTenXX(String idXX) {
        if (idXX == null || idXX.trim().isEmpty()) {
            return "";
        }
        String result = "";
        String sql = "SELECT ten FROM XuatXu WHERE idXX = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idXX);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("ten");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCloseHelper.closeAll(rs, stmt, conn);
        }
        return result != null ? result : "";
    }
}
