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
            if (e.getColumn() == 1) {
                tinhLaiTienMotDong(e.getFirstRow());
            }
        });
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
                m.getDonViTinh()
            });
        }
        
        // Vẽ lại bảng cho chắc chắn
        view.getTableMenu().repaint();
    }

    private void themVaoGio() {
        int row = view.getTableMenu().getSelectedRow();
        if (row >= 0) {
            String ten = view.getTableMenu().getValueAt(row, 1).toString();
            String giaStr = view.getTableMenu().getValueAt(row, 2).toString().replace(",", "").replace(".", "");
            double gia = 0;
            try { gia = Double.parseDouble(giaStr); } catch (Exception e) {}
            
            view.addMonToGio(ten, gia);
            updateTongTien();
        } else {
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
        dialog.addXacNhanListener(e -> {
            luuHoaDonVaoDB(dialog.getTenKhach());
            dialog.dispose();
        });
        dialog.setVisible(true);
    }

    private void luuHoaDonVaoDB(String tenKhach) {
        try {
            String maHD = hoaDonDao.getNewID();
            String ngayLap = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            HoaDon hd = new HoaDon(maHD, "Admin", tenKhach, ngayLap, currentTotal);
            hoaDonDao.add(hd);

            java.sql.Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO ChiTietHoaDon(MaHD, TenMon, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            DefaultTableModel model = view.getModelGioHang();
            
            for (int i = 0; i < model.getRowCount(); i++) {
                ps.setString(1, maHD);
                ps.setString(2, model.getValueAt(i, 0).toString());
                ps.setInt(3, Integer.parseInt(model.getValueAt(i, 1).toString()));
                ps.setDouble(4, Double.parseDouble(model.getValueAt(i, 2).toString().replace(",", "").replace(".", "")));
                ps.executeUpdate();
            }
            conn.close();
            
            JOptionPane.showMessageDialog(view, "Thanh toán thành công! Mã HĐ: " + maHD);
            view.getModelGioHang().setRowCount(0);
            updateTongTien();
            
            if(hoaDonController != null) hoaDonController.loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi thanh toán!");
        }
    }

    private void tinhLaiTienMotDong(int row) {
        if (row < 0) return;
        try {
            DefaultTableModel model = view.getModelGioHang();
            int sl = Integer.parseInt(model.getValueAt(row, 1).toString());
            if (sl <= 0) { sl = 1; model.setValueAt(1, row, 1); JOptionPane.showMessageDialog(view, "Số lượng > 0!"); }
            double gia = Double.parseDouble(model.getValueAt(row, 2).toString().replace(",", "").replace(".", ""));
            model.setValueAt(String.format("%,.0f", sl * gia), row, 3);
            updateTongTien();
        } catch (Exception e) { JOptionPane.showMessageDialog(view, "Nhập số nguyên!"); }
    }
}