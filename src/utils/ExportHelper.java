package utils;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Xuất dữ liệu ra file CSV (có thể mở bằng Excel).
 * Nếu muốn xuất PDF hoặc định dạng Excel (.xlsx) chuyên sâu, bạn cần bổ sung thư viện Apache POI (cho Excel) 
 * hoặc iText (cho PDF) vào project.
 */
public class ExportHelper {

    /**
     * Xuất JTable ra CSV (ngăn cách bằng dấu phẩy).
     * @param table JTable cần xuất
     * @param outputFile File đích (định dạng .csv)
     * @throws IOException nếu lỗi IO
     */
    public static void exportTableToCSV(JTable table, File outputFile) throws IOException {
        if (table == null || outputFile == null) {
            throw new IllegalArgumentException("Table hoặc outputFile không được null");
        }

        TableModel model = table.getModel();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            int columnCount = model.getColumnCount();
            int rowCount = model.getRowCount();

            // Viết header
            for (int col = 0; col < columnCount; col++) {
                bw.write(model.getColumnName(col));
                if (col < columnCount - 1) {
                    bw.write(",");
                }
            }
            bw.newLine();

            // Viết dữ liệu từng dòng
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Object value = model.getValueAt(row, col);
                    String cell = value != null ? value.toString().replaceAll(",", " ") : "";
                    bw.write(cell);
                    if (col < columnCount - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
        }
    }

    /**
     * (Tùy chọn) Xuất danh sách entity ra file CSV.
     * Ví dụ: List<Thuoc> → CSV với các cột tương ứng.
     * Bạn có thể overload hoặc thêm phương thức khác để xuất List<Entity>...
     */
    public static <T> void exportListToCSV(Iterable<T> list, File outputFile) throws IOException {
        // TODO: Cài đặt theo từng entity cụ thể hoặc sử dụng reflection.
        throw new UnsupportedOperationException("Phương thức exportListToCSV cần được cài đặt riêng theo entity.");
    }

    /**
     * (Placeholder) Xuất JTable ra file PDF.
     * Để làm việc này, cần bổ sung thư viện iText (hoặc OpenPDF). Sample ví dụ sẽ như sau:
     *
     *   Document document = new Document();
     *   PdfWriter.getInstance(document, new FileOutputStream(outputFile));
     *   document.open();
     *   PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
     *   // ... thêm header, dữ liệu ...
     *   document.add(pdfTable);
     *   document.close();
     *
     * Method này chưa cài đặt sẵn để tránh phụ thuộc thư viện.
     */
    public static void exportTableToPDF(JTable table, File outputFile) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Muốn xuất PDF, phải thêm thư viện iText hoặc OpenPDF và cài đặt logic ở đây.");
    }
}
// ExportHelper.java 
