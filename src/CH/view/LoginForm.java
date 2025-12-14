package CH.view;

import CH.dao.AdminDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {

    private final Color PRIMARY_COLOR = new Color(0, 102, 204); // Màu xanh dương đậm
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Màu nền nhẹ
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginListener loginListener;

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống Quản Lý");
        setSize(450, 300); // Tăng kích thước
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Khóa không cho thay đổi kích thước

        // Đặt màu nền chung
        getContentPane().setBackground(BACKGROUND_COLOR);

        // 1. Panel Chính (chứa tất cả các thành phần)
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE); 
        
        // Cần một Container để căn giữa mainPanel trong JFrame
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(BACKGROUND_COLOR);
        container.add(mainPanel);
        add(container);

        // 2. Header (Tiêu đề)
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(PRIMARY_COLOR);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // 3. Form Panel (Tài khoản, Mật khẩu)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15)); // 2 hàng, 2 cột
        formPanel.setBackground(Color.WHITE);

        // --- Tài khoản ---
        JLabel lblUsername = new JLabel("Tài khoản:");
        lblUsername.setForeground(TEXT_COLOR);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblUsername);

        txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setText("admin"); // Gợi ý sẵn
        formPanel.add(txtUsername);

        // --- Mật khẩu ---
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setForeground(TEXT_COLOR);
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblPassword);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtPassword);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 4. Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(150, 40));
        btnLogin.setFocusPainted(false); // Bỏ viền focus khi click
        
        // Thêm hiệu ứng hover nhẹ (Optional: tạo class riêng cho nút bấm nếu cần)
        ActionListener originalAction = e -> performLogin();
        btnLogin.addActionListener(originalAction);

        buttonPanel.add(btnLogin);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Cho phép ấn Enter để login
        getRootPane().setDefaultButton(btnLogin);

        // ===== Tự động thêm nhân viên mẫu (nếu chưa có) =====
        new AdminDAO().insertDefaultUsersIfEmpty();
    }

    private void performLogin() {
        // ... (Giữ nguyên logic login)
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        AdminDAO dao = new AdminDAO();
        String role = dao.login(username, password);

        if (role != null) {
            if (loginListener != null) {
                loginListener.onLogin(role);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Interface callback (Giữ nguyên)
    public interface LoginListener {
        void onLogin(String role);
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
}