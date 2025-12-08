package CH.dao;

import CH.model.HoaDon;
import CH.model.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Import cần thiết cho finally block
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // 1. Lấy danh sách tất cả hóa đơn
    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT * FROM HoaDon ORDER BY MaHD DESC";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon(
                    rs.getString("MaHD"),
                    rs.getString("TenNV"),
                    rs.getString("TenKH"),
                    rs.getString("NgayLap"),
                    rs.getDouble("TongTien")
                );
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    // 2. Lấy chi tiết hóa đơn
    public List<ChiTietHoaDon> getChiTiet(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";
            ps = cons.prepareStatement(sql);
            ps.setString(1, maHD);
            rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon(
                    rs.getString("TenMon"),
                    rs.getInt("SoLuong"),
                    rs.getDouble("DonGia")
                );
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    // 3. [MỚI] Thêm hóa đơn
    public boolean add(HoaDon hd) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "INSERT INTO HoaDon(MaHD, TenNV, TenKH, NgayLap, TongTien) VALUES (?, ?, ?, ?, ?)";
            ps = cons.prepareStatement(sql);
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getTenNV());
            ps.setString(3, hd.getTenKH());
            ps.setString(4, hd.getNgayLap());
            ps.setDouble(5, hd.getTongTien());
            
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 4. [MỚI] Sửa hóa đơn
    public boolean update(HoaDon hd) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "UPDATE HoaDon SET TenNV=?, TenKH=?, NgayLap=? WHERE MaHD=?";
            ps = cons.prepareStatement(sql);
            ps.setString(1, hd.getTenNV());
            ps.setString(2, hd.getTenKH());
            ps.setString(3, hd.getNgayLap());
            ps.setString(4, hd.getMaHD());
            
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 5. [MỚI] Xóa hóa đơn
    public boolean delete(String maHD) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "DELETE FROM HoaDon WHERE MaHD=?";
            ps = cons.prepareStatement(sql);
            ps.setString(1, maHD);
            
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 6. Thêm dữ liệu mẫu (Giữ nguyên, nhưng đóng kết nối an toàn hơn)
    public void addSampleDataIfEmpty() {
        Connection cons = null;
        try {
            cons = DBConnection.getConnection();
            ResultSet rs = cons.createStatement().executeQuery("SELECT COUNT(*) FROM HoaDon");
            if (rs.next() && rs.getInt(1) == 0) {
                // Thêm Hóa đơn 1
                cons.createStatement().executeUpdate("INSERT INTO HoaDon VALUES ('HD001', 'Nguyễn Văn A', 'Trần Thị B', '01/12/2025', 130000)");
                cons.createStatement().executeUpdate("INSERT INTO ChiTietHoaDon(MaHD, TenMon, SoLuong, DonGia) VALUES ('HD001', 'Gà rán', 2, 35000)");
                cons.createStatement().executeUpdate("INSERT INTO ChiTietHoaDon(MaHD, TenMon, SoLuong, DonGia) VALUES ('HD001', 'Khoai tây chiên', 1, 60000)");
                
                // Thêm Hóa đơn 2
                cons.createStatement().executeUpdate("INSERT INTO HoaDon VALUES ('HD002', 'Lê Văn C', 'Khách vãng lai', '02/12/2025', 20000)");
                cons.createStatement().executeUpdate("INSERT INTO ChiTietHoaDon(MaHD, TenMon, SoLuong, DonGia) VALUES ('HD002', 'Pepsi', 2, 10000)");
                
                System.out.println("Đã thêm dữ liệu mẫu cho Hóa đơn.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    // 7. [MỚI - TỐI ƯU DASHBOARD] Lấy tổng số hóa đơn
    public int countAll() {
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM HoaDon";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return 0;
    }

    // 8. [MỚI - TỐI ƯU DASHBOARD] Tính tổng doanh thu toàn bộ
    public double sumAllTongTien() {
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT SUM(TongTien) FROM HoaDon";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return 0;
    }

    // 9. Lấy ID mới
    public String getNewID() {
        String newID = "HD001";
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT MaHD FROM HoaDon ORDER BY MaHD DESC LIMIT 1";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                String lastID = rs.getString("MaHD"); // Ví dụ: HD009
                if (lastID.length() >= 4) {
                    String prefix = lastID.substring(0, 2); 
                    String numberPart = lastID.substring(2); 
                    
                    try {
                        int number = Integer.parseInt(numberPart);
                        number++;
                        newID = prefix + String.format("%03d", number); // -> "HD010"
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi parse mã cũ: " + lastID);
                        newID = "HD" + System.currentTimeMillis(); // Fallback an toàn
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cons != null) cons.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return newID;
    }
}