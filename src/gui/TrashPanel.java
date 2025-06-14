package gui;

import javax.swing.*;
import java.awt.*;

public class TrashPanel extends JPanel {

    private JTabbedPane trashTabs;

    public TrashPanel() {
        setLayout(new BorderLayout());
        trashTabs = new JTabbedPane();

        TrashPhanHoiPanel phanHoiPanel = new TrashPhanHoiPanel(trashTabs, 0);
        trashTabs.addTab("Phản hồi", phanHoiPanel);
        phanHoiPanel.loadData();  // gọi loadData sau khi đã addTab

        add(trashTabs, BorderLayout.CENTER);
    }

}

