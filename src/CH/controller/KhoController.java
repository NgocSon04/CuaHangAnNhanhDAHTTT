package CH.controller;

import CH.dao.KhoDAO;
import CH.model.Kho;
import CH.view.KhoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class KhoController {
    private KhoView view;
    private KhoDAO dao;

    public KhoController(KhoView view) {
        this.view = view;
        this.dao = new KhoDAO();

        // 1. Load dữ liệu ban đầu
        loadDataToView();

        // 2. Gán sự kiện cho các nút chức năng
        this.view.getBtnThem().addActionListener(new AddListener());
        this.view.getBtnSua().addActionListener(new UpdateListener());
        this.view.getBtnXoa().addActionListener(new DeleteListener());
        
        // Sự kiện tìm kiếm (Dùng Lambda expression cho gọn)
        this.view.getBtnTimKiem().addActionListener(e -> searchData());
        
        this.view.getBtnReset().addActionListener(e -> clearForm());
        // 3. QUAN TRỌNG: Sự kiện Click chuột vào bảng (Để lấy dữ liệu sửa/xóa)
        this.view.getKhoTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillDataFromTableToTextFields();
            }
        });
    }

    // Tải dữ liệu từ DAO và hiển thị lên bảng
    public void loadDataToView() {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        ArrayList<Kho> list = dao.getAll();
        for (Kho item : list) {
            model.addRow(item.toObjectArray());
        }
    }

    // Hàm lấy dữ liệu từ dòng được chọn đổ lên ô nhập liệu
    private void fillDataFromTableToTextFields() {
        JTable table = view.getKhoTable();
        int row = table.getSelectedRow();
        if (row >= 0) {
            view.getTxtMaHH().setText(table.getValueAt(row, 0).toString());
            view.getTxtTenHH().setText(table.getValueAt(row, 1).toString());
            view.getTxtSoLuong().setText(table.getValueAt(row, 2).toString());
            
            // Xử lý chuỗi tiền tệ (Bỏ chữ " VNĐ", dấu phẩy, dấu chấm để lấy số thô)
            String giaNhapStr = table.getValueAt(row, 3).toString()
                                     .replace(" VNĐ", "").replace(",", "").replace(".", "").trim();
            String giaBanStr = table.getValueAt(row, 4).toString()
                                     .replace(" VNĐ", "").replace(",", "").replace(".", "").trim();
            
            view.getTxtGiaNhap().setText(giaNhapStr);
            view.getTxtGiaBan().setText(giaBanStr);
            
            // Khóa ô Mã HH khi chọn (vì Mã không được sửa)
            view.getTxtMaHH().setEditable(false);
        }
    }
    
    // Nút Reset (để làm trắng ô nhập liệu khi cần thêm mới)
    private void clearForm() {
        view.getTxtMaHH().setText("");
        view.getTxtTenHH().setText("");
        view.getTxtSoLuong().setText("");
        view.getTxtGiaNhap().setText("");
        view.getTxtGiaBan().setText("");
        view.getTxtMaHH().setEditable(true); // Mở lại khóa mã cho phép nhập mới
    }

    // Hàm phụ trợ: Lấy dữ liệu từ Form -> tạo đối tượng Kho
    private Kho getModelFromForm() throws Exception {
        String ma = view.getTxtMaHH().getText().trim();
        String ten = view.getTxtTenHH().getText().trim();
        
        // Kiểm tra rỗng
        if (ma.isEmpty() || ten.isEmpty()) {
            throw new Exception("Mã và Tên hàng hóa không được để trống!");
        }

        int sl = Integer.parseInt(view.getTxtSoLuong().getText().trim());
        double gn = Double.parseDouble(view.getTxtGiaNhap().getText().trim().replace(",", ""));
        double gb = Double.parseDouble(view.getTxtGiaBan().getText().trim().replace(",", ""));
        
        return new Kho(ma, ten, sl, gn, gb);
    }
    
    // Hàm tìm kiếm đơn giản
    private void searchData() {
        String keyword = view.getTxtTimKiem().getText().trim().toLowerCase();
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        ArrayList<Kho> list = dao.getAll();
        for (Kho k : list) {
            // Tìm theo Mã hoặc Tên
            if (k.getTenHH().toLowerCase().contains(keyword) || k.getMaHH().toLowerCase().contains(keyword)) {
                 model.addRow(k.toObjectArray());
            }
        }
    }

    // --- CÁC CLASS XỬ LÝ SỰ KIỆN (INNER CLASSES) ---

    // 1. Class xử lý nút THÊM
    private class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Kho k = getModelFromForm();
                if (dao.add(k)) {
                    JOptionPane.showMessageDialog(view, "Thêm hàng hóa thành công!");
                    loadDataToView();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Thêm thất bại. Có thể trùng Mã HH!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: Số lượng và Giá phải là số!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 2. Class xử lý nút SỬA
    private class UpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Kiểm tra xem người dùng đã chọn hàng chưa
                if (view.getTxtMaHH().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Vui lòng chọn hàng hóa cần sửa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Kho k = getModelFromForm();
                if (dao.update(k)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                    loadDataToView();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: Số lượng và Giá phải là số!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 3. Class xử lý nút XÓA
    private class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String maHH = view.getTxtMaHH().getText().trim();
            
            if (maHH.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn hàng hóa cần xóa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(view, 
                    "Bạn có chắc muốn xóa mã hàng hóa: " + maHH + "?", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.delete(maHH)) {
                    JOptionPane.showMessageDialog(view, "Xóa thành công!");
                    loadDataToView();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}