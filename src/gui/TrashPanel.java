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
        
        
        trashTabs.addTab("Phản hồi", phanHoiPanel);
		trashTabs.addTab("Hợp đồng", hopDongPanel);
		trashTabs.addTab("Hóa đơn", hoaDonPanel);
        
		
		
		phanHoiPanel.loadData();  // gọi loadData sau khi đã addTab
        hopDongPanel.loadData();
        hoaDonPanel.loadData();
        add(trashTabs, BorderLayout.CENTER);
    }

}

