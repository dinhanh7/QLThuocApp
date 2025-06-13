package gui;

import controller.PhanHoiController;
import dao.KhachHangDAO;
import dao.ChiTietHoaDonDAO;
import entities.KhachHang;
import entities.PhanHoi;
import utils.MessageDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GuestFeedbackForm.java
 *
 * Cho phép khách (chưa đăng nhập) nhập:
 *  - Số điện thoại (tự tìm idKH)
 *  - IDHD (tự tìm idThuoc đầu tiên trong ChiTietHoaDon)
 *  - Nội dung phản hồi
 *  - Đánh giá
 *
 * Khi bấm "Gửi", ghi vào bảng PhanHoi:
 *  - idPH tự sinh ("PH" + timestamp)
 *  - idKH tìm theo sdt
 *  - idHD do user nhập
 *  - idThuoc do lookup ChiTietHoaDon
 *  - noiDung, danhGia do user nhập
 *  - thoiGian = now
 *
 */
public class GuestFeedbackForm extends JFrame {

    private JPanel contentPane;
    private JTextField txtSDT, txtIdHD, txtNoiDung, txtDanhGia;
    private JButton btnSubmit, btnCancel;

    private PhanHoiController phanHoiController;

    public GuestFeedbackForm() {
        setTitle("Chế độ khách - Gửi phản hồi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        initComponents();
        phanHoiController = new PhanHoiController();
    }

    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Gửi phản hồi (Chế độ khách)");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(100, 10, 250, 30);
        contentPane.add(lblTitle);

        // Số điện thoại
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setBounds(20, 60, 100, 25);
        contentPane.add(lblSDT);

        txtSDT = new JTextField();
        txtSDT.setBounds(130, 60, 200, 25);
        contentPane.add(txtSDT);

        // IDHD
        JLabel lblIdHD = new JLabel("ID Hóa đơn:");
        lblIdHD.setBounds(20, 100, 100, 25);
        contentPane.add(lblIdHD);

        txtIdHD = new JTextField();
        txtIdHD.setBounds(130, 100, 200, 25);
        contentPane.add(txtIdHD);

        // Nội dung phản hồi
        JLabel lblNoiDung = new JLabel("Nội dung:");
        lblNoiDung.setBounds(20, 140, 100, 25);
        contentPane.add(lblNoiDung);

        txtNoiDung = new JTextField();
        txtNoiDung.setBounds(130, 140, 260, 25);
        contentPane.add(txtNoiDung);

        // Đánh giá
        JLabel lblDanhGia = new JLabel("Đánh giá (1-5):");
        lblDanhGia.setBounds(20, 180, 100, 25);
        contentPane.add(lblDanhGia);

        txtDanhGia = new JTextField();
        txtDanhGia.setBounds(130, 180, 50, 25);
        contentPane.add(txtDanhGia);

        // Nút Gửi
        btnSubmit = new JButton("Gửi");
        btnSubmit.setBounds(100, 230, 80, 30);
        contentPane.add(btnSubmit);
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitFeedback();
            }
        });

        // Nút Hủy
        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(220, 230, 80, 30);
        contentPane.add(btnCancel);
        btnCancel.addActionListener(e -> {
            // Trở về LoginForm
            LoginForm login = new LoginForm();
            login.setVisible(true);
            this.dispose();
        });
    }

    /**
     * Xử lý khi nhấn "Gửi" trong chế độ khách.
     * 1. Lấy sdt --> tìm idKH (KhachHangDAO#getBySDT)
     * 2. Lấy idHD do người dùng nhập --> tìm idThuoc (ChiTietHoaDonDAO#getFirstIdThuocByHD)
     * 3. idPH = "PH" + System.currentTimeMillis()
     * 4. thoiGian = now
     * 5. Gọi PhanHoiController.addPhanHoiGuest(...)
     */
    private void submitFeedback() {
        String sdt = txtSDT.getText().trim();
        String idHD = txtIdHD.getText().trim();
        String noiDung = txtNoiDung.getText().trim();
        String strDanhGia = txtDanhGia.getText().trim();

        if (sdt.isEmpty() || idHD.isEmpty() || noiDung.isEmpty() || strDanhGia.isEmpty()) {
            MessageDialog.showWarning(this, "Vui lòng điền hết các trường", "Cảnh báo");
            return;
        }

        int danhGia;
        try {
            danhGia = Integer.parseInt(strDanhGia);
            if (danhGia < 1 || danhGia > 5) {
                MessageDialog.showWarning(this, "Đánh giá phải từ 1 đến 5", "Cảnh báo");
                return;
            }
        } catch (NumberFormatException ex) {
            MessageDialog.showWarning(this, "Đánh giá phải là số nguyên (1-5)", "Cảnh báo");
            return;
        }

        // Tìm idThuoc từ idHD
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        String idThuoc = chiTietHoaDonDAO.getFirstIdThuocByHD(idHD);
        if (idThuoc == null) {
            MessageDialog.showError(this, "Không tìm thấy thuốc trong hóa đơn " + idHD, "Lỗi");
            return;
        }

        // Gọi controller để gửi phản hồi với thông tin mặc định cho khách
        boolean success = phanHoiController.addPhanHoiGuest(idHD, sdt, noiDung, danhGia);
        if (success) {
            MessageDialog.showInfo(this, "Gửi phản hồi thành công. Cảm ơn bạn!", "Thông báo");
            new LoginForm().setVisible(true);
            this.dispose();
        } else {
            MessageDialog.showError(this, "Gửi phản hồi thất bại. Vui lòng kiểm tra lại thông tin.", "Lỗi");
        }
    }
}
