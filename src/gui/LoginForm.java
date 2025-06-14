package gui;

import controller.LoginController;
import entities.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginForm.java (có thêm nút "Chế độ khách" ở góc dưới bên phải)
 */
public class LoginForm extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnGuest;      // nút chế độ khách
    private LoginController loginController;

    public LoginForm() {
        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 260);
        setLocationRelativeTo(null);
        initComponents();
        loginController = new LoginController();
    }

    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(140, 10, 120, 30);
        contentPane.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 60, 80, 25);
        contentPane.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(140, 60, 200, 25);
        contentPane.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 100, 80, 25);
        contentPane.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 100, 200, 25);
        contentPane.add(txtPassword);
        // Thêm sự kiện Enter để login
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        btnLogin = new JButton("Login");
        btnLogin.setBounds(140, 150, 100, 30);
        contentPane.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Nút "Chế độ khách" ở góc dưới bên phải
        btnGuest = new JButton("Chế độ khách");
        btnGuest.setBounds(260, 200, 120, 25);
        contentPane.add(btnGuest);
        btnGuest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGuestMode();
            }
        });
    }

    /**
     * Xử lý đăng nhập bình thường (Admin/ Nhân viên).
     */
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        TaiKhoan tk = loginController.authenticateAndGetAccount(username, password);
        if (tk != null) {
            String roleId = tk.getIdVT();  // ví dụ "VT01" (Admin) hoặc "VT02" (Nhân viên)
            MainFrame main = new MainFrame(roleId);
            main.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Sai username hoặc password",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Mở chế độ khách: hiển thị Form phản hồi (GuestFeedbackForm).
     */
    private void openGuestMode() {
        // Không cần thông tin đăng nhập, mở thẳng form
        GuestFeedbackForm guestForm = new GuestFeedbackForm();
        guestForm.setVisible(true);
        this.dispose();  // Đóng cửa sổ LoginForm
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}
