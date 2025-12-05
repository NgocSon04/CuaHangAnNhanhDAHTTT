package CH.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    // =======================================================
    // 1. LOGIN: Kiểm tra trong bảng NhanVien
    // =======================================================
    public String login(String username, String password) {
        String role = null;
        try {
            Connection cons = DBConnection.getConnection();
            
            // Truy vấn vào các cột Username, Password của bảng NhanVien
            String sql = "SELECT Role FROM NhanVien WHERE Username=? AND Password=?";
            
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Lấy quyền (ADMIN hoặc NHÂN VIÊN)
                role = rs.getString("Role");
            }

            ps.close();
            cons.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    // =======================================================
    // 2. TẠO DỮ LIỆU MẪU CHO BẢNG NHÂN VIÊN
    // =======================================================
    public void insertDefaultUsersIfEmpty() {
        try {
            Connection cons = DBConnection.getConnection();

            // Kiểm tra xem bảng NhanVien có dữ liệu chưa
            ResultSet rs = cons.createStatement().executeQuery("SELECT COUNT(*) FROM NhanVien");
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                // Vì bảng NhanVien có nhiều cột NOT NULL (MaNV, TenNV...) 
                // nên ta phải insert đầy đủ thông tin, không chỉ user/pass.
                
                String sql = "INSERT INTO NhanVien (MaNV, TenNV, NgaySinh, GioiTinh, ChucVu, SoDienThoai, DiaChi, Username, Password, Role) VALUES "
                        // 1. Tài khoản QUẢN LÝ (Admin)
                        + "('NV01', 'Nguyễn Quản Lý', '1990-01-01', 'Nam', 'Cửa hàng trưởng', '0901234567', 'Hà Nội', 'admin', '123', 'ADMIN'),"
                        
                        // 2. Tài khoản NHÂN VIÊN (Staff)
                        + "('NV02', 'Trần Nhân Viên', '1995-05-05', 'Nữ', 'Thu ngân', '0909876543', 'Hồ Chí Minh', 'staff', '123', 'NHÂN VIÊN')";

                cons.createStatement().executeUpdate(sql);
                System.out.println("✓ Đã khởi tạo dữ liệu mẫu trong bảng NhanVien (admin/123 và staff/123)");
            }

            cons.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tạo dữ liệu mẫu nhân viên: " + e.getMessage());
        }
    }
}