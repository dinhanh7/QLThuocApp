package gui;

import javax.swing.*;
import java.awt.*;

public class TrashPanel extends JPanel {

    private JTabbedPane trashTabs;

    public TrashPanel() {
        setLayout(new BorderLayout());
        trashTabs = new JTabbedPane();

//        // Thêm các panel con cho từng bảng đã xóa mềm
        trashTabs.addTab("Phản hồi", new TrashPhanHoiPanel());
//        trashTabs.addTab("Khách hàng", new TrashKhachHangPanel());
//        trashTabs.addTab("Nhà cung cấp", new TrashNhaCungCapPanel());
//        trashTabs.addTab("Hóa đơn", new TrashHoaDonPanel());
//        trashTabs.addTab("Hợp đồng", new TrashHopDongPanel());
        add(trashTabs, BorderLayout.CENTER);
    }
}

