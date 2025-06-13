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
import java.awt.Dimension;

/**
 * HopDongPanel.java (đã sửa để hiển thị cột trangThai, và form Add/Edit có thêm ô nhập Trạng thái)
 *
 * Bố cục:
 *  - Dòng 1 (y = 10): 4 nút Thêm, Sửa, Xóa, Làm mới
 *  - Dòng 2 (y = 50): Panel tìm kiếm (IDHDong, IDNV, IDNCC, nút Tìm kiếm)
 *  - Dòng 3 (y = 90): inputPanel ẩn/hiện khi Add/Edit (có thêm ô txtTrangThai)
 *  - Dòng 4 (y = 200): JTable (cao = 310) hiển thị các cột: IDHDong, Ngày bắt đầu, Ngày kết thúc, Nội dung, IDNV, IDNCC, Trạng thái
 */
public class HopDongPanel extends JPanel {

    private JTable tblHopDong;
    private DefaultTableModel tblModel;

    // inputPanel (ẩn khi currentMode == "NONE")
    private JPanel inputPanel;
    private JTextField txtIdHDong, txtNgayBatDau, txtNgayKetThuc, txtNoiDung, txtIdNV, txtIdNCC, txtTrangThai;
    private JButton btnSave, btnCancel;

    // panel tìm kiếm
    private JTextField txtSearchIdHDong, txtSearchIdNV, txtSearchIdNCC;
    private JButton btnSearch;

    // 4 nút chức năng
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
     *  - JTable (y = 200, cao = 310) hiển thị 7 cột: IDHDong, Ngày bắt đầu, Ngày kết thúc, Nội dung, IDNV, IDNCC, Trạng thái
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
            "IDHDong", "Ngày bắt đầu", "Ngày kết thúc", "Nội dung", "IDNV", "IDNCC", "Trạng thái"
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
        txtSearchIdHDong.setBounds(75, 3, 120, 25);
        searchPanel.add(txtSearchIdHDong);

        JLabel lblSearchIdNV = new JLabel("IDNV:");
        lblSearchIdNV.setBounds(220, 5, 50, 20);
        searchPanel.add(lblSearchIdNV);

        txtSearchIdNV = new JTextField();
        txtSearchIdNV.setBounds(270, 3, 120, 25);
        searchPanel.add(txtSearchIdNV);

        JLabel lblSearchIdNCC = new JLabel("IDNCC:");
        lblSearchIdNCC.setBounds(420, 5, 50, 20);
        searchPanel.add(lblSearchIdNCC);

