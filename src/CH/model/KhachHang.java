package CH.model;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String theLoai;
    private String gioiTinh;
    private String email;
    private String soDienThoai;
    private String diaChi;

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String theLoai, String gioiTinh, String email, String soDienThoai, String diaChi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.theLoai = theLoai;
        this.gioiTinh = gioiTinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    // [MỚI] Getter/Setter cho TheLoai
    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    // Chuyển đổi sang Object[] để đổ vào JTable (Cột 2 là Thể loại)
    public Object[] toObjectArray() {
        return new Object[]{maKH, tenKH, theLoai, gioiTinh, email, soDienThoai, diaChi};
    }
}