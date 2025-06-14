package gui;

import controller.NhaCungCapController;
import controller.ThuocController;
import controller.PhieuNhapController;
import controller.ChiTietPhieuNhapController;
import dao.ChiTietPhieuNhapDAO;
import entities.PhieuNhap;
import entities.NhaCungCap;
import entities.Thuoc;
import entities.ChiTietPhieuNhap;
import utils.MessageDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class EditPhieuNhapDialog extends JDialog {
    private JTextField txtIdPN, txtThoiGian, txtIdNV, txtNCC;
    private JButton btnSave, btnCancel;
    private JComboBox<String> cbNCC;
    private DefaultComboBoxModel<String> modelNCC;
    private JPanel nccDetailPanel;
    private JTextField txtNewIdNCC, txtNewTenNCC, txtNewDiaChi, txtNewSdt;
    private JButton btnAddNCC;
 // Thành viên cho panel nhập thuốc
    private JTextField txtTenThuoc, txtThanhPhan, txtDonViTinh, txtDanhMuc, txtXuatXu;
    private JTextField txtSoLuong, txtGiaNhap, txtDonGia, txtHanSuDung;
    private JButton btnThemThuoc;
    private JPanel panelNhapThuoc;
    private JButton btnXoaThuoc;

//tong tien    
    private JLabel lblTongTien;
    private Map<String, Thuoc> mapThuocMoi = new HashMap<>();


    private JTable tblThuoc;
    private DefaultTableModel modelThuoc;

    private PhieuNhapController phieuNhapController = new PhieuNhapController();
    private NhaCungCapController nccController = new NhaCungCapController();
    private ThuocController thuocController = new ThuocController();
    private ChiTietPhieuNhapDAO chiTietPhieuNhapDAO = new ChiTietPhieuNhapDAO();

    private PhieuNhap phieuNhap;
    private List<ChiTietPhieuNhap> listCTPN;
    private List<NhaCungCap> allNCCList;
    private List<Thuoc> allThuocList;

    private boolean nccPanelVisible = false;
  
    public EditPhieuNhapDialog(Window parent, PhieuNhap pn) {
        super(parent, "Sửa phiếu nhập", ModalityType.APPLICATION_MODAL);
        this.phieuNhap = pn;
        setPreferredSize(new Dimension(900, 650));
        setSize(900, 650);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout());

        allNCCList = nccController.getAllNhaCungCap();
        allThuocList = thuocController.getAllThuoc();
        listCTPN = chiTietPhieuNhapDAO.getByIdPN(pn.getIdPN());

        // ==== Panel thông tin phiếu nhập ==== //
        JPanel pnlInfo = new JPanel(null);
        pnlInfo.setPreferredSize(new Dimension(900, 210));

        JLabel lblIdPN = new JLabel("Mã phiếu nhập:");
        lblIdPN.setBounds(20, 20, 120, 25);
        pnlInfo.add(lblIdPN);
        initPanelNhapThuoc();

        txtIdPN = new JTextField(pn.getIdPN());
        txtIdPN.setEditable(false);
        txtIdPN.setBounds(140, 20, 150, 25);
        pnlInfo.add(txtIdPN);

        JLabel lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setBounds(340, 20, 80, 25);
        pnlInfo.add(lblThoiGian);

        txtThoiGian = new JTextField(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(pn.getThoiGian()));
        txtThoiGian.setBounds(420, 20, 180, 25);
        pnlInfo.add(txtThoiGian);

        JLabel lblIdNV = new JLabel("ID Nhân viên:");
        lblIdNV.setBounds(20, 60, 120, 25);
        pnlInfo.add(lblIdNV);

        txtIdNV = new JTextField(pn.getIdNV());
        txtIdNV.setBounds(140, 60, 150, 25);
        pnlInfo.add(txtIdNV);

        JLabel lblNCC = new JLabel("Nhà cung cấp:");
        lblNCC.setBounds(340, 60, 100, 25);
        pnlInfo.add(lblNCC);

        modelNCC = new DefaultComboBoxModel<>();
        for (NhaCungCap ncc : allNCCList) {
            modelNCC.addElement(ncc.getTenNCC());
        }
        cbNCC = new JComboBox<>(modelNCC);
        cbNCC.setEditable(true);
        cbNCC.setBounds(440, 60, 200, 25);
        cbNCC.setSelectedItem(getTenNCCById(pn.getIdNCC()));

        txtNCC = (JTextField) cbNCC.getEditor().getEditorComponent();

        // Gợi ý khi gõ tên NCC
        txtNCC.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = txtNCC.getText().trim();
                modelNCC.removeAllElements();
                boolean found = false;
                for (NhaCungCap ncc : allNCCList) {
                    if (ncc.getTenNCC().toLowerCase().contains(input.toLowerCase())) {
                        modelNCC.addElement(ncc.getTenNCC());
                        found = true;
                    }
                }
                if (!found && !input.isEmpty()) {
                    modelNCC.addElement("Thêm nhà cung cấp");
                }
                cbNCC.setSelectedItem(input);
                cbNCC.showPopup();
            }
        });

        cbNCC.addActionListener(e -> {
            String selected = (String) cbNCC.getSelectedItem();
            if ("Thêm nhà cung cấp".equals(selected)) {
                showNCCDetailPanel(true);
            } else {
                showNCCDetailPanel(false);
            }
        });

        pnlInfo.add(cbNCC);

        // Panel nhập NCC mới (ẩn theo mặc định)
        nccDetailPanel = new JPanel(null);
        nccDetailPanel.setBorder(BorderFactory.createTitledBorder("Nhập thông tin NCC mới"));
        nccDetailPanel.setBounds(20, 100, 850, 55);

        JLabel lblIdNCC = new JLabel("IDNCC:");
        lblIdNCC.setBounds(10, 18, 60, 25);
        nccDetailPanel.add(lblIdNCC);
        txtNewIdNCC = new JTextField();
        txtNewIdNCC.setBounds(62, 18, 80, 25);
        nccDetailPanel.add(txtNewIdNCC);

        JLabel lblTenNCC = new JLabel("Tên NCC:");
        lblTenNCC.setBounds(150, 18, 70, 25);
        nccDetailPanel.add(lblTenNCC);
        txtNewTenNCC = new JTextField();
        txtNewTenNCC.setBounds(210, 18, 150, 25);
        nccDetailPanel.add(txtNewTenNCC);

        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(370, 18, 60, 25);
        nccDetailPanel.add(lblDiaChi);
        txtNewDiaChi = new JTextField();
        txtNewDiaChi.setBounds(420, 18, 140, 25);
        nccDetailPanel.add(txtNewDiaChi);

        JLabel lblSdt = new JLabel("SĐT:");
        lblSdt.setBounds(568, 18, 35, 25);
        nccDetailPanel.add(lblSdt);
        txtNewSdt = new JTextField();
        txtNewSdt.setBounds(596, 18, 110, 25);
        nccDetailPanel.add(txtNewSdt);

        btnAddNCC = new JButton("  Lưu NCC");
        btnAddNCC.setIcon(new ImageIcon(EditPhieuNhapDialog.class.getResource("/icon/chungSave.png")));
        btnAddNCC.setBounds(730, 16, 110, 30);
        nccDetailPanel.add(btnAddNCC);
        nccDetailPanel.setVisible(false);
        pnlInfo.add(nccDetailPanel);

        btnAddNCC.addActionListener(e -> {
            if (txtNewIdNCC.getText().trim().isEmpty() || txtNewTenNCC.getText().trim().isEmpty()) {
                MessageDialog.showWarning(this, "IDNCC và Tên NCC không được để trống!", "Cảnh báo");
                return;
            }
            NhaCungCap ncc = new NhaCungCap();
            ncc.setIdNCC(txtNewIdNCC.getText().trim());
            ncc.setTenNCC(txtNewTenNCC.getText().trim());
            ncc.setDiaChi(txtNewDiaChi.getText().trim());
            ncc.setSdt(txtNewSdt.getText().trim());
            boolean ok = nccController.addNhaCungCap(ncc);
            if (ok) {
                allNCCList = nccController.getAllNhaCungCap();
                modelNCC.addElement(ncc.getTenNCC());
                cbNCC.setSelectedItem(ncc.getTenNCC());
                showNCCDetailPanel(false);
                MessageDialog.showInfo(this, "Đã thêm nhà cung cấp mới!", "Thành công");
            } else {
                MessageDialog.showError(this, "Thêm NCC thất bại!", "Lỗi");
            }
        });

        getContentPane().add(pnlInfo, BorderLayout.NORTH);

        // ==== Bảng thuốc ==== //
        modelThuoc = new DefaultTableModel(new Object[]{"ID", "Tên", "SL", "Giá nhập", "Đơn giá", "Hạn SD"}, 0) {
        	    @Override
        	    public boolean isCellEditable(int row, int col) {
        	        // Tùy chọn cho phép sửa cột nào (VD: chỉ số lượng và giá nhập được sửa)
        	        return col == 2 || col == 3;
        	    }
        	};

        tblThuoc = new JTable(modelThuoc);
        //modelThuoc.addTableModelListener(e -> updateTongTien());
        
        //xoa thuoc
        btnXoaThuoc = new JButton("XÓA THUỐC ĐÃ CHỌN");
        btnXoaThuoc.setForeground(Color.RED);
        btnXoaThuoc.setFont(btnXoaThuoc.getFont().deriveFont(Font.BOLD, 16f));
        btnXoaThuoc.addActionListener(e -> {
            int selectedRow = tblThuoc.getSelectedRow();
            if (selectedRow != -1) {
                modelThuoc.removeRow(selectedRow);
                updateTongTien();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn phải chọn một dòng thuốc để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });


        // Fill data
        for (ChiTietPhieuNhap ct : listCTPN) {
            Thuoc t = getThuocById(ct.getIdThuoc());
            modelThuoc.addRow(new Object[]{
                ct.getIdThuoc(),
                ct.getTenThuoc(),
                ct.getSoLuong(),
                ct.getGiaNhap(),
                t != null ? t.getDonGia() : 0.0,
                t != null && t.getHanSuDung() != null
                    ? new SimpleDateFormat("dd/MM/yyyy").format(t.getHanSuDung())
                    : ""
            });
        }
        
        //xoa thuoc
        JButton btnXoaThuoc = new JButton("  Xóa thuốc");
        btnXoaThuoc.setIcon(new ImageIcon(EditPhieuNhapDialog.class.getResource("/icon/chungDelete.png")));
        btnXoaThuoc.setForeground(Color.BLACK);
        btnXoaThuoc.setFont(btnXoaThuoc.getFont().deriveFont(8f));

        btnXoaThuoc.addActionListener(e -> {
            int selectedRow = tblThuoc.getSelectedRow();
            if (selectedRow != -1) {
                modelThuoc.removeRow(selectedRow);
                updateTongTien();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn phải chọn một dòng thuốc để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setPreferredSize(new Dimension(850, 250));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
//        JPanel pnlXoaThuoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        pnlXoaThuoc.add(btnXoaThuoc);
//        pnlXoaThuoc.setBounds(20, 340, 300, 40); // Tùy chỉnh vị trí/size cho phù hợp
//        getContentPane().add(pnlXoaThuoc);
        btnXoaThuoc.setBounds(726, 1, 136, 25); // width 140 đủ cho chữ lớn, chỉnh lại nếu cần nhỏ hơn
        panelNhapThuoc.add(btnXoaThuoc);

        
     // Tạo label tổng tiền
        lblTongTien = new JLabel("Tổng tiền: 0");
        lblTongTien.setFont(new Font("Tahoma", Font.PLAIN, 15));

        // Panel tổng tiền căn trái
        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTongTien.add(lblTongTien);

        // Panel nút căn phải
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnSave.setIcon(new ImageIcon(EditPhieuNhapDialog.class.getResource("/icon/chungSave.png")));
        btnCancel = new JButton("  Hủy");
        btnCancel.setIcon(new ImageIcon(EditPhieuNhapDialog.class.getResource("/icon/chungCancel.png")));
        pnlBtns.add(btnSave);
        pnlBtns.add(btnCancel);

        // Panel dưới cùng chứa cả 2 panel nhỏ
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.add(pnlTongTien, BorderLayout.WEST);
        pnlFooter.add(pnlBtns, BorderLayout.EAST);

        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());


        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());
    }

    private void showNCCDetailPanel(boolean visible) {
        nccDetailPanel.setVisible(visible);
        nccPanelVisible = visible;
    }

    private Thuoc getThuocById(String id) {
        for (Thuoc t : allThuocList) {
            if (t.getIdThuoc().equals(id)) return t;
        }
        return null;
    }

    private String getTenNCCById(String idNCC) {
        for (NhaCungCap ncc : allNCCList) {
            if (ncc.getIdNCC().equals(idNCC)) return ncc.getTenNCC();
        }
        return "";
    }

    private String getIdNCCByTen(String tenNCC) {
        for (NhaCungCap ncc : allNCCList) {
            if (ncc.getTenNCC().equalsIgnoreCase(tenNCC)) return ncc.getIdNCC();
        }
        return null;
    }

    private String getIdThuocByTen(String ten) {
        for (Thuoc t : allThuocList) {
            if (t.getTenThuoc().equalsIgnoreCase(ten)) return t.getIdThuoc();
        }
        return null;
    }
    private void initPanelNhapThuoc() {
        panelNhapThuoc = new JPanel(null);
        panelNhapThuoc.setBounds(10, 340, 870, 90); // đặt vị trí bên dưới bảng, chỉnh cho phù hợp

        JLabel lblInfo = new JLabel("Thông tin thuốc");
        lblInfo.setBounds(10, 0, 100, 25);
        panelNhapThuoc.add(lblInfo);

        JLabel lblTenThuoc = new JLabel("Tên thuốc:");
        lblTenThuoc.setBounds(10, 25, 60, 25);
        panelNhapThuoc.add(lblTenThuoc);
        txtTenThuoc = new JTextField();
        txtTenThuoc.setBounds(70, 25, 110, 25);
        panelNhapThuoc.add(txtTenThuoc);

        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setBounds(190, 25, 70, 25);
        panelNhapThuoc.add(lblThanhPhan);
        txtThanhPhan = new JTextField();
        txtThanhPhan.setBounds(260, 25, 80, 25);
        panelNhapThuoc.add(txtThanhPhan);

        JLabel lblDonViTinh = new JLabel("Đơn vị tính:");
        lblDonViTinh.setBounds(350, 25, 65, 25);
        panelNhapThuoc.add(lblDonViTinh);
        txtDonViTinh = new JTextField();
        txtDonViTinh.setBounds(415, 25, 60, 25);
        panelNhapThuoc.add(txtDonViTinh);

        JLabel lblDanhMuc = new JLabel("Danh mục:");
        lblDanhMuc.setBounds(480, 25, 65, 25);
        panelNhapThuoc.add(lblDanhMuc);
        txtDanhMuc = new JTextField();
        txtDanhMuc.setBounds(545, 25, 70, 25);
        panelNhapThuoc.add(txtDanhMuc);

        btnThemThuoc = new JButton("Thêm thuốc");
        btnThemThuoc.setBounds(726, 25, 136, 25);
        panelNhapThuoc.add(btnThemThuoc);

        JLabel lblXuatXu = new JLabel("Xuất xứ:");
        lblXuatXu.setBounds(10, 55, 50, 25);
        panelNhapThuoc.add(lblXuatXu);
        txtXuatXu = new JTextField();
        txtXuatXu.setBounds(70, 55, 80, 25);
        panelNhapThuoc.add(txtXuatXu);

        JLabel lblSoLuong = new JLabel("Số lượng:");
        lblSoLuong.setBounds(190, 55, 60, 25);
        panelNhapThuoc.add(lblSoLuong);
        txtSoLuong = new JTextField();
        txtSoLuong.setBounds(260, 55, 60, 25);
        panelNhapThuoc.add(txtSoLuong);

        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setBounds(338, 55, 60, 25);
        panelNhapThuoc.add(lblGiaNhap);
        txtGiaNhap = new JTextField();
        txtGiaNhap.setBounds(395, 55, 80, 25);
        panelNhapThuoc.add(txtGiaNhap);

        JLabel lblDonGia = new JLabel("Đơn giá:");
        lblDonGia.setBounds(490, 55, 50, 25);
        panelNhapThuoc.add(lblDonGia);
        txtDonGia = new JTextField();
        txtDonGia.setBounds(545, 55, 70, 25);
        panelNhapThuoc.add(txtDonGia);

        JLabel lblHanSD = new JLabel("Hạn SD:");
        lblHanSD.setBounds(624, 55, 50, 25);
        panelNhapThuoc.add(lblHanSD);
        txtHanSuDung = new JTextField();
        txtHanSuDung.setBounds(682, 55, 90, 25);
        panelNhapThuoc.add(txtHanSuDung);

        // Sự kiện thêm thuốc
        btnThemThuoc.addActionListener(e -> themThuocVaoBang());

        // Thêm panel vào dialog
        getContentPane().add(panelNhapThuoc);
    }
//    private String sinhIDThuocTuDong() {
//        int max = 0;
//        for (int i = 0; i < modelThuoc.getRowCount(); i++) {
//            String id = (String) modelThuoc.getValueAt(i, 0);
//            if (id != null && id.startsWith("T")) {
//                try {
//                    int num = Integer.parseInt(id.substring(1));
//                    if (num > max) max = num;
//                } catch (Exception ignored) {}
//            }
//        }
//        return String.format("T%03d", max + 1);
//    }
    private String sinhIDThuocTuDong() {
        // Bước 1: Lấy danh sách tất cả thuốc từ Controller/DAO
        ThuocController thuocController = new ThuocController();
        List<Thuoc> ds = thuocController.getAllThuoc();

        // Bước 2: Duyệt để tìm số lớn nhất trong mã thuốc hiện tại
        int max = 0;
        for (Thuoc t : ds) {
            String id = t.getIdThuoc();
            if (id != null && id.startsWith("T")) {
                try {
                    int num = Integer.parseInt(id.substring(1)); // Bỏ chữ T
                    if (num > max) max = num;
                } catch (Exception ex) {
                    // Bỏ qua nếu mã không hợp lệ
                }
            }
        }

        // Bước 3: Tạo mã mới
        return String.format("T%03d", max + 1); // Ví dụ: T001, T025, ...
    }
//them tu day 
    private void themThuocVaoBang() {
        String tenThuoc = txtTenThuoc.getText().trim();
        String thanhPhan = txtThanhPhan.getText().trim();
        String donViTinh = txtDonViTinh.getText().trim();
        String danhMuc = txtDanhMuc.getText().trim();
        String xuatXu = txtXuatXu.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();
        String giaNhapStr = txtGiaNhap.getText().trim();
        String donGiaStr = txtDonGia.getText().trim();
        String hanSD = txtHanSuDung.getText().trim();
        String idThuoc = sinhIDThuocTuDong();

        // Validate như cũ
        if (tenThuoc.isEmpty() || soLuongStr.isEmpty() || giaNhapStr.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phải nhập đủ tên thuốc, số lượng, giá nhập, đơn giá bán!");
            return;
        }

        int soLuong = 0;
        double giaNhap = 0, donGia = 0;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            giaNhap = Double.parseDouble(giaNhapStr);
            donGia = Double.parseDouble(donGiaStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Số lượng, giá nhập, đơn giá phải là số!");
            return;
        }

        // Thêm vào model bảng (chỉ các trường hiển thị, như cũ)
        modelThuoc.addRow(new Object[]{ idThuoc, tenThuoc, soLuong, giaNhap, donGia, hanSD });
        updateTongTien();

        // *** Thêm vào map phụ ***
        Thuoc thuocMoi = new Thuoc();
        thuocMoi.setIdThuoc(idThuoc);
        thuocMoi.setTenThuoc(tenThuoc);
        thuocMoi.setThanhPhan(thanhPhan);
        thuocMoi.setDonViTinh(donViTinh);
        thuocMoi.setDanhMuc(danhMuc);
        thuocMoi.setXuatXu(xuatXu);
        thuocMoi.setSoLuongTon(soLuong);
        thuocMoi.setGiaNhap(giaNhap);
        thuocMoi.setDonGia(donGia);
        try {
            Date hanSuDung = null;
            if (!hanSD.trim().isEmpty()) {
                hanSuDung = new SimpleDateFormat("dd/MM/yyyy").parse(hanSD);
            }
            thuocMoi.setHanSuDung(hanSuDung);
        } catch (Exception ex) {
            thuocMoi.setHanSuDung(null);
        }
        thuocMoi.setIsDeleted(false);
        mapThuocMoi.put(idThuoc, thuocMoi);

        // Xóa trắng các ô nhập
        txtTenThuoc.setText("");
        txtThanhPhan.setText("");
        txtDonViTinh.setText("");
        txtDanhMuc.setText("");
        txtXuatXu.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtDonGia.setText("");
        txtHanSuDung.setText("");
    }

 private void updateTongTien() {
        double tongTien = 0.0;
        for (int i = 0; i < modelThuoc.getRowCount(); i++) {
            try {
                int soLuong = Integer.parseInt(modelThuoc.getValueAt(i, 2).toString());
                double giaNhap = Double.parseDouble(modelThuoc.getValueAt(i, 3).toString());
                tongTien += soLuong * giaNhap;
            } catch (Exception ex) {
                // Bỏ qua nếu lỗi convert
            }
        }
        lblTongTien.setText("Tổng tiền: " + String.format("%.0f", tongTien));
    }
//them tiep
 private void onSave() {
	    try {
	        // Lấy danh sách ID thuốc hiện tại trên giao diện (sau khi thêm/xóa/sửa)
	        Set<String> idThuocMoi = new HashSet<>();
	        for (int i = 0; i < modelThuoc.getRowCount(); i++) {
	            idThuocMoi.add(modelThuoc.getValueAt(i, 0).toString());
	        }

	        // Lấy danh sách ID thuốc cũ của phiếu nhập (lúc đầu mở dialog)
	        Set<String> idThuocCu = new HashSet<>();
	        for (ChiTietPhieuNhap ct : listCTPN) {
	            idThuocCu.add(ct.getIdThuoc());
	        }

	        // Xác định các thuốc bị xóa và các thuốc mới thêm
	        Set<String> idThuocBiXoa = new HashSet<>(idThuocCu);
	        idThuocBiXoa.removeAll(idThuocMoi);

	        Set<String> idThuocMoiThem = new HashSet<>(idThuocMoi);
	        idThuocMoiThem.removeAll(idThuocCu);

	        // Validate
	        String idPN = txtIdPN.getText().trim();
	        String thoiGianStr = txtThoiGian.getText().trim();
	        String idNV = txtIdNV.getText().trim();
	        String tenNCC = ((String) cbNCC.getSelectedItem()).trim();
	        if (idPN.isEmpty() || thoiGianStr.isEmpty() || idNV.isEmpty() || tenNCC.isEmpty()) {
	            MessageDialog.showWarning(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo");
	            return;
	        }
	        String idNCC = getIdNCCByTen(tenNCC);
	        if (idNCC == null) {
	            MessageDialog.showWarning(this, "Vui lòng thêm nhà cung cấp mới!", "Cảnh báo");
	            return;
	        }
	        // XỬ LÝ THUỐC BỊ XÓA
	        for (String idThuoc : idThuocBiXoa) {
	            // 1. Set isDeleted=1 trong bảng Thuoc
	            Thuoc t = getThuocById(idThuoc);
	            if (t != null) {
	                t.setIsDeleted(true);
	                thuocController.updateThuoc(t); // Đảm bảo hàm này update isDeleted
	            }
	            // 2. Xóa dòng khỏi ChiTietPhieuNhap
	            ChiTietPhieuNhapController ctController = new ChiTietPhieuNhapController();
	            ctController.deleteByPhieuNhapAndThuoc(idPN, idThuoc);
	        }

	        // Update phiếu nhập
	        PhieuNhap pnUpdate = new PhieuNhap();
	        pnUpdate.setIdPN(idPN);
	        pnUpdate.setThoiGian(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(thoiGianStr));
	        pnUpdate.setIdNV(idNV);
	        pnUpdate.setIdNCC(idNCC);

	        // Tính tổng tiền mới từ bảng thuốc
	        double tongTien = 0.0;
	        for (int i = 0; i < modelThuoc.getRowCount(); i++) {
	            int soLuong = Integer.parseInt(modelThuoc.getValueAt(i, 2).toString());
	            double giaNhap = Double.parseDouble(modelThuoc.getValueAt(i, 3).toString());
	            tongTien += soLuong * giaNhap;
	        }
	        pnUpdate.setTongTien(tongTien);

	        // Cập nhật phiếu nhập
	        boolean okPN = phieuNhapController.updatePhieuNhap(pnUpdate);
	        if (!okPN) {
	            MessageDialog.showError(this, "Cập nhật phiếu nhập thất bại!", "Lỗi");
	            return;
	        }

	        // Xóa chi tiết cũ, thêm chi tiết mới (đơn giản nhất)
	        ChiTietPhieuNhapController ctController = new ChiTietPhieuNhapController();
	        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
	        dao.deleteByPhieuNhap(idPN);

	        // Lưu toàn bộ các thuốc trên bảng vào ChiTietPhieuNhap & Thuoc
	        for (int i = 0; i < modelThuoc.getRowCount(); i++) {
	            String idThuoc = modelThuoc.getValueAt(i, 0).toString();
	            String tenThuoc = modelThuoc.getValueAt(i, 1).toString();
	            int soLuong = Integer.parseInt(modelThuoc.getValueAt(i, 2).toString());
	            double giaNhap = Double.parseDouble(modelThuoc.getValueAt(i, 3).toString());
	            double donGia = Double.parseDouble(modelThuoc.getValueAt(i, 4).toString());
	            String hanSDStr = modelThuoc.getValueAt(i, 5).toString();

	            // Nếu là thuốc mới (idThuoc thuộc idThuocMoiThem), lấy đầy đủ info từ map phụ
	            if (idThuocMoiThem.contains(idThuoc)) {
	                Thuoc thuocMoi = mapThuocMoi.get(idThuoc); // lấy từ map phụ đã lưu khi nhập thuốc
	                if (thuocMoi != null) {
	                    thuocController.addThuoc(thuocMoi);
	                } else {
	                    // fallback: vẫn lưu với info ít, không đủ field (không nên xảy ra)
	                    Thuoc thuocMoi2 = new Thuoc();
	                    thuocMoi2.setIdThuoc(idThuoc);
	                    thuocMoi2.setTenThuoc(tenThuoc);
	                    thuocMoi2.setSoLuongTon(soLuong);
	                    thuocMoi2.setGiaNhap(giaNhap);
	                    thuocMoi2.setDonGia(donGia);
	                    try {
	                        Date hanSuDung = null;
	                        if (!hanSDStr.trim().isEmpty()) {
	                            hanSuDung = new SimpleDateFormat("dd/MM/yyyy").parse(hanSDStr);
	                        }
	                        thuocMoi2.setHanSuDung(hanSuDung);
	                    } catch (Exception ex) {
	                        thuocMoi2.setHanSuDung(null);
	                    }
	                    thuocMoi2.setIsDeleted(false);
	                    thuocController.addThuoc(thuocMoi2);
	                }
	            }

	            // Lưu chi tiết phiếu nhập
	            ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
	            ct.setIdPN(idPN);
	            ct.setIdThuoc(idThuoc);
	            ct.setTenThuoc(tenThuoc);
	            ct.setSoLuong(soLuong);
	            ct.setGiaNhap(giaNhap);
	            ctController.addChiTietPhieuNhap(ct);
	        }

	        MessageDialog.showInfo(this, "Đã cập nhật phiếu nhập!", "Thành công");
	        dispose();

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        MessageDialog.showError(this, "Có lỗi khi cập nhật phiếu nhập: " + ex.getMessage(), "Lỗi");
	    }
	}

}
