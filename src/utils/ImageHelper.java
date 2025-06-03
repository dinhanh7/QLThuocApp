package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Xử lý ảnh (đọc/ghi byte[] ↔ BufferedImage, tạo ImageIcon, scale ảnh...).
 */
public class ImageHelper {

    /**
     * Chuyển byte[] (lấy từ CSDL) thành ImageIcon để hiển thị lên JLabel.
     * @param data mảng byte (hình ảnh)
     * @return ImageIcon hoặc null nếu lỗi
     */
    public static ImageIcon toImageIcon(byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            BufferedImage img = ImageIO.read(bis);
            return new ImageIcon(img);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển từ file ảnh (jpg, png, ...) thành byte[] để lưu vào CSDL.
     */
    public static byte[] toByteArray(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             InputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Scale ảnh (ImageIcon) về kích thước mong muốn.
     * @param icon ImageIcon gốc
     * @param width chiều rộng mới
     * @param height chiều cao mới
     * @return ImageIcon đã scale hoặc null nếu icon == null
     */
    public static ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return null;
        }
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Lưu byte[] thành file ảnh trên đĩa (đường dẫn chỉ định). 
     */
    public static boolean saveImage(byte[] data, String outputPath) {
        if (data == null || outputPath == null || outputPath.trim().isEmpty()) {
            return false;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            BufferedImage img = ImageIO.read(bis);
            String formatName = outputPath.substring(outputPath.lastIndexOf('.') + 1);
            return ImageIO.write(img, formatName, new File(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
// ImageHelper.java 
