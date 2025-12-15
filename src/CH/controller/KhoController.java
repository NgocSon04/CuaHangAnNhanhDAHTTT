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
    private ThucDonController thucDonController;

    public KhoController(KhoView view) {
        this.view = view;
        this.dao = new KhoDAO();
        loadDataToView();

        this.view.getBtnThem().addActionListener(new AddListener());
        this.view.getBtnSua().addActionListener(new UpdateListener());
        this.view.getBtnXoa().addActionListener(new DeleteListener());
        
        this.view.getBtnReset().addActionListener(e -> clearForm());
        this.view.getKhoTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillDataFromTableToTextFields();
            }
        });
    }
    public void setThucDonController(ThucDonController thucDonController){
        this.thucDonController = thucDonController;
    }
    private void updateThucDonSide() {
        if (thucDonController != null) {
            thucDonController.loadMaHHToComboBox();
        }
    }

    // Tải dữ liệu từ DAO và hiển thị lên bảng
    public void loadDataToView() {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
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
            // Chỉ lấy 3 cột: Mã, Tên, Số Lượng
            view.getTxtMaHH().setText(table.getValueAt(row, 0).toString());
            view.getTxtTenHH().setText(table.getValueAt(row, 1).toString());
            view.getTxtSoLuong().setText(table.getValueAt(row, 2).toString());
            
            view.getTxtMaHH().setEditable(false);
        }
    }
    
    private void clearForm() {
        view.getTxtMaHH().setText("");
        view.getTxtTenHH().setText("");
        view.getTxtSoLuong().setText("");
        view.getTxtMaHH().setEditable(true);
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
        
        // Constructor mới chỉ có 3 tham số
        return new Kho(ma, ten, sl);
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
                    updateThucDonSide();
                } else {
                    JOptionPane.showMessageDialog(view, "Thêm thất bại. Có thể trùng Mã HH!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                // Sửa thông báo lỗi
                JOptionPane.showMessageDialog(view, "Lỗi: Số lượng phải là số nguyên!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
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
                if (view.getTxtMaHH().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Vui lòng chọn hàng hóa cần sửa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Kho k = getModelFromForm();
                if (dao.update(k)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                    loadDataToView();
                    clearForm();
                    updateThucDonSide();
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: Số lượng phải là số nguyên!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
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
                    updateThucDonSide();
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}