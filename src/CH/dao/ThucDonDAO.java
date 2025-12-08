package CH.dao;

import CH.model.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThucDonDAO {
    
    // 1. Lấy tất cả món ăn
    public List<MonAn> getAll() {
        List<MonAn> list = new ArrayList<>();
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cons = DBConnection.getConnection(); // Gọi static method
            
            if (cons == null) return list;

            String sql = "SELECT * FROM ThucDon";
            ps = cons.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new MonAn(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(cons, ps, rs);
        }
        return list;
    }

    // 2. Thêm món ăn
    public boolean add(MonAn m) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "INSERT INTO ThucDon(MaMon, TenMon, DonGia, DonViTinh) VALUES(?,?,?,?)";
            ps = cons.prepareStatement(sql);
            ps.setString(1, m.getMaMon());
            ps.setString(2, m.getTenMon());
            ps.setDouble(3, m.getDonGia());
            ps.setString(4, m.getDonViTinh());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(cons, ps, null);
        }
    }

    // 3. Sửa món ăn
    public boolean update(MonAn m) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "UPDATE ThucDon SET TenMon=?, DonGia=?, DonViTinh=? WHERE MaMon=?";
            ps = cons.prepareStatement(sql);
            ps.setString(1, m.getTenMon());
            ps.setDouble(2, m.getDonGia());
            ps.setString(3, m.getDonViTinh());
            ps.setString(4, m.getMaMon());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(cons, ps, null);
        }
    }

    // 4. Xóa món ăn
    public boolean delete(String maMon) {
        Connection cons = null;
        PreparedStatement ps = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "DELETE FROM ThucDon WHERE MaMon=?";
            ps = cons.prepareStatement(sql);
            ps.setString(1, maMon);
            
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
        String newID = "M01";
        Connection cons = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            st = cons.createStatement();
            rs = st.executeQuery("SELECT MaMon FROM ThucDon ORDER BY length(MaMon) DESC, MaMon DESC LIMIT 1");
            
            if (rs.next()) {
                String lastID = rs.getString(1); // Ví dụ: M09
                if (lastID.length() >= 2) {
                    try {
                        int num = Integer.parseInt(lastID.substring(1)) + 1;
                        newID = "M" + (num < 10 ? "0" + num : num);
                    } catch (NumberFormatException e) {
                        newID = "M" + System.currentTimeMillis();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(cons, st, rs); // Lưu ý: st là Statement, nhưng có thể truyền vào tham số PreparedStatement vì kế thừa
        }
        return newID;
    }
    
    // 6. [MỚI - QUAN TRỌNG] Đếm tổng số món ăn cho Trang Chủ
    public int countAll() {
        Connection cons = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cons = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM ThucDon";
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

    // Hàm phụ trợ đóng kết nối gọn gàng
    private void closeResources(Connection c, Statement p, ResultSet r) {
        try { if (r != null) r.close(); } catch (SQLException e) {}
        try { if (p != null) p.close(); } catch (SQLException e) {}
        try { if (c != null) c.close(); } catch (SQLException e) {}
    }
}