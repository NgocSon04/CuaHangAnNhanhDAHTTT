package CH.view;

import CH.dao.AdminDAO;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginListener loginListener;

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(400, 250); // Tăng kích thước một chút cho đẹp
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // ===== GridBagLayout Config =====
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Label & Input Username ---
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Tài khoản:"), gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        txtUsername.setText("admin"); // Gợi ý sẵn để test nhanh
        add(txtUsername, gbc);

        // --- Label & Input Password ---
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        add(txtPassword, gbc);

        // --- Button Login ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(100, 35));
        add(btnLogin, gbc);

        // ===== SỰ KIỆN =====
        btnLogin.addActionListener(e -> performLogin());

        // Cho phép ấn Enter để login
        getRootPane().setDefaultButton(btnLogin);

        // ===== Tự động thêm nhân viên mẫu (nếu chưa có) =====
        new AdminDAO().insertDefaultUsersIfEmpty();
    }

    private void performLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        AdminDAO dao = new AdminDAO();
        // Hàm login này giờ sẽ tìm trong bảng NhanVien
        String role = dao.login(username, password);

        if (role != null) {
            // Gọi về Main để chuyển cảnh
            if (loginListener != null) {
                loginListener.onLogin(role);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Interface callback
    public interface LoginListener {
        void onLogin(String role);
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
}