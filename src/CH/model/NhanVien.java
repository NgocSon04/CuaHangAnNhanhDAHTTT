/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CH.model;

/**
 *
 * @author Ngoc Son
 */
public class NhanVien {
    private String maNV;
    private String tenNV;
    private String ngaySinh;
    private String gioiTinh;
    private String chucVu;
    private String soDienThoai;
    private String diaChi;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String ngaySinh, String gioiTinh, String chucVu, String soDienThoai, String diaChi) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public String getMaNV() { 
        return maNV; 
    }
    public void setMaNV(String maNV) { 
        this.maNV = maNV; 
    }
    public String getTenNV() { 
        return tenNV; 
    }
    public void setTenNV(String tenNV) { 
        this.tenNV = tenNV; 
    }
    public String getNgaySinh() { 
        return ngaySinh; 
    }
    public void setNgaySinh(String ngaySinh) { 
        this.ngaySinh = ngaySinh; 
    }
    public String getGioiTinh() { 
        return gioiTinh; 
    }
    public void setGioiTinh(String gioiTinh) { 
        this.gioiTinh = gioiTinh; 
    }
    public String getChucVu() { 
        return chucVu; 
    }
    public void setChucVu(String chucVu) { 
        this.chucVu = chucVu; 
    }
    public String getSoDienThoai() { 
        return soDienThoai; 
    }
    public void setSoDienThoai(String soDienThoai) { 
        this.soDienThoai = soDienThoai; 
    }
    public String getDiaChi() { 
        return diaChi; 
    }
    public void setDiaChi(String diaChi) { 
        this.diaChi = diaChi; 
    }
    
    public Object[] toObjectArray() {
        return new Object[]{maNV, tenNV, ngaySinh, gioiTinh, chucVu, soDienThoai, diaChi};
    }
}
