package CH.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {
    
    // Cấu hình server
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "QuanLyCuaHang";
    private static final String USER = "root";
    private static final String PASS = "1352004"; // Điền pass MySQL của bạn nếu có

    // URL kết nối chỉ đến Server (để tạo DB nếu chưa có)
    private static final String SERVER_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/";
    
    // URL kết nối đến Database cụ thể (để thao tác dữ liệu)
    private static final String DB_URL = SERVER_URL + DB_NAME;

    /**
     * Hàm này dùng để lấy kết nối thao tác dữ liệu
     */
    public static Connection getConnection() {
        Connection cons = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cons = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cons;
    }

    /**
     * [MỚI] Hàm kiểm tra và tự động tạo Database + Bảng nếu chưa có
     * Chạy hàm này đầu tiên khi khởi động Main
     */
    public static void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 1. Kết nối vào Server MySQL (chưa vào DB cụ thể)
            Connection serverConn = DriverManager.getConnection(SERVER_URL, USER, PASS);
            Statement stmt = serverConn.createStatement();

            // 2. Tạo Database nếu chưa tồn tại
            String sqlCreateDB = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sqlCreateDB);
            System.out.println("Da Kiem Tra Database: " + DB_NAME);
            
            // Đóng kết nối server để chuyển sang kết nối DB
            stmt.close();
            serverConn.close();

            // 3. Kết nối vào Database vừa tạo để tạo Bảng
            Connection dbConn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement dbStmt = dbConn.createStatement();

            // 4. Tạo bảng NhanVien nếu chưa tồn tại
            String sqlCreateNhanVien = "CREATE TABLE IF NOT EXISTS NhanVien ("
                    + "MaNV VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenNV VARCHAR(100) NOT NULL,"
                    + "NgaySinh VARCHAR(20),"
                    + "GioiTinh VARCHAR(10),"
                    + "ChucVu VARCHAR(50),"
                    + "SoDienThoai VARCHAR(15),"
                    + "DiaChi VARCHAR(255),"
                    + "Username VARCHAR(50),"  
                    + "Password VARCHAR(50),"  
                    + "Role VARCHAR(20)"        
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            
            dbStmt.executeUpdate(sqlCreateNhanVien);
            System.out.println("Da Kiem tra/ Tao bang NhanVien.");
            
            // 4. Tạo bảng KhachHang nếu chưa tồn tại [MỚI THÊM]
            String sqlCreateKhachHang = "CREATE TABLE IF NOT EXISTS KhachHang ("
                    + "MaKH VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenKH VARCHAR(100) NOT NULL,"
                    + "TheLoai VARCHAR(20),"
                    + "GioiTinh VARCHAR(10),"
                    + "Email VARCHAR(100),"
                    + "SoDienThoai VARCHAR(15),"
                    + "DiaChi VARCHAR(255)"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            
            dbStmt.executeUpdate(sqlCreateKhachHang);
            System.out.println("Da kiem tra/ Tao bang KhachHang.");
            
            // 6. Tạo bảng HoaDon [MỚI]
            String sqlCreateHoaDon = "CREATE TABLE IF NOT EXISTS HoaDon ("
                    + "MaHD VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenNV VARCHAR(100)," 
                    + "TenKH VARCHAR(100)," 
                    + "NgayLap VARCHAR(20),"
                    + "TongTien DOUBLE"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateHoaDon);
            System.out.println("Da Kiem tra/ Tao bang HoaDon.");

            // 7. Tạo bảng ChiTietHoaDon [MỚI]
            String sqlCreateChiTiet = "CREATE TABLE IF NOT EXISTS ChiTietHoaDon ("
                    + "ID INT AUTO_INCREMENT PRIMARY KEY," // Khóa chính tự tăng
                    + "MaHD VARCHAR(20) NOT NULL,"
                    + "TenMon VARCHAR(100),"
                    + "SoLuong INT,"
                    + "DonGia DOUBLE,"
                    + "CONSTRAINT fk_hoadon FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD) ON DELETE CASCADE"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateChiTiet);
            System.out.println("Da Kiem tra/ Tao bang ChiTietHoaDon.");
            
            // 8. Tạo bảng ThucDon (Menu)
            String sqlCreateThucDon = "CREATE TABLE IF NOT EXISTS ThucDon ("
                    + "MaHH VARCHAR(20),"
                    + "MaMon VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenMon VARCHAR(100),"
                    + "DonGia DOUBLE,"
                    + "DonViTinh VARCHAR(20)"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateThucDon);
            System.out.println("Da Kiem tra/ Tao bang ThucDon.");
           
            String sqlCreateKho = "CREATE TABLE IF NOT EXISTS Kho ("
                    + "MaHH VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenHH VARCHAR(100),"
                    + "SoLuong INT,"
                    + "GiaNhap DOUBLE,"
                    + "GiaBan DOUBLE"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateKho);
            System.out.println("Da Kiem tra/ Tao bang Kho.");
            // (Optional) Tạo thêm các bảng khác ở đây nếu cần (Khách hàng, Hóa đơn...)
            
            dbStmt.close();
            dbConn.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khởi tạo Database! Kiểm tra xem XAMPP/MySQL đã bật chưa?");
        }
    }
}