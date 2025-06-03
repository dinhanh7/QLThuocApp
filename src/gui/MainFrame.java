package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import gui.ThuocPanel;
import gui.NhanVienPanel;
import gui.KhachHangPanel;
import gui.HoaDonPanel;
import gui.PhieuNhapPanel;
import gui.PhieuDatHangPanel;
import gui.PhanHoiPanel;
import gui.HopDongPanel;
import gui.NhaCungCapPanel; // <--- import cho tab Nhà cung cấp

/**
 * MainFrame.java (với nút Đăng xuất cố định ở góc trên bên phải)
 */
public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private ThuocPanel thuocPanel;
    private NhanVienPanel nhanVienPanel;
    private KhachHangPanel khachHangPanel;
    private HoaDonPanel hoaDonPanel;
    private PhieuNhapPanel phieuNhapPanel;
    private PhieuDatHangPanel phieuDatHangPanel;
    private PhanHoiPanel phanHoiPanel;
    private HopDongPanel hopDongPanel;
    private NhaCungCapPanel nccPanel; // <--- panel Nhà cung cấp

    /**
     * @param roleId Chuỗi idVT (vai trò) của người dùng.
     *               Ví dụ: "VT01" = Admin, "VT02" = Nhân viên.
     */
    public MainFrame(String roleId) {
        setTitle("Hệ thống Quản lý Nhà thuốc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Khởi tạo giao diện
        initTopBar();
        initTabs(roleId);
    }

    /**
     * Tạo thanh trên cùng chứa nút Đăng xuất ở góc trên bên phải.
     */
    private void initTopBar() {
        // Panel vùng NORTH dùng BorderLayout để đặt nút ở Đông
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(getWidth(), 40));
        topBar.setBackground(new Color(240, 240, 240));

        // Khoảng trống phía trái (để nút nằm góc phải)
        JPanel glue = new JPanel();
        glue.setOpaque(false);
        topBar.add(glue, BorderLayout.CENTER);

        // Nút Đăng xuất
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setMargin(new Insets(5, 10, 5, 10));
        btnLogout.addActionListener(e -> {
            // Đóng MainFrame và mở lại LoginForm
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginForm login = new LoginForm();
                login.setVisible(true);
            });
        });

        // Đặt nút ở góc trên bên phải (BorderLayout.EAST)
        topBar.add(btnLogout, BorderLayout.EAST);

        getContentPane().add(topBar, BorderLayout.NORTH);
    }

    /**
     * Tạo JTabbedPane theo vai trò và thêm vào vùng CENTER.
     */
    private void initTabs(String roleId) {
        tabbedPane = new JTabbedPane();

        // Khởi tạo tất cả panel
        thuocPanel        = new ThuocPanel();
        nhanVienPanel     = new NhanVienPanel();
        khachHangPanel    = new KhachHangPanel();
        hoaDonPanel       = new HoaDonPanel();
        phieuNhapPanel    = new PhieuNhapPanel();
        phieuDatHangPanel = new PhieuDatHangPanel();
        phanHoiPanel      = new PhanHoiPanel();
        hopDongPanel      = new HopDongPanel();
        nccPanel          = new NhaCungCapPanel(); // <--- khởi tạo panel Nhà cung cấp

        // Nếu roleId = "VT01" (Admin), hiển thị đủ 9 tab (Bao gồm Nhà cung cấp)
        if ("VT01".equals(roleId)) {
            tabbedPane.addTab("Thuốc", thuocPanel);
            tabbedPane.addTab("Nhân viên", nhanVienPanel);
            tabbedPane.addTab("Khách hàng", khachHangPanel);
            tabbedPane.addTab("Nhà cung cấp", nccPanel);      // <--- thêm tab Nhà cung cấp
            tabbedPane.addTab("Hóa đơn", hoaDonPanel);
            tabbedPane.addTab("Phiếu nhập", phieuNhapPanel);
            tabbedPane.addTab("Phiếu đặt hàng", phieuDatHangPanel);
            tabbedPane.addTab("Phản hồi", phanHoiPanel);
            tabbedPane.addTab("Hợp đồng", hopDongPanel);
        }
        // Nếu roleId = "VT02" (Nhân viên), hiển thị 7 tab (Bao gồm Nhà cung cấp)
        else if ("VT02".equals(roleId)) {
            tabbedPane.addTab("Thuốc", thuocPanel);
            tabbedPane.addTab("Khách hàng", khachHangPanel);
            tabbedPane.addTab("Nhà cung cấp", nccPanel);      // <--- thêm tab Nhà cung cấp
            tabbedPane.addTab("Hóa đơn", hoaDonPanel);
            tabbedPane.addTab("Phiếu nhập", phieuNhapPanel);
            tabbedPane.addTab("Phiếu đặt hàng", phieuDatHangPanel);
            tabbedPane.addTab("Phản hồi", phanHoiPanel);
        }
        // Nếu có thêm vai trò khác, bạn mở rộng thêm điều kiện tại đây

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
}
