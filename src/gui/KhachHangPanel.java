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

public class KhachHangPanel extends JPanel {

    private JTable tblKhachHang;
    private DefaultTableModel tblModel;
    private JTextField txtId, txtHoTen, txtSDT, txtGioiTinh, txtNgayThamGia;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private KhachHangController controller;

    public KhachHangPanel() {
        controller = new KhachHangController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        JLabel lblId = new JLabel("ID KH:");
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

        JLabel lblNgayTG = new JLabel("Ngày tham gia (dd/MM/yyyy):");
        lblNgayTG.setBounds(10, 150, 160, 25);
        add(lblNgayTG);

        txtNgayThamGia = new JTextField();
        txtNgayThamGia.setBounds(180, 150, 100, 25);
        add(txtNgayThamGia);

        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 190, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addKhachHang());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 190, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editKhachHang());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 190, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deleteKhachHang());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 190, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "IDKH", "Họ tên", "SĐT", "Giới tính", "Ngày tham gia"
        });
        tblKhachHang = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        scrollPane.setBounds(10, 240, 860, 130);
        add(scrollPane);

        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<KhachHang> list = controller.getAllKhachHang();
        for (KhachHang kh : list) {
            tblModel.addRow(new Object[] {
                    kh.getIdKH(),
                    kh.getHoTen(),
                    kh.getSdt(),
                    kh.getGioiTinh(),
                    DateHelper.toString(kh.getNgayThamGia())
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtHoTen.setText((String) tblModel.getValueAt(row, 1));
        txtSDT.setText((String) tblModel.getValueAt(row, 2));
        txtGioiTinh.setText((String) tblModel.getValueAt(row, 3));
        txtNgayThamGia.setText((String) tblModel.getValueAt(row, 4));
    }

    private void clearForm() {
        txtId.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtGioiTinh.setText("");
        txtNgayThamGia.setText("");
    }

    private void addKhachHang() {
        if (!validateInput()) return;
        KhachHang kh = new KhachHang();
        kh.setIdKH(txtId.getText().trim());
        kh.setHoTen(txtHoTen.getText().trim());
        kh.setSdt(txtSDT.getText().trim());
        kh.setGioiTinh(txtGioiTinh.getText().trim());
        kh.setNgayThamGia(DateHelper.toDate(txtNgayThamGia.getText().trim()));

        if (controller.addKhachHang(kh)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editKhachHang() {
        if (!validateInput()) return;
        KhachHang kh = new KhachHang();
        kh.setIdKH(txtId.getText().trim());
        kh.setHoTen(txtHoTen.getText().trim());
        kh.setSdt(txtSDT.getText().trim());
        kh.setGioiTinh(txtGioiTinh.getText().trim());
        kh.setNgayThamGia(DateHelper.toDate(txtNgayThamGia.getText().trim()));

        if (controller.updateKhachHang(kh)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deleteKhachHang() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn khách hàng cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deleteKhachHang(id)) {
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
            MessageDialog.showWarning(this, "IDKH không được để trống", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtHoTen.getText())) {
            MessageDialog.showWarning(this, "Họ tên không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtNgayThamGia.getText())) {
            MessageDialog.showWarning(this, "Ngày tham gia phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// KhachHangPanel.java 
