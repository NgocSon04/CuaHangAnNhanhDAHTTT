package CH.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet; // <--- BẮT BUỘC PHẢI CÓ DÒNG NÀY
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

            // (ĐÃ XÓA ĐOẠN INSERT ADMIN01 THỪA Ở ĐÂY ĐỂ TRÁNH LỖI)
            
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

            // Tạo bảng ThucDon
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

            // --------------------------------------------------------
            // 3. THÊM DỮ LIỆU MẪU (CHỈ CHẠY KHI BẢNG RỖNG)
            // --------------------------------------------------------
            
            // a) Thêm Admin và Nhân viên
            String sqlCheckNV = "SELECT COUNT(*) FROM NhanVien";
            ResultSet rsNV = dbStmt.executeQuery(sqlCheckNV);
            if (rsNV.next() && rsNV.getInt(1) == 0) {
                // Admin: admin/123
                dbStmt.executeUpdate("INSERT INTO NhanVien VALUES ('NV01', 'Quản Trị Viên', '01/01/1990', 'Nam', 'Quản lý', '0901234567', 'Hà Nội', 'admin', '123', 'ADMIN')");
                // Staff: staff/123
                dbStmt.executeUpdate("INSERT INTO NhanVien VALUES ('NV02', 'Nhân Viên A', '05/05/2000', 'Nữ', 'Thu ngân', '0909876543', 'TPHCM', 'staff', '123', 'NHÂN VIÊN')");
                System.out.println("-> Da them user mau: admin/123 va staff/123");
            }
            rsNV.close();

            // b) Thêm Hàng hóa vào Kho (Để có cái mà bán)
            String sqlCheckKho = "SELECT COUNT(*) FROM Kho";
            ResultSet rsKho = dbStmt.executeQuery(sqlCheckKho);
            if (rsKho.next() && rsKho.getInt(1) == 0) {
                dbStmt.executeUpdate("INSERT INTO Kho VALUES ('HH01', 'Gà tươi', 100, 50000, 0)");
                dbStmt.executeUpdate("INSERT INTO Kho VALUES ('HH02', 'Khoai tây', 200, 10000, 0)");
                dbStmt.executeUpdate("INSERT INTO Kho VALUES ('HH03', 'Cocacola', 300, 8000, 0)");
                System.out.println("-> Da them du lieu mau Kho");
            }
            rsKho.close();

            // c) Thêm Thực đơn (Liên kết với Kho)
            String sqlCheckMenu = "SELECT COUNT(*) FROM ThucDon";
            ResultSet rsMenu = dbStmt.executeQuery(sqlCheckMenu);
            if (rsMenu.next() && rsMenu.getInt(1) == 0) {
                dbStmt.executeUpdate("INSERT INTO ThucDon VALUES ('M01', 'Gà Rán Giòn', 35000, 'Cái', 'HH01', '')");
                dbStmt.executeUpdate("INSERT INTO ThucDon VALUES ('M02', 'Khoai Tây Chiên', 25000, 'Suất', 'HH02', '')");
                dbStmt.executeUpdate("INSERT INTO ThucDon VALUES ('M03', 'Nước Ngọt', 15000, 'Lon', 'HH03', '')");
                System.out.println("-> Da them du lieu mau Thuc Don");
            }
            rsMenu.close();

            System.out.println("Khoi tao Database thanh cong!");
            
            dbStmt.close();
            dbConn.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Loi ket noi hoac khoi tao Database!");
        }
    }
    
  
}