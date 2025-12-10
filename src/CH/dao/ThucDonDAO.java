package CH.dao;

import CH.model.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThucDonDAO {
    // 1. Lấy tất cả món ăn
    public List<MonAn> getAll() {
        List<MonAn> list = new ArrayList<>();
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "SELECT * FROM ThucDon";
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new MonAn(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)));
            }
            ps.close();
            cons.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Thêm món ăn
    public boolean add(MonAn m) {
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "INSERT INTO ThucDon(MaMon, TenMon, DonGia, DonViTinh) VALUES(?,?,?,?)";
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, m.getMaMon());
            ps.setString(2, m.getTenMon());
            ps.setDouble(3, m.getDonGia());
            ps.setString(4, m.getDonViTinh());
            int row = ps.executeUpdate();
            ps.close();
            cons.close();
            return row > 0;
        } catch (Exception e) { return false; }
    }

    // 3. Sửa món ăn
    public boolean update(MonAn m) {
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "UPDATE ThucDon SET TenMon=?, DonGia=?, DonViTinh=? WHERE MaMon=?";
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, m.getTenMon());
            ps.setDouble(2, m.getDonGia());
            ps.setString(3, m.getDonViTinh());
            ps.setString(4, m.getMaMon());
            int row = ps.executeUpdate();
            ps.close();
            cons.close();
            return row > 0;
        } catch (Exception e) { return false; }
    }

    // 4. Xóa món ăn
    public boolean delete(String maMon) {
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "DELETE FROM ThucDon WHERE MaMon=?";
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, maMon);
            int row = ps.executeUpdate();
            ps.close();
            cons.close();
            return row > 0;
        } catch (Exception e) { return false; }
    }

    // 5. Sinh mã tự động
    public String getNewID() {
        String newID = "M01";
        try {
            Connection cons = DBConnection.getConnection();
            ResultSet rs = cons.createStatement().executeQuery("SELECT MaMon FROM ThucDon ORDER BY length(MaMon) DESC, MaMon DESC LIMIT 1");
            if (rs.next()) {
                String lastID = rs.getString(1); // M09
                int num = Integer.parseInt(lastID.substring(1)) + 1;
                newID = "M" + (num < 10 ? "0" + num : num);
            }
            cons.close();
        } catch (Exception e) {}
        return newID;
    }

    public int countAll() {
        int count = 0;
        try {
            Connection cons = DBConnection.getConnection();
            // Lưu ý: Tên bảng là ThucDon hay MonAn tùy vào lúc bạn tạo DB
            String sql = "SELECT COUNT(*) FROM ThucDon"; 
            PreparedStatement ps = cons.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            ps.close();
            cons.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}