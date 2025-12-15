package CH.main;

import CH.controller.*;
import CH.view.*;
import CH.dao.DBConnection;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1. Khởi tạo Database nếu chưa có
        DBConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) { e.printStackTrace(); }

            LoginForm loginForm = new LoginForm();

            
            loginForm.setLoginListener(role -> {

                if (role == null) {
                    JOptionPane.showMessageDialog(null, "Sai username hoặc password!");
                    return;
                }

                // ===============================================================
                //                            ADMIN
                // ===============================================================
                if (role.equals("ADMIN")) {

                    MainView mainView = new MainView();
                    mainView.setRole("ADMIN");

                    // 1. Các Controller độc lập
                    new NhanVienController(mainView.getNhanVienView());
                    new KhachHangController(mainView.getKhachHangView());
                    new TrangChuController(mainView.getTrangChuView()); 

                    // 2. Controller Hóa đơn
                    HoaDonController hoaDonCtrl = new HoaDonController(mainView.getHoaDonView());

                    // 3. Controller Đặt món (Cần HoaDonCtrl)
                    DatMonController datMonCtrl = new DatMonController(mainView.getDatMonView(), hoaDonCtrl);

                    // 4. KHỞI TẠO THỰC ĐƠN TRƯỚC (Để lấy tham chiếu truyền vào Kho)
                    ThucDonController thucDonCtrl = new ThucDonController(mainView.getThucDonView(), datMonCtrl);

                    // 5. KHỞI TẠO KHO VÀ LIÊN KẾT
                    KhoController khoCtrl = new KhoController(mainView.getKhoView());
                    khoCtrl.setThucDonController(thucDonCtrl); 
                    
                    datMonCtrl.setKhoController(khoCtrl);

                    mainView.setVisible(true);
                    loginForm.dispose();
                }

                // ===============================================================
                //                          STAFF (NHÂN VIÊN)
                // ===============================================================
                else if (role.equals("NHÂN VIÊN")) {

                    MainView mainView = new MainView();
                    mainView.setRole("NHÂN VIÊN");

                    // STAFF không có NhanVienController (đã ẩn menu)
                    new KhachHangController(mainView.getKhachHangView());

                    // Controller Hóa đơn
                    HoaDonController hoaDonCtrl = new HoaDonController(mainView.getHoaDonView());

                   
                    DatMonController datMonCtrl = new DatMonController(mainView.getDatMonView(), hoaDonCtrl);

                    
                    new ThucDonController(mainView.getThucDonView(), datMonCtrl);

                    mainView.setVisible(true);
                    loginForm.dispose();
                }
            });

            loginForm.setVisible(true);
        });
    }
}