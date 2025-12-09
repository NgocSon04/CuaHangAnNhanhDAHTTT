# Hướng Dẫn Chạy Dự Án Quản Lý Cửa Hàng Ăn Nhanh

## Yêu Cầu Hệ Thống

1. **Java Development Kit (JDK)**: Phiên bản 24 hoặc tương thích
2. **MySQL Server**: XAMPP hoặc MySQL Server đã cài đặt
3. **IDE**: NetBeans (khuyến nghị) hoặc IDE khác hỗ trợ Java
4. **Thư viện cần thiết**:
   - `jcalendar-1.4.jar` (đã có trong thư mục dự án)
   - `mysql-connector-j-9.5.0.jar` (cần tải về)

## Các Bước Cài Đặt và Chạy

### Bước 1: Cài Đặt MySQL

1. Cài đặt **XAMPP** hoặc **MySQL Server**
2. Khởi động MySQL service:
   - Với XAMPP: Mở XAMPP Control Panel và bật MySQL
   - Với MySQL Server: Đảm bảo MySQL service đang chạy

### Bước 2: Tải Thư Viện MySQL Connector

1. Tải file `mysql-connector-j-9.5.0.jar` từ:
   - [MySQL Official Website](https://dev.mysql.com/downloads/connector/j/)
   - Hoặc tìm kiếm "mysql-connector-j-9.5.0.jar download"

2. Đặt file vào thư mục dự án (cùng cấp với `jcalendar-1.4.jar`)

### Bước 3: Cấu Hình Database

Mở file `src/CH/dao/DBConnection.java` và kiểm tra/cập nhật thông tin kết nối:

```java
private static final String HOST = "localhost";
private static final String PORT = "3306";
private static final String DB_NAME = "QuanLyCuaHang";
private static final String USER = "root";
private static final String PASS = "070704"; // Thay đổi mật khẩu MySQL của bạn
```

**Lưu ý**: 
- Thay đổi `PASS` thành mật khẩu MySQL của bạn (nếu có)
- Nếu MySQL của bạn không có mật khẩu, để `PASS = ""`

### Bước 4: Cấu Hình Dự Án trong NetBeans

1. Mở NetBeans IDE
2. Chọn **File → Open Project**
3. Chọn thư mục dự án `QuanLyCuaHangAnNhanhJAVA-main`
4. Nếu thiếu thư viện, thêm vào Libraries:
   - Click chuột phải vào project → **Properties**
   - Chọn **Libraries** → **Add JAR/Folder**
   - Thêm `jcalendar-1.4.jar` và `mysql-connector-j-9.5.0.jar`

### Bước 5: Cập Nhật Đường Dẫn Thư Viện (Nếu Cần)

Nếu gặp lỗi về đường dẫn thư viện, mở file `nbproject/project.properties` và cập nhật:

```
file.reference.jcalendar-1.4.jar=<đường_dẫn_đến>/jcalendar-1.4.jar
file.reference.mysql-connector-j-9.5.0.jar=<đường_dẫn_đến>/mysql-connector-j-9.5.0.jar
```

### Bước 6: Chạy Dự Án

**Cách 1: Chạy từ NetBeans**
1. Click chuột phải vào project → **Run**
2. Hoặc nhấn **F6**

**Cách 2: Chạy từ Command Line**

1. Mở Terminal/Command Prompt
2. Di chuyển đến thư mục dự án:
   ```bash
   cd QuanLyCuaHangAnNhanhJAVA-main
   ```

3. Compile dự án:
   ```bash
   javac -cp "jcalendar-1.4.jar;mysql-connector-j-9.5.0.jar" -d build/classes src/CH/**/*.java
   ```

4. Chạy ứng dụng:
   ```bash
   java -cp "build/classes;jcalendar-1.4.jar;mysql-connector-j-9.5.0.jar" CH.main.Main
   ```

**Lưu ý**: Trên Linux/Mac, thay dấu `;` bằng `:` trong classpath

## Cơ Chế Tự Động Tạo Database

Dự án có tính năng **tự động tạo database và các bảng** khi chạy lần đầu:

- Database: `QuanLyCuaHang`
- Các bảng tự động tạo:
  - `NhanVien` (Nhân viên)
  - `KhachHang` (Khách hàng)
  - `HoaDon` (Hóa đơn)
  - `ChiTietHoaDon` (Chi tiết hóa đơn)
  - `ThucDon` (Thực đơn)

Bạn **không cần** tạo database thủ công, chỉ cần đảm bảo MySQL đang chạy.

## Xử Lý Lỗi Thường Gặp

### Lỗi: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- **Nguyên nhân**: Thiếu file `mysql-connector-j-9.5.0.jar`
- **Giải pháp**: Tải và thêm file vào Libraries của project

### Lỗi: "Access denied for user 'root'@'localhost'"
- **Nguyên nhân**: Sai mật khẩu MySQL
- **Giải pháp**: Kiểm tra và cập nhật mật khẩu trong `DBConnection.java`

### Lỗi: "Communications link failure"
- **Nguyên nhân**: MySQL chưa được khởi động
- **Giải pháp**: Khởi động MySQL service trong XAMPP hoặc MySQL Server

### Lỗi: "Unknown database 'QuanLyCuaHang'"
- **Nguyên nhân**: Hàm `initializeDatabase()` chưa chạy hoặc lỗi
- **Giải pháp**: Kiểm tra log console, đảm bảo MySQL đang chạy và thông tin kết nối đúng

## Cấu Trúc Dự Án

```
QuanLyCuaHangAnNhanhJAVA-main/
├── src/
│   └── CH/
│       ├── main/          # Main class
│       ├── model/         # Các model (entity)
│       ├── view/          # Giao diện Swing
│       ├── controller/    # Controller xử lý logic
│       └── dao/           # Data Access Object (kết nối DB)
├── build/                 # Thư mục chứa file .class sau khi compile
├── nbproject/            # Cấu hình NetBeans
├── jcalendar-1.4.jar     # Thư viện lịch
└── build.xml             # File build Ant
```

## Thông Tin Liên Hệ

- **Tác giả**: Ngoc Son
- **Dự án**: Quản Lý Cửa Hàng Ăn Nhanh

---

**Chúc bạn chạy dự án thành công!** 🎉

