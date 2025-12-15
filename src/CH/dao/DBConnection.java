package CH.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {
    
    // Cấu hình server
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "quanlycuahang";
    private static final String USER = "root";
    private static final String PASS = "070704"; // Pass của bạn

    private static final String SERVER_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/";
    private static final String DB_URL = SERVER_URL + DB_NAME;

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

    public static void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 1. Tạo Database
            Connection serverConn = DriverManager.getConnection(SERVER_URL, USER, PASS);
            Statement stmt = serverConn.createStatement();
            String sqlCreateDB = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sqlCreateDB);
            System.out.println("Kiem tra Database: " + DB_NAME);
            stmt.close();
            serverConn.close();

            // 2. Kết nối vào DB để tạo bảng
            Connection dbConn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement dbStmt = dbConn.createStatement();

            // Tạo bảng NhanVien
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
            
            // Tạo bảng KhachHang
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
            
            // Tạo bảng Kho 
            String sqlCreateKho = "CREATE TABLE IF NOT EXISTS Kho ("
                    + "MaHH VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenHH VARCHAR(100),"
                    + "SoLuong INT,"
                    + "GiaNhap DOUBLE,"
                    + "GiaBan DOUBLE"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateKho);

            // Tạo bảng ThucDon (Đã thêm HinhAnh)
            String sqlCreateThucDon = "CREATE TABLE IF NOT EXISTS ThucDon ("
                    + "MaMon VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenMon VARCHAR(100),"
                    + "DonGia DOUBLE,"
                    + "DonViTinh VARCHAR(20),"
                    + "MaHH VARCHAR(20),"       // Liên kết kho
                    + "HinhAnh VARCHAR(255)"     // Đường dẫn ảnh
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateThucDon);
            
            // Tạo bảng HoaDon
            String sqlCreateHoaDon = "CREATE TABLE IF NOT EXISTS HoaDon ("
                    + "MaHD VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TenNV VARCHAR(100)," 
                    + "TenKH VARCHAR(100)," 
                    + "NgayLap VARCHAR(20),"
                    + "TongTien DOUBLE"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateHoaDon);

            // Tạo bảng ChiTietHoaDon
            String sqlCreateChiTiet = "CREATE TABLE IF NOT EXISTS ChiTietHoaDon ("
                    + "ID INT AUTO_INCREMENT PRIMARY KEY,"
                    + "MaHD VARCHAR(20) NOT NULL,"
                    + "TenMon VARCHAR(100),"
                    + "SoLuong INT,"
                    + "DonGia DOUBLE,"
                    + "CONSTRAINT fk_hoadon FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD) ON DELETE CASCADE"
                    + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            dbStmt.executeUpdate(sqlCreateChiTiet);
            
            System.out.println("Khoi tao Database thanh cong!");
            
            dbStmt.close();
            dbConn.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Loi ket noi hoac khoi tao Database!");
        }
    }
}