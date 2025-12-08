package CH.controller;

import CH.dao.KhachHangDAO;
import CH.model.KhachHang;
import CH.view.KhachHangView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Pattern;

public class KhachHangController {
    
    private KhachHangView view;
    private KhachHangDAO khachHangDAO;

    public KhachHangController(KhachHangView view) {
        this.view = view;
        this.khachHangDAO = new KhachHangDAO();

        loadDataToView();

        view.addThemListener(new AddListener());
        view.addSuaListener(new EditListener());
        view.addXoaListener(new DeleteListener());
        view.addResetListener(e -> {
            view.clearForm();
            KhachHang kh = view.getKhachHangInfo();
            if(kh != null) kh.setMaKH("Tự động sinh"); 
        });
        
        view.addTableSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getSelectedRow();
                if (row >= 0) {
                    try {
                        KhachHang kh = new KhachHang(
                            view.getTable().getValueAt(row, 0).toString(),
                            view.getTable().getValueAt(row, 1).toString(),
                            view.getTable().getValueAt(row, 2).toString(), // TheLoai
                            view.getTable().getValueAt(row, 3).toString(),
                            view.getTable().getValueAt(row, 4).toString(),
                            view.getTable().getValueAt(row, 5).toString(),
                            view.getTable().getValueAt(row, 6).toString()
                        );
                        view.fillForm(kh);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadDataToView() {
        view.clearTable();
        List<KhachHang> list = khachHangDAO.getAll(); 
        for (KhachHang kh : list) {
            view.addRowToTable(kh);
        }
    }

    // [QUAN TRỌNG] Validate cho cấu trúc mới
    private boolean validateForm(KhachHang kh) {
        if (kh.getTenKH().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên khách hàng không được để trống!");
            return false;
        }
        
        // [MỚI] Validate thể loại
        if (kh.getTheLoai() == null || kh.getTheLoai().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn loại khách hàng (VIP/Vãng lai)!");
            return false;
        }

        if (kh.getSoDienThoai().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Số điện thoại không được để trống!");
            return false;
        }

        String phoneRegex = "^0\\d{9}$";
        if (!Pattern.matches(phoneRegex, kh.getSoDienThoai())) {
            JOptionPane.showMessageDialog(view, "Số điện thoại không hợp lệ!");
            return false;
        }

        return true; 
    }

    // --- INNER CLASSES (Giữ nguyên logic gọi hàm) ---
    class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            KhachHang kh = view.getKhachHangInfo();
            if (!validateForm(kh)) return; 
            
            String newID = khachHangDAO.getNewID();
            kh.setMaKH(newID);

            if (khachHangDAO.add(kh)) {
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
                loadDataToView();
                view.clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm thất bại!");
            }
        }
    }

    class EditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(view, "Chọn khách hàng cần sửa!");
                return;
            }
            KhachHang kh = view.getKhachHangInfo();
            if (!validateForm(kh)) return;

            if (khachHangDAO.update(kh)) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                loadDataToView();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
            }
        }
    }

    class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.getSelectedRow();
            if (row >= 0) {
                String maKH = view.getTable().getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(view, "Xóa khách hàng " + maKH + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (khachHangDAO.delete(maKH)) {
                        JOptionPane.showMessageDialog(view, "Xóa thành công!");
                        loadDataToView();
                        view.clearForm();
                    } else {
                        JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng để xóa!");
            }
        }
    }
}