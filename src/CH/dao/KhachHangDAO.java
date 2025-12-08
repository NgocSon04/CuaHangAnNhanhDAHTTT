package CH.dao;

import CH.model.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    // 1. Lấy danh sách khách hàng
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT * FROM KhachHang";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setTheLoai(rs.getString("TheLoai")); 
                kh.setGioiTinh(rs.getString("GioiTinh"));
                kh.setEmail(rs.getString("Email"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDiaChi(rs.getString("DiaChi"));
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(cons, ps, rs);
        }
        return list;
    }

    // 2. Thêm khách hàng
    public boolean add(KhachHang kh) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "INSERT INTO KhachHang(MaKH, TenKH, TheLoai, GioiTinh, Email, SoDienThoai, DiaChi) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = cons.prepareStatement(sql);
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getTheLoai());
            ps.setString(4, kh.getGioiTinh());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getSoDienThoai());
            ps.setString(7, kh.getDiaChi());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(cons, ps, null);
        }
    }

    // 3. Cập nhật khách hàng
    public boolean update(KhachHang kh) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "UPDATE KhachHang SET TenKH=?, TheLoai=?, GioiTinh=?, Email=?, SoDienThoai=?, DiaChi=? WHERE MaKH=?";
            ps = cons.prepareStatement(sql);
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getTheLoai());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getSoDienThoai());
            ps.setString(6, kh.getDiaChi());
            ps.setString(7, kh.getMaKH());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(cons, ps, null);
        }
    }

    // 4. Xóa khách hàng
    public boolean delete(String maKH) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "DELETE FROM KhachHang WHERE MaKH=?";
            ps = cons.prepareStatement(sql);
            ps.setString(1, maKH);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(cons, ps, null);
        }
    }

    // 5. Sinh mã tự động
    public String getNewID() {
        String newID = "KH001"; 
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT MaKH FROM KhachHang ORDER BY MaKH DESC LIMIT 1";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String lastID = rs.getString("MaKH");
                if (lastID.length() >= 3) {
                    String prefix = lastID.substring(0, 2);
                    String numberPart = lastID.substring(2);
                    try {
                        int number = Integer.parseInt(numberPart);
                        number++;
                        newID = prefix + String.format("%03d", number);
                    } catch (NumberFormatException e) {
                        newID = "KH" + System.currentTimeMillis();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(cons, ps, rs);
        }
        return newID;
    }

    // 6. [MỚI - QUAN TRỌNG] Đếm tổng số khách hàng cho Trang Chủ
    public int countAll() {
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM KhachHang";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(cons, ps, rs);
        }
        return 0;
    }
    
    // Hàm phụ trợ để đóng kết nối gọn gàng
    private void closeResources(Connection c, PreparedStatement p, ResultSet r) {
        try { if (r != null) r.close(); } catch (SQLException e) {}
        try { if (p != null) p.close(); } catch (SQLException e) {}
        try { if (c != null) c.close(); } catch (SQLException e) {}
    }
}