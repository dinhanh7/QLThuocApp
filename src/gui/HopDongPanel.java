package gui;

import controller.HopDongController;
import entities.HopDong;
import utils.DateHelper;
import utils.MessageDialog;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HopDongPanel extends JPanel {

    private JTable tblHopDong;
    private DefaultTableModel tblModel;
    private JTextField txtId, txtNgayBatDau, txtNgayKetThuc, txtNoiDung, txtIdNV, txtIdNCC;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private HopDongController controller;

    public HopDongPanel() {
        controller = new HopDongController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        JLabel lblId = new JLabel("ID HDong:");
        lblId.setBounds(10, 10, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 10, 150, 25);
        add(txtId);

        JLabel lblNgayBD = new JLabel("Ngày bắt đầu (dd/MM/yyyy):");
        lblNgayBD.setBounds(10, 45, 180, 25);
        add(lblNgayBD);

        txtNgayBatDau = new JTextField();
        txtNgayBatDau.setBounds(200, 45, 100, 25);
        add(txtNgayBatDau);

        JLabel lblNgayKT = new JLabel("Ngày kết thúc (dd/MM/yyyy):");
        lblNgayKT.setBounds(10, 80, 180, 25);
        add(lblNgayKT);

        txtNgayKetThuc = new JTextField();
        txtNgayKetThuc.setBounds(200, 80, 100, 25);
        add(txtNgayKetThuc);

        JLabel lblNoiDung = new JLabel("Nội dung:");
        lblNoiDung.setBounds(10, 115, 80, 25);
        add(lblNoiDung);

        txtNoiDung = new JTextField();
        txtNoiDung.setBounds(100, 115, 150, 25);
        add(txtNoiDung);

        JLabel lblIdNV = new JLabel("ID NV:");
        lblIdNV.setBounds(10, 150, 80, 25);
        add(lblIdNV);

        txtIdNV = new JTextField();
        txtIdNV.setBounds(100, 150, 150, 25);
        add(txtIdNV);

        JLabel lblIdNCC = new JLabel("ID NCC:");
        lblIdNCC.setBounds(10, 185, 80, 25);
        add(lblIdNCC);

        txtIdNCC = new JTextField();
        txtIdNCC.setBounds(100, 185, 150, 25);
        add(txtIdNCC);

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 230, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addHopDong());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 230, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editHopDong());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 230, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deleteHopDong());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 230, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "IDHDong", "NgàyBD", "NgàyKT", "Nội dung", "IDNV", "IDNCC"
        });
        tblHopDong = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblHopDong);
        scrollPane.setBounds(10, 280, 860, 130);
        add(scrollPane);

        tblHopDong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblHopDong.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<HopDong> list = controller.getAllHopDong();
        for (HopDong hd : list) {
            tblModel.addRow(new Object[] {
                    hd.getIdHDong(),
                    DateHelper.toString(hd.getNgayBatDau()),
                    DateHelper.toString(hd.getNgayKetThuc()),
                    hd.getNoiDung(),
                    hd.getIdNV() != null ? hd.getIdNV() : "",
                    hd.getIdNCC() != null ? hd.getIdNCC() : ""
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtNgayBatDau.setText((String) tblModel.getValueAt(row, 1));
        txtNgayKetThuc.setText((String) tblModel.getValueAt(row, 2));
        txtNoiDung.setText((String) tblModel.getValueAt(row, 3));
        txtIdNV.setText((String) tblModel.getValueAt(row, 4));
        txtIdNCC.setText((String) tblModel.getValueAt(row, 5));
    }

    private void clearForm() {
        txtId.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtNoiDung.setText("");
        txtIdNV.setText("");
        txtIdNCC.setText("");
    }

    private void addHopDong() {
        if (!validateInput()) return;
        HopDong hd = new HopDong();
        hd.setIdHDong(txtId.getText().trim());
        hd.setNgayBatDau(DateHelper.toDate(txtNgayBatDau.getText().trim()));
        hd.setNgayKetThuc(DateHelper.toDate(txtNgayKetThuc.getText().trim()));
        hd.setNoiDung(txtNoiDung.getText().trim());
        String idnv = txtIdNV.getText().trim();
        String idncc = txtIdNCC.getText().trim();
        if (!idnv.isEmpty()) {
            hd.setIdNV(idnv);
            hd.setIdNCC(null);
        } else {
            hd.setIdNV(null);
            hd.setIdNCC(idncc);
        }

        if (controller.addHopDong(hd)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editHopDong() {
        if (!validateInput()) return;
        HopDong hd = new HopDong();
        hd.setIdHDong(txtId.getText().trim());
        hd.setNgayBatDau(DateHelper.toDate(txtNgayBatDau.getText().trim()));
        hd.setNgayKetThuc(DateHelper.toDate(txtNgayKetThuc.getText().trim()));
        hd.setNoiDung(txtNoiDung.getText().trim());
        String idnv = txtIdNV.getText().trim();
        String idncc = txtIdNCC.getText().trim();
        if (!idnv.isEmpty()) {
            hd.setIdNV(idnv);
            hd.setIdNCC(null);
        } else {
            hd.setIdNV(null);
            hd.setIdNCC(idncc);
        }

        if (controller.updateHopDong(hd)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deleteHopDong() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn hợp đồng cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deleteHopDong(id)) {
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
            MessageDialog.showWarning(this, "IDHDong không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtNgayBatDau.getText()) || !Validator.isDate(txtNgayKetThuc.getText())) {
            MessageDialog.showWarning(this, "Ngày phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtNoiDung.getText())) {
            MessageDialog.showWarning(this, "Nội dung không được để trống", "Cảnh báo");
            return false;
        }
        // idNV hoặc idNCC phải có ít nhất một
        if (Validator.isNullOrEmpty(txtIdNV.getText()) && Validator.isNullOrEmpty(txtIdNCC.getText())) {
            MessageDialog.showWarning(this, "Phải nhập IDNV hoặc IDNCC", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// HopDongPanel.java 
