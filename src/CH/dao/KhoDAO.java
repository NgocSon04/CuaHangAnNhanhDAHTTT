package CH.dao;

import CH.model.Kho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KhoDAO {
    // Lấy tất cả danh sách hàng hóa
    public ArrayList<Kho> getAll() {
        ArrayList<Kho> danhSachKho = new ArrayList<>();
        Connection cons = DBConnection.getConnection();
        String sql = "SELECT * FROM Kho";

        try (PreparedStatement ps = cons.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Kho item = new Kho();
                item.setMaHH(rs.getString("MaHH"));
                item.setTenHH(rs.getString("TenHH"));
                item.setSoLuong(rs.getInt("SoLuong"));
                item.setGiaNhap(rs.getDouble("GiaNhap"));
                item.setGiaBan(rs.getDouble("GiaBan"));
                danhSachKho.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cons != null) cons.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return danhSachKho;
    }
    
    // Thêm một mặt hàng mới vào kho
    public boolean add(Kho item) {
        Connection cons = DBConnection.getConnection();
        String sql = "INSERT INTO Kho (MaHH, TenHH, SoLuong, GiaNhap, GiaBan) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setString(1, item.getMaHH());
            ps.setString(2, item.getTenHH());
            ps.setInt(3, item.getSoLuong());
            ps.setDouble(4, item.getGiaNhap());
            ps.setDouble(5, item.getGiaBan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (cons != null) cons.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public boolean update(Kho item) {
        Connection cons = DBConnection.getConnection();
        String sql = "UPDATE Kho SET TenHH=?, SoLuong=?, GiaNhap=?, GiaBan=? WHERE MaHH=?";
        try {
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, item.getTenHH());
            ps.setInt(2, item.getSoLuong());
            ps.setDouble(3, item.getGiaNhap());
            ps.setDouble(4, item.getGiaBan());
            ps.setString(5, item.getMaHH()); 
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(String maHH) {
        Connection cons = DBConnection.getConnection();
        String sql = "DELETE FROM Kho WHERE MaHH=?";
        try {
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, maHH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}