package CH.controller;

import CH.dao.*;
import CH.model.*;
import CH.view.TrangChuView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class TrangChuController {
    private TrangChuView view;
    
    // Cần gọi các DAO để lấy số liệu
    private HoaDonDAO hoaDonDAO;
    private KhachHangDAO khachHangDAO;
    private ThucDonDAO thucDonDAO;

    public TrangChuController(TrangChuView view) {
        this.view = view;
        this.hoaDonDAO = new HoaDonDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.thucDonDAO = new ThucDonDAO();

        // Tải dữ liệu ngay khi khởi động
        loadStatistics();

        // Gán sự kiện cho nút làm mới
        view.getBtnLamMoi().addActionListener(e -> loadStatistics());
    }

    private void loadStatistics() {
        try {
            // 1. Đếm số hóa đơn & Tính tổng doanh thu
            double tongDoanhThu = hoaDonDAO.sumAllTongTien();
               int soHD = hoaDonDAO.countAll();

            // 2. Đếm số khách hàng
            int soKH = khachHangDAO.countAll();

            // 3. Đếm số món ăn
            int soMon = thucDonDAO.countAll();

            DecimalFormat df = new DecimalFormat("#,###");
            
            view.getLblTongDoanhThu().setText(df.format(tongDoanhThu) + " VNĐ");
            view.getLblSoHoaDon().setText(String.valueOf(soHD));
            view.getLblSoKhachHang().setText(String.valueOf(soKH));
            view.getLblSoMonAn().setText(String.valueOf(soMon));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu thống kê: " + e.getMessage());
        }
    }
}