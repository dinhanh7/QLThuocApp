package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainFrame.java (đã sửa initTabs(...) để luôn thêm ít nhất một vài tab nếu roleId không khớp).
 */
public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private ThuocPanel thuocPanel;
    private NhanVienPanel nhanVienPanel;
    private KhachHangPanel khachHangPanel;
    private NhaCungCapPanel nhaCungCapPanel;
    private HoaDonPanel hoaDonPanel;
    private PhieuNhapPanel phieuNhapPanel;
    private PhanHoiPanel phanHoiPanel;
    private HopDongPanel hopDongPanel;
    private TrashPanel trashPanel;
    /**
     * @param roleId Chuỗi idVT (vai trò) của người dùng.
     *               Thường là "VT01" = Admin, hoặc "VT02" = Nhân viên.
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
     * Nếu roleId không khớp "VT01" hay "VT02", vẫn thêm tab "Thuốc" để tránh giao diện trắng.
     */
    private void initTabs(String roleId) {
        tabbedPane = new JTabbedPane();

        // Khởi tạo tất cả panel trước (có NhaCungCapPanel mới)
        thuocPanel        = new ThuocPanel();
        nhanVienPanel     = new NhanVienPanel();
        khachHangPanel    = new KhachHangPanel();
        nhaCungCapPanel   = new NhaCungCapPanel();
        hoaDonPanel       = new HoaDonPanel();
        phieuNhapPanel    = new PhieuNhapPanel();
        phanHoiPanel      = new PhanHoiPanel();
        hopDongPanel      = new HopDongPanel();
        trashPanel 		  = new TrashPanel();
        if (roleId != null) {
            roleId = roleId.trim(); // bỏ dấu cách thừa
        }

        // Nếu roleId = "VT01" => Admin, hiển thị đủ 8 tab
        if ("VT01".equalsIgnoreCase(roleId)) {
            tabbedPane.addTab("Thuốc", thuocPanel);
            tabbedPane.addTab("Nhân viên", nhanVienPanel);
            tabbedPane.addTab("Khách hàng", khachHangPanel);
            tabbedPane.addTab("Nhà cung cấp", nhaCungCapPanel);
            tabbedPane.addTab("Hóa đơn", hoaDonPanel);
            tabbedPane.addTab("Phiếu nhập", phieuNhapPanel);
            tabbedPane.addTab("Phản hồi", phanHoiPanel);
            tabbedPane.addTab("Hợp đồng", hopDongPanel);
            tabbedPane.addTab("Thùng rác", trashPanel);
        }
        // Nếu roleId = "VT02" => Nhân viên, hiển thị 6 tab
        else if ("VT02".equalsIgnoreCase(roleId)) {
            tabbedPane.addTab("Thuốc", thuocPanel);
            tabbedPane.addTab("Khách hàng", khachHangPanel);
            tabbedPane.addTab("Hóa đơn", hoaDonPanel);
            tabbedPane.addTab("Phiếu nhập", phieuNhapPanel);
            tabbedPane.addTab("Phản hồi", phanHoiPanel);
            // Nhân viên cũng được truy cập "Hợp đồng"?
            tabbedPane.addTab("Hợp đồng", hopDongPanel);
        }
        // Nếu roleId khác (hoặc null/empty), ít nhất cho hiển thị tab "Thuốc" để không bị trắng
        else {
            tabbedPane.addTab("Thuốc", thuocPanel);
            // bạn có thể thêm tab "Khách hàng" mặc định nếu muốn
            tabbedPane.addTab("Khách hàng", khachHangPanel);
        }

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
}
