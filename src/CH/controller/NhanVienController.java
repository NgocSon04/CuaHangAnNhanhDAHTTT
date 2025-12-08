package CH.controller;

import CH.dao.NhanVienDAO;
import CH.model.NhanVien;
import CH.view.NhanVienView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Pattern;    // Import Regex

public class NhanVienController {
    
    private NhanVienView view;
    private NhanVienDAO nhanVienDAO;

    public NhanVienController(NhanVienView view) {
        this.view = view;
        this.nhanVienDAO = new NhanVienDAO();

        // Load dữ liệu ban đầu
        loadDataToView();

        // Gán sự kiện cho các nút
        view.addThemListener(new AddListener());
        view.addSuaListener(new EditListener());
        view.addXoaListener(new DeleteListener());
        view.addResetListener(e -> {
            view.clearForm();
            view.getNhanVienInfo().setMaNV("Tự động sinh"); 
        });
        
        // Sự kiện click vào bảng
        view.addTableSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getSelectedRow();
                if (row >= 0) {
                    // Lấy dữ liệu từ dòng được chọn
                    try {
                        NhanVien nv = new NhanVien(
                            view.getTable().getValueAt(row, 0).toString(),
                            view.getTable().getValueAt(row, 1).toString(),
                            view.getTable().getValueAt(row, 2).toString(),
                            view.getTable().getValueAt(row, 3).toString(),
                            view.getTable().getValueAt(row, 4).toString(),
                            view.getTable().getValueAt(row, 5).toString(),
                            view.getTable().getValueAt(row, 6).toString()
                        );
                        view.fillForm(nv);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    } // --- KẾT THÚC CONSTRUCTOR TẠI ĐÂY ---

    // --- CÁC PHƯƠNG THỨC KHÁC PHẢI NẰM NGOÀI CONSTRUCTOR ---

    private void loadDataToView() {
        view.clearTable();
        List<NhanVien> list = nhanVienDAO.getAll();
        for (NhanVien nv : list) {
            view.addRowToTable(nv);
        }
    }

    private boolean validateForm(NhanVien nv) {
        // 1. Kiểm tra rỗng
        if (nv.getTenNV().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên nhân viên không được để trống!");
            return false;
        }
        if (nv.getNgaySinh() == null || nv.getNgaySinh().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn ngày sinh!");
            return false;
        }
        if (nv.getSoDienThoai().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Số điện thoại không được để trống!");
            return false;
        }

        // 2. Validate Số điện thoại (Phải là số, 10 ký tự, bắt đầu bằng số 0)
        String phoneRegex = "^0\\d{9}$";
        if (!Pattern.matches(phoneRegex, nv.getSoDienThoai())) {
            JOptionPane.showMessageDialog(view, "Số điện thoại không hợp lệ (Phải là 10 số, bắt đầu bằng 0)!");
            return false;
        }

//        // 3. Validate Tuổi (Phải đủ 18 tuổi)
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            Date birthDate = sdf.parse(nv.getNgaySinh());
//            Date now = new Date();
//
//            // Tính tuổi sơ bộ (lấy năm hiện tại - năm sinh)
//            long ageInMillis = now.getTime() - birthDate.getTime();
//            long years = ageInMillis / (1000L * 60 * 60 * 24 * 365);
//
//            if (years < 18) {
//                JOptionPane.showMessageDialog(view, "Nhân viên phải đủ 18 tuổi!");
//                return false;
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(view, "Định dạng ngày sinh không hợp lệ!");
//            return false;
//        }

        return true; 
    }

    // --- INNER CLASSES (LISTENERS) ---

    class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NhanVien nv = view.getNhanVienInfo();

            // Validate trước khi xử lý
            if (!validateForm(nv)) {
                return; 
            }

            // Tự động sinh mã
            String newID = nhanVienDAO.getNewID();
            nv.setMaNV(newID);

            if (nhanVienDAO.add(nv)) {
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
                loadDataToView();
                view.clearForm();
                // Vì hàm clearForm reset mã, ta cần set lại text hiển thị
                // Tuy nhiên ở View chúng ta đã set "Tự động sinh" khi clearForm rồi nên có thể bỏ qua
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
                JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên để sửa!");
                return;
            }

            NhanVien nv = view.getNhanVienInfo();
            
            // Validate khi sửa
            if (!validateForm(nv)) {
                return;
            }

            if (nhanVienDAO.update(nv)) {
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
                String maNV = view.getTable().getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa nhân viên " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (nhanVienDAO.delete(maNV)) {
                        JOptionPane.showMessageDialog(view, "Xóa thành công!");
                        loadDataToView();
                        view.clearForm();
                    } else {
                        JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên để xóa!");
            }
        }
    }
}