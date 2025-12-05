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

    // Getters
    public String getMaHD() { return maHD; }
    public String getTenNV() { return tenNV; }
    public String getTenKH() { return tenKH; }
    public String getNgayLap() { return ngayLap; }
    public double getTongTien() { return tongTien; }

    public Object[] toObjectArray() {
        return new Object[]{maHD, tenNV, tenKH, ngayLap, String.format("%,.0f VNĐ", tongTien)};
    }

    public void setMaHD(String newID) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}