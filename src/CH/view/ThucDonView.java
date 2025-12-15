package CH.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import CH.model.MonAn;

public class ThucDonView extends JPanel {
    private JTextField txtMaMon, txtTenMon, txtDonGia, txtDVT;
    
    // Phần mới từ Github (Mã Hàng Hóa)
    private JComboBox<String> cboMaHH;

    // Phần của bạn (Hình ảnh)
    private JLabel lblHinhAnh; 
    private JButton btnChonAnh; 
    private String duongDanAnh = ""; 
    
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnReset;

    public ThucDonView() {
        setLayout(new BorderLayout());
        
        // --- FORM PANEL ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(new Color(0, 77, 77));
        pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- CỘT TRÁI: NHẬP LIỆU ---
        addInput(pnlForm, gbc, 0, 0, "Mã món", txtMaMon = new JTextField(15));
        txtMaMon.setEditable(false); txtMaMon.setText("Tự động");
        
        addInput(pnlForm, gbc, 0, 1, "Tên món", txtTenMon = new JTextField(15));
        addInput(pnlForm, gbc, 0, 2, "Đơn giá", txtDonGia = new JTextField(15));
        addInput(pnlForm, gbc, 0, 3, "Đơn vị tính", txtDVT = new JTextField(15)); 

        // Thêm ComboBox Mã Hàng Hóa (Từ Github)
        cboMaHH = new JComboBox<>();
        cboMaHH.setPreferredSize(new Dimension(150, 28)); 
        addInput(pnlForm, gbc, 0, 4, "Mã Hàng Hóa", cboMaHH); 

        // --- CỘT PHẢI: ẢNH VÀ NÚT (Từ code của bạn) ---
        
        // 1. Label hiển thị ảnh
        lblHinhAnh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblHinhAnh.setPreferredSize(new Dimension(150, 150));
        lblHinhAnh.setBorder(new LineBorder(Color.WHITE, 1));
        lblHinhAnh.setOpaque(true);
        lblHinhAnh.setBackground(Color.LIGHT_GRAY);
        
        // Đặt vị trí ảnh: Cột 2, Hàng 0, Chiếm 5 hàng dọc
        gbc.gridx = 2; gbc.gridy = 0; 
        gbc.gridheight = 5; 
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 20, 5, 5); 
        pnlForm.add(lblHinhAnh, gbc);

        // 2. Nút Chọn Ảnh
        btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.setFocusPainted(false);
        btnChonAnh.setBackground(Color.WHITE);
        btnChonAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridx = 2; gbc.gridy = 5;
        gbc.gridheight = 1; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlForm.add(btnChonAnh, gbc);

        // Sự kiện chọn ảnh
        btnChonAnh.addActionListener(e -> chonAnh());
        lblHinhAnh.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { chonAnh(); }
        });

        // --- BUTTONS CHỨC NĂNG ---
        JPanel pnlBtn = new JPanel();
        pnlBtn.setBackground(new Color(0, 77, 77));
        btnThem = new JButton("Thêm"); btnSua = new JButton("Sửa"); 
        btnXoa = new JButton("Xóa"); btnReset = new JButton("Reset");
        pnlBtn.add(btnThem); pnlBtn.add(btnSua); pnlBtn.add(btnXoa); pnlBtn.add(btnReset);
        
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(pnlForm, BorderLayout.CENTER);
        pnlNorth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // --- TABLE ---
        // Gộp cột: Có cả Mã HH và Hình ảnh
        String[] cols = {"Mã món", "Tên món", "Đơn giá", "Đơn vị tính", "Mã HH", "Hình ảnh"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void addInput(JPanel p, GridBagConstraints gbc, int x, int y, String lbl, Component cmp) {
        gbc.gridx = x; gbc.gridy = y; 
        gbc.gridheight = 1; 
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel l = new JLabel(lbl); l.setForeground(Color.WHITE); p.add(l, gbc);
        gbc.gridx = x+1; p.add(cmp, gbc);
    }

    // --- HÀM XỬ LÝ CHỌN ẢNH ---
    private void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh (JPG, PNG, JPEG)", "jpg", "png", "jpeg"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            duongDanAnh = selectedFile.getAbsolutePath();
            setHinhAnhToLabel(duongDanAnh);
        }
    }

    public void setHinhAnhToLabel(String path) {
        if (path == null || path.isEmpty()) {
            lblHinhAnh.setIcon(null);
            lblHinhAnh.setText("Chưa có ảnh");
            return;
        }
        
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            int width = lblHinhAnh.getWidth() > 0 ? lblHinhAnh.getWidth() : 150;
            int height = lblHinhAnh.getHeight() > 0 ? lblHinhAnh.getHeight() : 150;
            Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            lblHinhAnh.setIcon(new ImageIcon(newImg));
            lblHinhAnh.setText(""); 
        } catch (Exception e) {
            lblHinhAnh.setIcon(null);
            lblHinhAnh.setText("Lỗi ảnh");
        }
    }

    public MonAn getMonAnInfo() {
        double gia = 0;
        // Sử dụng logic xử lý giá từ Github (tốt hơn)
        try {
            String giaStr = txtDonGia.getText().trim().replace(",", "").replace(".", "");
            if (!giaStr.isEmpty()) {
                gia = Double.parseDouble(giaStr);
            }
        } catch (Exception e) {
            System.err.println("Error parsing price: " + e.getMessage());
            gia = 0;
        }

        String maHH = "";
        if (cboMaHH.getSelectedItem() != null) {
            maHH = cboMaHH.getSelectedItem().toString();
        }
        
        // Gộp tất cả dữ liệu: Mã HH và Đường dẫn ảnh
        // LƯU Ý: Bạn cần đảm bảo Model MonAn đã có constructor nhận cả maHH và hinhAnh
        return new MonAn(txtMaMon.getText(), txtTenMon.getText(), gia, txtDVT.getText(), maHH, duongDanAnh);
    }

    public void fillForm(MonAn m) {
        txtMaMon.setText(m.getMaMon()); 
        txtTenMon.setText(m.getTenMon());
        txtDonGia.setText(String.format("%.0f", m.getDonGia())); 
        txtDVT.setText(m.getDonViTinh());
        
        // Điền MaHH vào ComboBox
        if(m.getMaHH() != null) {
            cboMaHH.setSelectedItem(m.getMaHH()); 
        }

        // Điền Ảnh
        duongDanAnh = m.getHinhAnh(); 
        setHinhAnhToLabel(duongDanAnh);
    }
    
    public void clearForm() {
        txtMaMon.setText("Tự động"); 
        txtTenMon.setText(""); 
        txtDonGia.setText(""); 
        txtDVT.setText("");
        
        // Reset cả 2
        cboMaHH.setSelectedIndex(-1);
        duongDanAnh = "";
        lblHinhAnh.setIcon(null);
        lblHinhAnh.setText("Chưa có ảnh");
    }

    // Getter cho Controller sử dụng
    public JComboBox<String> getCboMaHH() {
        return cboMaHH;
    }
    
    // Getters & Listeners giữ nguyên
    public void addRow(MonAn m) { model.addRow(m.toObjectArray()); }
    public void clearTable() { model.setRowCount(0); }
    public int getSelectedRow() { return table.getSelectedRow(); }
    public JTable getTable() { return table; }
    public void addThemListener(ActionListener al) { btnThem.addActionListener(al); }
    public void addSuaListener(ActionListener al) { btnSua.addActionListener(al); }
    public void addXoaListener(ActionListener al) { btnXoa.addActionListener(al); }
    public void addResetListener(ActionListener al) { btnReset.addActionListener(al); }
}