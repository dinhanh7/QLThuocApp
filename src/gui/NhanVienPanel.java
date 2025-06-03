package gui;

import controller.NhanVienController;
import entities.NhanVien;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class NhanVienPanel extends JPanel {

    private JTable tblNhanVien;
    private DefaultTableModel tblModel;
    private JTextField txtId, txtHoTen, txtSDT, txtGioiTinh, txtNamSinh, txtNgayVaoLam;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private NhanVienController controller;

    public NhanVienPanel() {
        controller = new NhanVienController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        JLabel lblId = new JLabel("ID NV:");
        lblId.setBounds(10, 10, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 10, 150, 25);
        add(txtId);

        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setBounds(10, 45, 80, 25);
        add(lblHoTen);

        txtHoTen = new JTextField();
        txtHoTen.setBounds(100, 45, 150, 25);
        add(txtHoTen);

        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setBounds(10, 80, 80, 25);
        add(lblSDT);

        txtSDT = new JTextField();
        txtSDT.setBounds(100, 80, 150, 25);
        add(txtSDT);

        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setBounds(10, 115, 80, 25);
        add(lblGioiTinh);

        txtGioiTinh = new JTextField();
        txtGioiTinh.setBounds(100, 115, 150, 25);
        add(txtGioiTinh);

        JLabel lblNamSinh = new JLabel("Năm sinh:");
        lblNamSinh.setBounds(10, 150, 80, 25);
        add(lblNamSinh);

        txtNamSinh = new JTextField();
        txtNamSinh.setBounds(100, 150, 150, 25);
        add(txtNamSinh);

        JLabel lblNgayVL = new JLabel("Ngày vào (dd/MM/yyyy):");
        lblNgayVL.setBounds(10, 185, 120, 25);
        add(lblNgayVL);

        txtNgayVaoLam = new JTextField();
        txtNgayVaoLam.setBounds(140, 185, 110, 25);
        add(txtNgayVaoLam);

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 230, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addNhanVien());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 230, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editNhanVien());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 230, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deleteNhanVien());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 230, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "IDNV", "Họ tên", "SĐT", "Giới tính", "Năm sinh", "Ngày vào"
        });
        tblNhanVien = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBounds(10, 280, 860, 130);
        add(scrollPane);

        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<NhanVien> list = controller.getAllNhanVien();
        for (NhanVien nv : list) {
            tblModel.addRow(new Object[] {
                    nv.getIdNV(),
                    nv.getHoTen(),
                    nv.getSdt(),
                    nv.getGioiTinh(),
                    nv.getNamSinh(),
                    DateHelper.toString(nv.getNgayVaoLam())
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtHoTen.setText((String) tblModel.getValueAt(row, 1));
        txtSDT.setText((String) tblModel.getValueAt(row, 2));
        txtGioiTinh.setText((String) tblModel.getValueAt(row, 3));
        txtNamSinh.setText(tblModel.getValueAt(row, 4).toString());
        txtNgayVaoLam.setText((String) tblModel.getValueAt(row, 5));
    }

    private void clearForm() {
        txtId.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtGioiTinh.setText("");
        txtNamSinh.setText("");
        txtNgayVaoLam.setText("");
    }

    private void addNhanVien() {
        if (!validateInput()) return;
        NhanVien nv = new NhanVien();
        nv.setIdNV(txtId.getText().trim());
        nv.setHoTen(txtHoTen.getText().trim());
        nv.setSdt(txtSDT.getText().trim());
        nv.setGioiTinh(txtGioiTinh.getText().trim());
        nv.setNamSinh(Integer.parseInt(txtNamSinh.getText().trim()));
        nv.setNgayVaoLam(DateHelper.toDate(txtNgayVaoLam.getText().trim()));

        if (controller.addNhanVien(nv)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editNhanVien() {
        if (!validateInput()) return;
        NhanVien nv = new NhanVien();
        nv.setIdNV(txtId.getText().trim());
        nv.setHoTen(txtHoTen.getText().trim());
        nv.setSdt(txtSDT.getText().trim());
        nv.setGioiTinh(txtGioiTinh.getText().trim());
        nv.setNamSinh(Integer.parseInt(txtNamSinh.getText().trim()));
        nv.setNgayVaoLam(DateHelper.toDate(txtNgayVaoLam.getText().trim()));

        if (controller.updateNhanVien(nv)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deleteNhanVien() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn nhân viên cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deleteNhanVien(id)) {
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
            MessageDialog.showWarning(this, "IDNV không được để trống", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtHoTen.getText())) {
            MessageDialog.showWarning(this, "Họ tên không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isPositiveInteger(txtNamSinh.getText())) {
            MessageDialog.showWarning(this, "Năm sinh phải là số nguyên", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtNgayVaoLam.getText())) {
            MessageDialog.showWarning(this, "Ngày vào phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// NhanVienPanel.java 
