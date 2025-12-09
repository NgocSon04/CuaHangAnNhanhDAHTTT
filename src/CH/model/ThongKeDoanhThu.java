package CH.model;

import java.text.DecimalFormat;

public class ThongKeDoanhThu {
    private String thoiGian; // Ngày, Tháng, Năm
    private double tongDoanhThu;

    public ThongKeDoanhThu(String thoiGian, double tongDoanhThu) {
        this.thoiGian = thoiGian;
        this.tongDoanhThu = tongDoanhThu;
    }

    // Dùng để hiển thị lên JTable
    public Object[] toObjectArray() {
        DecimalFormat df = new DecimalFormat("#,###"); 
        return new Object[]{
            thoiGian,
            df.format(tongDoanhThu) + " VNĐ"
        };
    }

    // Getters & Setters
    public String getThoiGian() { return thoiGian; }
    public double getTongDoanhThu() { return tongDoanhThu; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }
    public void setTongDoanhThu(double tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }
}