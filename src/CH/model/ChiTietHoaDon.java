package CH.model;

public class ChiTietHoaDon {
    private String tenMon;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    public ChiTietHoaDon(String tenMon, int soLuong, double donGia) {
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }

    public Object[] toObjectArray() {
        return new Object[]{
            tenMon, 
            soLuong, 
            String.format("%,.0f", donGia), 
            String.format("%,.0f", thanhTien)
        };
    }
}