package CH.main;

import CH.controller.LoginController; // Nhớ import Controller
import CH.dao.DBConnection;
import CH.view.LoginView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1. Khởi tạo database (Tạo bảng nếu chưa có)
        DBConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            try {
                // Set giao diện giống hệ điều hành (Windows) cho đẹp
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 2. Khởi tạo View (Giao diện)
            LoginView loginView = new LoginView();

            // 3. Khởi tạo Controller (Điều khiển Logic)
            // Controller sẽ tự động gắn sự kiện vào nút bấm trong View
            new LoginController(loginView); 

            // 4. Hiển thị Giao diện
            loginView.setVisible(true);
        });
    }
}