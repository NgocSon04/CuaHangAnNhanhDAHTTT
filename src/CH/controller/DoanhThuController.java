package CH.controller;

import CH.dao.HoaDonDAO;
import CH.model.ThongKeDoanhThu;
import CH.view.DoanhThuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class DoanhThuController {
    private DoanhThuView view;
    private HoaDonDAO dao;

    public DoanhThuController(DoanhThuView view) {
        this.view = view;
        this.dao = new HoaDonDAO();

        view.getBtnXemBaoCao().addActionListener(new ReportListener());
        
        xemBaoCao(); 
    }
    private void xemBaoCao() {
        String tuNgay = view.getTxtTuNgay().getText().trim();
        String denNgay = view.getTxtDenNgay().getText().trim();

        if (tuNgay.isEmpty() || denNgay.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ Từ Ngày và Đến Ngày.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Pattern datePattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        if (!datePattern.matcher(tuNgay).matches() || !datePattern.matcher(denNgay).matches()) {
            JOptionPane.showMessageDialog(view, "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng DD/MM/YYYY.", "Lỗi Định Dạng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0); 
        
        List<ThongKeDoanhThu> list = dao.getDoanhThuTheoNgay(tuNgay, denNgay);
        double tongDoanhThu = 0;
        
        for (ThongKeDoanhThu item : list) {
            model.addRow(item.toObjectArray());
            tongDoanhThu += item.getTongDoanhThu();
        }
        
        DecimalFormat df = new DecimalFormat("#,###"); 
        view.getLblTongDoanhThu().setText("TỔNG DOANH THU: " + df.format(tongDoanhThu) + " VNĐ");
    }

    class ReportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            xemBaoCao();
        }
    }
}