package gui;

import controller.ThuocController;
import entities.Thuoc;
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
 * ThuocPanel.java (đã sửa để hiển thị trực tiếp các trường donViTinh, danhMuc, xuatXu)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (IDThuoc, Tên thuốc, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn khi không thao tác Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310) hiển thị danh sách
 */
public class ThuocPanel extends JPanel {

    private JTable tblThuoc;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode = "NONE")
    private JPanel inputPanel;
    private JTextField txtIdThuoc, txtTenThuoc, txtThanhPhan, txtDonViTinh, txtDanhMuc, txtXuatXu,
                       txtSoLuongTon, txtGiaNhap, txtDonGia, txtHanSuDung;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdThuoc, txtSearchTenThuoc;
    private JButton btnSearch;

    // 4 nút chức năng
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
    	setPreferredSize(new Dimension(1600, 800));
        setLayout(null);

        // --- Nút Thêm ---
        btnAdd = new JButton("Thêm");
        btnAdd.setIcon(new ImageIcon(ThuocPanel.class.getResource("/icon/Add.png")));
        btnAdd.setBounds(10, 10, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> onAdd());

        // --- Nút Sửa ---
        btnEdit = new JButton("Sửa");
        btnEdit.setIcon(new ImageIcon(ThuocPanel.class.getResource("/icon/chungEdit.png")));
        btnEdit.setBounds(100, 10, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        // --- Nút Xóa ---
        btnDelete = new JButton("Xóa");
        btnDelete.setIcon(new ImageIcon(ThuocPanel.class.getResource("/icon/chungDelete.png")));
        btnDelete.setBounds(190, 10, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> onDelete());

        // --- Nút Làm mới ---
        btnRefresh = new JButton("Làm mới");
        btnRefresh.setIcon(new ImageIcon(ThuocPanel.class.getResource("/icon/chungRefresh.png")));
        btnRefresh.setBounds(280, 10, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> onRefresh());

        // --- Bảng dữ liệu (y = 200, cao = 310) ---
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[]{
            "IDThuoc", "Tên thuốc", "Thành phần",
            "Đơn vị tính", "Danh mục", "Xuất xứ",
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
        btnSearch.setIcon(new ImageIcon(ThuocPanel.class.getResource("/icon/chungSearch.png")));
        btnSearch.setBounds(450, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (y = 90, cao = 100), ẩn khi visible = false.
     * Gồm các ô:
     *  - IDThuoc, TenThuoc, ThanhPhan, DonViTinh, DanhMuc, XuatXu,
     *    SoLuongTon, GiaNhap, DonGia, HanSuDung,
     *  - Nút Lưu, Nút Hủy.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        // IDThuoc
        JLabel lblIdThuoc = new JLabel("IDThuoc:");
        lblIdThuoc.setBounds(10, 0, 60, 25);
        inputPanel.add(lblIdThuoc);
        txtIdThuoc = new JTextField();
        txtIdThuoc.setBounds(65, 0, 100, 25);
        inputPanel.add(txtIdThuoc);

        // Tên thuốc
        JLabel lblTenThuoc = new JLabel("Tên thuốc:");
        lblTenThuoc.setBounds(180, 0, 70, 25);
        inputPanel.add(lblTenThuoc);
        txtTenThuoc = new JTextField();
        txtTenThuoc.setBounds(265, 0, 200, 25);
        inputPanel.add(txtTenThuoc);

        // Thành phần
        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setBounds(395, 35, 80, 25);
        inputPanel.add(lblThanhPhan);
        txtThanhPhan = new JTextField();
        txtThanhPhan.setBounds(483, 35, 250, 25);
        inputPanel.add(txtThanhPhan);

        // DonViTinh
        JLabel lblDonViTinh = new JLabel("ĐVT:");
        lblDonViTinh.setBounds(10, 35, 50, 25);
        inputPanel.add(lblDonViTinh);
        txtDonViTinh = new JTextField();
        txtDonViTinh.setBounds(65, 35, 100, 25);
        inputPanel.add(txtDonViTinh);

        // DanhMuc
        JLabel lblDanhMuc = new JLabel("DM:");
        lblDanhMuc.setBounds(180, 35, 40, 25);
        inputPanel.add(lblDanhMuc);
        txtDanhMuc = new JTextField();
        txtDanhMuc.setBounds(265, 35, 100, 25);
        inputPanel.add(txtDanhMuc);

        // XuatXu
        JLabel lblXuatXu = new JLabel("Xuất xứ:");
        lblXuatXu.setBounds(384, 70, 60, 25);
        inputPanel.add(lblXuatXu);
        txtXuatXu = new JTextField();
        txtXuatXu.setBounds(460, 70, 100, 25);
        inputPanel.add(txtXuatXu);

        // Số lượng tồn
        JLabel lblSoLuongTon = new JLabel("SL tồn:");
        lblSoLuongTon.setBounds(473, 0, 50, 25);
        inputPanel.add(lblSoLuongTon);
        txtSoLuongTon = new JTextField();
        txtSoLuongTon.setBounds(517, 0, 80, 25);
        inputPanel.add(txtSoLuongTon);

        // Giá nhập
        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setBounds(568, 70, 60, 25);
        inputPanel.add(lblGiaNhap);
        txtGiaNhap = new JTextField();
        txtGiaNhap.setBounds(643, 70, 90, 25);
        inputPanel.add(txtGiaNhap);

        // Đơn giá
        JLabel lblDonGia = new JLabel("Đơn giá:");
        lblDonGia.setBounds(10, 70, 60, 25);
        inputPanel.add(lblDonGia);
        txtDonGia = new JTextField();
        txtDonGia.setBounds(65, 70, 100, 25);
        inputPanel.add(txtDonGia);

        // Hạn sử dụng
        JLabel lblHanSuDung = new JLabel("Hạn sử dụng:");
        lblHanSuDung.setBounds(180, 70, 80, 25);
        inputPanel.add(lblHanSuDung);
        txtHanSuDung = new JTextField();
        txtHanSuDung.setBounds(264, 70, 100, 25);
        inputPanel.add(txtHanSuDung);

        // Nút Lưu
        btnSave = new JButton("Lưu");
        btnSave.setIcon(new ImageIcon(ThuocPanel.class.getResource("/icon/chungSave.png")));
        btnSave.setBounds(780, 10, 80, 30);
        inputPanel.add(btnSave);
        btnSave.addActionListener(e -> onSave());

        // Nút Hủy
        btnCancel = new JButton("Hủy");
        btnCancel.setIcon(new ImageIcon(ThuocPanel.class.getResource("/icon/chungCancel.png")));
        btnCancel.setBounds(780, 50, 80, 30);
        inputPanel.add(btnCancel);
        btnCancel.addActionListener(e -> onCancel());

        inputPanel.setVisible(visible);
    }

    /**
     * Load dữ liệu Thuốc vào JTable:
     *  - Mỗi dòng hiển thị: idThuoc, tenThuoc, thanhPhan,
     *    donViTinh, danhMuc, xuatXu, soLuongTon, giaNhap, donGia, hanSuDung.
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<Thuoc> list = controller.getAllThuoc();
        for (Thuoc t : list) {
            tblModel.addRow(new Object[]{
                t.getIdThuoc(),
                t.getTenThuoc(),
                t.getThanhPhan(),
                t.getDonViTinh(),
                t.getDanhMuc(),
                t.getXuatXu(),
                t.getSoLuongTon(),
                t.getGiaNhap(),
                t.getDonGia(),
                DateHelper.toString(t.getHanSuDung(), "dd/MM/yyyy")
            });
        }
    }

    /**
     * Xử lý khi nhấn “Tìm kiếm”:
     *  - Lấy giá trị từ txtSearchIdThuoc, txtSearchTenThuoc,
     *  - Gọi controller.searchThuoc(...),
     *  - Hiển thị kết quả lên table; nếu có dòng, tự động chọn dòng đầu tiên.
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
                t.getDonViTinh(),
                t.getDanhMuc(),
                t.getXuatXu(),
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
     * Điền dữ liệu từ hàng bảng lên inputPanel (nếu currentMode == "NONE").
     */
    private void populateInputFromTable(int row) {
        txtIdThuoc.setText((String) tblModel.getValueAt(row, 0));
        txtTenThuoc.setText((String) tblModel.getValueAt(row, 1));
        txtThanhPhan.setText((String) tblModel.getValueAt(row, 2));
        txtDonViTinh.setText((String) tblModel.getValueAt(row, 3));
        txtDanhMuc.setText((String) tblModel.getValueAt(row, 4));
        txtXuatXu.setText((String) tblModel.getValueAt(row, 5));
        txtSoLuongTon.setText(tblModel.getValueAt(row, 6).toString());
        txtGiaNhap.setText(tblModel.getValueAt(row, 7).toString());
        txtDonGia.setText(tblModel.getValueAt(row, 8).toString());
        txtHanSuDung.setText((String) tblModel.getValueAt(row, 9));
    }

    /**
     * Ẩn inputPanel và reset các ô, enable lại các thành phần tìm kiếm và bảng.
     */
    private void hideInputPanel() {
        txtIdThuoc.setText("");
        txtTenThuoc.setText("");
        txtThanhPhan.setText("");
        txtDonViTinh.setText("");
        txtDanhMuc.setText("");
        txtXuatXu.setText("");
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
     * Khi bấm “Thêm”:
     *  - Hiện inputPanel (rỗng),
     *  - Disable các nút khác, bảng và panel tìm kiếm,
     *  - currentMode = "ADDING".
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdThuoc.setText("");
        txtTenThuoc.setText("");
        txtThanhPhan.setText("");
        txtDonViTinh.setText("");
        txtDanhMuc.setText("");
        txtXuatXu.setText("");
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
     * Khi bấm “Sửa”:
     *  - Phải có hàng được chọn trên bảng,
     *  - Điền dữ liệu lên inputPanel,
     *  - Disable các nút khác, bảng và panel tìm kiếm,
     *  - currentMode = "EDITING".
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
     * Khi bấm “Xóa”:
     *  - Phải có hàng được chọn,
     *  - Xác nhận trước khi xóa,
     *  - Gọi controller.deleteThuoc(idThuoc),
     *  - Nếu thành công, load lại table.
     */
   private void onDelete() {
        int row = tblThuoc.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn thuốc cần xóa!", "Cảnh báo");
            return;
        }
        String idThuoc = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this,
                "Bạn có chắc muốn xóa thuốc " + idThuoc + "?", "Xác nhận");
        if (confirm) {
            StringBuilder errorMessage = new StringBuilder();
            boolean success = controller.deleteThuoc(idThuoc, errorMessage);
            if (success) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
            } else {
                String msg = errorMessage.length() > 0
                    ? errorMessage.toString()
                    : "Xóa thất bại!";
                MessageDialog.showError(this, msg, "Lỗi");
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
     *  - Kiểm tra dữ liệu hợp lệ (ID, Tên, DonViTinh, DanhMuc, XuatXu không rỗng;
     *    Số lượng, giá phải là số; Hạn sử dụng đúng định dạng),
     *  - Nếu currentMode == "ADDING": gọi addThuoc,
     *    nếu currentMode == "EDITING": gọi updateThuoc,
     *  - Ẩn inputPanel, reload table.
     */
    private void onSave() {
        String idThuoc = txtIdThuoc.getText().trim();
        String tenThuoc = txtTenThuoc.getText().trim();
        String thanhPhan = txtThanhPhan.getText().trim();
        String donViTinh = txtDonViTinh.getText().trim();
        String danhMuc = txtDanhMuc.getText().trim();
        String xuatXu = txtXuatXu.getText().trim();
        String soLuongTonStr = txtSoLuongTon.getText().trim();
        String giaNhapStr = txtGiaNhap.getText().trim();
        String donGiaStr = txtDonGia.getText().trim();
        String hanSuDungStr = txtHanSuDung.getText().trim();

        if (idThuoc.isEmpty()) {
            MessageDialog.showWarning(this, "IDThuoc không được để trống!", "Cảnh báo");
            return;
        }
        if (tenThuoc.isEmpty()) {
            MessageDialog.showWarning(this, "Tên thuốc không được để trống!", "Cảnh báo");
            return;
        }
        if (donViTinh.isEmpty()) {
            MessageDialog.showWarning(this, "Đơn vị tính không được để trống!", "Cảnh báo");
            return;
        }
        if (danhMuc.isEmpty()) {
            MessageDialog.showWarning(this, "Danh mục không được để trống!", "Cảnh báo");
            return;
        }
        if (xuatXu.isEmpty()) {
            MessageDialog.showWarning(this, "Xuất xứ không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isInteger(soLuongTonStr)) {
            MessageDialog.showWarning(this, "Số lượng tồn phải là số nguyên!", "Cảnh báo");
            return;
        }
        if (!Validator.isDouble(giaNhapStr)) {
            MessageDialog.showWarning(this, "Giá nhập phải là số!", "Cảnh báo");
            return;
        }
        if (!Validator.isDouble(donGiaStr)) {
            MessageDialog.showWarning(this, "Đơn giá phải là số!", "Cảnh báo");
            return;
        }
        if (!Validator.isDate(hanSuDungStr, "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Hạn sử dụng phải đúng định dạng dd/MM/yyyy!", "Cảnh báo");
            return;
        }

        Thuoc t = new Thuoc();
        t.setIdThuoc(idThuoc);
        t.setTenThuoc(tenThuoc);
        t.setThanhPhan(thanhPhan.isEmpty() ? null : thanhPhan);
        t.setDonViTinh(donViTinh);
        t.setDanhMuc(danhMuc);
        t.setXuatXu(xuatXu);
        t.setSoLuongTon(Integer.parseInt(soLuongTonStr));
        t.setGiaNhap(Double.parseDouble(giaNhapStr));
        t.setDonGia(Double.parseDouble(donGiaStr));
        t.setHanSuDung(DateHelper.toDate(hanSuDungStr, "dd/MM/yyyy"));

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addThuoc(t);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại! Kiểm tra lại IDThuoc hoặc kết nối DB.", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateThuoc(t);
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
     * Khi bấm “Hủy” trong inputPanel: chỉ cần ẩn inputPanel mà không thay đổi dữ liệu.
     */
    private void onCancel() {
        hideInputPanel();
    }
}