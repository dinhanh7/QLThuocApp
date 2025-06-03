package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Các phương thức kiểm tra tính hợp lệ dữ liệu đầu vào chung.
 */
public class Validator {

    /**
     * Kiểm tra chuỗi có null hoặc rỗng hay không.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Kiểm tra chuỗi có phải số nguyên dương hay không.
     */
    public static boolean isPositiveInteger(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        try {
            int val = Integer.parseInt(str.trim());
            return val >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra chuỗi có phải số thực (double) hay không.
     */
    public static boolean isDouble(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra định dạng ngày theo mẫu "dd/MM/yyyy".
     */
    public static boolean isDate(String value, String pattern) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            sdf.parse(value.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Nếu bạn vẫn muốn giữ lại phiên bản cũ chỉ nhận 1 tham số,
    // bạn có thể để nó mặc định kiểm tra theo một pattern mặc định:
    public static boolean isDate(String value) {
        return isDate(value, "dd/MM/yyyy");
    }

    /**
     * Kiểm tra format email cơ bản (có chứa "@" và ".").
     */
    public static boolean isEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.trim().matches(regex);
    }
}
// Validator.java 
