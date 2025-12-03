//package vn.CH.dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class AdminDAO {
//
//    public static class LoginResult {
//        public boolean success;
//        public String role;
//        public LoginResult(boolean success, String role) {
//            this.success = success;
//            this.role = role;
//        }
//    }
//
//    public LoginResult login(String username, String password) {
//        try {
//            Connection cons = DBConnection.getConnection();
//            String sql = "SELECT role FROM Admin WHERE username=? AND password=?";
//            PreparedStatement ps = cons.prepareStatement(sql);
//            ps.setString(1, username);
//            ps.setString(2, password);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                String role = rs.getString("role");
//                ps.close();
//                cons.close();
//                return new LoginResult(true, role);
//            } else {
//                ps.close();
//                cons.close();
//                return new LoginResult(false, null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new LoginResult(false, null);
//        }
//    }
//}
package CH.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    // =======================================================
    // 1. LOGIN
    // =======================================================
    public String login(String username, String password) {
        String role = null;
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "SELECT role FROM Users WHERE username=? AND password=?";
            PreparedStatement ps = cons.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
            }

            ps.close();
            cons.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    // =======================================================
    // 2. INSERT USER (thêm 1 tài khoản)
    // =======================================================
    public boolean insertUser(String username, String password, String role) {
        try {
            Connection cons = DBConnection.getConnection();
            String sql = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = cons.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            int rows = ps.executeUpdate();

            ps.close();
            cons.close();

            return rows > 0;  // true nếu thêm thành công

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =======================================================
    // 3. INSERT DỮ LIỆU MẶC ĐỊNH NẾU BẢNG RỖNG
    // =======================================================
    public void insertDefaultUsersIfEmpty() {
        try {
            Connection cons = DBConnection.getConnection();

            // Kiểm tra bảng Users có dữ liệu chưa
            ResultSet rs = cons.createStatement().executeQuery("SELECT COUNT(*) FROM Users");
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {

                // Thêm tài khoản mặc định
                String sql = "INSERT INTO Users (username, password, role) VALUES "
                        + "('admin', '123456', 'ADMIN'),"
                        + "('nv01', '123456', 'NHÂN VIÊN')";

                cons.createStatement().executeUpdate(sql);
                System.out.println("✓ Đã thêm tài khoản mặc định vào bảng Users");
            }

            cons.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}