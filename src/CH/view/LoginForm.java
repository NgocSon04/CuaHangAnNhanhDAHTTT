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
        setTitle("Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // ===== GridBagLayout =====
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        btnLogin = new JButton("Login");
        add(btnLogin, gbc);

        // ===== SỰ KIỆN LOGIN =====
        btnLogin.addActionListener(e -> login());

        // ===== Tự động thêm user mẫu =====
        new AdminDAO().insertDefaultUsersIfEmpty();
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        AdminDAO dao = new AdminDAO();
        String role = dao.login(username, password);


        // Callback nếu có listener
        if (loginListener != null) {
            loginListener.onLogin(role);
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
