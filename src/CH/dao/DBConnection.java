package CH.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DBConnection {

    // Cấu hình (Dựa trên thông tin trong hình ảnh của bạn)
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    // Tên Database: QuanLyCuaHang (giả định)
    private static final String DB_NAME = "QuanLyCuaHang"; 
    private static final String USER = "root";
    // Mật khẩu: 1352004 (dựa trên hình ảnh)
    private static final String PASS = "1352004"; 

    // URL kết nối
    private static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + 
                                         "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    
    // Biến lưu trữ kết nối
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            // 1. Tải MySQL JDBC Driver (Driver mới không cần Class.forName() nhưng tốt nhất nên dùng)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Thiết lập kết nối
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
            }
            return connection;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Lỗi Driver MySQL: Không tìm thấy thư viện MySQL Connector J.", 
                                          "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi kết nối cơ sở dữ liệu. Vui lòng kiểm tra MySQL Server (Port: 3306, DB: QuanLyCuaHang, User/Pass). Chi tiết: " + e.getMessage(), 
                "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Hàm dùng để đóng kết nối
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void initializeDatabase() {
        Connection cons = getConnection();
        if (cons != null) {
            try {
                // Ví dụ tạo bảng NhanVien (Nếu bạn đã có hàm này ở chỗ khác, bạn có thể gọi nó ở đây)
                String sqlCreateNhanVien = "CREATE TABLE IF NOT EXISTS NhanVien ("
                                        + "MaNV VARCHAR(10) PRIMARY KEY NOT NULL,"
                                        + "TenNV VARCHAR(50) NOT NULL,"
                                        + "SDT VARCHAR(15),"
                                        + "DiaChi VARCHAR(100),"
                                        + "VaiTro VARCHAR(20))";
                
                Statement st = cons.createStatement();
                st.executeUpdate(sqlCreateNhanVien);
                
                // Thêm các lệnh tạo bảng khác (HoaDon, Kho, KhachHang...) tại đây.
                
                System.out.println("Kiem tra va tao cau truc Database thanh cong.");
                // Không đóng kết nối ở đây, để hàm getConnection() quản lý
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khởi tạo cấu trúc Database: " + e.getMessage(), 
                                              "Lỗi SQL Khởi Tạo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Hàm main dùng để test kết nối

    public static void main(String[] args) {
        Connection testConn = getConnection();
        if (testConn != null) {
            System.out.println("Ket noi thanh cong!");
            initializeDatabase(); // Khởi tạo cấu trúc bảng sau khi kết nối thành công
            closeConnection();
        } else {
            System.out.println("Ket noi that bai!");
        }
    }

}