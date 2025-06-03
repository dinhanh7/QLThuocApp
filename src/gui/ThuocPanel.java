package gui;

import controller.ThuocController;
import entities.Thuoc;
import utils.DateHelper;
import utils.MessageDialog;
import utils.ImageHelper;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class ThuocPanel extends JPanel {

    private JTable tblThuoc;
    private DefaultTableModel tblModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnChooseImage;
    private JTextField txtId, txtTen, txtThanhPhan, txtSoLuong, txtGiaNhap, txtDonGia, txtHanSuDung;
    private JComboBox<String> cboDVT, cboDM, cboXX;
    private JLabel lblImage;
    private byte[] imageData;
    private ThuocController controller;

    public ThuocPanel() {
        controller = new ThuocController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(null);

        // --- Form nhập liệu --- //
        JLabel lblId = new JLabel("ID Thuốc:");
        lblId.setBounds(10, 10, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 10, 150, 25);
        add(txtId);

        JLabel lblTen = new JLabel("Tên:");
        lblTen.setBounds(10, 45, 80, 25);
        add(lblTen);

        txtTen = new JTextField();
        txtTen.setBounds(100, 45, 150, 25);
        add(txtTen);

        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setBounds(10, 80, 80, 25);
        add(lblThanhPhan);

        txtThanhPhan = new JTextField();
        txtThanhPhan.setBounds(100, 80, 150, 25);
        add(txtThanhPhan);

        JLabel lblSoLuong = new JLabel("SL Tồn:");
        lblSoLuong.setBounds(10, 115, 80, 25);
        add(lblSoLuong);

        txtSoLuong = new JTextField();
        txtSoLuong.setBounds(100, 115, 150, 25);
        add(txtSoLuong);

        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setBounds(10, 150, 80, 25);
        add(lblGiaNhap);

        txtGiaNhap = new JTextField();
        txtGiaNhap.setBounds(100, 150, 150, 25);
        add(txtGiaNhap);

        JLabel lblDonGia = new JLabel("Đơn giá:");
        lblDonGia.setBounds(10, 185, 80, 25);
        add(lblDonGia);

        txtDonGia = new JTextField();
        txtDonGia.setBounds(100, 185, 150, 25);
        add(txtDonGia);

        JLabel lblHan = new JLabel("HSD (dd/MM/yyyy):");
        lblHan.setBounds(10, 220, 120, 25);
        add(lblHan);

        txtHanSuDung = new JTextField();
        txtHanSuDung.setBounds(130, 220, 120, 25);
        add(txtHanSuDung);

        JLabel lblDVT = new JLabel("Đơn vị:");
        lblDVT.setBounds(10, 255, 80, 25);
        add(lblDVT);

        cboDVT = new JComboBox<>();
        cboDVT.setBounds(100, 255, 150, 25);
        // TODO: load dữ liệu đơn vị từ DAO DonViTinh
        add(cboDVT);

        JLabel lblDM = new JLabel("Danh mục:");
        lblDM.setBounds(10, 290, 80, 25);
        add(lblDM);

        cboDM = new JComboBox<>();
        cboDM.setBounds(100, 290, 150, 25);
        // TODO: load dữ liệu danh mục từ DAO DanhMuc
        add(cboDM);

        JLabel lblXX = new JLabel("Xuất xứ:");
        lblXX.setBounds(10, 325, 80, 25);
        add(lblXX);

        cboXX = new JComboBox<>();
        cboXX.setBounds(100, 325, 150, 25);
        // TODO: load dữ liệu xuất xứ từ DAO XuatXu
        add(cboXX);

        lblImage = new JLabel();
        lblImage.setBounds(300, 10, 150, 150);
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(lblImage);

        btnChooseImage = new JButton("Chọn ảnh");
        btnChooseImage.setBounds(300, 170, 150, 25);
        add(btnChooseImage);
        btnChooseImage.addActionListener(e -> chooseImage());

        // --- Buttons --- //
        btnAdd = new JButton("Thêm");
        btnAdd.setBounds(10, 370, 80, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> addThuoc());

        btnEdit = new JButton("Sửa");
        btnEdit.setBounds(100, 370, 80, 30);
        add(btnEdit);
        btnEdit.addActionListener(e -> editThuoc());

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(190, 370, 80, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> deleteThuoc());

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(280, 370, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> clearForm());

        // --- Table hiển thị --- //
        tblModel = new DefaultTableModel();
        tblModel.setColumnIdentifiers(new String[] {
                "ID", "Tên", "Thành phần", "SL Tồn", "Giá nhập", "Đơn giá", "HSD", "ĐV", "DM", "XX"
        });
        tblThuoc = new JTable(tblModel);
        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setBounds(10, 420, 860, 130);
        add(scrollPane);

        tblThuoc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblThuoc.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });
    }

    private void loadDataToTable() {
        tblModel.setRowCount(0);
        List<Thuoc> list = controller.getAllThuoc();
        for (Thuoc t : list) {
            tblModel.addRow(new Object[] {
                    t.getIdThuoc(),
                    t.getTenThuoc(),
                    t.getThanhPhan(),
                    t.getSoLuongTon(),
                    t.getGiaNhap(),
                    t.getDonGia(),
                    DateHelper.toString(t.getHanSuDung()),
                    t.getIdDVT(),
                    t.getIdDM(),
                    t.getIdXX()
            });
        }
    }

    private void populateFormFromTable(int row) {
        txtId.setText((String) tblModel.getValueAt(row, 0));
        txtTen.setText((String) tblModel.getValueAt(row, 1));
        txtThanhPhan.setText((String) tblModel.getValueAt(row, 2));
        txtSoLuong.setText(tblModel.getValueAt(row, 3).toString());
        txtGiaNhap.setText(tblModel.getValueAt(row, 4).toString());
        txtDonGia.setText(tblModel.getValueAt(row, 5).toString());
        txtHanSuDung.setText((String) tblModel.getValueAt(row, 6));
        cboDVT.setSelectedItem(tblModel.getValueAt(row, 7));
        cboDM.setSelectedItem(tblModel.getValueAt(row, 8));
        cboXX.setSelectedItem(tblModel.getValueAt(row, 9));
        // TODO: nếu bạn lưu image trong DB, load lại vào lblImage
    }

    private void clearForm() {
        txtId.setText("");
        txtTen.setText("");
        txtThanhPhan.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtDonGia.setText("");
        txtHanSuDung.setText("");
        cboDVT.setSelectedIndex(-1);
        cboDM.setSelectedIndex(-1);
        cboXX.setSelectedIndex(-1);
        lblImage.setIcon(null);
        imageData = null;
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            imageData = ImageHelper.toByteArray(file);
            ImageIcon icon = ImageHelper.toImageIcon(imageData);
            lblImage.setIcon(ImageHelper.scaleImage(icon, lblImage.getWidth(), lblImage.getHeight()));
        }
    }

    private void addThuoc() {
        if (!validateInput()) return;
        Thuoc t = new Thuoc();
        t.setIdThuoc(txtId.getText().trim());
        t.setTenThuoc(txtTen.getText().trim());
        t.setThanhPhan(txtThanhPhan.getText().trim());
        t.setSoLuongTon(Integer.parseInt(txtSoLuong.getText().trim()));
        t.setGiaNhap(Double.parseDouble(txtGiaNhap.getText().trim()));
        t.setDonGia(Double.parseDouble(txtDonGia.getText().trim()));
        t.setHanSuDung(DateHelper.toDate(txtHanSuDung.getText().trim()));
        t.setIdDVT((String) cboDVT.getSelectedItem());
        t.setIdDM((String) cboDM.getSelectedItem());
        t.setIdXX((String) cboXX.getSelectedItem());
        t.setHinhAnh(imageData);

        if (controller.addThuoc(t)) {
            MessageDialog.showInfo(this, "Thêm thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Thêm thất bại!", "Lỗi");
        }
    }

    private void editThuoc() {
        if (!validateInput()) return;
        Thuoc t = new Thuoc();
        t.setIdThuoc(txtId.getText().trim());
        t.setTenThuoc(txtTen.getText().trim());
        t.setThanhPhan(txtThanhPhan.getText().trim());
        t.setSoLuongTon(Integer.parseInt(txtSoLuong.getText().trim()));
        t.setGiaNhap(Double.parseDouble(txtGiaNhap.getText().trim()));
        t.setDonGia(Double.parseDouble(txtDonGia.getText().trim()));
        t.setHanSuDung(DateHelper.toDate(txtHanSuDung.getText().trim()));
        t.setIdDVT((String) cboDVT.getSelectedItem());
        t.setIdDM((String) cboDM.getSelectedItem());
        t.setIdXX((String) cboXX.getSelectedItem());
        t.setHinhAnh(imageData);

        if (controller.updateThuoc(t)) {
            MessageDialog.showInfo(this, "Cập nhật thành công!", "Thông báo");
            loadDataToTable();
            clearForm();
        } else {
            MessageDialog.showError(this, "Cập nhật thất bại!", "Lỗi");
        }
    }

    private void deleteThuoc() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            MessageDialog.showWarning(this, "Chọn thuốc cần xóa!", "Cảnh báo");
            return;
        }
        boolean confirm = MessageDialog.showConfirm(this, "Bạn có chắc muốn xóa?", "Xác nhận");
        if (confirm) {
            if (controller.deleteThuoc(id)) {
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
            MessageDialog.showWarning(this, "ID không được để trống", "Cảnh báo");
            return false;
        }
        if (Validator.isNullOrEmpty(txtTen.getText())) {
            MessageDialog.showWarning(this, "Tên không được để trống", "Cảnh báo");
            return false;
        }
        if (!Validator.isPositiveInteger(txtSoLuong.getText())) {
            MessageDialog.showWarning(this, "Số lượng phải là số nguyên >= 0", "Cảnh báo");
            return false;
        }
        if (!Validator.isDouble(txtGiaNhap.getText()) || !Validator.isDouble(txtDonGia.getText())) {
            MessageDialog.showWarning(this, "Giá phải là số", "Cảnh báo");
            return false;
        }
        if (!Validator.isDate(txtHanSuDung.getText())) {
            MessageDialog.showWarning(this, "Hạn sử dụng phải đúng định dạng dd/MM/yyyy", "Cảnh báo");
            return false;
        }
        if (cboDVT.getSelectedIndex() < 0 || cboDM.getSelectedIndex() < 0 || cboXX.getSelectedIndex() < 0) {
            MessageDialog.showWarning(this, "Vui lòng chọn Đơn vị, Danh mục và Xuất xứ", "Cảnh báo");
            return false;
        }
        return true;
    }
}
// ThuocPanel.java 
