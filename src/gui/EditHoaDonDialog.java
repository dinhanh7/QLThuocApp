package gui;

import controller.HoaDonController;
import controller.ThuocController;
import entities.ChiTietHoaDon;
import entities.HoaDon;
import entities.Thuoc;
import utils.MessageDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditHoaDonDialog extends JDialog {
    private HoaDonController hoaDonController = new HoaDonController();
    private JTextField txtIdHD, txtThoiGian, txtIdNV, txtIdKH, txtTongTien, txtPhuongThucThanhToan, txtTrangThai;
    private JTable tblThuoc;
    private DefaultTableModel modelThuoc;
    private JButton btnAddThuoc, btnDeleteThuoc, btnSave, btnCancel;
    private boolean updated = false;
    private HoaDon hoaDon;
    private List<ChiTietHoaDon> chiTietList;
    private List<Thuoc> allThuocList = new ArrayList<>();

    public EditHoaDonDialog(Frame parent, HoaDon hoaDon, List<ChiTietHoaDon> chiTietList) {
        super(parent, "Sửa hóa đơn", true);
        this.hoaDon = hoaDon;
        this.chiTietList = chiTietList;
        setPreferredSize(new Dimension(900, 600));
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        allThuocList = new ThuocController().getAllThuoc();
        initComponents();
        fillData();
    }

    private void initComponents() {
        // ====== Panel thông tin hóa đơn ======
        JPanel pnlInfo = new JPanel(new GridLayout(3, 4, 8, 8));
        pnlInfo.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        txtIdHD = new JTextField();
        txtThoiGian = new JTextField();
        txtIdNV = new JTextField();
        txtIdKH = new JTextField();
        txtTongTien = new JTextField("0");
        txtPhuongThucThanhToan = new JTextField();
        txtTrangThai = new JTextField();

        txtIdHD.setEditable(false);
        txtThoiGian.setEditable(false);
        txtTongTien.setEditable(false);

        pnlInfo.add(new JLabel("Mã hóa đơn:"));
        pnlInfo.add(txtIdHD);
        pnlInfo.add(new JLabel("ID Nhân viên:"));
        pnlInfo.add(txtIdNV);
        pnlInfo.add(txtThoiGian);
        pnlInfo.add(new JLabel("Mã khách hàng:"));
        pnlInfo.add(txtIdKH);
        pnlInfo.add(new JLabel("Tổng tiền:"));
        pnlInfo.add(txtTongTien);
        pnlInfo.add(new JLabel("Phương thức TT:"));
        pnlInfo.add(txtPhuongThucThanhToan);
        pnlInfo.add(new JLabel("Trạng thái:"));
        pnlInfo.add(txtTrangThai);

        // ====== Bảng thuốc ======
        String[] colNames = {"Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        modelThuoc = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 1; // Chỉ cho sửa tên thuốc và số lượng
            }
        };
        tblThuoc = new JTable(modelThuoc);

        // ComboBox autocomplete cho cột "Tên thuốc"
        JComboBox<String> cbTenThuoc = new JComboBox<>();
        for (Thuoc t : allThuocList) cbTenThuoc.addItem(t.getTenThuoc());
        cbTenThuoc.setEditable(true);
        ((JTextField) cbTenThuoc.getEditor().getEditorComponent()).addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String input = ((JTextField) cbTenThuoc.getEditor().getEditorComponent()).getText();
                cbTenThuoc.removeAllItems();
                for (Thuoc t : allThuocList) {
                    if (t.getTenThuoc().toLowerCase().contains(input.toLowerCase())) {
                        cbTenThuoc.addItem(t.getTenThuoc());
                    }
                }
                cbTenThuoc.setSelectedItem(input);
                cbTenThuoc.showPopup();
            }
        });
        tblThuoc.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbTenThuoc));

        // Tự động cập nhật đơn giá, thành tiền, tổng tiền khi sửa tên thuốc hoặc số lượng
        modelThuoc.addTableModelListener(e -> updateThanhTienVaTongTien());

        JScrollPane scrollPane = new JScrollPane(tblThuoc);

        // ====== Nút thêm/xóa thuốc ======
        JPanel pnlBtnThuoc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddThuoc = new JButton("Thêm thuốc");
        btnDeleteThuoc = new JButton("Xóa dòng");
        pnlBtnThuoc.add(btnAddThuoc);
        pnlBtnThuoc.add(btnDeleteThuoc);

        btnAddThuoc.addActionListener(e -> modelThuoc.addRow(new Object[]{"", 1, 0.0, 0.0}));
        btnDeleteThuoc.addActionListener(e -> {
            int row = tblThuoc.getSelectedRow();
            if (row >= 0) modelThuoc.removeRow(row);
        });

        // ====== Nút lưu/hủy ======
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        pnlBtns.add(btnSave);
        pnlBtns.add(btnCancel);

        btnSave.addActionListener(this::onSave);
        btnCancel.addActionListener(e -> dispose());

        add(pnlInfo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlBtnThuoc, BorderLayout.WEST);
        add(pnlBtns, BorderLayout.SOUTH);
    }

    private void fillData() {
        txtIdHD.setText(hoaDon.getIdHD());
        txtThoiGian.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        txtIdNV.setText(hoaDon.getIdNV());
        txtIdKH.setText(hoaDon.getIdKH());
        txtTongTien.setText(String.format("%.0f", hoaDon.getTongTien()));
        txtPhuongThucThanhToan.setText(hoaDon.getPhuongThucThanhToan());
        txtTrangThai.setText(hoaDon.getTrangThaiDonHang());

        // Nạp chi tiết thuốc
        modelThuoc.setRowCount(0);
        for (ChiTietHoaDon ct : chiTietList) {
            double thanhTien = ct.getSoLuong() * ct.getDonGia();
            modelThuoc.addRow(new Object[]{
                ct.getTenThuoc(),
                ct.getSoLuong(),
                ct.getDonGia(),
                thanhTien
            });
        }
        updateThanhTienVaTongTien();
    }

    private void updateThanhTienVaTongTien() {
        double tongTien = 0.0;
        for (int i = 0; i < modelThuoc.getRowCount(); i++) {
            String tenThuoc = String.valueOf(modelThuoc.getValueAt(i, 0));
            int soLuong = 1;
            try {
                soLuong = Integer.parseInt(String.valueOf(modelThuoc.getValueAt(i, 1)));
                if (soLuong < 0) soLuong = 1;
            } catch (Exception ex) {
                soLuong = 1;
            }
            double donGia = 0.0;
            for (Thuoc t : allThuocList) {
                if (t.getTenThuoc().equals(tenThuoc)) {
                    donGia = t.getDonGia();
                    break;
                }
            }
            double thanhTien = soLuong * donGia;
            // Cập nhật giá trị đơn giá/thành tiền nếu khác
            if (!String.valueOf(modelThuoc.getValueAt(i, 2)).equals(String.valueOf(donGia))) {
                modelThuoc.setValueAt(donGia, i, 2);
            }
            if (!String.valueOf(modelThuoc.getValueAt(i, 3)).equals(String.valueOf(thanhTien))) {
                modelThuoc.setValueAt(thanhTien, i, 3);
            }
            tongTien += thanhTien;
        }
        txtTongTien.setText(String.format("%.0f", tongTien));
    }

    private void onSave(ActionEvent e) {
        try {
            if (txtIdHD.getText().trim().isEmpty()) {
                MessageDialog.showWarning(this, "Mã hóa đơn không được để trống!", "Lỗi");
                return;
            }
            if (modelThuoc.getRowCount() == 0) {
                MessageDialog.showWarning(this, "Hóa đơn cần ít nhất một loại thuốc!", "Lỗi");
                return;
            }

            HoaDon hd = new HoaDon();
            hd.setIdHD(txtIdHD.getText().trim());
            hd.setThoiGian(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(txtThoiGian.getText().trim()));
            hd.setIdNV(txtIdNV.getText().trim());
            hd.setIdKH(txtIdKH.getText().trim());
            hd.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));
            hd.setPhuongThucThanhToan(txtPhuongThucThanhToan.getText().trim());
            hd.setTrangThaiDonHang(txtTrangThai.getText().trim());

            List<ChiTietHoaDon> list = new ArrayList<>();
            for (int i = 0; i < modelThuoc.getRowCount(); i++) {
                String tenThuoc = String.valueOf(modelThuoc.getValueAt(i, 0));
                String idThuoc = "";
                double donGia = 0.0;
                for (Thuoc t : allThuocList) {
                    if (t.getTenThuoc().equals(tenThuoc)) {
                        idThuoc = t.getIdThuoc();
                        donGia = t.getDonGia();
                        break;
                    }
                }
                int soLuong = 1;
                try {
                    soLuong = Integer.parseInt(String.valueOf(modelThuoc.getValueAt(i, 1)));
                    if (soLuong < 0) soLuong = 1;
                } catch (Exception ex2) { soLuong = 1; }
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setIdHD(hd.getIdHD());
                cthd.setIdThuoc(idThuoc);
                cthd.setTenThuoc(tenThuoc);
                cthd.setSoLuong(soLuong);
                cthd.setDonGia(donGia);
                list.add(cthd);
            }

            boolean result = hoaDonController.updateHoaDonWithDetails(hd, list);
            if (result) {
                MessageDialog.showInfo(this, "Cập nhật hóa đơn thành công!", "Thành công");
                updated = true;
                dispose();
            } else {
                MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageDialog.showError(this, "Lỗi: " + ex.getMessage(), "Lỗi");
        }
    }

    public boolean isUpdated() {
        return updated;
    }
}
