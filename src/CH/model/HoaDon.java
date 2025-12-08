package CH.model;

public class HoaDon {
    private String maHD;
    private String tenNV; // Tên nhân viên lập
    private String tenKH; // Tên khách hàng
    private String ngayLap;
    private double tongTien;

    public HoaDon() { }

    public HoaDon(String maHD, String tenNV, String tenKH, String ngayLap, double tongTien) {
        this.maHD = maHD;
        this.tenNV = tenNV;
        this.tenKH = tenKH;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
    }

    // --- PHẦN GETTER (Lấy dữ liệu) ---
    public String getMaHD() { return maHD; }
    public String getTenNV() { return tenNV; }
    public String getTenKH() { return tenKH; }
    public String getNgayLap() { return ngayLap; }
    public double getTongTien() { return tongTien; }

    // --- PHẦN SETTER (Sửa dữ liệu - Đã sửa lỗi) ---
    
    // Đã sửa lỗi ở hàm này
    public void setMaHD(String maHD) {
        this.maHD = maHD; 
    }

    // Nên thêm các hàm set này để tránh lỗi tương tự sau này
    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    // Hiển thị ra bảng
    public Object[] toObjectArray() {
        return new Object[]{maHD, tenNV, tenKH, ngayLap, String.format("%,.0f VNĐ", tongTien)};
    }
}