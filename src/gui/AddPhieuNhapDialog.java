package gui;

import controller.ChiTietPhieuNhapController;
import controller.NhaCungCapController;
import controller.PhieuNhapController;
import controller.ThuocController;
import entities.NhaCungCap;
import entities.PhieuNhap;
import entities.Thuoc;
import entities.ChiTietPhieuNhap;
import utils.DateHelper;
import utils.MessageDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AddPhieuNhapDialog extends JDialog {
    private JTextField txtIDPN, txtThoiGian, txtIDNV, txtNCC, txtTenThuoc, txtThanhPhan, txtDonViTinh, txtDanhMuc, txtXuatXu, txtSoLuong, txtGiaNhap, txtDonGia, txtHanSuDung;
    private JButton btnChonNCC, btnThemNCC, btnThemThuoc, btnLuu, btnHuy;
    private JComboBox<String> comboNCC;
    private DefaultTableModel tblModelThuoc;
    private JTable tblThuoc;
    private JLabel lblTongTien;

    // Dữ liệu tạm
    private List<Thuoc> listThuocTam = new ArrayList<>();
    private List<ChiTietPhieuNhap> listChiTietTam = new ArrayList<>();
    private double tongTien = 0.0;
    private List<NhaCungCap> dsNCC; // Gợi ý NCC

    public AddPhieuNhapDialog(JFrame parent) {
        super(parent, "Thêm phiếu nhập mới", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(null);

        // Panel phiếu nhập
        JPanel pnPN = new JPanel(null);
        pnPN.setBounds(10, 10, 870, 90);
        add(pnPN);

        JLabel lblIDPN = new JLabel("IDPN:");
        lblIDPN.setBounds(10, 10, 50, 25); pnPN.add(lblIDPN);
        txtIDPN = new JTextField();
        txtIDPN.setBounds(60, 10, 80, 25); pnPN.add(txtIDPN);

        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(180, 10, 70, 25); pnPN.add(lblThoiGian);
        txtThoiGian = new JTextField();
        txtThoiGian.setBounds(250, 10, 150, 25); pnPN.add(txtThoiGian);

        JLabel lblIDNV = new JLabel("IDNV:");
        lblIDNV.setBounds(430, 10, 50, 25); pnPN.add(lblIDNV);
        txtIDNV = new JTextField();
        txtIDNV.setBounds(480, 10, 80, 25); pnPN.add(txtIDNV);

        JLabel lblNCC = new JLabel("Nhà cung cấp:");
        lblNCC.setBounds(10, 45, 100, 25); pnPN.add(lblNCC);
        txtNCC = new JTextField();
        txtNCC.setBounds(110, 45, 200, 25); pnPN.add(txtNCC);

        comboNCC = new JComboBox<>();
        comboNCC.setBounds(320, 45, 300, 25); pnPN.add(comboNCC);
        comboNCC.setEditable(false);

        btnThemNCC = new JButton("Thêm nhà cung cấp");
        btnThemNCC.setBounds(630, 45, 160, 25);
        pnPN.add(btnThemNCC);
        btnThemNCC.setVisible(false);

        // Thêm thuốc
        JPanel pnThuoc = new JPanel(null);
        pnThuoc.setBorder(BorderFactory.createTitledBorder("Thông tin thuốc"));
        pnThuoc.setBounds(10, 110, 860, 130);
        add(pnThuoc);

        JLabel lblTenThuoc = new JLabel("Tên thuốc:");
        lblTenThuoc.setBounds(10, 20, 70, 25); pnThuoc.add(lblTenThuoc);
        txtTenThuoc = new JTextField(); txtTenThuoc.setBounds(80, 20, 120, 25); pnThuoc.add(txtTenThuoc);

        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setBounds(210, 20, 80, 25); pnThuoc.add(lblThanhPhan);
        txtThanhPhan = new JTextField(); txtThanhPhan.setBounds(290, 20, 100, 25); pnThuoc.add(txtThanhPhan);

        JLabel lblDonVi = new JLabel("Đơn vị tính:");
        lblDonVi.setBounds(400, 20, 70, 25); pnThuoc.add(lblDonVi);
        txtDonViTinh = new JTextField(); txtDonViTinh.setBounds(470, 20, 80, 25); pnThuoc.add(txtDonViTinh);

        JLabel lblDanhMuc = new JLabel("Danh mục:");
        lblDanhMuc.setBounds(560, 20, 70, 25); pnThuoc.add(lblDanhMuc);
        txtDanhMuc = new JTextField(); txtDanhMuc.setBounds(630, 20, 90, 25); pnThuoc.add(txtDanhMuc);

        JLabel lblXuatXu = new JLabel("Xuất xứ:");
        lblXuatXu.setBounds(10, 55, 70, 25); pnThuoc.add(lblXuatXu);
        txtXuatXu = new JTextField(); txtXuatXu.setBounds(80, 55, 120, 25); pnThuoc.add(txtXuatXu);

        JLabel lblSoLuong = new JLabel("Số lượng:");
        lblSoLuong.setBounds(210, 55, 80, 25); pnThuoc.add(lblSoLuong);
        txtSoLuong = new JTextField(); txtSoLuong.setBounds(290, 55, 60, 25); pnThuoc.add(txtSoLuong);

        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setBounds(360, 55, 60, 25); pnThuoc.add(lblGiaNhap);
        txtGiaNhap = new JTextField(); txtGiaNhap.setBounds(420, 55, 80, 25); pnThuoc.add(txtGiaNhap);

        JLabel lblDonGia = new JLabel("Đơn giá:");
        lblDonGia.setBounds(510, 55, 60, 25); pnThuoc.add(lblDonGia);
        txtDonGia = new JTextField(); txtDonGia.setBounds(570, 55, 80, 25); pnThuoc.add(txtDonGia);

        JLabel lblHanSD = new JLabel("Hạn SD:");
        lblHanSD.setBounds(660, 55, 50, 25); pnThuoc.add(lblHanSD);
        txtHanSuDung = new JTextField(); txtHanSuDung.setBounds(710, 55, 80, 25); pnThuoc.add(txtHanSuDung);

        btnThemThuoc = new JButton("Thêm thuốc");
        btnThemThuoc.setBounds(730, 20, 110, 25); pnThuoc.add(btnThemThuoc);

        // Bảng hiển thị các thuốc đã thêm tạm
        tblModelThuoc = new DefaultTableModel(new String[]{"ID", "Tên", "SL", "Giá nhập", "Đơn giá", "Hạn SD"}, 0);
        tblThuoc = new JTable(tblModelThuoc);
        JScrollPane scroll = new JScrollPane(tblThuoc);
        scroll.setBounds(10, 250, 860, 130);
        add(scroll);

        // Tổng tiền
        lblTongTien = new JLabel("Tổng tiền: 0");
        lblTongTien.setBounds(700, 390, 160, 25);
        add(lblTongTien);

        // Nút Lưu, Hủy
        btnLuu = new JButton("Lưu");
        btnLuu.setBounds(650, 440, 100, 35); add(btnLuu);
        btnHuy = new JButton("Hủy");
        btnHuy.setBounds(770, 440, 100, 35); add(btnHuy);

        // --- Xử lý dữ liệu mặc định ---
        txtIDPN.setEditable(false);
        txtThoiGian.setEditable(false);
        sinhIDPNTuDong();
        txtThoiGian.setText(DateHelper.toString(new Date(), "dd/MM/yy HH:mm"));

        // Gợi ý NCC
        NhaCungCapController nccController = new NhaCungCapController();
        dsNCC = nccController.getAllNhaCungCap();

        txtNCC.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String key = txtNCC.getText().trim().toLowerCase();
                comboNCC.removeAllItems();
                boolean found = false;
                for (NhaCungCap ncc : dsNCC) {
                    if (ncc.getTenNCC().toLowerCase().contains(key)) {
                        comboNCC.addItem(ncc.getTenNCC() + " - " + ncc.getIdNCC());
                        found = true;
                    }
                }
                btnThemNCC.setVisible(!found && key.length() > 0);
                comboNCC.setPopupVisible(found);
            }
        });

        btnThemNCC.addActionListener(e -> showThemNCCDialog());

        // Thêm thuốc
        btnThemThuoc.addActionListener(e -> themThuocVaoTam());

        // Nút Lưu
        btnLuu.addActionListener(e -> luuPhieuNhap());

        // Nút Hủy
        btnHuy.addActionListener(e -> {
            if (MessageDialog.showConfirm(this, "Bạn có chắc muốn hủy?", "Xác nhận")) {
                dispose();
            }
        });
    }

    // Sinh IDPN tự động
    private void sinhIDPNTuDong() {
        // Giả sử đã có controller để lấy mã cuối
        PhieuNhapController pnController = new PhieuNhapController();
        List<PhieuNhap> ds = pnController.getAllPhieuNhap();
        String lastID = "PN000";
        if (!ds.isEmpty()) {
            lastID = ds.get(ds.size() - 1).getIdPN();
        }
        int num = Integer.parseInt(lastID.replaceAll("\\D+", ""));
        txtIDPN.setText(String.format("PN%03d", num + 1));
    }

    // Sinh ID thuốc tự động
    private String sinhIDThuocTuDong() {
        ThuocController thuocController = new ThuocController();
        List<Thuoc> ds = thuocController.getAllThuoc();
        String lastID = "T000";
        if (!ds.isEmpty()) {
            lastID = ds.get(ds.size() - 1).getIdThuoc();
        }
        int num = Integer.parseInt(lastID.replaceAll("\\D+", ""));
        return String.format("T%03d", num + listThuocTam.size() + 1);
    }

    // Dialog thêm NCC mới
    private void showThemNCCDialog() {
        JTextField id = new JTextField();
        JTextField ten = new JTextField(txtNCC.getText());
        JTextField diachi = new JTextField();
        JTextField sdt = new JTextField();
        Object[] fields = {
                "IDNCC:", id,
                "Tên NCC:", ten,
                "Địa chỉ:", diachi,
                "SĐT:", sdt
        };
        int ok = JOptionPane.showConfirmDialog(this, fields, "Thêm nhà cung cấp", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            NhaCungCap ncc = new NhaCungCap();
            ncc.setIdNCC(id.getText().trim());
            ncc.setTenNCC(ten.getText().trim());
            ncc.setDiaChi(diachi.getText().trim());
            ncc.setSdt(sdt.getText().trim());
            if (!ncc.getIdNCC().isEmpty() && !ncc.getTenNCC().isEmpty()) {
                NhaCungCapController nccController = new NhaCungCapController();
                if (nccController.addNhaCungCap(ncc)) {
                    dsNCC.add(ncc);
                    comboNCC.addItem(ncc.getTenNCC() + " - " + ncc.getIdNCC());
                    comboNCC.setSelectedItem(ncc.getTenNCC() + " - " + ncc.getIdNCC());
                    MessageDialog.showInfo(this, "Đã thêm nhà cung cấp mới!", "Thành công");
                } else {
                    MessageDialog.showError(this, "Lỗi khi thêm NCC!", "Lỗi");
                }
            }
        }
    }

    // Thêm thuốc vào danh sách tạm
    private void themThuocVaoTam() {
        String ten = txtTenThuoc.getText().trim();
        String tp = txtThanhPhan.getText().trim();
        String dvt = txtDonViTinh.getText().trim();
        String dm = txtDanhMuc.getText().trim();
        String xx = txtXuatXu.getText().trim();
        String sl = txtSoLuong.getText().trim();
        String giaNhap = txtGiaNhap.getText().trim();
        String donGia = txtDonGia.getText().trim();
        String hanSD = txtHanSuDung.getText().trim();
        if (ten.isEmpty() || dvt.isEmpty() || dm.isEmpty() || sl.isEmpty() || giaNhap.isEmpty() || donGia.isEmpty() || hanSD.isEmpty()) {
            MessageDialog.showWarning(this, "Vui lòng nhập đầy đủ thông tin thuốc!", "Cảnh báo");
            return;
        }
        Date hanSuDungDate = DateHelper.toDate(hanSD, "dd/MM/yyyy"); // Bạn dùng format nào thì điền đúng
        if (hanSuDungDate == null) {
            MessageDialog.showWarning(this, "Hạn sử dụng phải đúng định dạng dd/MM/yyyy!", "Cảnh báo");
            return;
        }
        String idThuoc = sinhIDThuocTuDong();
        Thuoc t = new Thuoc();
        t.setIdThuoc(idThuoc);
        t.setTenThuoc(ten);
        t.setThanhPhan(tp);
        t.setDonViTinh(dvt);
        t.setDanhMuc(dm);
        t.setXuatXu(xx);
        t.setSoLuongTon(Integer.parseInt(sl));
        t.setGiaNhap(Double.parseDouble(giaNhap));
        t.setDonGia(Double.parseDouble(donGia));
        t.setHanSuDung(hanSuDungDate);

        listThuocTam.add(t);

        // Chi tiết phiếu nhập tạm
        ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
        ct.setIdPN(txtIDPN.getText());
        ct.setIdThuoc(idThuoc);
        ct.setSoLuong(Integer.parseInt(sl));
        ct.setGiaNhap(Double.parseDouble(giaNhap)); 
        listChiTietTam.add(ct);

        // Hiển thị ra bảng
        tblModelThuoc.addRow(new Object[]{
        	    idThuoc, ten, sl, giaNhap, donGia, DateHelper.toString(hanSuDungDate, "dd/MM/yyyy")
        	});


        tongTien += Double.parseDouble(giaNhap) * Integer.parseInt(sl);
        lblTongTien.setText("Tổng tiền: " + tongTien);

        // reset ô nhập thuốc
        txtTenThuoc.setText(""); txtThanhPhan.setText(""); txtDonViTinh.setText(""); txtDanhMuc.setText(""); txtXuatXu.setText("");
        txtSoLuong.setText(""); txtGiaNhap.setText(""); txtDonGia.setText(""); txtHanSuDung.setText("");
    }

    // Lưu phiếu nhập và thuốc vào DB
    private void luuPhieuNhap() {
        if (txtIDNV.getText().trim().isEmpty()) {
            MessageDialog.showWarning(this, "Vui lòng nhập IDNV!", "Cảnh báo");
            return;
        }
        if (comboNCC.getSelectedItem() == null) {
            MessageDialog.showWarning(this, "Vui lòng chọn nhà cung cấp!", "Cảnh báo");
            return;
        }
        if (listThuocTam.isEmpty()) {
            MessageDialog.showWarning(this, "Chưa có thuốc nào được nhập!", "Cảnh báo");
            return;
        }
        String idNCC = comboNCC.getSelectedItem().toString();
        idNCC = idNCC.substring(idNCC.lastIndexOf('-') + 1).trim();

        // Lưu thuốc mới vào DB
        ThuocController thuocController = new ThuocController();
        for (Thuoc t : listThuocTam) {
            if (!thuocController.addThuoc(t)) {
                MessageDialog.showError(this, "Lưu thuốc " + t.getTenThuoc() + " thất bại!", "Lỗi");
                return;
            }
        }

        // Lưu phiếu nhập
        PhieuNhap pn = new PhieuNhap();
        pn.setIdPN(txtIDPN.getText());
        pn.setThoiGian(DateHelper.toDateTime(txtThoiGian.getText(), "dd/MM/yy HH:mm"));
        pn.setIdNV(txtIDNV.getText().trim());
        pn.setIdNCC(idNCC);
        pn.setTongTien(tongTien);

        PhieuNhapController pnController = new PhieuNhapController();
        if (!pnController.addPhieuNhap(pn)) {
            MessageDialog.showError(this, "Lưu phiếu nhập thất bại!", "Lỗi");
            return;
        }

        // Lưu chi tiết phiếu nhập
        for (ChiTietPhieuNhap ct : listChiTietTam) {
            // Thực hiện lưu vào DB qua DAO (bạn cần tạo thêm DAO/Controller cho ChiTietPhieuNhap)
            // ví dụ: chiTietPhieuNhapController.addChiTietPhieuNhap(ct);
        }
        ChiTietPhieuNhapController ctController = new ChiTietPhieuNhapController();
        for (ChiTietPhieuNhap ct : listChiTietTam) {
            if (!ctController.addChiTietPhieuNhap(ct)) {
                MessageDialog.showError(this, "Lưu chi tiết phiếu nhập thất bại!", "Lỗi");
                return;
            }
        }

        MessageDialog.showInfo(this, "Lưu phiếu nhập thành công!", "Thành công");
        dispose();
    }
}
