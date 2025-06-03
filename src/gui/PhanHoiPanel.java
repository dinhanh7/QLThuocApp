package gui;

import controller.PhanHoiController;
import entities.PhanHoi;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PhanHoiPanel extends JPanel {

    private JTable tblPhanHoi;
    private DefaultTableModel tblModel;
    private JTextField txtId, txtIdKH, txtIdHD, txtIdThuoc, txtNoiDung, txtThoiGian, txtDanhGia;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private PhanHoiController controller;

    public PhanHoiPanel() {
        controller = new PhanHoiController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        JLabel lblId = new JLabel("ID PH:");
        lblId.setBounds(10, 10, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 10, 150, 25);
        add(txtId);

        JLabel lblIdKH = new JLabel("ID KH:");
        lblIdKH.setBounds(10, 45, 80, 25);
        add(lblIdKH);

        txtIdKH = new JTextField();
        txtIdKH.setBounds(100, 45, 150, 25);
        add(txtIdKH);

        JLabel lblIdHD = new JLabel("ID HD:");
        lblIdHD.setBounds(10, 80, 80, 25);
        add(lblIdHD);

        txtIdHD = new JTextField();
        txtIdHD.setBounds(100, 80, 150, 25);
        add(txtIdHD);

        JLabel lblIdThuoc = new JLabel("ID Thuốc:");
        lblIdThuoc.setBounds(10, 115, 80, 25);
        add(lblIdThuoc);

        txtIdThuoc = new JTextField();
        txtIdThuoc.setBounds(100, 115, 150, 25);
        add(txtIdThuoc);

        JLabel lblNoiDung = new JLabel("Nội dung:");
        lblNoiDung.setBounds(10, 150, 80, 25);
        add(lblNoiDung);

        txtNoiDung = new JTextField();
        txtNoiDung.setBounds(100, 150, 150, 25);
        add(txtNoiDung);

        JLabel lblThoiGian = new JLabel("Thời gian (dd/MM/yyyy HH:mm):");
        lblThoiGian.setBounds(10, 185, 200, 25);
        add(lblThoiGian);

        txtThoiGian = new JTextField();
        txtThoiGian.setBounds(210, 185, 130, 25);
        add(txtThoiGian);

        JLabel lblDanhGia = new JLabel("Đánh giá (1-5):");
        lblDanhGia.setBounds(10, 220, 100, 25);
        add(lblDanhGia);

        txtDanhGia = new JTextField();
        txtDanhGia.setBounds(120, 220, 130, 25);
        add(txtDanhGia);

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 260, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addPhanHoi());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 260, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editPhanHoi());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 260, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deletePhanHoi());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 260, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "IDPH", "IDKH", "IDHD", "IDThuoc", "Nội dung", "Thời gian", "Đánh giá"
        });
        tblPhanHoi = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblPhanHoi);
        scrollPane.setBounds(10, 310, 860, 130);
        add(scrollPane);

        tblPhanHoi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhanHoi.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<PhanHoi> list = controller.getAllPhanHoi();
        for (PhanHoi ph : list) {
            tblModel.addRow(new Object[] {
                    ph.getIdPH(),
                    ph.getIdKH(),
                    ph.getIdHD(),
                    ph.getIdThuoc(),
                    ph.getNoiDung(),
                    DateHelper.toString(ph.getThoiGian(), "dd/MM/yyyy HH:mm"),
                    ph.getDanhGia() != null ? ph.getDanhGia() : ""
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtIdKH.setText((String) tblModel.getValueAt(row, 1));
        txtIdHD.setText((String) tblModel.getValueAt(row, 2));
        txtIdThuoc.setText((String) tblModel.getValueAt(row, 3));
        txtNoiDung.setText((String) tblModel.getValueAt(row, 4));
        txtThoiGian.setText((String) tblModel.getValueAt(row, 5));
        txtDanhGia.setText(tblModel.getValueAt(row, 6).toString());
    }

    private void clearForm() {
        txtId.setText("");
        txtIdKH.setText("");
        txtIdHD.setText("");
        txtIdThuoc.setText("");
        txtNoiDung.setText("");
        txtThoiGian.setText("");
        txtDanhGia.setText("");
    }

    private void addPhanHoi() {
        if (!validateInput()) return;
        PhanHoi ph = new PhanHoi();
        ph.setIdPH(txtId.getText().trim());
        ph.setIdKH(txtIdKH.getText().trim());
        ph.setIdHD(txtIdHD.getText().trim());
        ph.setIdThuoc(txtIdThuoc.getText().trim());
        ph.setNoiDung(txtNoiDung.getText().trim());
        ph.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        ph.setDanhGia(Integer.parseInt(txtDanhGia.getText().trim()));

        if (controller.addPhanHoi(ph)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editPhanHoi() {
        if (!validateInput()) return;
        PhanHoi ph = new PhanHoi();
        ph.setIdPH(txtId.getText().trim());
        ph.setIdKH(txtIdKH.getText().trim());
        ph.setIdHD(txtIdHD.getText().trim());
        ph.setIdThuoc(txtIdThuoc.getText().trim());
        ph.setNoiDung(txtNoiDung.getText().trim());
        ph.setThoiGian(DateHelper.toDate(txtThoiGian.getText().trim(), "dd/MM/yyyy HH:mm"));
        ph.setDanhGia(Integer.parseInt(txtDanhGia.getText().trim()));

        if (controller.updatePhanHoi(ph)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deletePhanHoi() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn phản hồi cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deletePhanHoi(id)) {
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
            MessageDialog.showWarning(this, "IDPH không được để trống", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtIdKH.getText()) || Validator.isNullOrEmpty(txtIdHD.getText())
                || Validator.isNullOrEmpty(txtIdThuoc.getText()) || Validator.isNullOrEmpty(txtNoiDung.getText())) {
            MessageDialog.showWarning(this, "Các trường không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtThoiGian.getText(), "dd/MM/yyyy HH:mm")) {
            MessageDialog.showWarning(this, "Thời gian phải đúng định dạng dd/MM/yyyy HH:mm", "Cảnh báo");
            return false;
        }
        if (!Validator.isPositiveInteger(txtDanhGia.getText())) {
            MessageDialog.showWarning(this, "Đánh giá phải là số nguyên", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// PhanHoiPanel.java 