        txtSearchIdNCC = new JTextField();
        txtSearchIdNCC.setBounds(480, 3, 120, 25);
        searchPanel.add(txtSearchIdNCC);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBounds(630, 3, 100, 25);
        searchPanel.add(btnSearch);
        btnSearch.addActionListener(e -> onSearch());
    }

    /**
     * Khởi tạo inputPanel (y = 90, cao = 100), ẩn khi visible = false.
     * Gồm các ô: IDHDong, NgayBatDau, NgayKetThuc, NoiDung, IDNV, IDNCC, TrangThai, 
     * và hai nút Lưu, Hủy.
     */
    private void initInputPanel(boolean visible) {
        inputPanel = new JPanel(null);
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
        lblNgayBatDau.setBounds(230, 10, 90, 25);
        inputPanel.add(lblNgayBatDau);
        txtNgayBatDau = new JTextField();
        txtNgayBatDau.setBounds(317, 10, 120, 25);
        inputPanel.add(txtNgayBatDau);

        // Ngày kết thúc
        JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        lblNgayKetThuc.setBounds(461, 10, 90, 25);
        inputPanel.add(lblNgayKetThuc);
        txtNgayKetThuc = new JTextField();
        txtNgayKetThuc.setBounds(559, 10, 120, 25);
        inputPanel.add(txtNgayKetThuc);

        // Nội dung
        JLabel lblNoiDung = new JLabel("Nội dung:");
        lblNoiDung.setBounds(10, 70, 70, 25);
        inputPanel.add(lblNoiDung);
        txtNoiDung = new JTextField();
        txtNoiDung.setBounds(90, 75, 400, 25);
        inputPanel.add(txtNoiDung);

        // IDNV
        JLabel lblIdNV = new JLabel("IDNV:");
        lblIdNV.setBounds(230, 40, 40, 25);
        inputPanel.add(lblIdNV);
        txtIdNV = new JTextField();
        txtIdNV.setBounds(317, 40, 120, 25);
        inputPanel.add(txtIdNV);

        // IDNCC
        JLabel lblIdNCC = new JLabel("IDNCC:");
        lblIdNCC.setBounds(461, 40, 50, 25);
        inputPanel.add(lblIdNCC);
        txtIdNCC = new JTextField();
        txtIdNCC.setBounds(559, 40, 120, 25);
        inputPanel.add(txtIdNCC);

        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(10, 45, 70, 25);
        inputPanel.add(lblTrangThai);
        txtTrangThai = new JTextField();
        txtTrangThai.setBounds(90, 45, 120, 25);
        inputPanel.add(txtTrangThai);

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
     * Load dữ liệu HopDong vào JTable, gồm 7 cột:
     *  IDHDong, Ngày bắt đầu, Ngày kết thúc, Nội dung, IDNV, IDNCC, Trạng thái.
     */
    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<HopDong> list = controller.getAllHopDong();
        for (HopDong hd : list) {
            tblModel.addRow(new Object[]{
                hd.getIdHDong(),
                DateHelper.toString(hd.getNgayBatDau(), "dd/MM/yyyy"),
                DateHelper.toString(hd.getNgayKetThuc(), "dd/MM/yyyy"),
                hd.getNoiDung() != null ? hd.getNoiDung() : "",
                hd.getIdNV() != null ? hd.getIdNV() : "",
                hd.getIdNCC() != null ? hd.getIdNCC() : "",
                hd.getTrangThai()
            });
        }
    }

    /**
     * Khi nhấn “Tìm kiếm”: lấy idHDong, idNV, idNCC, gọi controller.searchHopDong(...),
     * hiển thị kết quả lên table, chọn tự động dòng đầu tiên nếu có.
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
                hd.getNoiDung() != null ? hd.getNoiDung() : "",
                hd.getIdNV() != null ? hd.getIdNV() : "",
                hd.getIdNCC() != null ? hd.getIdNCC() : "",
                hd.getTrangThai()
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
     * Điền dữ liệu từ bảng lên inputPanel (nếu currentMode == "NONE").
     */
    private void populateInputFromTable(int row) {
        txtIdHDong.setText((String) tblModel.getValueAt(row, 0));
        txtNgayBatDau.setText((String) tblModel.getValueAt(row, 1));
        txtNgayKetThuc.setText((String) tblModel.getValueAt(row, 2));
        txtNoiDung.setText((String) tblModel.getValueAt(row, 3));
        txtIdNV.setText((String) tblModel.getValueAt(row, 4));
        txtIdNCC.setText((String) tblModel.getValueAt(row, 5));
        txtTrangThai.setText((String) tblModel.getValueAt(row, 6));
    }

    /**
     * Ẩn inputPanel và reset các ô, đồng thời enable lại nút/tìm kiếm/bảng.
     */
    private void hideInputPanel() {
        txtIdHDong.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtNoiDung.setText("");
        txtIdNV.setText("");
        txtIdNCC.setText("");
        txtTrangThai.setText("");

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
     * Khi bấm “Thêm”:
     *  - Hiện inputPanel (rỗng),
     *  - Disable các thành phần khác (nút, bảng, tìm kiếm),
     *  - currentMode = "ADDING".
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
        txtTrangThai.setText("");

        txtIdHDong.setEditable(true);

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
     * Khi bấm “Sửa”:
     *  - Phải có dòng được chọn,
     *  - Điền dữ liệu lên inputPanel,
     *  - Disable các thành phần khác,
     *  - currentMode = "EDITING".
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
     * Khi bấm “Xóa”:
     *  - Phải có dòng được chọn,
     *  - Xác nhận trước khi xóa,
     *  - Gọi controller.deleteHopDong(idHDong), nếu thành công reload bảng.
     */
    private void onDelete() {
        int row = tblHopDong.getSelectedRow();
        if (row < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn hợp đồng cần xóa!", "Cảnh báo");
            return;
        }
        String idHDong = (String) tblModel.getValueAt(row, 0);
        boolean confirm = MessageDialog.showConfirm(this,
                "Bạn có chắc muốn xóa hợp đồng " + idHDong + "?", "Xác nhận");
        if (confirm) {
            if (controller.deleteHopDong(idHDong)) {
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
     *  - Reload lại dữ liệu bảng.
     */
    private void onRefresh() {
        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Lưu” trong inputPanel:
     *  - Validate dữ liệu: IDHDong, Ngày bắt đầu, Ngày kết thúc, Trạng thái không được để trống;
     *    Ngày phải đúng định dạng dd/MM/yyyy; Nếu nhập IDNV hoặc IDNCC, không bắt buộc nhưng nếu nhập
     *    thì phải đúng độ dài (tùy bạn kiểm tra).
     *  - Nếu currentMode == "ADDING": gọi controller.addHopDong(hd),
     *    nếu currentMode == "EDITING": gọi controller.updateHopDong(hd),
     *  - Ẩn inputPanel và reload bảng nếu thành công.
     */
    private void onSave() {
        String idHDong = txtIdHDong.getText().trim();
        String ngayBatDauStr = txtNgayBatDau.getText().trim();
        String ngayKetThucStr = txtNgayKetThuc.getText().trim();
        String noiDung = txtNoiDung.getText().trim();
        String idNV = txtIdNV.getText().trim();
        String idNCC = txtIdNCC.getText().trim();
        String trangThai = txtTrangThai.getText().trim();

        if (idHDong.isEmpty()) {
            MessageDialog.showWarning(this, "IDHDong không được để trống!", "Cảnh báo");
            return;
        }
        if (!Validator.isDate(ngayBatDauStr, "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Ngày bắt đầu phải đúng định dạng dd/MM/yyyy!", "Cảnh báo");
            return;
        }
        if (!Validator.isDate(ngayKetThucStr, "dd/MM/yyyy")) {
            MessageDialog.showWarning(this, "Ngày kết thúc phải đúng định dạng dd/MM/yyyy!", "Cảnh báo");
            return;
        }
        if (trangThai.isEmpty()) {
            MessageDialog.showWarning(this, "Trạng thái không được để trống!", "Cảnh báo");
            return;
        }
        // idNV và idNCC có thể để trống, không kiểm tra thêm

        HopDong hd = new HopDong();
        hd.setIdHDong(idHDong);
        hd.setNgayBatDau(DateHelper.toDate(ngayBatDauStr, "dd/MM/yyyy"));
        hd.setNgayKetThuc(DateHelper.toDate(ngayKetThucStr, "dd/MM/yyyy"));
        hd.setNoiDung(noiDung.isEmpty() ? null : noiDung);
        hd.setIdNV(idNV.isEmpty() ? null : idNV);
        hd.setIdNCC(idNCC.isEmpty() ? null : idNCC);
        hd.setTrangThai(trangThai);

        boolean success;
        if (currentMode.equals("ADDING")) {
            success = controller.addHopDong(hd);
            if (success) {
                MessageDialog.showInfo(this, "Thêm hợp đồng thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Thêm hợp đồng thất bại! Kiểm tra IDHDong hoặc kết nối DB.", "Lỗi");
                return;
            }
        } else { // EDITING
            success = controller.updateHopDong(hd);
            if (success) {
                MessageDialog.showInfo(this, "Cập nhật hợp đồng thành công!", "Thông báo");
            } else {
                MessageDialog.showError(this, "Cập nhật hợp đồng thất bại! Kiểm tra lại dữ liệu.", "Lỗi");
                return;
            }
        }

        hideInputPanel();
        loadDataToTable();
    }

    /**
     * Khi bấm “Hủy” trong inputPanel:
     *  - Chỉ cần ẩn inputPanel, không thực hiện lưu.
     */
    private void onCancel() {
        hideInputPanel();
    }
}
