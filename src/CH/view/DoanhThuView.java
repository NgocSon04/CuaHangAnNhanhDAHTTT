package CH.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class DoanhThuView extends JPanel {
    private JTable doanhThuTable;
    private DefaultTableModel tableModel;
    
    // Components
    private JTextField txtTuNgay, txtDenNgay;
    private JButton btnXemBaoCao;
    private JLabel lblTongDoanhThu;

    // Màu sắc chủ đạo
    private final Color PRIMARY_COLOR = new Color(0, 77, 77); 
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color BUTTON_BG = Color.WHITE;
    private final Color BUTTON_TEXT = Color.BLACK;

    public DoanhThuView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- PHẦN 1: PANEL NHẬP LIỆU & BÁO CÁO ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("BÁO CÁO DOANH THU", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Form nhập liệu (Ngày)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        inputPanel.setBackground(PRIMARY_COLOR);
        
        // Từ ngày
        inputPanel.add(createLabel("Từ Ngày (DD/MM/YYYY):"));
        txtTuNgay = createTextField();
        txtTuNgay.setText("01/01/2025"); // Ngày mặc định
        inputPanel.add(txtTuNgay);

        // Đến ngày
        inputPanel.add(createLabel("Đến Ngày (DD/MM/YYYY):"));
        txtDenNgay = createTextField();
        txtDenNgay.setText("31/12/2025"); // Ngày mặc định
        inputPanel.add(txtDenNgay);

        // Nút xem báo cáo
        btnXemBaoCao = createButton("Xem Báo Cáo");
        inputPanel.add(btnXemBaoCao);

        // 1.3 Tổng Doanh thu
        JPanel tongDTPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        tongDTPanel.setBackground(PRIMARY_COLOR);
        lblTongDoanhThu = new JLabel("TỔNG DOANH THU: 0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongDoanhThu.setForeground(Color.YELLOW); 
        tongDTPanel.add(lblTongDoanhThu);

        // Đóng gói
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(tongDTPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);

        // --- PHẦN 2: BẢNG DỮ LIỆU ---
        String[] headers = {"Ngày", "Doanh Thu"};
        tableModel = new DefaultTableModel(headers, 0);
        doanhThuTable = new JTable(tableModel);
        doanhThuTable.setRowHeight(28); 
        doanhThuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(doanhThuTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // --- HÀM HỖ TRỢ TẠO GIAO DIỆN ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_COLOR); 
        return lbl;
    }
    
    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(150, 30));
        return tf;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(BUTTON_BG); 
        btn.setForeground(BUTTON_TEXT); 
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- GETTERS ---
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtTuNgay() { return txtTuNgay; }
    public JTextField getTxtDenNgay() { return txtDenNgay; }
    public JButton getBtnXemBaoCao() { return btnXemBaoCao; }
    public JLabel getLblTongDoanhThu() { return lblTongDoanhThu; }
}