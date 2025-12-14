package CH.dao;

import CH.model.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "SELECT * FROM KhachHang";
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setTheLoai(rs.getString("TheLoai")); // [MỚI]
                kh.setGioiTinh(rs.getString("GioiTinh"));
                kh.setEmail(rs.getString("Email"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDiaChi(rs.getString("DiaChi"));
                list.add(kh);
            }
            ps.close();
            cons.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    

    public boolean add(KhachHang kh) {
        try {
            Connection cons = DBConnection.getConnection();
            // [MỚI] Sửa SQL Insert
            String sql = "INSERT INTO KhachHang(MaKH, TenKH, TheLoai, GioiTinh, Email, SoDienThoai, DiaChi) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getTheLoai()); // [MỚI]
            ps.setString(4, kh.getGioiTinh());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getSoDienThoai());
            ps.setString(7, kh.getDiaChi());
            int row = ps.executeUpdate();
            ps.close();
            cons.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhachHang kh) {
        try {
            Connection cons = DBConnection.getConnection();
            // [MỚI] Sửa SQL Update
            String sql = "UPDATE KhachHang SET TenKH=?, TheLoai=?, GioiTinh=?, Email=?, SoDienThoai=?, DiaChi=? WHERE MaKH=?";
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getTheLoai()); // [MỚI]
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getSoDienThoai());
            ps.setString(6, kh.getDiaChi());
            ps.setString(7, kh.getMaKH());
            int row = ps.executeUpdate();
            ps.close();
            cons.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Các hàm delete và getNewID giữ nguyên như cũ
    public boolean delete(String maKH) {
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "DELETE FROM KhachHang WHERE MaKH=?";
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, maKH);
            int row = ps.executeUpdate();
            ps.close();
            cons.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNewID() {
        String newID = "KH001"; 
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "SELECT MaKH FROM KhachHang ORDER BY MaKH DESC LIMIT 1";
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastID = rs.getString("MaKH");
                String prefix = lastID.substring(0, 2);
                String numberPart = lastID.substring(2);
                int number = Integer.parseInt(numberPart);
                number++;
                newID = prefix + String.format("%03d", number);
            }
            ps.close();
            cons.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newID;
    }

    public int countAll() {
        int count = 0;
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM KhachHang");
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
}