package CH.model;

import java.text.DecimalFormat;

public class Kho {
    private String maHH;    
    private String tenHH;   
    private int soLuong;    
    private double giaNhap; 
    private double giaBan;

    public Kho() {
    }

    public Kho(String maHH, String tenHH, int soLuong, double giaNhap, double giaBan) {
        this.maHH = maHH;
        this.tenHH = tenHH;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
    }

    // --- Getters ---
    public String getMaHH() { return maHH; }
    public String getTenHH() { return tenHH; }
    public int getSoLuong() { return soLuong; }
    public double getGiaNhap() { return giaNhap; }
    public double getGiaBan() { return giaBan; }

    // --- Setters ---
    public void setMaHH(String maHH) { this.maHH = maHH; }
    public void setTenHH(String tenHH) { this.tenHH = tenHH; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    // Dùng để hiển thị lên JTable
    public Object[] toObjectArray() {
        DecimalFormat df = new DecimalFormat("#,###");
        return new Object[]{
            maHH,
            tenHH,
            soLuong,
            df.format(giaNhap) + " VNĐ",
            df.format(giaBan) + " VNĐ"
        };
    }
}