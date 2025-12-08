package CH.controller;

import CH.dao.*;
import CH.model.*;
import CH.view.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent;   
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DatMonController {
    private DatMonView view;
    private ThucDonDAO menuDao;
    private HoaDonDAO hoaDonDao; 
    private double currentTotal = 0;
    private HoaDonController hoaDonController;
    
    
    public DatMonController(DatMonView view, HoaDonController hoaDonController) {
        this.view = view;
        this.hoaDonController = hoaDonController;
        this.menuDao = new ThucDonDAO();
        this.hoaDonDao = new HoaDonDAO();
        
        loadMenu();

        view.addThemListener(e -> themVaoGio());
        
        // kiện Click đúp chuột vào bảng Menu -> Tự động thêm vào giỏ
        view.getTableMenu().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Nếu click 2 lần liên tiếp
                    themVaoGio(); // Gọi hàm thêm món
                }
            }
        });

        // Các sự kiện khác giữ nguyên
        view.addXoaListener(e -> xoaKhoiGio());
        view.addThanhToanListener(e -> moPopupThanhToan());
        view.getModelGioHang().addTableModelListener(e -> {
            // Chỉ xử lý khi cột bị thay đổi là cột Số Lượng (index = 1)
            if (e.getColumn() == 1) {
                int row = e.getFirstRow();
                tinhLaiTienMotDong(row);
            }
        });
    }

    private void loadMenu() {
        List<MonAn> list = menuDao.getAll();
        for (MonAn m : list) view.addMonToMenu(m);
    }

    private void themVaoGio() {
        int row = view.getTableMenu().getSelectedRow();
        if (row >= 0) {
            String ten = view.getTableMenu().getValueAt(row, 1).toString();
            // Xử lý giá tiền (bỏ dấu phẩy, chấm để tính toán)
            String giaStr = view.getTableMenu().getValueAt(row, 2).toString().replace(",", "").replace(".", "");
            double gia = 0;
            try {
                gia = Double.parseDouble(giaStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            view.addMonToGio(ten, gia);
            updateTongTien();
        } else {
            // Chỉ hiện thông báo nếu bấm nút mà chưa chọn dòng nào
            JOptionPane.showMessageDialog(view, "Vui lòng chọn món từ thực đơn!");
        }
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
            // Lấy cột thành tiền (index 3)
            String tienStr = model.getValueAt(i, 3).toString().replace(",", "").replace(".", "");
            currentTotal += Double.parseDouble(tienStr);
        }
        view.setTongTien(currentTotal);
    }

    private void moPopupThanhToan() {
        if (currentTotal == 0) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng đang trống!");
            return;
        }

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        XacNhanThanhToanDialog dialog = new XacNhanThanhToanDialog(parent, view.getModelGioHang(), currentTotal);
        
        dialog.addXacNhanListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                luuHoaDonVaoDB(dialog.getTenKhach());
                dialog.dispose();
            }
        });
        
        dialog.setVisible(true);
    }

    private void luuHoaDonVaoDB(String tenKhach) {
        try {
            // 1. Lưu Hóa Đơn
            String maHD = hoaDonDao.getNewID();
            String ngayLap = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            
            HoaDon hd = new HoaDon(maHD, "Admin", tenKhach, ngayLap, currentTotal);
            hoaDonDao.add(hd);

            // 2. Lưu Chi Tiết
            java.sql.Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO ChiTietHoaDon(MaHD, TenMon, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);

            DefaultTableModel model = view.getModelGioHang();
            for (int i = 0; i < model.getRowCount(); i++) {
                String tenMon = model.getValueAt(i, 0).toString();
                int sl = Integer.parseInt(model.getValueAt(i, 1).toString());
                double donGia = Double.parseDouble(model.getValueAt(i, 2).toString().replace(",", "").replace(".", ""));
                
                ps.setString(1, maHD);
                ps.setString(2, tenMon);
                ps.setInt(3, sl);
                ps.setDouble(4, donGia);
                ps.executeUpdate();
            }
            conn.close();
            
            JOptionPane.showMessageDialog(view, "Thanh toán thành công! Mã HĐ: " + maHD);
            view.getModelGioHang().setRowCount(0); // Xóa trắng giỏ
            updateTongTien();
            
            if(hoaDonController != null){
                hoaDonController.loadData();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi thanh toán!");
        }
    }
    // Hàm tính lại tiền khi số lượng thay đổi
    private void tinhLaiTienMotDong(int row) {
        if (row < 0) return;
        try {
            DefaultTableModel model = view.getModelGioHang();
            
            // 1. Lấy số lượng mới vừa nhập
            Object slObj = model.getValueAt(row, 1);
            int soLuongMoi = Integer.parseInt(slObj.toString());
            
            // Kiểm tra nếu nhập số âm hoặc 0 -> Reset về 1 hoặc xóa (ở đây mình reset về 1)
            if (soLuongMoi <= 0) {
                soLuongMoi = 1;
                model.setValueAt(1, row, 1); // Cập nhật lại số 1 lên bảng
                JOptionPane.showMessageDialog(view, "Số lượng phải lớn hơn 0!");
            }

            // 2. Lấy đơn giá (Xử lý chuỗi 15,000 -> 15000)
            String giaStr = model.getValueAt(row, 2).toString().replace(",", "").replace(".", "");
            double donGia = Double.parseDouble(giaStr);

            // 3. Tính thành tiền mới
            double thanhTienMoi = soLuongMoi * donGia;
            
            // 4. Cập nhật cột Thành tiền (index 3)
            // Lưu ý: Việc setValuAt này sẽ kích hoạt lại Listener -> Có thể gây lặp vô tận
            // Nhưng vì ở trên ta có check `if (e.getColumn() == 1)` nên update cột 3 sẽ không sao.
            model.setValueAt(String.format("%,.0f", thanhTienMoi), row, 3);
            
            // 5. Cập nhật Tổng tiền bên dưới
            updateTongTien();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Vui lòng chỉ nhập số nguyên!");
            // Reset lại dòng đó nếu nhập sai
            // (Bạn có thể thêm logic load lại giá cũ ở đây nếu muốn kỹ hơn)
        }
    }
}