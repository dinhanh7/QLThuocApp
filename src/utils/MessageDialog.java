package utils;

import javax.swing.*;
import java.awt.*;

/**
 * Các phương thức tiện ích hiển thị thông báo bằng JOptionPane.
 */
public class MessageDialog {

    /**
     * Hiển thị thông báo thông tin (INFO).
     */
    public static void showInfo(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Hiển thị cảnh báo (WARNING).
     */
    public static void showWarning(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Hiển thị lỗi (ERROR).
     */
    public static void showError(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Hiển thị hộp thoại xác nhận (YES/NO).
     * @return true nếu người dùng chọn Yes
     */
    public static boolean showConfirm(Component parent, String message, String title) {
        int option = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return option == JOptionPane.YES_OPTION;
    }
}
// MessageDialog.java 
