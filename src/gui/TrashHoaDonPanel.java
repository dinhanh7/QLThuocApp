package gui;

import dao.HoaDonDAO;
import entities.HoaDon;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TrashHoaDonPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private HoaDonDAO dao = new HoaDonDAO();
    private JTabbedPane parentTabbedPane;
    private int tabIndex;

    public TrashHoaDonPanel(JTabbedPane tabbedPane, int index) {
        this.parentTabbedPane = tabbedPane;
        this.tabIndex = index;
        setLayout(new BorderLayout());

        // Search
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JLabel searchLabel = new JLabel("Tìm kiếm: ");
        JTextField searchField = new JTextField();
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"", "ID Hóa đơn", "Thời gian","ID Nhân viên", "ID Khách hàng", "Tổng tiền","Phương thức thanh toán", "Trạng thái"};
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

        // Header checkbox
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

        // Select all toggle
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

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnRestore = new JButton("Khôi phục");
        JButton btnDeleteForever = new JButton("Xóa vĩnh viễn");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnRestore);
        buttonPanel.add(btnDeleteForever);
        add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        btnRestore.addActionListener(e -> restoreSelectedRows());
        btnDeleteForever.addActionListener(e -> deleteSelectedRows());

        // Realtime search
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
        List<HoaDon> list = dao.getDeleted();
        for (HoaDon hd : list) {
            model.addRow(new Object[]{
                false,
                hd.getIdHD(),
                hd.getThoiGian(),
                hd.getIdNV(),
                hd.getIdKH(),
                hd.getTongTien(),
                hd.getPhuongThucThanhToan(),
                hd.getTrangThaiDonHang()
            });
        }
        table.clearSelection();
        if (parentTabbedPane != null && tabIndex < parentTabbedPane.getTabCount()) {
            parentTabbedPane.setTitleAt(tabIndex, "Hóa đơn (" + getDeletedCount() + ")");
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để khôi phục");
            return;
        }
        for (String id : ids) dao.restore(id);
        JOptionPane.showMessageDialog(this, "Khôi phục thành công");
        loadData();
    }

    private void deleteSelectedRows() {
        List<String> ids = getSelectedIds();
        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xóa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa vĩnh viễn các hóa đơn đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
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

