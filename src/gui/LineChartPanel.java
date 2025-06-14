package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class LineChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> data;

    public LineChartPanel(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            g.drawString("Không có dữ liệu để hiển thị.", 50, 50);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 60;
        int labelPadding = 40;
        int width = getWidth();
        int height = getHeight();
        int pointSize = 6;

        List<String> labels = new ArrayList<>(data.keySet());
        List<Integer> values = new ArrayList<>(data.values());

        int maxVal = Collections.max(values);
        int minVal = 0;

        double xScale = ((double) (width - 2 * padding - labelPadding)) / (labels.size() - 1);
        double yScale = ((double) (height - 2 * padding)) / (maxVal - minVal);

        // Vẽ lưới ngang
        g2.setColor(new Color(220, 220, 220));
        for (int i = 0; i <= 5; i++) {
            int y = height - padding - (int) (i * (height - 2 * padding) / 5.0);
            g2.drawLine(padding, y, width - padding, y);
        }

        // Vẽ đường
        g2.setColor(new Color(0, 120, 215));
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2f));

        for (int i = 0; i < values.size() - 1; i++) {
            int x1 = (int) (padding + i * xScale);
            int y1 = height - padding - (int) ((values.get(i) - minVal) * yScale);
            int x2 = (int) (padding + (i + 1) * xScale);
            int y2 = height - padding - (int) ((values.get(i + 1) - minVal) * yScale);
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);

        // Vẽ điểm và giá trị
        for (int i = 0; i < values.size(); i++) {
            int x = (int) (padding + i * xScale);
            int y = height - padding - (int) ((values.get(i) - minVal) * yScale);
            g2.setColor(Color.RED);
            g2.fillOval(x - pointSize / 2, y - pointSize / 2, pointSize, pointSize);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 7));
            g2.drawString(String.valueOf(values.get(i)), x - 5, y - 10);
        }

        // Vẽ nhãn ngày dưới trục
        for (int i = 0; i < labels.size(); i++) {
            int x = (int) (padding + i * xScale);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));

            if (labels.size() > 15 && i % 2 != 0) continue; // bỏ nhãn xen kẽ nếu quá nhiều
            g2.drawString(labels.get(i), x - 10, height - padding + 15);
        }

        // Vẽ trục
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, height - padding, padding, padding); // Y
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X

        // Tiêu đề
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Biểu đồ doanh thu theo ngày", width / 2 - 100, 25);
        
     // Doanh thu trung bình và tổng doanh thu
        double avg = values.stream().mapToInt(i -> i).average().orElse(0);
        int total = values.stream().mapToInt(i -> i).sum();
        int n = values.size();

        String avgLabel = String.format("Doanh thu trung bình trong %d ngày: %.0f", n, avg);
        String sumLabel = String.format("Tổng doanh thu trong %d ngày: %d", n, total);

        // Thiết lập font và vị trí
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics metrics = g2.getFontMetrics();
        int x = 60;
        int y = 50;
        int lineSpacing = 20;

        // Tính kích thước khung
        int xwidth = Math.max(metrics.stringWidth(avgLabel), metrics.stringWidth(sumLabel)) + 20;
        int xheight = lineSpacing * 2 + 20;

        // Vẽ khung nền sáng
        g2.setColor(new Color(240, 240, 240));  // nền sáng
        g2.fillRoundRect(x - 10, y - 25, xwidth, xheight, 15, 15);

        // Viền khung
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(x - 10, y - 25, xwidth, xheight, 15, 15);

        // Vẽ chữ
        g2.setColor(Color.BLACK);
        g2.drawString(avgLabel, x, y);
        g2.drawString(sumLabel, x, y + lineSpacing);
    }
}
