package CH.controller;

import CH.dao.NhanVienDAO;
import CH.model.NhanVien;
import CH.view.NhanVienView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NhanVienController {
    
    private NhanVienView view;
    private NhanVienDAO nhanVienDAO;

    public NhanVienController(NhanVienView view) {
        this.view = view;
        this.nhanVienDAO = new NhanVienDAO();

        // 1. Load dữ liệu ban đầu
        loadDataToView();

        // 2. Gán sự kiện cho các nút
        view.addThemListener(new AddListener());
        view.addSuaListener(new EditListener());
        view.addXoaListener(new DeleteListener());
        
        view.addResetListener(e -> {
            view.clearForm();
            view.setMaNV("Tự động sinh"); 
        });
        view.addLiveSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { xuLyTimKiem(); }
            @Override
            public void removeUpdate(DocumentEvent e) { xuLyTimKiem(); }
            @Override
            public void changedUpdate(DocumentEvent e) { xuLyTimKiem(); }
        });
        
        // 3. Sự kiện click vào bảng (SỬA LẠI ĐỂ TRÁNH LỖI NULL)
        view.addTableSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        // Dùng hàm getSafeValue để không bị lỗi nếu ô đó trống
                        String maNV = getSafeValue(view.getTable(), row, 0);
                        String tenNV = getSafeValue(view.getTable(), row, 1);
                        String ngaySinh = getSafeValue(view.getTable(), row, 2);
                        String gioiTinh = getSafeValue(view.getTable(), row, 3);
                        String chucVu = getSafeValue(view.getTable(), row, 4);
                        String sdt = getSafeValue(view.getTable(), row, 5);
                        String diaChi = getSafeValue(view.getTable(), row, 6);
                        String username = getSafeValue(view.getTable(), row, 7);
                        String role = getSafeValue(view.getTable(), row, 8);

                        // Tạo đối tượng nhân viên
                        // Lưu ý: Password để trống vì trên bảng không hiện
                        NhanVien nv = new NhanVien(maNV, tenNV, ngaySinh, gioiTinh, chucVu, sdt, diaChi, username, "", role);
                        
                        view.fillForm(nv);
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    // --- HÀM MỚI: Lấy dữ liệu an toàn từ bảng ---
    private String getSafeValue(JTable table, int row, int col) {
        Object val = table.getValueAt(row, col);
        return val != null ? val.toString() : "";
    }

    // --- LOAD DATA ---
    private void loadDataToView() {
        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        model.setRowCount(0); 

        List<NhanVien> list = nhanVienDAO.getAll();

        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getNgaySinh(),
                nv.getGioiTinh(),
                nv.getChucVu(),
                nv.getSoDienThoai(),
                nv.getDiaChi(),
                nv.getUsername(), 
                nv.getRole()     
            });
        }
    }

    // --- VALIDATE ---
    private boolean validateForm(NhanVien nv) {
        if (nv.getTenNV().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên nhân viên không được để trống!");
            return false;
        }
        if (nv.getNgaySinh() == null || nv.getNgaySinh().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn ngày sinh!");
            return false;
        }
        String phoneRegex = "^0\\d{9}$";
        if (!Pattern.matches(phoneRegex, nv.getSoDienThoai())) {
            JOptionPane.showMessageDialog(view, "Số điện thoại phải là 10 số và bắt đầu bằng 0!");
            return false;
        }
        if (nv.getUsername() == null || nv.getUsername().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Tài khoản!");
            return false;
        }
        return true; 
    }

    // --- LISTENERS ---
    class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NhanVien nv = view.getNhanVienInfo();
            if (!validateForm(nv)) return; 

            String newID = nhanVienDAO.getNewID();
            nv.setMaNV(newID);
            
            if(nv.getPassword() == null || nv.getPassword().isEmpty()) {
                nv.setPassword("123"); 
            }

            if (nhanVienDAO.add(nv)) {
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
                loadDataToView();
                view.clearForm();
                view.setMaNV("Tự động sinh");
            } else {
                JOptionPane.showMessageDialog(view, "Thêm thất bại (Có thể trùng Username)!");
            }
        }
    }

    class EditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.getTable().getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên để sửa!");
                return;
            }
            NhanVien nv = view.getNhanVienInfo();
            if (!validateForm(nv)) return;

            if (nhanVienDAO.update(nv)) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                loadDataToView();
                view.clearForm(); 
                view.setMaNV("Tự động sinh");
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
            }
        }
    }

    class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.getTable().getSelectedRow();
            if (row >= 0) {
                String maNV = view.getTable().getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(view, "Xóa nhân viên " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (nhanVienDAO.delete(maNV)) {
                        JOptionPane.showMessageDialog(view, "Xóa thành công!");
                        loadDataToView();
                        view.clearForm();
                        view.setMaNV("Tự động sinh");
                    } else {
                        JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên để xóa!");
            }
        }
    }
    private void xuLyTimKiem() {
        String keyword = view.getTuKhoaTimKiem();
        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        model.setRowCount(0);

        List<NhanVien> list;
        if (keyword.isEmpty()) {
            list = nhanVienDAO.getAll();
        } else {
            list = nhanVienDAO.search(keyword);
        }

        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getTenNV(), nv.getNgaySinh(), nv.getGioiTinh(),
                nv.getChucVu(), nv.getSoDienThoai(), nv.getDiaChi(),
                nv.getUsername(), nv.getRole()
            });
        }
        
    }
}