package CH.controller;

import CH.dao.*;
import CH.model.*;
import CH.view.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent;    
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DatMonController {
    private DatMonView view;
    private ThucDonDAO menuDao;
    private HoaDonDAO hoaDonDao; 
    private KhoDAO khoDao;
    private double currentTotal = 0;
    private HoaDonController hoaDonController;
    private KhoController khoController;
    private KhachHangDAO khachHangDAO;
    private KhachHangController khachHangController;
    
    public DatMonController(DatMonView view, HoaDonController hoaDonController) {
        this.view = view;
        this.hoaDonController = hoaDonController;
        this.menuDao = new ThucDonDAO();
        this.hoaDonDao = new HoaDonDAO();
        this.khoDao = new KhoDAO();
        this.khachHangDAO = new KhachHangDAO();
        
        // Gọi hàm load menu ngay khi mở
        loadMenu();

        // 1. Sự kiện thêm vào giỏ
        view.addThemListener(e -> themVaoGio());
        
        // 2. Sự kiện click đúp
        view.getTableMenu().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    themVaoGio(); 
                }
            }
        });

        // Các sự kiện khác
        view.addXoaListener(e -> xoaKhoiGio());
        view.addThanhToanListener(e -> moPopupThanhToan());
        
        view.getModelGioHang().addTableModelListener(e -> {
            if (e.getColumn() == 2) {
                tinhLaiTienMotDong(e.getFirstRow());
            }
        });
    }
    public void setKhoController(KhoController khoController) {
        this.khoController = khoController;
    }


    public void loadMenu() {

        DefaultTableModel model = (DefaultTableModel) view.getTableMenu().getModel();

        model.setRowCount(0);

        List<MonAn> list = menuDao.getAll();
        for (MonAn m : list) {
            model.addRow(new Object[]{
                m.getMaMon(),
                m.getTenMon(),
                String.format("%,.0f", m.getDonGia()), 
                m.getDonViTinh(),
                m.getMaHH()
            });
        }
        
        view.getTableMenu().repaint();
    }

    private void themVaoGio() {
        int row = view.getTableMenu().getSelectedRow();
        if (row >= 0) {
            String maMon = view.getTableMenu().getValueAt(row, 0).toString(); // [MỚI] Lấy Mã Món
            String ten = view.getTableMenu().getValueAt(row, 1).toString();
            String giaStr = view.getTableMenu().getValueAt(row, 2).toString().replace(",", "").replace(".", "");
            Object maHHObj = view.getTableMenu().getValueAt(row, 4);
            String maHH = (maHHObj != null) ? maHHObj.toString() : "";

            if (maHH.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Món này chưa được liên kết với Kho (Mã HH trống)!", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double gia = 0;
            try { gia = Double.parseDouble(giaStr); } catch (Exception e) {}
            
            // --- KIỂM TRA KHO ---
            int tonKho = khoDao.laySoLuongTon(maHH);
            int tongDangDung = layTongSoLuongDangDungTrongGio(maHH);
            
            if(tonKho <= 0) {
                JOptionPane.showMessageDialog(view, "Món này đã hết nguyên liệu trong kho!", "Hết hàng", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(tongDangDung + 1 > tonKho) {
                JOptionPane.showMessageDialog(view, "Kho chỉ còn " + tonKho + " suất. Không thể đặt thêm!", "Không đủ hàng", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // --------------------
            
            DefaultTableModel modelGio = view.getModelGioHang();
            boolean daCo = false;

            for (int i = 0; i < modelGio.getRowCount(); i++) {
                String maMonTrongGio = modelGio.getValueAt(i, 0).toString(); // Cột 0 là Mã Món
                
                if (maMonTrongGio.equals(maMon)) {
                    // Nếu đã có: Tăng số lượng lên 1
                    int slCu = Integer.parseInt(modelGio.getValueAt(i, 2).toString());
                    int slMoi = slCu + 1;
                    
                    modelGio.setValueAt(slMoi, i, 2); 
                    modelGio.setValueAt(String.format("%,.0f", slMoi * gia), i, 4); // Cập nhật Thành tiền
                    
                    daCo = true;
                    break;
                }
            }
            if (!daCo) {
                view.addMonToGio(maMon, maHH, ten, gia); 
            }

            updateTongTien();
        } else {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn món từ thực đơn!");
        }
    }
    private int layTongSoLuongDangDungTrongGio(String maHHCanKiemTra) {
        int tongSL = 0;
        DefaultTableModel model = view.getModelGioHang();
        for(int i=0; i<model.getRowCount(); i++) {
            // Cột 5 là Mã HH (theo cấu trúc mới trong View)
            String maHHTrongGio = model.getValueAt(i, 5).toString();
            if(maHHTrongGio.equals(maHHCanKiemTra)) {
                tongSL += Integer.parseInt(model.getValueAt(i, 2).toString());
            }
        }
        return tongSL;
    }

    private void xoaKhoiGio() {
        int row = view.getTableGioHang().getSelectedRow();
        if (row >= 0) {
            ((DefaultTableModel)view.getTableGioHang().getModel()).removeRow(row);
            updateTongTien();
        } else {
            JOptionPane.showMessageDialog(view, "Chọn món trong giỏ để xóa!");
        }
    }

    private void updateTongTien() {
        currentTotal = 0;
        DefaultTableModel model = view.getModelGioHang();
        for (int i = 0; i < model.getRowCount(); i++) {
            String tienStr = model.getValueAt(i, 4).toString().replace(",", "").replace(".", "");
            currentTotal += Double.parseDouble(tienStr);
        }
        view.setTongTien(currentTotal);
    }

    private void moPopupThanhToan() {
        if (view.getModelGioHang().getRowCount() == 0) { 
            JOptionPane.showMessageDialog(view, "Giỏ hàng đang trống!");
            return;
        }
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        XacNhanThanhToanDialog dialog = new XacNhanThanhToanDialog(parent, view.getModelGioHang(), currentTotal);
        
        dialog.addXacNhanListener(e -> {
            String tenKhach = dialog.getTenKhach();
            String sdt = dialog.getSDT();
            
            if (tenKhach.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên khách hàng!");
                return;
            }
            
            // Gọi hàm lưu với đầy đủ thông tin
            luuHoaDonVaoDB(tenKhach, sdt); 
            dialog.dispose();
        });
        dialog.setVisible(true);
    }

    private void luuHoaDonVaoDB(String tenKhach, String sdt) {
        java.sql.Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // =================================================================
            // 1. LOGIC TỰ ĐỘNG LƯU KHÁCH HÀNG
            // =================================================================
            String maKH = null; 
            
            if (!sdt.isEmpty()) {
                // A. Kiểm tra xem SĐT này đã có chưa
                String sqlCheck = "SELECT MaKH, TenKH FROM KhachHang WHERE SoDienThoai = ?";
                PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                psCheck.setString(1, sdt);
                java.sql.ResultSet rsCheck = psCheck.executeQuery();
                
                if (rsCheck.next()) {
                    // Đã tồn tại -> Lấy MaKH cũ
                    maKH = rsCheck.getString("MaKH");
                    // (Tùy chọn: Có thể cập nhật lại tên khách nếu muốn)
                } else {
                    // Chưa tồn tại -> Thêm mới vào bảng KhachHang
                    
                    // Sinh mã KH mới (Gọi DAO hoặc tự sinh trong transaction)
                    // Ở đây gọi DAO để lấy ID mới nhất cho an toàn
                    String newMaKH = new KhachHangDAO().getNewID(); 
                    
                    String sqlInsertKH = "INSERT INTO KhachHang(MaKH, TenKH, TheLoai, GioiTinh, Email, SoDienThoai, DiaChi) " +
                                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement psKH = conn.prepareStatement(sqlInsertKH);
                    psKH.setString(1, newMaKH);
                    psKH.setString(2, tenKhach);
                    psKH.setString(3, "Vãng lai"); // Mặc định
                    psKH.setString(4, "Khác");     // Mặc định
                    psKH.setString(5, "");         // Email trống
                    psKH.setString(6, sdt);
                    psKH.setString(7, "");         // Địa chỉ trống
                    
                    psKH.executeUpdate();
                    maKH = newMaKH;
                }
            }

            // =================================================================
            // 2. LƯU HÓA ĐƠN (Giữ nguyên logic cũ)
            // =================================================================
            String maHD = hoaDonDao.getNewID();
            String ngayLap = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            
            // Lưu ý: Nếu bảng HoaDon của bạn chưa có cột MaKH, bạn vẫn lưu TenKH như cũ.
            // Nếu bảng HoaDon đã có cột MaKH, bạn hãy setString cho cột đó bằng biến maKH ở trên.
            
            HoaDon hd = new HoaDon(maHD, "Admin", tenKhach, ngayLap, currentTotal);
            hoaDonDao.add(hd);

            // =================================================================
            // 3. LƯU CHI TIẾT & TRỪ KHO (Giữ nguyên logic cũ)
            // =================================================================
            String sqlChiTiet = "INSERT INTO ChiTietHoaDon(MaHD, TenMon, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
            PreparedStatement psChiTiet = conn.prepareStatement(sqlChiTiet);
            DefaultTableModel model = view.getModelGioHang();
            
            for (int i = 0; i < model.getRowCount(); i++) {
                String tenMon = model.getValueAt(i, 1).toString();
                int soLuong = Integer.parseInt(model.getValueAt(i, 2).toString());
                double donGia = Double.parseDouble(model.getValueAt(i, 3).toString().replace(",", "").replace(".", ""));
                String maHH = model.getValueAt(i, 5).toString();

                psChiTiet.setString(1, maHD);
                psChiTiet.setString(2, tenMon);
                psChiTiet.setInt(3, soLuong);
                psChiTiet.setDouble(4, donGia);
                psChiTiet.executeUpdate();

                khoDao.truKho(conn, maHH, soLuong);
            }
            
            conn.commit(); // Xác nhận Transaction thành công
            
            JOptionPane.showMessageDialog(view, "Thanh toán thành công! Mã HĐ: " + maHD + 
                    (maKH != null ? "\nĐã liên kết khách hàng: " + tenKhach : ""));
            
            // Reset giao diện
            view.getModelGioHang().setRowCount(0);
            updateTongTien();
            
            // Cập nhật các màn hình khác (Real-time update)
            if(khoController != null) khoController.loadDataToView();
            if(hoaDonController != null) hoaDonController.loadData();
            if(khachHangController != null) {
                khachHangController.loadDataToView(); 
            }


        } catch (Exception ex) {
            try { if(conn != null) conn.rollback(); } catch(Exception e) {}
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi thanh toán: " + ex.getMessage());
        } finally {
            try { if(conn != null) { conn.setAutoCommit(true); conn.close(); } } catch(Exception e) {}
        }
    }


    private void tinhLaiTienMotDong(int row) {
        if (row < 0) return;
        try {
            DefaultTableModel model = view.getModelGioHang();
            int sl = Integer.parseInt(model.getValueAt(row, 2).toString());
            
            String maHH = model.getValueAt(row, 5).toString(); 
            
            int tonKho = khoDao.laySoLuongTon(maHH);
            
            // Tính tổng SL các món KHÁC (không tính dòng hiện tại) cũng dùng mã HH này
            int slMonKhacCungLoai = 0;
            for(int i=0; i<model.getRowCount(); i++) {
                if(i != row && model.getValueAt(i, 5).toString().equals(maHH)) {
                    slMonKhacCungLoai += Integer.parseInt(model.getValueAt(i, 2).toString());
                }
            }

            if (sl + slMonKhacCungLoai > tonKho) {
                int maxSl = tonKho - slMonKhacCungLoai;
                JOptionPane.showMessageDialog(view, "Kho chỉ còn " + tonKho + " đơn vị. Các món khác đã chiếm " + slMonKhacCungLoai + ". Bạn chỉ có thể đặt tối đa: " + maxSl);
                model.setValueAt(maxSl > 0 ? maxSl : 1, row, 2);
                sl = (maxSl > 0 ? maxSl : 1);
            }
            
            if (sl <= 0) { sl = 1; model.setValueAt(1, row, 2); }
            
            double gia = Double.parseDouble(model.getValueAt(row, 3).toString().replace(",", "").replace(".", ""));
            model.setValueAt(String.format("%,.0f", sl * gia), row, 4);
            updateTongTien();
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(view, "Nhập số nguyên!"); 
        }
    }
    public void setKhachHangController(KhachHangController controller) {
        this.khachHangController = controller;
    }
}