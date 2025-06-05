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
 * HoaDonPanel.java
 *
 * Phiên bản đã bổ sung nút "Xem chi tiết" bên cạnh "Xóa". Khi nhấn, nếu có hàng Hóa đơn được chọn,
 * sẽ mở ViewChiTietHDDialog hiển thị chi tiết (Tên thuốc, Số lượng, Đơn giá) của Hóa đơn đó.
 *
 * Bố cục panel:
 * - Dòng 1 (y=10): 5 nút: Thêm, Sửa, Xóa, Xem chi tiết, Làm mới
 * - Dòng 2 (y=50): Panel tìm kiếm (IDHD, IDNV, IDKH, nút Tìm kiếm)
 * - Dòng 3 (y=90): inputPanel (ẩn/hiện khi Thêm/Sửa)
 * - Dòng 4 (y=200): JTable (cao=310) hiển thị 7 cột:
 *      "IDHD", "Thời gian", "IDNV", "IDKH", "Tổng tiền", "PT Thanh toán", "Trạng thái"
 */
public class HoaDonPanel extends JPanel {

    private JTable tblHoaDon;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi không ADD/EDIT)
    private JPanel inputPanel;
    private JTextField txtIdHD, txtThoiGian, txtIdNV, txtIdKH, txtTongTien;
    private JTextField txtPhuongThucThanhToan, txtTrangThaiDonHang;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdHD, txtSearchIdNV, txtSearchIdKH;
    private JButton btnSearch;

    // 5 nút chức năng
    private JButton btnAdd, btnEdit, btnDelete, btnViewDetail, btnRefresh;

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
     * - Nút Thêm, Sửa, Xóa, Xem chi tiết, Làm mới (y = 10)
     * - JTable (y = 200, cao = 310) hiển thị 7 cột:
     *      "IDHD", "Thời gian", "IDNV", "IDKH", "Tổng tiền", "PT Thanh toán", "Trạng thái"
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

        // --- Nút "Xem chi tiết" (mới) --- //
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
            "IDHD", "Thời gian", "IDNV", "IDKH", "Tổng tiền", "PT Thanh toán", "Trạng thái"
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
        lblSearchIdHD.setBounds(0, 5, 40, 20);
        searchPanel.add(lblSearchIdHD);

        txtSearchIdHD = new JTextField();
        txtSearchIdHD.setBounds(45, 3, 120, 25);
        searchPanel.add(txtSearchIdHD);

        JLabel lblSearchIdNV = new JLabel("IDNV:");
        lblSearchIdNV.setBounds(180, 5, 40, 20);
        searchPanel.add(lblSearchIdNV);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(225, 3, 120, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchIdKH = new JLabel("IDKH:");
        lblSearchIdKH.setBounds(360, 5, 40, 20);
        searchPanel.add(lblSearchIdKH);

        txtSearchIdKH = new JTextField();
        txtSearchIdKH.setBounds(405, 3, 120, 25);
        searchPanel.add(txtSearchIdKH);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(550, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (y = 90, cao = 100), ẩn khi visible = false.
     * Gồm: txtIdHD, txtThoiGian, txtIdNV, txtIdKH, txtTongTien,
     *       txtPhuongThucThanhToan, txtTrangThaiDonHang,
     * và hai nút Lưu, Hủy.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
        inputPanel.setBounds(10, 90, 860, 100);
        add(inputPanel);

        // IDHD
        JLabel lblIdHD = new JLabel("IDHD:");
        lblIdHD.setBounds(10, 10, 40, 25);
        inputPanel.add(lblIdHD);
        txtIdHD = new JTextField();
        txtIdHD.setBounds(60, 10, 100, 25);
        inputPanel.add(txtIdHD);

        // Thời gian
        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(180, 10, 70, 25);
        inputPanel.add(lblThoiGian);
        txtThoiGian = new JTextField();
        txtThoiGian.setBounds(262, 10, 150, 25);
        inputPanel.add(txtThoiGian);

        // IDNV
        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(420, 10, 40, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(475, 10, 100, 25);
        inputPanel.add(txtIdNV);

        // IDKH
        JLabel lblIdKH = new JLabel("IDKH:");
        lblIdKH.setBounds(626, 10, 40, 25);
        inputPanel.add(lblIdKH);
        txtIdKH = new JTextField();
        txtIdKH.setBounds(663, 10, 100, 25);
        inputPanel.add(txtIdKH);

        // Tổng tiền
        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setBounds(10, 40, 70, 25);
        inputPanel.add(lblTongTien);
        txtTongTien = new JTextField();
        txtTongTien.setBounds(60, 40, 100, 25);
        inputPanel.add(txtTongTien);

        // Phương thức thanh toán
        JLabel lblPTTT = new JLabel("PT Thanh toán:");
        lblPTTT.setBounds(180, 40, 100, 25);
        inputPanel.add(lblPTTT);
        txtPhuongThucThanhToan = new JTextField();
        txtPhuongThucThanhToan.setBounds(262, 40, 150, 25);
        inputPanel.add(txtPhuongThucThanhToan);

        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(420, 40, 70, 25);
        inputPanel.add(lblTrangThai);
        txtTrangThaiDonHang = new JTextField();
        txtTrangThaiDonHang.setBounds(475, 40, 100, 25);
        inputPanel.add(txtTrangThaiDonHang);

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
     * Load dữ liệu Hóa đơn vào JTable, gồm 7 cột:
     *  "IDHD", "Thời gian", "IDNV", "IDKH", "Tổng tiền", "PT Thanh toán", "Trạng thái"
     * Lưu ý: Cột "Tổng tiền" hiển thị thông thường (String.format).
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<HoaDon> list = controller.getAllHoaDon();
        for (HoaDon hd : list) {
            tblModel.addRow(new Object[]{
                hd.getIdHD(),
                DateHelper.toString(hd.getThoiGian(), "dd/MM/yyyy HH:mm:ss"),
                hd.getIdNV(),
                hd.getIdKH(),
                String.format("%.1f", hd.getTongTien()),
                hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan() : "",
                hd.getTrangThaiDonHang()
            });
        }
    }

    /**
     * Khi bấm “Tìm kiếm”: lấy idHD, idNV, idKH, gọi controller.searchHoaDon(...),
     * hiển thị kết quả lên bảng, chọn tự động dòng đầu tiên nếu có.
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
                DateHelper.toString(hd.getThoiGian(), "dd/MM/yyyy HH:mm:ss"),
                hd.getIdNV(),
                hd.getIdKH(),
                String.format("%.1f", hd.getTongTien()),
                hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan() : "",
                hd.getTrangThaiDonHang()
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
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == "NONE").
     */
    private void populateInputFromTable(int row) {
        txtIdHD.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdNV.setText((String) tblModel.getValueAt(row, 2));
        txtIdKH.setText((String) tblModel.getValueAt(row, 3));
        txtTongTien.setText((String) tblModel.getValueAt(row, 4));
        txtPhuongThucThanhToan.setText((String) tblModel.getValueAt(row, 5));
        txtTrangThaiDonHang.setText((String) tblModel.getValueAt(row, 6));
    }

    /**
     * Ẩn inputPanel và reset các ô, đồng thời enable lại các thành phần khác.
     */
    private void hideInputPanel() {
        txtIdHD.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");
        txtPhuongThucThanhToan.setText("");
        txtTrangThaiDonHang.setText("");

        inputPanel.setVisible(false);
        currentMode = "NONE";

        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnViewDetail.setEnabled(true);
        btnRefresh.setEnabled(true);
        tblHoaDon.setEnabled(true);
        btnSearch.setEnabled(true);
        txtSearchIdHD.setEnabled(true);
        txtSearchIdNV.setEnabled(true);
        txtSearchIdKH.setEnabled(true);
    }

    // ========================================
    // ======== Các phương thức xử lý =========
    // ========================================

    /**
     * Khi bấm “Thêm”:
     * - Hiện inputPanel (rỗng),
     * - Disable các thành phần khác,
     * - currentMode = "ADDING".
     */
    private void onAdd() {
        currentMode = "ADDING";
        inputPanel.setVisible(true);

        txtIdHD.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");
        txtPhuongThucThanhToan.setText("");
        txtTrangThaiDonHang.setText("");

        txtIdHD.setEditable(true);

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnViewDetail.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblHoaDon.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdHD.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
    }

    /**
     * Khi bấm “Sửa”:
     * - Phải có dòng được chọn,
     * - Điền dữ liệu lên inputPanel,
     * - Disable các thành phần khác,
     * - currentMode = "EDITING".
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
        btnViewDetail.setEnabled(false);
        btnRefresh.setEnabled(false);
        tblHoaDon.setEnabled(false);
        btnSearch.setEnabled(false);
        txtSearchIdHD.setEnabled(false);
        txtSearchIdNV.setEnabled(false);
        txtSearchIdKH.setEnabled(false);
    }

    /**
     * Khi bấm “Xóa”:
     * - Phải có dòng được chọn,
     * - Xác nhận trước khi xóa,
     * - Gọi controller.deleteHoaDon(idHD), nếu thành công reload bảng.
     */
    private void onDelete() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hóa đơn cần xóa!", "Cảnh báo");
            return;
        }
        String idHD = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this,
                "Bạn có chắc muốn xóa hóa đơn " + idHD + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteHoaDon(idHD)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
            } else {
                MessageDialog.showError(this, "Xóa thất bại!", "Lỗi");
            }
        }
    }

    /**
     * Khi bấm “Xem chi tiết”:
     * - Phải có dòng được chọn,
     * - Lấy idHD, mở dialog ViewChiTietHDDialog để hiển thị chi tiết.
     */
    private void onViewDetail() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hóa đơn để xem chi tiết!", "Cảnh báo");
            return;
        }
        String idHD = (String) tblModel.getValueAt(row, 0);
        ViewChiTietHDDialog dialog = new ViewChiTietHDDialog(SwingUtilities.getWindowAncestor(this), idHD);
        dialog.setVisible(true);
    }

    /**
     * Khi bấm “Làm mới”:
     * - Ẩn inputPanel nếu đang hiển thị,
     * - Reload lại dữ liệu bảng.
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Lưu” trong inputPanel:
     * - Validate dữ liệu:
     *     + IDHD không được để trống.
     *     + Thời gian phải đúng định dạng "dd/MM/yyyy HH:mm:ss".
     *     + IDNV, IDKH không được để trống.
     *     + Tổng tiền phải là số.
     *     + Trạng thái đơn hàng không được để trống.
     *   Phương thức thanh toán có thể để trống (NULL).
     * - Nếu currentMode == "ADDING": gọi controller.addHoaDon(hd)
     *   Nếu currentMode == "EDITING": gọi controller.updateHoaDon(hd)
     * - Ẩn inputPanel và reload bảng nếu thành công.
     */
    private void onSave() {
        String idHD = txtIdHD.getText().trim();
        String thoiGianStr = txtThoiGian.getText().trim();
        String idNV = txtIdNV.getText().trim();
        String idKH = txtIdKH.getText().trim();
        String tongTienStr = txtTongTien.getText().trim();
        String phuongThuc = txtPhuongThucThanhToan.getText().trim();
        String trangThai = txtTrangThaiDonHang.getText().trim();

        if (idHD.isEmpty()) {
            MessageDialog.showWarning(this, "IDHD không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isDateTime(thoiGianStr, "dd/MM/yyyy HH:mm:ss")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm:ss!", "Cảnh báo");
            return;
        }
        if (idNV.isEmpty()) {
            MessageDialog.showWarning(this, "IDNV không được để trống!", "Cảnh báo");
            return;
        }
        if (idKH.isEmpty()) {
            MessageDialog.showWarning(this, "IDKH không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isDouble(tongTienStr)) {
            MessageDialog.showWarning(this, "Tổng tiền phải là số!", "Cảnh báo");
            return;
        }
        if (trangThai.isEmpty()) {
            MessageDialog.showWarning(this, "Trạng thái đơn hàng không được để trống!", "Cảnh báo");
            return;
        }
        // phuongThucThanhToan có thể để trống (NULL)

        HoaDon hd = new HoaDon();
        hd.setIdHD(idHD);
        hd.setThoiGian(DateHelper.toDateTime(thoiGianStr, "dd/MM/yyyy HH:mm:ss"));
        hd.setIdNV(idNV);
        hd.setIdKH(idKH);
        hd.setTongTien(Double.parseDouble(tongTienStr));
        hd.setPhuongThucThanhToan(phuongThuc.isEmpty() ? null : phuongThuc);
        hd.setTrangThaiDonHang(trangThai);

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addHoaDon(hd);
            if (success) {
                MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm thất bại! Kiểm tra IDHD hoặc kết nối DB.", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateHoaDon(hd);
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
     * - Chỉ cần ẩn inputPanel, không lưu.
     */
    private void onCancel() {
        hideInputPanel();
    }
}
