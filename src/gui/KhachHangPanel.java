package gui;

import controller.KhachHangController;
import entities.KhachHang;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.Dimension;

/**
 * KhachHangPanel.java (đã bổ sung chức năng Tìm kiếm)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): Nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel Search (Họ tên, SĐT, Nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/chỉ hiển thị khi Add/Edit
 *  - Dòng 4 (y = 200): JTable (cao = 310)
 */
public class KhachHangPanel extends JPanel {

    private JTable tblKhachHang;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == NONE)
    private JPanel inputPanel;
    private JTextField txtIdKH, txtHoTen, txtSDT, txtGioiTinh;
    private JFormattedTextField txtNgayThamGia;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchHoTen, txtSearchSDT;
    private JButton   btnSearch;

    // nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private KhachHangController controller;
    private String currentMode = "NONE"; // "NONE" | "ADDING" | "EDITING"

    public KhachHangPanel() {
        controller = new KhachHangController();
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

        // --- Nút chức năng (y = 10) --- //
        btnAdd = new JButton("  Thêm");
        btnAdd.setIcon(new ImageIcon(KhachHangPanel.class.getResource("/icon/Add.png")));
        btnAdd.setBounds(10, 10, 100, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> onAdd());

        btnEdit = new JButton("  Sửa");
        btnEdit.setIcon(new ImageIcon(KhachHangPanel.class.getResource("/icon/chungEdit.png")));
        btnEdit.setBounds(144, 10, 100, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        btnDelete = new JButton("  Xóa");
        btnDelete.setIcon(new ImageIcon(KhachHangPanel.class.getResource("/icon/chungDelete.png")));
        btnDelete.setBounds(282, 10, 105, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> onDelete());

        btnRefresh = new JButton("  Làm mới");
        btnRefresh.setIcon(new ImageIcon(KhachHangPanel.class.getResource("/icon/chungRefresh.png")));
        btnRefresh.setBounds(427, 10, 120, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> onRefresh());

        // --- Bảng dữ liệu (y = 200, cao = 310) --- //
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[]{
            "IDKH", "Họ tên", "SĐT", "Giới tính", "Ngày tham gia"
        });
        tblKhachHang = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        scrollPane.setBounds(10, 200, 860, 310);
        add(scrollPane);

        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0 && currentMode.equals("NONE")) {
                    populateInputFromTable(row);
                }
            }
        });
    }

    /**
     * Khởi tạo panel tìm kiếm (y = 50, cao = 30):
     *  - txtSearchHoTen, txtSearchSDT, btnSearch
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(10, 50, 860, 30);
        add(searchPanel);

        JLabel lblSearchHoTen = new JLabel("Họ tên:");
        lblSearchHoTen.setBounds(0, 5, 60, 20);
        searchPanel.add(lblSearchHoTen);

        txtSearchHoTen = new JTextField();
        txtSearchHoTen.setBounds(65, 3, 150, 25);
        searchPanel.add(txtSearchHoTen);

        JLabel lblSearchSDT = new JLabel("SĐT:");
        lblSearchSDT.setBounds(230, 5, 40, 20);
        searchPanel.add(lblSearchSDT);

        txtSearchSDT = new JTextField();
        txtSearchSDT.setBounds(275, 3, 120, 25);
        searchPanel.add(txtSearchSDT);

        btnSearch = new JButton("  Tìm kiếm");
        btnSearch.setIcon(new ImageIcon(KhachHangPanel.class.getResource("/icon/chungSearch.png")));
        btnSearch.setBounds(440, 3, 120, 25);
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

        // IDKH
        JLabel lblIdKH = new JLabel("IDKH:");
        lblIdKH.setBounds(10, 10, 50, 25);
        inputPanel.add(lblIdKH);
        txtIdKH = new JTextField();
        txtIdKH.setBounds(70, 10, 100, 25);
        inputPanel.add(txtIdKH);

        // Họ tên
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setBounds(200, 10, 50, 25);
        inputPanel.add(lblHoTen);
        txtHoTen = new JTextField();
        txtHoTen.setBounds(292, 10, 200, 25);
        inputPanel.add(txtHoTen);

        // SĐT
        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setBounds(519, 10, 40, 25);
        inputPanel.add(lblSDT);
        txtSDT = new JTextField();
        txtSDT.setBounds(567, 10, 120, 25);
        inputPanel.add(txtSDT);

        // Giới tính
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setBounds(10, 45, 60, 25);
        inputPanel.add(lblGioiTinh);
        txtGioiTinh = new JTextField();
        txtGioiTinh.setBounds(70, 45, 100, 25);
        inputPanel.add(txtGioiTinh);

        // Ngày tham gia
        JLabel lblNgay = new JLabel("Ngày tham gia:");
        lblNgay.setBounds(200, 45, 90, 25);
        inputPanel.add(lblNgay);
        txtNgayThamGia = new JFormattedTextField();
        txtNgayThamGia.setBounds(290, 45, 200, 25);
        inputPanel.add(txtNgayThamGia);

        // Nút Lưu
        btnSave = new JButton("  Lưu");
        btnSave.setIcon(new ImageIcon(KhachHangPanel.class.getResource("/icon/chungSave.png")));
        btnSave.setBounds(760, 10, 100, 30);
        inputPanel.add(btnSave);
        btnSave.addActionListener(e -> onSave());

        // Nút Hủy
        btnCancel = new JButton("  Hủy");
        btnCancel.setIcon(new ImageIcon(KhachHangPanel.class.getResource("/icon/chungCancel.png")));
        btnCancel.setBounds(760, 50, 100, 30);
        inputPanel.add(btnCancel);
        btnCancel.addActionListener(e -> onCancel());

        inputPanel.setVisible(visible);
    }

    /**
     * Tải toàn bộ dữ liệu vào JTable (khi khởi động hoặc khi làm mới).
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<KhachHang> list = controller.getAllKhachHang();
        for (KhachHang kh : list) {
            tblModel.addRow(new Object[]{
                kh.getIdKH(),
                kh.getHoTen(),
                kh.getSdt(),
                kh.getGioiTinh(),
                DateHelper.toString(kh.getNgayThamGia(), "dd/MM/yyyy")
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy họ tên và sdt, gọi controller.searchKhachHang(...),
     * hiển thị kết quả, nếu có ít nhất 1 dòng, tự động chọn dòng đầu tiên.
     */
    private void onSearch() {
        String hoTen = txtSearchHoTen.getText().trim();
        String sdt = txtSearchSDT.getText().trim();

        List<KhachHang> results = controller.searchKhachHang(hoTen, sdt);

        tblModel.setRowCount(0);
        for (KhachHang kh : results) {
            tblModel.addRow(new Object[]{
                kh.getIdKH(),
                kh.getHoTen(),
                kh.getSdt(),
                kh.getGioiTinh(),
                DateHelper.toString(kh.getNgayThamGia(), "dd/MM/yyyy")
            });
        }

        if (!results.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                tblKhachHang.setRowSelectionInterval(0, 0);
                tblKhachHang.scrollRectToVisible(tblKhachHang.getCellRect(0, 0, true));
            });
        }
    }

    /**
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == NONE).
     */
    private void populateInputFromTable(int row) {
        txtIdKH.setText((String) tblModel.getValueAt(row, 0));
        txtHoTen.setText((String) tblModel.getValueAt(row, 1));
        txtSDT.setText((String) tblModel.getValueAt(row, 2));
        txtGioiTinh.setText((String) tblModel.getValueAt(row, 3));
        txtNgayThamGia.setText((String) tblModel.getValueAt(row, 4));
    }

    /**
     * Ẩn inputPanel và reset fields, enable lại phần tìm kiếm, bảng và các nút.
     */
    private void hideInputPanel() {
        txtIdKH.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtGioiTinh.setText("");
        txtNgayThamGia.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblKhachHang.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchHoTen.setEnabled(true);
        txtSearchSDT.setEnabled(true);
    }

    /**
     * Khi bấm “Thêm”: hiện inputPanel, reset ô, disable các thành phần còn lại.
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdKH.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtGioiTinh.setText("");
        txtNgayThamGia.setText("");

        txtIdKH.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblKhachHang.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchHoTen.setEnabled(false);
        txtSearchSDT.setEnabled(false);
    }

    /**
     * Khi bấm “Sửa”: phải có dòng được chọn, điền dữ liệu vào inputPanel, disable các thành phần khác.
     */
    private void onEdit() {
        int row = tblKhachHang.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn khách hàng cần sửa!", "Cảnh báo");
            return;
        }
        currentMode = "EDITING";
        inputPanel.setVisible(true);

        populateInputFromTable(row);

        txtIdKH.setEditable(false);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblKhachHang.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchHoTen.setEnabled(false);
        txtSearchSDT.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”: phải có dòng được chọn, xác nhận, gọi controller.deleteKhachHang(idKH).
     */
    private void onSave() {
        // Validate dữ liệu, tạo KhachHang...
        KhachHang kh = new KhachHang();
        kh.setIdKH(txtIdKH.getText().trim());
        kh.setHoTen(txtHoTen.getText().trim());
        kh.setSdt(txtSDT.getText().trim());
        kh.setGioiTinh(txtGioiTinh.getText().trim());
        kh.setNgayThamGia(DateHelper.toDate(txtNgayThamGia.getText().trim(), "dd/MM/yyyy"));

        boolean success;
        StringBuilder errorMsg = new StringBuilder();
        if (currentMode.equals("ADDING")) {
            success = controller.addKhachHang(kh, errorMsg);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, errorMsg.length() > 0 ? errorMsg.toString() : "Thêm thất bại!", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateKhachHang(kh, errorMsg);
            if (success) {
                MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, errorMsg.length() > 0 ? errorMsg.toString() : "Cập nhật thất bại!", "Lỗi");
                return;
            }
        }

        hideInputPanel();
        loadDataToTable();
    }

    private void onDelete() {
        int row = tblKhachHang.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn khách hàng cần xóa!", "Cảnh báo");
            return;
        }
        String id = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa khách hàng " + id + "?", "Xác nhận");
        if (confirm) {
            StringBuilder errorMsg = new StringBuilder();
            if (controller.deleteKhachHang(id, errorMsg)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable(); // Chỉ KH chưa xóa mới hiện!
            } else {
                MessageDialog.showError(this, errorMsg.length() > 0 ? errorMsg.toString() : "Xóa thất bại!", "Lỗi");
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
     *  - nếu ADDING, gọi addKhachHang,
     *    nếu EDITING, gọi updateKhachHang,
     *  - ẩn inputPanel, load lại dữ liệu.
     */


    /**
     * Khi bấm “Hủy” trong inputPanel: chỉ cần ẩn inputPanel.
     */
    private void onCancel() {
        hideInputPanel();
    }
}
