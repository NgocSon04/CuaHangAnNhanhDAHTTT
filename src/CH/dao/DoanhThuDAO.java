package CH.dao;

import CH.model.ThongKeDoanhThu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoanhThuDAO {

    public ArrayList<ThongKeDoanhThu> getDoanhThuTheoKhoangThoiGian(String tuNgay, String denNgay) {
        ArrayList<ThongKeDoanhThu> list = new ArrayList<>();
        Connection cons = DBConnection.getConnection();
        
        // SQL: Lấy ngày lập hóa đơn (hoặc chỉ ngày), tổng tiền của hóa đơn
        String sql = "SELECT NgayLap, SUM(TongTien) AS TongDoanhThu "
                    + "FROM HoaDon "
                    + "WHERE STR_TO_DATE(NgayLap, '%d/%m/%Y') BETWEEN ? AND ?"
                    + "GROUP BY NgayLap "
                    + "ORDER BY STR_TO_DATE(NgayLap, '%d/%m/%Y') ASC";
        
        try {
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, tuNgay);
            ps.setString(2, denNgay);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String ngay = rs.getString("NgayLap");
                double tongDoanhThu = rs.getDouble("TongDoanhThu");
                
                // Giả sử chúng ta sẽ tạo một Model tạm thời (ThongKeDoanhThu) để chứa kết quả này
                // (Bạn cần tạo class ThongKeDoanhThu trong CH.model. Nhưng hiện tại ta dùng tạm Object[] hoặc tạo sau)
                
                // Tạm thời, tôi sẽ trả về một đối tượng chứa thông tin thống kê:
                ThongKeDoanhThu tk = new ThongKeDoanhThu(ngay, tongDoanhThu);
                list.add(tk);
            }
            cons.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}