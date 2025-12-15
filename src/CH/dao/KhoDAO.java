package CH.dao;

import CH.model.Kho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhoDAO {
    // 1. Lấy tất cả
    public ArrayList<Kho> getAll() {
        ArrayList<Kho> danhSachKho = new ArrayList<>();
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT * FROM Kho");
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Kho item = new Kho(); // Dùng constructor rỗng để tránh lỗi
                item.setMaHH(rs.getString("MaHH"));
                item.setTenHH(rs.getString("TenHH"));
                item.setSoLuong(rs.getInt("SoLuong"));
                danhSachKho.add(item);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return danhSachKho;
    }

    // 2. Cập nhật (SỬA LỖI Ở ĐÂY: Câu lệnh SQL chỉ được update 2 trường Tên và Số lượng)
    public boolean update(Kho item) {
        // [QUAN TRỌNG] Kiểm tra xem câu lệnh SQL của bạn có giống hệt dòng dưới không
        String sql = "UPDATE Kho SET TenHH=?, SoLuong=? WHERE MaHH=?";
        
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, item.getTenHH());
            ps.setInt(2, item.getSoLuong());
            ps.setString(3, item.getMaHH()); // MaHH là điều kiện WHERE (tham số thứ 3)
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Nhìn vào Console để xem lỗi nếu có
            return false;
        }
    }

    // 3. Thêm mới
    public boolean add(Kho item) {
        String sql = "INSERT INTO Kho (MaHH, TenHH, SoLuong) VALUES (?, ?, ?)";
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, item.getMaHH());
            ps.setString(2, item.getTenHH());
            ps.setInt(3, item.getSoLuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 4. Xóa
    public boolean delete(String maHH) {
        String sql = "DELETE FROM Kho WHERE MaHH=?";
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setString(1, maHH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 5. Các hàm hỗ trợ khác (Giữ nguyên cho Thực đơn dùng)
    public List<String> getAllMaHH() {
        List<String> maHHList = new ArrayList<>();
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT MaHH FROM Kho ORDER BY MaHH");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) maHHList.add(rs.getString("MaHH"));
        } catch (SQLException e) { e.printStackTrace(); }
        return maHHList;
    }
    
    public int laySoLuongTon(String maHH) {
        int soLuong = 0;
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT SoLuong FROM Kho WHERE MaHH = ?")) {
            ps.setString(1, maHH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) soLuong = rs.getInt("SoLuong");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return soLuong;
    }
    
    public void truKho(Connection conn, String maHH, int soLuongMua) throws Exception {
        String sql = "UPDATE Kho SET SoLuong = SoLuong - ? WHERE MaHH = ?"; 
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongMua);
            ps.setString(2, maHH);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Lỗi trừ kho mã " + maHH, e); 
        }
    }
}