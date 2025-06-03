package gui;

import controller.LoginController;
import utils.MessageDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginController loginController;

    public LoginForm() {
        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
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

        btnLogin = new JButton("Login");
        btnLogin.setBounds(140, 150, 100, 30);
        contentPane.add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (loginController.authenticate(username, password)) {
            // Nếu đăng nhập thành công, mở MainFrame
            MessageDialog.showInfo(this, "Đăng nhập thành công!", "Thông báo");
            MainFrame main = new MainFrame();
            main.setVisible(true);
            this.dispose();
        } else {
            MessageDialog.showError(this, "Sai username hoặc password", "Lỗi");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}
// LoginForm.java 
