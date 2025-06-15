package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class LineChartPanel2 extends JPanel {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> data;
    private String title, tongText, tbText;

    public LineChartPanel2(Map<String, Integer> data, String title, String tongText, String tbText) {
        this.data = data;
        this.title = title;
        this.tongText = tongText;
        this.tbText = tbText;
        setPreferredSize(new Dimension(800, 500));
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

        int maxValue = data.values().stream().mapToInt(i -> i).max().orElse(1);
        int xStep = (width - 2 * padding) / (data.size() - 1);
        int yStep = (height - 2 * padding) / 10;  // chia 10 mức trục Y

        int[] xPoints = new int[data.size()];
        int[] yPoints = new int[data.size()];

        // Vẽ lưới ngang và nhãn trục Y
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= 10; i++) {
            int y = height - padding - i * yStep;
            g2.drawLine(padding, y, width - padding, y);
            int value = maxValue * i / 10;
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.format("%,d", value), padding - 50, y + 5);
            g2.setColor(Color.LIGHT_GRAY);
        }

        // Vẽ trục X và Y
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X
        g2.drawLine(padding, padding, padding, height - padding); // Y

        // Tính tọa độ điểm
        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int x = padding + i * xStep;
            int y = height - padding - (int)((double)entry.getValue() / maxValue * (height - 2 * padding));
            xPoints[i] = x;
            yPoints[i] = y;
            i++;
        }

        // Vẽ đường biểu đồ
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawPolyline(xPoints, yPoints, data.size());

        // Vẽ điểm và nhãn X
        g2.setColor(Color.RED);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
        i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int x = xPoints[i];
            int y = yPoints[i];
            g2.fillOval(x - 3, y - 3, 6, 6);
            g2.drawString(String.valueOf(entry.getValue()), x - 5, y - 7);
            g2.drawString(entry.getKey(), x - 15, height - padding + 15);
            i++;
        }

        // Tiêu đề biểu đồ
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.setColor(Color.BLACK);
        g2.drawString(title, padding, 30);

        // --- Vẽ khung tổng/trung bình với gradient và bo góc ---
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int maxTextWidth = Math.max(fm.stringWidth(tongText), fm.stringWidth(tbText));
        int boxWidth = maxTextWidth + 20;
        int boxHeight = 45;
        int boxX = padding;
        int boxY = 40;

        // Gradient màu nền khung
        GradientPaint gradient = new GradientPaint(boxX, boxY, new Color(255, 255, 255, 200),
                                                   boxX + boxWidth, boxY + boxHeight, new Color(200, 200, 255, 200));
        g2.setPaint(gradient);
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        g2.drawString(tongText, boxX + 10, boxY + 20);
        g2.drawString(tbText, boxX + 10, boxY + 40);
    }
}
