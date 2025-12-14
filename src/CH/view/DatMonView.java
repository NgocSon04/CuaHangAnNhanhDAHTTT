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
        
        String[] colMenu = {"Mã món", "Tên món", "Đơn giá", "ĐVT", "Mã HH"};
        modelMenu = new DefaultTableModel(colMenu, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMenu = new JTable(modelMenu);
        tableMenu.setRowHeight(30);
        tableMenu.getColumnModel().getColumn(4).setMinWidth(0);
        tableMenu.getColumnModel().getColumn(4).setMaxWidth(0);
        tableMenu.getColumnModel().getColumn(4).setPreferredWidth(0);
        pnlLeft.add(new JScrollPane(tableMenu), BorderLayout.CENTER);
        
        btnThemMon = new JButton("Thêm vào giỏ >>");
        btnThemMon.setBackground(new Color(0, 77, 77));
        btnThemMon.setForeground(Color.WHITE);
        pnlLeft.add(btnThemMon, BorderLayout.SOUTH);

        // --- PHẢI: GIỎ HÀNG ---
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBorder(new TitledBorder("GIỎ HÀNG ĐANG CHỌN"));

        String[] colGio = {"Mã món", "Tên món", "SL", "Đơn giá", "Thành tiền", "Mã HH"};        //  Override hàm isCellEditable
        modelGioHang = new DefaultTableModel(colGio, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
            }
            // Định nghĩa kiểu dữ liệu cho cột để nhập số không bị lỗi
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class; 
                return String.class; 
            }
        };
        tableGioHang = new JTable(modelGioHang);
        tableGioHang.setRowHeight(30);
        tableGioHang.getColumnModel().getColumn(5).setMinWidth(0);
        tableGioHang.getColumnModel().getColumn(5).setMaxWidth(0);
        tableGioHang.getColumnModel().getColumn(5).setPreferredWidth(0);
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
    public DefaultTableModel getModelMenu() { return modelMenu; } // Getter mới
    public DefaultTableModel getModelGioHang() { return modelGioHang; }
    public JTable getTableMenu() { return tableMenu; }
    public JTable getTableGioHang() { return tableGioHang; }
    
    public void setTongTien(double tien) {
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", tien));
    }

    public void addThemListener(ActionListener al) { btnThemMon.addActionListener(al); }
    public void addXoaListener(ActionListener al) { btnXoaMon.addActionListener(al); }
    public void addThanhToanListener(ActionListener al) { btnThanhToan.addActionListener(al); }
    
    // Hàm thêm món vào giỏ hiển thị (Logic hiển thị)
    public void addMonToGio(String maMon,String maHH, String ten, double gia) {
        // Kiểm tra trùng
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            if (modelGioHang.getValueAt(i, 0).equals(maHH)) { // So sánh Mã HH
                int slCu = Integer.parseInt(modelGioHang.getValueAt(i, 2).toString());
                int slMoi = slCu + 1;
                modelGioHang.setValueAt(slMoi, i, 2);
                modelGioHang.setValueAt(String.format("%,.0f", slMoi * gia), i, 4);
                return;
            }
        }
        // Thêm mới: [Mã HH, Tên, SL, Giá, Thành tiền]
        modelGioHang.addRow(new Object[]{
            maMon,                        
            ten,                          
            1,                            
            String.format("%,.0f", gia),  
            String.format("%,.0f", gia),
            maHH
        });
    }
}