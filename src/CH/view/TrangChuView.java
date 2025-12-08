package CH.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TrangChuView extends JPanel {
    // Các Label để hiển thị số liệu (Controller sẽ set text cho các label này)
    private JLabel lblTongDoanhThu;
    private JLabel lblSoHoaDon;
    private JLabel lblSoKhachHang;
    private JLabel lblSoMonAn;
    
    // Nút refresh để cập nhật số liệu
    private JButton btnLamMoi;

    // Màu sắc
    private final Color PRIMARY_COLOR = new Color(0, 91, 110);
    private final Color CARD_BG = Color.WHITE;
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    public TrangChuView() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 249)); // Màu nền xám xanh nhạt dịu mắt

        // --- 1. TIÊU ĐỀ ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 245, 249));
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel lblTitle = new JLabel("TỔNG QUAN CỬA HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(PRIMARY_COLOR);
        
        btnLamMoi = new JButton("Cập nhật số liệu");
        btnLamMoi.setBackground(PRIMARY_COLOR);
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLamMoi, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. KHU VỰC THỐNG KÊ (GRID 4 CỘT) ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(new Color(240, 245, 249));
        statsPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        statsPanel.setPreferredSize(new Dimension(0, 150)); // Chiều cao cố định cho hàng thẻ

        // Tạo 4 thẻ thống kê
        lblTongDoanhThu = new JLabel("0 VNĐ");
        lblSoHoaDon = new JLabel("0");
        lblSoKhachHang = new JLabel("0");
        lblSoMonAn = new JLabel("0");

        statsPanel.add(createCard("DOANH THU", lblTongDoanhThu, new Color(255, 159, 67))); // Cam
        statsPanel.add(createCard("HÓA ĐƠN", lblSoHoaDon, new Color(52, 152, 219)));      // Xanh dương
        statsPanel.add(createCard("KHÁCH HÀNG", lblSoKhachHang, new Color(46, 204, 113))); // Xanh lá
        statsPanel.add(createCard("MÓN ĂN", lblSoMonAn, new Color(155, 89, 182)));         // Tím

        // --- 3. HÌNH ẢNH HOẶC NỘI DUNG CHÍNH ---
        // Phần này để trống hoặc chứa một hình ảnh minh họa cho đẹp
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblWelcome = new JLabel("Chào mừng trở lại hệ thống quản lý!", JLabel.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        lblWelcome.setForeground(Color.GRAY);
        
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(lblWelcome, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    // Hàm tạo thẻ thống kê đẹp mắt
    private JPanel createCard(String title, JLabel valueLabel, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Tiêu đề thẻ
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.GRAY);

        // Giá trị số
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(TEXT_COLOR);

        // Thanh màu trang trí bên trái
        JPanel colorBar = new JPanel();
        colorBar.setBackground(iconColor);
        colorBar.setPreferredSize(new Dimension(5, 0));

        JPanel content = new JPanel(new GridLayout(2, 1, 0, 5));
        content.setBackground(Color.WHITE);
        content.add(lblTitle);
        content.add(valueLabel);

        card.add(colorBar, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);
        
        // Thêm khoảng trắng nhỏ giữa thanh màu và nội dung
        JPanel spacer = new JPanel();
        spacer.setBackground(Color.WHITE);
        spacer.setPreferredSize(new Dimension(10, 0));
        card.add(spacer, BorderLayout.EAST); // Mẹo: Dùng EAST của BorderLayout trong content panel sẽ tốt hơn, nhưng đây là layout lồng
        
        // Sửa lại layout một chút để đẹp hơn
        JPanel mainContainer = new JPanel(new BorderLayout(15, 0));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.add(colorBar, BorderLayout.WEST);
        mainContainer.add(content, BorderLayout.CENTER);

        return mainContainer;
    }

    // Getters
    public JLabel getLblTongDoanhThu() { return lblTongDoanhThu; }
    public JLabel getLblSoHoaDon() { return lblSoHoaDon; }
    public JLabel getLblSoKhachHang() { return lblSoKhachHang; }
    public JLabel getLblSoMonAn() { return lblSoMonAn; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
}