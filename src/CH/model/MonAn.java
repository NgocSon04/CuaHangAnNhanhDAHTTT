package CH.model;

public class MonAn {
    private String maMon;
    private String tenMon;
    private double donGia;
    private String donViTinh;
    private String maHH;    
    private String hinhAnh;

    public MonAn() {
    }

    // Constructor ĐẦY ĐỦ cả MaHH và HinhAnh
    public MonAn(String maMon, String tenMon, double donGia, String donViTinh, String maHH, String hinhAnh) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.maHH = maHH;
        this.hinhAnh = hinhAnh;
    }

    // --- GETTERS VÀ SETTERS ---
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public String getMaHH() { return maHH; }
    public void setMaHH(String maHH) { this.maHH = maHH; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    // Chuyển đổi sang mảng Object để hiển thị lên JTable (Hiển thị cả MaHH cho dễ quản lý)
    public Object[] toObjectArray() {
        return new Object[]{maMon, tenMon, String.format("%,.0f", donGia), donViTinh, maHH, hinhAnh};
    }
}