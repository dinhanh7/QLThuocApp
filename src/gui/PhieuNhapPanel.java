package gui;

import controller.PhieuNhapController;
import entities.PhieuNhap;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PhieuNhapPanel extends JPanel {

    private JTable tblPhieuNhap;
    private DefaultTableModel tblModel;
    private JTextField txtId, txtThoiGian, txtIdNV, txtIdNCC, txtTongTien;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private PhieuNhapController controller;

    public PhieuNhapPanel() {
        controller = new PhieuNhapController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        JLabel lblId = new JLabel("ID PN:");
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

        JLabel lblIdNV = new JLabel("ID NV:");
        lblIdNV.setBounds(10, 80, 80, 25);
        add(lblIdNV);

        txtIdNV = new JTextField();
        txtIdNV.setBounds(100, 80, 150, 25);
        add(txtIdNV);

        JLabel lblIdNCC = new JLabel("ID NCC:");
        lblIdNCC.setBounds(10, 115, 80, 25);
        add(lblIdNCC);

        txtIdNCC = new JTextField();
        txtIdNCC.setBounds(100, 115, 150, 25);
        add(txtIdNCC);

        JLabel lblTong = new JLabel("Tổng tiền:");
        lblTong.setBounds(10, 150, 80, 25);
        add(lblTong);

        txtTongTien = new JTextField();
        txtTongTien.setBounds(100, 150, 150, 25);
        add(txtTongTien);

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 190, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addPhieuNhap());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 190, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editPhieuNhap());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 190, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deletePhieuNhap());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 190, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "IDPN", "Thời gian", "IDNV", "IDNCC", "Tổng tiền"
        });
        tblPhieuNhap = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblPhieuNhap);
        scrollPane.setBounds(10, 240, 860, 130);
        add(scrollPane);

        tblPhieuNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuNhap.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<PhieuNhap> list = controller.getAllPhieuNhap();
        for (PhieuNhap pn : list) {
            tblModel.addRow(new Object[] {
                    pn.getIdPN(),
                    DateHelper.toString(pn.getThoiGian(), "dd/MM/yyyy HH:mm"),
                    pn.getIdNV(),
                    pn.getIdNCC(),
                    pn.getTongTien()
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdNV.setText((String) tblModel.getValueAt(row, 2));
        txtIdNCC.setText((String) tblModel.getValueAt(row, 3));
        txtTongTien.setText(tblModel.getValueAt(row, 4).toString());
    }

    private void clearForm() {
        txtId.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdNCC.setText("");
        txtTongTien.setText("");
    }

    private void addPhieuNhap() {
        if (!validateInput()) return;
        PhieuNhap pn = new PhieuNhap();
        pn.setIdPN(txtId.getText().trim());
        pn.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        pn.setIdNV(txtIdNV.getText().trim());
        pn.setIdNCC(txtIdNCC.getText().trim());
        pn.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));

        if (controller.addPhieuNhap(pn)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editPhieuNhap() {
        if (!validateInput()) return;
        PhieuNhap pn = new PhieuNhap();
        pn.setIdPN(txtId.getText().trim());
        pn.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        pn.setIdNV(txtIdNV.getText().trim());
        pn.setIdNCC(txtIdNCC.getText().trim());
        pn.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));

        if (controller.updatePhieuNhap(pn)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deletePhieuNhap() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn phiếu nhập cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deletePhieuNhap(id)) {
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
            MessageDialog.showWarning(this, "IDPN không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtThoiGian.getText(), "dd/MM/yyyy HH:mm")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtIdNV.getText()) || Validator.isNullOrEmpty(txtIdNCC.getText())) {
            MessageDialog.showWarning(this, "IDNV và IDNCC không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDouble(txtTongTien.getText())) {
            MessageDialog.showWarning(this, "Tổng tiền phải là số", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// PhieuNhapPanel.java 
