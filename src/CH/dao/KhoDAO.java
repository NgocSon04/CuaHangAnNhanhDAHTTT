package CH.dao;

import CH.model.Kho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List<String> getAllMaHH() {
        List<String> maHHList = new ArrayList<>();
        Connection cons = DBConnection.getConnection();
        String sql = "SELECT MaHH FROM Kho ORDER BY MaHH";
        try (PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                maHHList.add(rs.getString("MaHH"));
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
        return maHHList;
    }
    
    // Hàm lấy số lượng tồn kho theo MaHH (Mã hàng hóa)
    public int laySoLuongTon(String maHH) {
        int soLuong = 0;
        Connection conn = DBConnection.getConnection();
        // [FIXED] Sử dụng MaHH thay vì MaMon
        String sql = "SELECT SoLuong FROM Kho WHERE MaHH = ?"; 
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    soLuong = rs.getInt("SoLuong");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return soLuong;
    }
    
    // Hàm trừ kho: Nhận vào Connection để dùng Transaction
    public void truKho(Connection conn, String maHH, int soLuongMua) throws Exception {
        // [FIXED] Sử dụng MaHH thay vì MaMon
        String sql = "UPDATE Kho SET SoLuong = SoLuong - ? WHERE MaHH = ?"; 
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongMua);
            ps.setString(2, maHH);
            ps.executeUpdate();
        } catch (Exception e) {
            // Ném exception để Controller bắt và rollback
            throw new Exception("Lỗi trừ kho cho mã " + maHH, e); 
        }
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