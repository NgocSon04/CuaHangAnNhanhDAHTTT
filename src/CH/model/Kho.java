package CH.model;

public class Kho {
    private String maHH;
    private String tenHH;
    private int soLuong;

    public Kho() {
    }

    // [QUAN TRỌNG] Controller đang gọi cái này, nếu thiếu sẽ báo lỗi biên dịch
    public Kho(String maHH, String tenHH, int soLuong) {
        this.maHH = maHH;
        this.tenHH = tenHH;
        this.soLuong = soLuong;
    }

    public String getMaHH() { return maHH; }
    public void setMaHH(String maHH) { this.maHH = maHH; }

    public String getTenHH() { return tenHH; }
    public void setTenHH(String tenHH) { this.tenHH = tenHH; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    // Hiển thị lên bảng 3 cột
    public Object[] toObjectArray() {
        return new Object[]{maHH, tenHH, soLuong};
    }
}