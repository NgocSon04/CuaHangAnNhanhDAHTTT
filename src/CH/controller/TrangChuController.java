package CH.controller;

import CH.dao.*;
import CH.view.TrangChuView;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

public class TrangChuController {
    private TrangChuView view;
    
    // Các DAO
    private HoaDonDAO hoaDonDAO;
    private KhachHangDAO khachHangDAO;
    private ThucDonDAO thucDonDAO;

    public TrangChuController(TrangChuView view) {
        this.view = view;
        this.hoaDonDAO = new HoaDonDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.thucDonDAO = new ThucDonDAO();

        // 1. Tải dữ liệu ngay khi mở
        loadStatistics();

        // 2. Gán sự kiện cho nút làm mới (Chỉ gán 1 lần duy nhất)
        this.view.getBtnLamMoi().addActionListener(e -> loadStatistics());
    }

    private void loadStatistics() {
        try {
            // --- LẤY SỐ LIỆU TỪ DB ---
            double tongDoanhThu = hoaDonDAO.sumAllTongTien();
            int soHD = hoaDonDAO.countAll();
            int soKhach = khachHangDAO.countAll();
            
            // Đếm số món ăn
            int soMon = thucDonDAO.countAll(); 

            // --- ĐỊNH DẠNG ---
            DecimalFormat df = new DecimalFormat("#,###");
            
            // --- HIỂN THỊ LÊN VIEW ---
            view.getLblTongDoanhThu().setText(df.format(tongDoanhThu) + " VNĐ");
            view.getLblSoHoaDon().setText(String.valueOf(soHD));
            view.getLblSoKhachHang().setText(String.valueOf(soKhach));
            view.getLblSoMonAn().setText(String.valueOf(soMon));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi tải thống kê: " + e.getMessage());
        }
    }
}