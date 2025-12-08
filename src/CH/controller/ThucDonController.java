package CH.controller;

import CH.dao.ThucDonDAO;
import CH.model.MonAn;
import CH.view.ThucDonView;
import javax.swing.*;

public class ThucDonController {
    private ThucDonView view;
    private ThucDonDAO dao;

    public ThucDonController(ThucDonView view) {
        this.view = view;
        this.dao = new ThucDonDAO();
        loadData();

        view.addThemListener(e -> {
            MonAn m = view.getMonAnInfo();
            if(m.getTenMon().isEmpty()) { JOptionPane.showMessageDialog(view, "Nhập tên món!"); 
            return; 
            }
            m.setMaMon(dao.getNewID()); 
            if(dao.add(m)) { reload(); JOptionPane.showMessageDialog(view, "Thêm thành công!"); }
        });

        view.addSuaListener(e -> {
            if(view.getSelectedRow() < 0) return;
            if(dao.update(view.getMonAnInfo())) { reload(); JOptionPane.showMessageDialog(view, "Sửa thành công!"); }
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
                String ma = view.getTable().getValueAt(r, 0).toString();
                String ten = view.getTable().getValueAt(r, 1).toString();
                String giaStr = view.getTable().getValueAt(r, 2).toString().replace(",", "");
                String dvt = view.getTable().getValueAt(r, 3).toString();
                view.fillForm(new MonAn(ma, ten, Double.parseDouble(giaStr), dvt));
            }
        });
    }

    private void loadData() {
        view.clearTable();
        for(MonAn m : dao.getAll()) view.addRow(m);
    }
    private void reload() { loadData(); view.clearForm(); }
}