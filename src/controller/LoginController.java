package controller;

import dao.TaiKhoanDAO;

public class LoginController {
    private TaiKhoanDAO taiKhoanDAO;

    public LoginController() {
        taiKhoanDAO = new TaiKhoanDAO();
    }

    /**
     * Kiểm tra đăng nhập với username và password.
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return true nếu đúng, false nếu sai
     */
    public boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }
        return taiKhoanDAO.checkLogin(username, password);
    }
}
// LoginController.java 
