package CH.main;

import CH.controller.*;
import CH.view.*;
import CH.dao.DBConnection;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

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

                // ============================
                //            ADMIN
                // ============================
                if (role.equals("ADMIN")) {

                    MainView mainView = new MainView();
                    mainView.setRole("ADMIN");

                    new NhanVienController(mainView.getNhanVienView());
                    new KhachHangController(mainView.getKhachHangView());

                    HoaDonController hoaDonCtrl =
                            new HoaDonController(mainView.getHoaDonView());

                    new DatMonController(mainView.getDatMonView(), hoaDonCtrl);

                    new ThucDonController(mainView.getThucDonView());

                    mainView.setVisible(true);
                    loginForm.dispose();
                }

                // ============================
                //            STAFF
                // ============================
                else if (role.equals("NHÂN VIÊN")) {

                    MainView mainView = new MainView();
                    mainView.setRole("NHÂN VIÊN"); // <- quan trọng!

                    // STAFF không có NhanVienController
                    new KhachHangController(mainView.getKhachHangView());

                    HoaDonController hoaDonCtrl =
                            new HoaDonController(mainView.getHoaDonView());

                    new DatMonController(mainView.getDatMonView(), hoaDonCtrl);

                    new ThucDonController(mainView.getThucDonView());

                    mainView.setVisible(true);
                    loginForm.dispose();
                }
            });

            loginForm.setVisible(true);
        });
    }
}
