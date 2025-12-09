package CH.controller;

import CH.dao.DoanhThuDAO;
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

public class DoanhThuController {
    private DoanhThuView view;
    private DoanhThuDAO dao;

    public DoanhThuController(DoanhThuView view) {
        this.view = view;
        this.dao = new DoanhThuDAO();

        // Gán sự kiện cho nút Xem Báo Cáo
        view.getBtnXemBaoCao().addActionListener(new ReportListener());
        
        // Tải báo cáo mặc định khi khởi động (ví dụ: cả năm)
        xemBaoCao(); 
    }
    private String convertDateFromViewToDAO(String dateString) {
        // Định dạng nhập vào từ View
        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // Định dạng cần cho DAO/SQL
        DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate date = LocalDate.parse(dateString, formatterIn);
            return date.format(formatterOut);
        } catch (DateTimeParseException e) {
            return null; // Trả về null nếu định dạng ngày tháng không hợp lệ
        }
    }
    private void xemBaoCao() {
        String tuNgay = view.getTxtTuNgay().getText();
        String denNgay = view.getTxtDenNgay().getText();

        if (tuNgay.isEmpty() || denNgay.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ Từ Ngày và Đến Ngày.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String tuNgayDAO = convertDateFromViewToDAO(tuNgay);
        String denNgayDAO = convertDateFromViewToDAO(denNgay);
        
        if (tuNgayDAO == null || denNgayDAO == null) {
            JOptionPane.showMessageDialog(view, "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng DD-MM-YYYY.", "Lỗi Định Dạng", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0); 
        
        ArrayList<ThongKeDoanhThu> list = dao.getDoanhThuTheoKhoangThoiGian(tuNgayDAO, denNgayDAO);
        double tongDoanhThu = 0;
        
        for (ThongKeDoanhThu item : list) {
            model.addRow(item.toObjectArray());
            tongDoanhThu += item.getTongDoanhThu();
        }
        
        // Hiển thị tổng doanh thu
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