package CH.controller;

import CH.dao.KhoDAO;
import CH.dao.ThucDonDAO;
import CH.model.MonAn;
import CH.view.ThucDonView;
import javax.swing.*;

public class ThucDonController {
    private ThucDonView view;
    private ThucDonDAO dao;
    private KhoDAO khoDao;
    private DatMonController datMonController;

    public ThucDonController(ThucDonView view, DatMonController datMonController) {
        this.view = view;
        this.dao = new ThucDonDAO();
        this.khoDao = new KhoDAO();
        
        this.datMonController = datMonController;
        loadMaHHToComboBox();
        loadData();

        view.addThemListener(e -> {
            MonAn m = view.getMonAnInfo();
            if(m.getTenMon().isEmpty()) { JOptionPane.showMessageDialog(view, "Nhập tên món!"); 
            return; 
            }
            if(m.getMaHH() == null || m.getMaHH().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn Mã Hàng Hóa từ kho!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            m.setMaMon(dao.getNewID()); 
            if(dao.add(m)) { reload(); JOptionPane.showMessageDialog(view, "Thêm thành công!"); }
            else { JOptionPane.showMessageDialog(view, "Thêm thất bại (Có thể lỗi Database).", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        view.addSuaListener(e -> {
            if(view.getSelectedRow() < 0) return;
            if(view.getMonAnInfo().getMaHH() == null || view.getMonAnInfo().getMaHH().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn Mã Hàng Hóa từ kho!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(dao.update(view.getMonAnInfo())) { reload(); JOptionPane.showMessageDialog(view, "Sửa thành công!"); }
            else { JOptionPane.showMessageDialog(view, "Sửa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        view.addXoaListener(e -> {
            if(view.getSelectedRow() < 0) return;
            String ma = view.getMonAnInfo().getMaMon();
            if(JOptionPane.showConfirmDialog(view, "Xóa " + ma + "?") == JOptionPane.YES_OPTION) {
                if(dao.delete(ma)) { reload(); JOptionPane.showMessageDialog(view, "Xóa thành công!"); }
            }
        });
        
        view.addResetListener(e -> view.clearForm());

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting() && view.getSelectedRow() >= 0) {
                int r = view.getSelectedRow();
                try{
                    String ma = view.getTable().getValueAt(r, 0).toString();
                    String ten = view.getTable().getValueAt(r, 1).toString();
                    String giaStr = view.getTable().getValueAt(r, 2).toString().replace(",", "");
                    String dvt = view.getTable().getValueAt(r, 3).toString();
                    Object imgObj = view.getTable().getValueAt(r, 4);
                    String hinhAnh = (imgObj != null) ? imgObj.toString(): "";
                    view.fillForm(new MonAn(ma, ten, Double.parseDouble(giaStr), dvt, hinhAnh, null));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    public void loadMaHHToComboBox() {
        view.getCboMaHH().removeAllItems();
        try {
            for (String maHH : khoDao.getAllMaHH()) {
                view.getCboMaHH().addItem(maHH);
            }
            view.getCboMaHH().setSelectedIndex(-1); // Không chọn gì mặc định
        } catch (Exception e) {
             JOptionPane.showMessageDialog(view, "Lỗi tải Mã Hàng Hóa từ Kho!", "Lỗi DB", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }

    private void loadData() {
        view.clearTable();
        for(MonAn m : dao.getAll()) view.addRow(m);
    }
    private void reload() { 
        loadData();          
        view.clearForm();    

        if (datMonController != null) {
            datMonController.loadMenu();
        }
    }
}