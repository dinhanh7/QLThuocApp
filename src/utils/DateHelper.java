package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Các phương thức định dạng và chuyển đổi Date ↔ String.
 */
public class DateHelper {
    // Mẫu định dạng ngày-tháng-năm
    private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat(DEFAULT_DATE_PATTERN);

    /**
     * Chuyển Date thành String với mẫu mặc định "dd/MM/yyyy".
     * @param date đối tượng Date
     * @return chuỗi định dạng ngày hoặc empty nếu date == null
     */
    
    public static String toString(Date date) {
        if (date == null) {
            return "";
        }
        return DEFAULT_FORMAT.format(date);
    }

    /**
     * Chuyển String thành Date theo mẫu mặc định "dd/MM/yyyy".
     * @param dateStr chuỗi ngày (vd: "31/12/2024")
     * @return đối tượng Date hoặc null nếu không thể parse
     */
    public static Date toDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return DEFAULT_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển Date thành String theo mẫu custom, ví dụ "yyyy-MM-dd HH:mm:ss".
     * @param date đối tượng Date
     * @param pattern chuỗi mẫu (SimpleDateFormat)
     * @return chuỗi định dạng hoặc empty nếu date == null
     */
    public static String toString(java.util.Date date, String pattern) {
        if (date == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * Chuyển String thành Date theo mẫu custom.
     * @param dateStr chuỗi ngày
     * @param pattern mẫu tương ứng
     * @return Date hoặc null nếu lỗi parse
     */
    public static Date toDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty() || pattern == null || pattern.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(pattern);
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static java.util.Date toDateTime(String dateStr, String pattern) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
            sdf.setLenient(false);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Lấy Date hiện tại (không giờ, chỉ ngày).
     * @return đối tượng Date của ngày hiện tại (00:00:00)
     */
    public static Date now() {
        return toDate(toString(new Date()));
    }
}
// DateHelper.java 
