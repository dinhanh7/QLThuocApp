package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private LoginForm loginForm; // nếu cần gọi từ đây
    private ThuocPanel thuocPanel;
    private NhanVienPanel nhanVienPanel;
    private KhachHangPanel khachHangPanel;
    private HoaDonPanel hoaDonPanel;
    private PhieuNhapPanel phieuNhapPanel;
    private PhieuDatHangPanel phieuDatHangPanel;
    private PhanHoiPanel phanHoiPanel;
    private HopDongPanel hopDongPanel;

    public MainFrame() {
        setTitle("Hệ thống Quản lý Nhà thuốc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // Khởi tạo các panel
        thuocPanel = new ThuocPanel();
        nhanVienPanel = new NhanVienPanel();
        khachHangPanel = new KhachHangPanel();
        hoaDonPanel = new HoaDonPanel();
        phieuNhapPanel = new PhieuNhapPanel();
        phieuDatHangPanel = new PhieuDatHangPanel();
        phanHoiPanel = new PhanHoiPanel();
        hopDongPanel = new HopDongPanel();

        // Thêm từng tab vào tabbedPane
        tabbedPane.addTab("Thuốc", thuocPanel);
        tabbedPane.addTab("Nhân viên", nhanVienPanel);
        tabbedPane.addTab("Khách hàng", khachHangPanel);
        tabbedPane.addTab("Hóa đơn", hoaDonPanel);
        tabbedPane.addTab("Phiếu nhập", phieuNhapPanel);
        tabbedPane.addTab("Phiếu đặt hàng", phieuDatHangPanel);
        tabbedPane.addTab("Phản hồi", phanHoiPanel);
        tabbedPane.addTab("Hợp đồng", hopDongPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Nếu có form đăng nhập, bạn có thể khởi động LoginForm trước rồi mới show MainFrame
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
// MainFrame.java 
