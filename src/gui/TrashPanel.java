package gui;

import javax.swing.*;
import java.awt.*;

public class TrashPanel extends JPanel {

    private JTabbedPane trashTabs;

    public TrashPanel() {
        setLayout(new BorderLayout());
        trashTabs = new JTabbedPane();

        TrashPhanHoiPanel phanHoiPanel = new TrashPhanHoiPanel(trashTabs, 0);
        TrashHopDongPanel hopDongPanel = new TrashHopDongPanel(trashTabs, 1);
        TrashHoaDonPanel hoaDonPanel = new TrashHoaDonPanel(trashTabs, 2);
        TrashNhanVienPanel nhanVienPanel = new TrashNhanVienPanel(trashTabs, 3);
        TrashThuocPanel ThuocPanel = new TrashThuocPanel(trashTabs, 4);
        TrashNhaCungCapPanel NCCPanel = new TrashNhaCungCapPanel(trashTabs, 5);
        TrashPhieuNhapPanel phieuNhapPanel = new TrashPhieuNhapPanel(trashTabs, 6);
        
        
        trashTabs.addTab("Phản hồi", phanHoiPanel);
        trashTabs.addTab("Hợp đồng", hopDongPanel);
        trashTabs.addTab("Hóa đơn", hoaDonPanel);
        trashTabs.addTab("Nhân viên", nhanVienPanel);
        trashTabs.addTab("Thuốc", ThuocPanel);
        trashTabs.addTab("Nhà cung cấp", NCCPanel);
        trashTabs.addTab("Phiếu nhập", phieuNhapPanel);

        
        phanHoiPanel.loadData();  // gọi loadData sau khi đã addTab
        hopDongPanel.loadData();
        hoaDonPanel.loadData();
        nhanVienPanel.loadData();
        ThuocPanel.loadData();
        NCCPanel.loadData();
        phieuNhapPanel.loadData();
        
        add(trashTabs, BorderLayout.CENTER);
    }

}

