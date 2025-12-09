# Hướng Dẫn Sửa Lỗi JDateChooser

## Vấn Đề
Nếu bạn thấy lỗi đỏ về `JDateChooser cannot be resolved` hoặc `The import com.toedter cannot be resolved`, đây là do IDE chưa nhận diện được thư viện `jcalendar-1.4.jar`.

## Giải Pháp

### Cách 1: Refresh Project trong NetBeans (Khuyến nghị)

1. **Clean and Build Project**:
   - Click chuột phải vào project → **Clean and Build**
   - Hoặc nhấn **Shift + F11**

2. **Mở lại Project**:
   - Đóng NetBeans
   - Mở lại NetBeans và mở lại project

3. **Kiểm tra Libraries**:
   - Click chuột phải vào project → **Properties**
   - Chọn **Libraries** ở bên trái
   - Kiểm tra xem có thấy `jcalendar-1.4.jar` và `mysql-connector-j-9.5.0.jar` trong danh sách không
   - Nếu không có, thêm thủ công (xem Cách 2)

### Cách 2: Thêm Thư Viện Thủ Công trong NetBeans

1. Click chuột phải vào project → **Properties**

2. Chọn **Libraries** ở bên trái

3. Trong tab **Compile**, click nút **Add JAR/Folder...**

4. Tìm và chọn file `jcalendar-1.4.jar` trong thư mục dự án:
   ```
   QuanLyCuaHangAnNhanhJAVA-main/jcalendar-1.4.jar
   ```

5. Làm tương tự với `mysql-connector-j-9.5.0.jar`:
   ```
   QuanLyCuaHangAnNhanhJAVA-main/mysql-connector-j-9.5.0.jar
   ```

6. Click **OK** để lưu

7. **Clean and Build** project lại (Shift + F11)

### Cách 3: Kiểm Tra Đường Dẫn Thư Viện

Nếu vẫn còn lỗi, kiểm tra file `nbproject/project.properties`:

1. Mở file `nbproject/project.properties`

2. Tìm các dòng:
   ```
   file.reference.jcalendar-1.4.jar=...
   file.reference.mysql-connector-j-9.5.0.jar=...
   ```

3. Đảm bảo đường dẫn trỏ đúng đến file jar trong thư mục dự án của bạn

4. Nếu đường dẫn sai, cập nhật lại đường dẫn đúng

5. **Clean and Build** project lại

## Lưu Ý

- Đảm bảo file `jcalendar-1.4.jar` và `mysql-connector-j-9.5.0.jar` có trong thư mục dự án
- Sau khi thêm thư viện, luôn **Clean and Build** project
- Nếu dùng IDE khác (IntelliJ, Eclipse), cần thêm thư viện vào classpath tương ứng

## Kiểm Tra Lỗi Đã Được Sửa

Sau khi thực hiện các bước trên:
1. Đợi IDE index lại project (có thể mất vài giây)
2. Kiểm tra xem các dòng import `com.toedter.calendar.JDateChooser` còn báo lỗi đỏ không
3. Nếu vẫn còn lỗi, thử đóng và mở lại file Java đó

---

**Nếu vẫn còn lỗi sau khi thử các cách trên, vui lòng kiểm tra:**
- File jar có bị hỏng không (thử tải lại)
- JDK version có tương thích không
- IDE có được cập nhật lên phiên bản mới nhất không

