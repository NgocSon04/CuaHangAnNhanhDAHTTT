package CH.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import CH.model.MonAn;

public class DatMonView extends JPanel {
    private JTable tableMenu, tableGioHang;
    private DefaultTableModel modelMenu, modelGioHang;
    private JButton btnThemMon, btnXoaMon, btnThanhToan;
    private JLabel lblTongTien;

    public DatMonView() {
        setLayout(new GridLayout(1, 2, 10, 10)); // Chia đôi màn hình
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- TRÁI: DANH SÁCH THỰC ĐƠN ---
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBorder(new TitledBorder("THỰC ĐƠN"));
        
        String[] colMenu = {"Mã", "Tên món", "Đơn giá", "ĐVT"};
        modelMenu = new DefaultTableModel(colMenu, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMenu = new JTable(modelMenu);
        tableMenu.setRowHeight(30);
        pnlLeft.add(new JScrollPane(tableMenu), BorderLayout.CENTER);
        
        btnThemMon = new JButton("Thêm vào giỏ >>");
        btnThemMon.setBackground(new Color(0, 77, 77));
        btnThemMon.setForeground(Color.WHITE);
        pnlLeft.add(btnThemMon, BorderLayout.SOUTH);

        // --- PHẢI: GIỎ HÀNG ---
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBorder(new TitledBorder("GIỎ HÀNG ĐANG CHỌN"));

        String[] colGio = {"Tên món", "SL", "Đơn giá", "Thành tiền"};
        //  Override hàm isCellEditable
        modelGioHang = new DefaultTableModel(colGio, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép sửa cột 1 (Cột Số Lượng)
                return column == 1; 
            }
            // Định nghĩa kiểu dữ liệu cho cột để nhập số không bị lỗi
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Integer.class; // Cột SL là số
                return String.class; // Các cột khác là chữ
            }
        };
        tableGioHang = new JTable(modelGioHang);
        tableGioHang.setRowHeight(30);
        pnlRight.add(new JScrollPane(tableGioHang), BorderLayout.CENTER);

        // Panel dưới của giỏ hàng (Tổng tiền + Nút Thanh toán)
        JPanel pnlFooter = new JPanel(new GridLayout(2, 1));
        
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(Color.RED);
        lblTongTien.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnXoaMon = new JButton("Xóa món");
        btnThanhToan = new JButton("THANH TOÁN & IN HÓA ĐƠN");
        btnThanhToan.setBackground(new Color(255, 77, 77));
        btnThanhToan.setForeground(Color.BLACK);
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 12));
        
        pnlBtns.add(btnXoaMon);
        pnlBtns.add(btnThanhToan);
        
        pnlFooter.add(lblTongTien);
        pnlFooter.add(pnlBtns);
        pnlRight.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlLeft);
        add(pnlRight);
    }

    // Methods
    public void addMonToMenu(MonAn m) { modelMenu.addRow(m.toObjectArray()); }
    
    // Thêm món vào giỏ (Logic hiển thị)
    public void addMonToGio(String ten, double gia) {
        // Kiểm tra xem món đã có trong giỏ chưa
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            if (modelGioHang.getValueAt(i, 0).equals(ten)) {
                int slCu = Integer.parseInt(modelGioHang.getValueAt(i, 1).toString());
                int slMoi = slCu + 1;
                modelGioHang.setValueAt(slMoi, i, 1); // Tăng SL
                modelGioHang.setValueAt(String.format("%,.0f", slMoi * gia), i, 3); // Tính lại tiền
                return;
            }
        }
        // Nếu chưa có thì thêm dòng mới
        modelGioHang.addRow(new Object[]{ten, 1, String.format("%,.0f", gia), String.format("%,.0f", gia)});
    }

    public DefaultTableModel getModelGioHang() { return modelGioHang; }
    public JTable getTableMenu() { return tableMenu; }
    public JTable getTableGioHang() { return tableGioHang; }
    
    public void setTongTien(double tien) {
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", tien));
    }

    public void addThemListener(ActionListener al) { btnThemMon.addActionListener(al); }
    public void addXoaListener(ActionListener al) { btnXoaMon.addActionListener(al); }
    public void addThanhToanListener(ActionListener al) { btnThanhToan.addActionListener(al); }
}