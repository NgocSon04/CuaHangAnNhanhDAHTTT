/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.CH.main;

import vn.CH.controller.NhanVienController;
import vn.CH.view.MainView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vn.CH.controller.KhachHangController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            
            //1.Tao frame chinsh (Chua Sidebar + Layout)
            MainView mainView = new MainView();
            
            // 2. Lấy Panel Nhân Viên ra từ MainView
            // 3. Gắn Panel đó vào Controller để xử lý logic
            new NhanVienController(mainView.getNhanVienView());
            new KhachHangController(mainView.getKhachHangView());
            
            mainView.setVisible(true);
        });
        
    }
}
