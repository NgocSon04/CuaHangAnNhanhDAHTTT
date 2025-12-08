package CH.model;

public class DatMon {
    private String maMon;
    private String tenMon;
    private int soLuong;
    private double donGia;

    public DatMon() {
    }

    public DatMon(String maMon, String tenMon, int soLuong, double donGia) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // --- Getters & Setters ---
    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    // Hàm tính thành tiền tiện ích
    public double getThanhTien() {
        return this.soLuong * this.donGia;
    }

    // Chuyển đổi sang mảng để hiển thị lên bảng giỏ hàng (nếu cần)
    public Object[] toObjectArray() {
        return new Object[]{tenMon, soLuong, String.format("%,.0f", donGia), String.format("%,.0f", getThanhTien())};
    }
}