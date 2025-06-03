package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Validator.java
 *
 * Các phương thức tiện ích để kiểm tra tính hợp lệ của dữ liệu đầu vào.
 */
public class Validator {

    /**
     * Kiểm tra xem chuỗi text có thể được chuyển sang Date với định dạng format không.
     * Ví dụ: isDate("31/12/2023", "dd/MM/yyyy") trả về true,
     * isDate("2023-12-31", "dd/MM/yyyy") trả về false.
     *
     * @param text   Chuỗi cần kiểm tra.
     * @param format Mẫu định dạng (ví dụ: "dd/MM/yyyy" hoặc "dd/MM/yyyy HH:mm").
     * @return true nếu text đúng định dạng, false nếu không.
     */
    public static boolean isDate(String text, String format) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(text.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Kiểm tra xem chuỗi text có thể parse thành kiểu double không.
     * Ví dụ: isDouble("123.45") trả về true, isDouble("abc") trả về false.
     *
     * @param text Chuỗi cần kiểm tra.
     * @return true nếu có thể parse thành double, false nếu không.
     */
    public static boolean isDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra xem chuỗi text có thể parse thành kiểu int không.
     * Ví dụ: isInteger("123") trả về true, isInteger("12.3") hoặc isInteger("abc") trả về false.
     *
     * @param text Chuỗi cần kiểm tra.
     * @return true nếu có thể parse thành integer, false nếu không.
     */
    public static boolean isInteger(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra xem chuỗi text có phải là số điện thoại hợp lệ (10 hoặc 11 chữ số) hay không.
     * Ví dụ: isPhone("0912345678") trả về true, isPhone("12345") trả về false.
     *
     * @param text Chuỗi cần kiểm tra.
     * @return true nếu text là 10-11 chữ số, false nếu không.
     */
    public static boolean isPhone(String text) {
        if (text == null) {
            return false;
        }
        return text.trim().matches("\\d{10,11}");
    }

    /**
     * Kiểm tra xem chuỗi text có phải là email hợp lệ hay không.
     * (Nếu hệ thống cần kiểm tra email, có thể dùng regex đơn giản dưới đây.)
     *
     * Ví dụ: isEmail("user@example.com") trả về true,
     * isEmail("user@example") trả về false.
     *
     * @param text Chuỗi cần kiểm tra.
     * @return true nếu text khớp regex email, false nếu không.
     */
    public static boolean isEmail(String text) {
        if (text == null) {
            return false;
        }
        // Regex cơ bản cho email
        return text.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
