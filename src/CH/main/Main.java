package CH.main;

import CH.controller.*;
import CH.view.MainView;
import CH.dao.DBConnection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        
        DBConnection.initializeDatabase();
        
        SwingUtilities.invokeLater(()->{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e){ e.printStackTrace(); }
            
            MainView mainView = new MainView();
            
            new TrangChuController(mainView.getTrangChuView());
            
            new NhanVienController(mainView.getNhanVienView());
            new KhachHangController(mainView.getKhachHangView());
            
            // Khởi tạo HoaDonController trước và lưu vào biến
            HoaDonController hoaDonCtrl = new HoaDonController(mainView.getHoaDonView());
            
            // Truyền biến hoaDonCtrl vào DatMonController
            new DatMonController(mainView.getDatMonView(), hoaDonCtrl);
            
            new ThucDonController(mainView.getThucDonView());  
            
            new KhoController(mainView.getKhoView());
            new DoanhThuController(mainView.getDoanhThuView());
            
            mainView.setVisible(true);
        });
    }
}