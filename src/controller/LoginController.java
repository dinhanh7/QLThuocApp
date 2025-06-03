package controller;

import dao.TaiKhoanDAO;
import entities.TaiKhoan;

/**
 * LoginController.java
 * Xử lý nghiệp vụ liên quan đến đăng nhập.
 */
public class LoginController {

    private TaiKhoanDAO taiKhoanDAO;

    public LoginController() {
        taiKhoanDAO = new TaiKhoanDAO();
    }

    /**
     * Kiểm tra username/password. Nếu hợp lệ, trả về đối tượng TaiKhoan (chứa cả idVT).
     * Nếu không, trả về null.
     */
    public TaiKhoan authenticateAndGetAccount(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return null;
        }

        // Lấy TaiKhoan theo username
        TaiKhoan tk = taiKhoanDAO.getByUsername(username);
        // Nếu tồn tại và password khớp
        if (tk != null && tk.getPassword().equals(password)) {
            return tk;
        }
        return null;
    }
}
