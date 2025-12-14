package CH.view;

import CH.controller.DatMonController; 
import CH.controller.HoaDonController; 
import CH.controller.KhoController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MainView extends JFrame {

    private final Color SIDEBAR_COLOR = new Color(0, 91, 110);
    private final Color SIDEBAR_HOVER = new Color(0, 77, 95);
    private final Color ACCENT_RED = new Color(255, 77, 77); // Màu highlight

    private CardLayout cardLayout;
    private JPanel pnlContent;
    private Map<String, MenuButton> menuButtons = new HashMap<>(); // Sử dụng Custom Button
    private JLabel lblRole;

    // Các View Con
    private NhanVienView nhanVienView;
    private KhachHangView khachHangView;
    private HoaDonView hoaDonView;
    private DatMonView datMonView;
    private ThucDonView qlThucDonView;
    private KhoView khoView;
    private DoanhThuView doanhThuView;
    private TrangChuView trangChuView;
    
    private HoaDonController hoaDonController;
    private DatMonController datMonController;
    private KhoController khoController;

    public MainView() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng Đồ Ăn Nhanh");
        setSize(1300, 750); // Tăng kích thước một chút cho thoáng
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. Header (Thanh trên cùng) ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        JLabel lblBrand = new JLabel("  HỆ THỐNG QUẢN LÝ CỬA HÀNG");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBrand.setForeground(SIDEBAR_COLOR);
        
        JPanel pnlUser = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 18));
        pnlUser.setBackground(Color.WHITE);
        pnlUser.add(new JLabel("Xin chào, Admin |"));
        pnlUser.add(new JLabel("Đăng xuất") {{
            setForeground(Color.GRAY);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }});
        
        pnlHeader.add(lblBrand, BorderLayout.WEST);
        pnlHeader.add(pnlUser, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. Content Area (CardLayout) ---
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        
        // Khởi tạo và thêm các view con
        initViews();
        
        add(pnlContent, BorderLayout.CENTER);

        // --- 3. Sidebar (Bên trái) ---
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setPreferredSize(new Dimension(240, 0));
        pnlSidebar.setBackground(SIDEBAR_COLOR);
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));

        // Phần hiển thị Role
        lblRole = new JLabel("ADMIN");
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRole.setBorder(new EmptyBorder(30, 0, 30, 0));

        pnlSidebar.add(lblRole);
        pnlSidebar.add(new JSeparator() {{ 
            setMaximumSize(new Dimension(200, 1)); 
            setForeground(new Color(255, 255, 255, 50)); 
        }});
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Menu items với Icon (Unicode)
        Object[][] menuItems = {
            {"Trang chủ", "🏠"}, {"Đặt Món", "🛒"}, {"Thực đơn", "📖"}, 
            {"Nhân viên", "👤"}, {"Khách hàng", "👥"}, {"Hóa đơn", "📄"}, 
            {"Kho", "📦"}, {"Doanh thu", "💰"}, {"Thoát", "🚪"}
        };

        for (Object[] item : menuItems) {
            String name = (String) item[0];
            String icon = (String) item[1];
            
            MenuButton btnMenu = new MenuButton(name, icon);
            btnMenu.addActionListener(e -> {
                if (name.equals("Thoát")) System.exit(0);
                else {
                    cardLayout.show(pnlContent, name);
                    updateActiveButton(name);
                }
            });
            menuButtons.put(name, btnMenu);
            pnlSidebar.add(btnMenu);
            pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        add(pnlSidebar, BorderLayout.WEST);
        updateActiveButton("Trang chủ");
    }

    private void initViews() {
        trangChuView = new TrangChuView();
        nhanVienView = new NhanVienView();
        khachHangView = new KhachHangView();
        qlThucDonView = new ThucDonView();
        datMonView = new DatMonView();
        hoaDonView = new HoaDonView();
        khoView = new KhoView();
        khoController = new KhoController(khoView);
        doanhThuView = new DoanhThuView();
<<<<<<< Upstream, based on origin/main
=======
        new CH.controller.DoanhThuController(doanhThuView);
        trangChuView = new TrangChuView();
        new CH.controller.TrangChuController(trangChuView);
>>>>>>> df53823 thang _ thực đơn, doanh thu, hóa đơn, trang chủ, kho 

        pnlContent.add(trangChuView, "Trang chủ");
        pnlContent.add(nhanVienView, "Nhân viên");
        pnlContent.add(khachHangView, "Khách hàng");
        pnlContent.add(qlThucDonView, "Thực đơn");
        pnlContent.add(datMonView, "Đặt Món");
        pnlContent.add(hoaDonView, "Hóa đơn");
        pnlContent.add(khoView, "Kho");
        pnlContent.add(doanhThuView, "Doanh thu");
    }

    // Custom Button Class để vẽ Thanh Highlight bên trái
    private class MenuButton extends JButton {
        private boolean active = false;
        private String icon;

        public MenuButton(String text, String icon) {
            super("  " + icon + "    " + text);
            this.icon = icon;
            setMaximumSize(new Dimension(240, 50));
            setPreferredSize(new Dimension(240, 50));
            setBackground(SIDEBAR_COLOR);
            setForeground(new Color(200, 200, 200));
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.LEFT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(0, 20, 0, 0));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!active) setBackground(SIDEBAR_HOVER);
                }
                public void mouseExited(MouseEvent e) {
                    if (!active) setBackground(SIDEBAR_COLOR);
                }
            });
        }

        public void setActive(boolean active) {
            this.active = active;
            if (active) {
                setBackground(new Color(255, 255, 255, 30)); // Nền sáng hơn một chút
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
            } else {
                setBackground(SIDEBAR_COLOR);
                setForeground(new Color(200, 200, 200));
                setFont(new Font("Segoe UI", Font.PLAIN, 15));
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (active) {
                // Vẽ thanh dọc màu đỏ bên trái khi nút Active
                g.setColor(ACCENT_RED);
                g.fillRect(0, 0, 5, getHeight());
            }
        }
    }

    private void updateActiveButton(String activeName) {
        for (Map.Entry<String, MenuButton> entry : menuButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(activeName));
        }
    }

    // Getters giữ nguyên để Controller sử dụng...
    public NhanVienView getNhanVienView() { return nhanVienView; }
    public KhachHangView getKhachHangView(){ return khachHangView; }
    public HoaDonView getHoaDonView(){ return hoaDonView; }
    public DatMonView getDatMonView(){ return datMonView; }
    public ThucDonView getThucDonView(){ return qlThucDonView; }
    public KhoView getKhoView(){return khoView;}
    public DoanhThuView getDoanhThuView(){return doanhThuView;}
    public TrangChuView getTrangChuView(){return trangChuView;}

    public void setRole(String role) {
        lblRole.setText(role);
        if (role.equals("NHÂN VIÊN")) {
            hideMenu("Nhân viên");
            hideMenu("Kho");
            hideMenu("Doanh thu");
        }
    }

    private void hideMenu(String name) {
        MenuButton btn = menuButtons.get(name);
        if (btn != null) btn.setVisible(false);
    }
}