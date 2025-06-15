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

        JLabel lblTitle = new JLabel("  ĐĂNG NHẬP");
        lblTitle.setIcon(new ImageIcon(LoginForm.class.getResource("/icon/pharmacy.png")));
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(103, 10, 200, 30);
        contentPane.add(lblTitle);

        JLabel lblUser = new JLabel("  Username:");
        lblUser.setIcon(new ImageIcon(LoginForm.class.getResource("/icon/username.png")));
        lblUser.setBounds(35, 60, 97, 25);
        contentPane.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(140, 60, 200, 25);
        contentPane.add(txtUsername);

        JLabel lblPass = new JLabel("   Password:");
        lblPass.setIcon(new ImageIcon(LoginForm.class.getResource("/icon/password.png")));
        lblPass.setBounds(35, 100, 97, 25);
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
        btnLogin = new JButton("  Login");
        btnLogin.setIcon(new ImageIcon(LoginForm.class.getResource("/icon/login.png")));
        btnLogin.setBounds(243, 145, 97, 30);
        contentPane.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Nút "Chế độ khách" ở góc dưới bên phải
        btnGuest = new JButton("  Chế độ khách");
        btnGuest.setIcon(new ImageIcon(LoginForm.class.getResource("/icon/chungGuest.png")));
        btnGuest.setBounds(8, 145, 173, 30);
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
