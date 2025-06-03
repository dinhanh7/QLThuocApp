package gui;

import controller.HoaDonController;
import entities.HoaDon;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HoaDonPanel extends JPanel {

    private JTable tblHoaDon;
    private DefaultTableModel tblModel;
    private JTextField txtId, txtThoiGian, txtIdNV, txtIdKH, txtTongTien;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private HoaDonController controller;

    public HoaDonPanel() {
        controller = new HoaDonController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        JLabel lblId = new JLabel("ID HD:");
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

        JLabel lblIdKH = new JLabel("ID KH:");
        lblIdKH.setBounds(10, 115, 80, 25);
        add(lblIdKH);

        txtIdKH = new JTextField();
        txtIdKH.setBounds(100, 115, 150, 25);
        add(txtIdKH);

        JLabel lblTong = new JLabel("Tổng tiền:");
        lblTong.setBounds(10, 150, 80, 25);
        add(lblTong);

        txtTongTien = new JTextField();
        txtTongTien.setBounds(100, 150, 150, 25);
        add(txtTongTien);

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 190, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addHoaDon());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 190, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editHoaDon());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 190, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deleteHoaDon());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 190, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "IDHD", "Thời gian", "IDNV", "IDKH", "Tổng tiền"
        });
        tblHoaDon = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBounds(10, 240, 860, 130);
        add(scrollPane);

        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblHoaDon.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<HoaDon> list = controller.getAllHoaDon();
        for (HoaDon hd : list) {
            tblModel.addRow(new Object[] {
                    hd.getIdHD(),
                    DateHelper.toString(hd.getThoiGian(), "dd/MM/yyyy HH:mm"),
                    hd.getIdNV(),
                    hd.getIdKH(),
                    hd.getTongTien()
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 1));
        txtIdNV.setText((String) tblModel.getValueAt(row, 2));
        txtIdKH.setText((String) tblModel.getValueAt(row, 3));
        txtTongTien.setText(tblModel.getValueAt(row, 4).toString());
    }

    private void clearForm() {
        txtId.setText("");
        txtThoiGian.setText("");
        txtIdNV.setText("");
        txtIdKH.setText("");
        txtTongTien.setText("");
    }

    private void addHoaDon() {
        if (!validateInput()) return;
        HoaDon hd = new HoaDon();
        hd.setIdHD(txtId.getText().trim());
        hd.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        hd.setIdNV(txtIdNV.getText().trim());
        hd.setIdKH(txtIdKH.getText().trim());
        hd.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));

        if (controller.addHoaDon(hd)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editHoaDon() {
        if (!validateInput()) return;
        HoaDon hd = new HoaDon();
        hd.setIdHD(txtId.getText().trim());
        hd.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        hd.setIdNV(txtIdNV.getText().trim());
        hd.setIdKH(txtIdKH.getText().trim());
        hd.setTongTien(Double.parseDouble(txtTongTien.getText().trim()));

        if (controller.updateHoaDon(hd)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deleteHoaDon() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn hóa đơn cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deleteHoaDon(id)) {
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
            MessageDialog.showWarning(this, "IDHD không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtThoiGian.getText(), "dd/MM/yyyy HH:mm")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtIdNV.getText()) || Validator.isNullOrEmpty(txtIdKH.getText())) {
            MessageDialog.showWarning(this, "IDNV và IDKH không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDouble(txtTongTien.getText())) {
            MessageDialog.showWarning(this, "Tổng tiền phải là số", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// HoaDonPanel.java 
