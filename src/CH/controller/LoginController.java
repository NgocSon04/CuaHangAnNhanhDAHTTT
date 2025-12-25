package CH.controller;

import CH.dao.DBConnection;
import CH.dao.NhanVienDAO; // Đã đổi từ TaiKhoanDAO sang NhanVienDAO
import CH.view.LoginView;
import CH.view.MainView;
import javax.swing.*;
import java.sql.Connection;

public class LoginController {
    private LoginView view;
    private NhanVienDAO nhanVienDAO; // Sử dụng đúng lớp DAO bạn có


    public LoginController(LoginView view) {
        this.view = view;
        this.nhanVienDAO = new NhanVienDAO(); // Khởi tạo lại DAO để làm mới phiên làm việc
        initEvents();
    }

    private void initEvents() {
        // Gắn sự kiện cho nút Đăng nhập
        view.getBtnLogin().addActionListener(e -> handleLogin());

        // Hỗ trợ nhấn Enter trong ô mật khẩu để đăng nhập nhanh
        view.getTxtPassword().addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập tài khoản và mật khẩu!");
            return;
        }

        // Kiểm tra kết nối Database trước khi truy vấn
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(view, "Không thể kết nối cơ sở dữ liệu! Hãy kiểm tra MySQL.");
            return;
        }

        // Gọi hàm đăng nhập từ NhanVienDAO
        // Lưu ý: Đảm bảo trong NhanVienDAO bạn có hàm login(user, pass) trả về Role
        String role = nhanVienDAO.login(username, password); 

        if (role != null) {
            MainView mainView = new MainView();
            mainView.setRole(role); // Phân quyền giao diện dựa trên Role trả về
            mainView.setVisible(true);
            view.dispose(); // Đóng màn hình đăng nhập
        } else {
            JOptionPane.showMessageDialog(view, "Tài khoản hoặc mật khẩu không đúng!");
        }
    }
    
}