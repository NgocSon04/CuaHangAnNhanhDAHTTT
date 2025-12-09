package CH.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MainView extends JFrame {

    private final Color SIDEBAR_COLOR = new Color(0, 91, 110);
    private final Color ACCENT_RED = new Color(255, 77, 77);

    private CardLayout cardLayout;
    private JPanel pnlContent;
    private Map<String, JButton> menuButtons = new HashMap<>();

    // Label hiển thị vai trò (ADMIN / STAFF)
    private JLabel lblRole;

    // Các View Con
    private NhanVienView nhanVienView;
    private KhachHangView khachHangView;
    private HoaDonView hoaDonView;
    private DatMonView datMonView;
    private ThucDonView qlThucDonView;
    private KhoView khoView;
    private DoanhThuView doanhThuView;

    public MainView() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng Đồ Ăn Nhanh");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlHeader.add(new JLabel("Hệ Thống Quản Lý Cửa Hàng Đồ Ăn Nhanh") {{
            setFont(new Font("Segoe UI", Font.BOLD, 18));
        }}, BorderLayout.WEST);

        JPanel pnlUser = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlUser.setBackground(Color.WHITE);
        pnlUser.add(new JLabel("Xin chào!"));
        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.setBackground(ACCENT_RED);
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        pnlUser.add(btnLogout);
        pnlHeader.add(pnlUser, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Card Layout
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);

        // Khởi tạo view con
        nhanVienView = new NhanVienView();
        khachHangView = new KhachHangView();
        hoaDonView = new HoaDonView();
        datMonView = new DatMonView();
        qlThucDonView = new ThucDonView();
        khoView = new KhoView();
        doanhThuView = new DoanhThuView();

        pnlContent.add(createTrangChuPanel(), "Trang chủ");
        pnlContent.add(nhanVienView, "Nhân viên");
        pnlContent.add(khachHangView, "Khách hàng");
        pnlContent.add(qlThucDonView, "Thực đơn");
        pnlContent.add(datMonView, "Đặt Món");
        pnlContent.add(hoaDonView, "Hóa đơn");

        pnlContent.add(khoView, "Kho");
        pnlContent.add(doanhThuView, "Doanh thu");

        add(pnlContent, BorderLayout.CENTER);

        // 3. Sidebar
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setPreferredSize(new Dimension(220, 0));
        pnlSidebar.setBackground(SIDEBAR_COLOR);
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));

        // Label Role hiển thị ADMIN hoặc STAFF
        lblRole = new JLabel("ROLE");
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlSidebar.add(lblRole);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        String[] menuItems = {
                "Trang chủ", "Đặt Món", "Thực đơn", "Nhân viên",
                "Khách hàng", "Hóa đơn", "Kho", "Doanh thu", "Thoát"
        };

        for (String item : menuItems) {
            JButton btnMenu = createMenuButton(item);
            btnMenu.addActionListener(e -> {
                if (item.equals("Thoát")) System.exit(0);
                else {
                    cardLayout.show(pnlContent, item);
                    updateActiveButton(item);
                }
            });
            menuButtons.put(item, btnMenu);
            pnlSidebar.add(btnMenu);
            pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        add(pnlSidebar, BorderLayout.WEST);

        cardLayout.show(pnlContent, "Trang chủ");
        updateActiveButton("Trang chủ");
    }

    // GETTER cho Controller
    public NhanVienView getNhanVienView() { return nhanVienView; }
    public KhachHangView getKhachHangView(){ return khachHangView; }
    public HoaDonView getHoaDonView(){ return hoaDonView; }
    public DatMonView getDatMonView(){ return datMonView; }
    public ThucDonView getThucDonView(){ return qlThucDonView; }
    public KhoView getKhoView(){return khoView;}
    public DoanhThuView getDoanhThuView(){return doanhThuView;}

    // =====================================
    //           PHÂN QUYỀN
    // =====================================
    public void setRole(String role) {

        // Đổi text hiển thị role
        lblRole.setText(role);

        if (role.equals("NHÂN VIÊN")) {

            hideMenu("Nhân viên");
            hideMenu("Kho");
            hideMenu("Doanh thu");
        }
    }

    private void hideMenu(String name) {
        JButton btn = menuButtons.get(name);
        if (btn != null) btn.setVisible(false);
    }


    private JPanel createTrangChuPanel() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Trang Chủ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 30));
        pnl.add(lbl);
        return pnl;
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(Color.GRAY);
        pnl.add(lbl);
        return pnl;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBackground(SIDEBAR_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 40, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0, 77, 77)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(SIDEBAR_COLOR); }
        });

        return btn;
    }

    private void updateActiveButton(String activeName) {
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            JButton btn = entry.getValue();
            if (entry.getKey().equals(activeName)) {
                btn.setForeground(ACCENT_RED);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            } else {
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            }
            btn.setBackground(SIDEBAR_COLOR);
        }
    }
}
