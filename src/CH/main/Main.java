package CH.main;

import CH.dao.DBConnection;
import CH.view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1. Khởi tạo database
        DBConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginForm loginForm = new LoginForm();

            loginForm.setLoginListener(role -> {

                if (role == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Sai username hoặc password!",
                            "Lỗi đăng nhập",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                MainView mainView = new MainView();
                mainView.setRole(role); // ADMIN hoặc NHÂN VIÊN

                mainView.setVisible(true);
                loginForm.dispose();
            });

            loginForm.setVisible(true);
        });
    }
}
