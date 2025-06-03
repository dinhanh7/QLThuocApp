package gui;

import controller.PhieuDatHangController;
import entities.PhieuDatHang;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PhieuDatHangPanel extends JPanel {

    private JTable tblPhieuDatHang;
    private DefaultTableModel tblModel;
    private JTextField txtId, txtThoiGian, txtIdKH, txtTongTien, txtDiaChi, txtPhuongThuc, txtTrangThai;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private PhieuDatHangController controller;

    public PhieuDatHangPanel() {
        controller = new PhieuDatHangController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        JLabel lblId = new JLabel("ID PDH:");
        lblId.setBounds(10, 10, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 10, 150, 25);
        add(txtId);

        JLabel lblTG = new JLabel("Thời gian (dd/MM/yyyy HH:mm):");
        lblTG.setBounds(10, 45, 200, 25);
        add(lblTG);

        txtThoiGian = new JTextField();
        txtThoiGian.setBounds(210, 45, 130, 25);
        add(txtThoiGian);

        JLabel lblIdKH = new JLabel("ID KH:");
        lblIdKH.setBounds(10, 80, 80, 25);
        add(lblIdKH);

        txtIdKH = new JTextField();
        txtIdKH.setBounds(100, 80, 150, 25);
        add(txtIdKH);

        JLabel lblTong = new JLabel("Tổng tiền:");
        lblTong.setBounds(10, 115, 80, 25);
        add(lblTong);

        txtTongTien = new JTextField();
        txtTongTien.setBounds(100, 115, 150, 25);
        add(txtTongTien);

        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(10, 150, 80, 25);
        add(lblDiaChi);

        txtDiaChi = new JTextField();
        txtDiaChi.setBounds(100, 150, 150, 25);
        add(txtDiaChi);

        JLabel lblPTTT = new JLabel("PTTT:");
        lblPTTT.setBounds(10, 185, 80, 25);
        add(lblPTTT);

        txtPhuongThuc = new JTextField();
        txtPhuongThuc.setBounds(100, 185, 150, 25);
        add(txtPhuongThuc);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(10, 220, 80, 25);
        add(lblTrangThai);

        txtTrangThai = new JTextField();
        txtTrangThai.setBounds(100, 220, 150, 25);
        add(txtTrangThai);

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 260, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addPhieuDatHang());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 260, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editPhieuDatHang());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 260, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deletePhieuDatHang());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 260, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "IDPDH", "Thời gian", "IDKH", "Tổng tiền", "Địa chỉ", "PTTT", "Trạng thái"
        });
        tblPhieuDatHang = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblPhieuDatHang);
        scrollPane.setBounds(10, 310, 860, 130);
        add(scrollPane);

        tblPhieuDatHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuDatHang.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<PhieuDatHang> list = controller.getAllPhieuDatHang();
        for (PhieuDatHang pdh : list) {
            tblModel.addRow(new Object[] {
                    pdh.getIdPDH(),
                    DateHelper.toString(pdh.getThoiGian(), "dd/MM/yyyy HH:mm"),
                    pdh.getIdKH(),
                    pdh.getTongTien(),
                    pdh.getDiaChi(),
                    pdh.getPhuongThucThanhToan(),
                    pdh.getTrangThai()
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdKH.setText((String) tblModel.getValueAt(row, 2));
        txtTongTien.setText(tblModel.getValueAt(row, 3).toString());
        txtDiaChi.setText((String) tblModel.getValueAt(row, 4));
        txtPhuongThuc.setText((String) tblModel.getValueAt(row, 5));
        txtTrangThai.setText((String) tblModel.getValueAt(row, 6));
    }

    private void clearForm() {
        txtId.setText("");
        txtThoiGian.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");
        txtDiaChi.setText("");
        txtPhuongThuc.setText("");
        txtTrangThai.setText("");
    }

    private void addPhieuDatHang() {
        if (!validateInput()) return;
        PhieuDatHang pdh = new PhieuDatHang();
        pdh.setIdPDH(txtId.getText().trim());
        pdh.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        pdh.setIdKH(txtIdKH.getText().trim());
        pdh.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));
        pdh.setDiaChi(txtDiaChi.getText().trim());
        pdh.setPhuongThucThanhToan(txtPhuongThuc.getText().trim());
        pdh.setTrangThai(txtTrangThai.getText().trim());

        if (controller.addPhieuDatHang(pdh)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editPhieuDatHang() {
        if (!validateInput()) return;
        PhieuDatHang pdh = new PhieuDatHang();
        pdh.setIdPDH(txtId.getText().trim());
        pdh.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        pdh.setIdKH(txtIdKH.getText().trim());
        pdh.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));
        pdh.setDiaChi(txtDiaChi.getText().trim());
        pdh.setPhuongThucThanhToan(txtPhuongThuc.getText().trim());
        pdh.setTrangThai(txtTrangThai.getText().trim());

        if (controller.updatePhieuDatHang(pdh)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deletePhieuDatHang() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn phiếu đặt hàng cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deletePhieuDatHang(id)) {
                MessageDialog.showInfo(this, "Xóa thành công!", "Thông báo");
                loadDataToTable();
                clearForm();
            } else {
                MessageDialog.showError(this, "Xóa thất bại!", "Lỗi");
            }
        }
    }

    private boolean validateInput() {
        if (Validator.isNullOrEmpty(txtId.getText())) {
            MessageDialog.showWarning(this, "IDPDH không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtThoiGian.getText(), "dd/MM/yyyy HH:mm")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtIdKH.getText()) || Validator.isNullOrEmpty(txtDiaChi.getText())
                || Validator.isNullOrEmpty(txtPhuongThuc.getText()) || Validator.isNullOrEmpty(txtTrangThai.getText())) {
            MessageDialog.showWarning(this, "Các trường không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDouble(txtTongTien.getText())) {
            MessageDialog.showWarning(this, "Tổng tiền phải là số", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// PhieuDatHangPanel.java 
