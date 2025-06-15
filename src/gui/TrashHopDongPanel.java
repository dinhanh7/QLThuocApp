package gui;

import dao.HopDongDAO;
import entities.HopDong;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TrashHopDongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private HopDongDAO dao = new HopDongDAO();
    private JTabbedPane parentTabbedPane;
    private int tabIndex;

    public TrashHopDongPanel(JTabbedPane tabbedPane, int index) {
        this.parentTabbedPane = tabbedPane;
        this.tabIndex = index;
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JLabel searchLabel = new JLabel("Tìm kiếm: ");
        JTextField searchField = new JTextField();
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"", "ID", "Ngày bắt đầu", "Ngày kết thúc", "Nội dung", "ID Nhân viên", "ID Nhà cung cấp", "Trạng thái"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        TableColumn checkColumn = table.getColumnModel().getColumn(0);
        checkColumn.setHeaderRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JCheckBox cb = new JCheckBox();
                cb.setHorizontalAlignment(JLabel.CENTER);
                cb.setSelected(false);
                return cb;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (col == 0) {
                    boolean allSelected = true;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Boolean checked = (Boolean) model.getValueAt(i, 0);
                        if (checked == null || !checked) {
                            allSelected = false;
                            break;
                        }
                    }
                    boolean newState = !allSelected;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        model.setValueAt(newState, i, 0);
                    }
                    table.getTableHeader().repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setIcon(new ImageIcon(TrashHopDongPanel.class.getResource("/icon/chungRefresh.png")));
        JButton btnRestore = new JButton("Khôi phục");
        btnRestore.setIcon(new ImageIcon(TrashHopDongPanel.class.getResource("/icon/ChungUndo.png")));
        JButton btnDeleteForever = new JButton("Xóa vĩnh viễn");
        btnDeleteForever.setIcon(new ImageIcon(TrashHopDongPanel.class.getResource("/icon/chungTrash.png")));

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnRestore);
        buttonPanel.add(btnDeleteForever);
        add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        btnRestore.addActionListener(e -> restoreSelectedRows());
        btnDeleteForever.addActionListener(e -> deleteSelectedRows());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void update() {
                String text = searchField.getText().trim().toLowerCase();
                table.clearSelection();
                for (int i = 0; i < model.getRowCount(); i++) {
                    boolean match = false;
                    for (int j = 1; j < model.getColumnCount(); j++) {
                        Object val = model.getValueAt(i, j);
                        if (val != null && val.toString().toLowerCase().contains(text)) {
                            match = true;
                            break;
                        }
                    }
                    if (match) table.addRowSelectionInterval(i, i);
                }
            }

            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        loadData();
    }

    void loadData() {
        model.setRowCount(0);
        List<HopDong> list = dao.getDeleted();
        for (HopDong hd : list) {
            model.addRow(new Object[]{
                false,
                hd.getIdHDong(),
                hd.getNgayBatDau(),
                hd.getNgayKetThuc(),
                hd.getNoiDung(),
                hd.getIdNV(),
                hd.getIdNCC(),
                hd.getTrangThai()
            });
        }
        table.clearSelection();
        if (parentTabbedPane != null && tabIndex < parentTabbedPane.getTabCount()) {
            parentTabbedPane.setTitleAt(tabIndex, "Hợp đồng (" + getDeletedCount() + ")");
        }
    }

    private List<String> getSelectedIds() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean selected = (Boolean) model.getValueAt(i, 0);
            if (selected != null && selected) {
                ids.add(model.getValueAt(i, 1).toString());
            }
        }
        return ids;
    }

    private void restoreSelectedRows() {
        List<String> ids = getSelectedIds();
        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng để khôi phục");
            return;
        }
        for (String id : ids) dao.restore(id);
        JOptionPane.showMessageDialog(this, "Khôi phục thành công");
        loadData();
    }

    private void deleteSelectedRows() {
        List<String> ids = getSelectedIds();
        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng để xóa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa vĩnh viễn các hợp đồng đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (String id : ids) dao.deleteForever(id);
            JOptionPane.showMessageDialog(this, "Đã xóa vĩnh viễn");
            loadData();
        }
    }

    public int getDeletedCount() {
        return dao.getDeleted().size();
    }
}

