package CH.view;

import CH.controller.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MainView extends JFrame {

    // ======================
    // CONSTANT
    // ======================
    private final Color SIDEBAR_COLOR = new Color(0, 91, 110);
    private final Color HOVER_COLOR   = new Color(0, 120, 145);
    private final Color ACCENT_RED    = new Color(255, 77, 77);

    private CardLayout cardLayout;
    private JPanel pnlContent;
    private JLabel lblRole;

    private final Map<String, JButton> menuButtons = new HashMap<>();

    // ======================
    // VIEW
    // ======================
    private TrangChuView trangChuView;
    private DatMonView datMonView;
    private ThucDonView thucDonView;
    private NhanVienView nhanVienView;
    private KhachHangView khachHangView;
    private HoaDonView hoaDonView;
    private KhoView khoView;
    private DoanhThuView doanhThuView;

    // ======================
    // CONSTRUCTOR
    // ======================
    public MainView() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng Đồ Ăn Nhanh");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initHeader();
        initContent();
        initSidebar();

        cardLayout.show(pnlContent, "Trang chủ");
        updateActiveButton("Trang chủ");
    }

    // ======================
    // HEADER
    // ======================
    private void initHeader() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG ĐỒ ĂN NHANH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(SIDEBAR_COLOR);

        JPanel pnlUser = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlUser.setBackground(Color.WHITE);
        pnlUser.add(new JLabel("Xin chào, Admin"));

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlUser, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);
    }

    // ======================
    // CONTENT (CARD LAYOUT)
    // ======================
    private void initContent() {
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);

        trangChuView  = new TrangChuView();
        datMonView    = new DatMonView();
        thucDonView   = new ThucDonView();
        nhanVienView  = new NhanVienView();
        khachHangView = new KhachHangView();
        hoaDonView    = new HoaDonView();
        khoView       = new KhoView();
        doanhThuView  = new DoanhThuView();

        new TrangChuController(trangChuView);
        new KhoController(khoView);
        new DoanhThuController(doanhThuView);

        pnlContent.add(trangChuView,  "Trang chủ");
        pnlContent.add(datMonView,    "Đặt Món");
        pnlContent.add(thucDonView,   "Thực đơn");
        pnlContent.add(nhanVienView,  "Nhân viên");
        pnlContent.add(khachHangView, "Khách hàng");
        pnlContent.add(hoaDonView,    "Hóa đơn");
        pnlContent.add(khoView,       "Kho");
        pnlContent.add(doanhThuView,  "Doanh thu");

        add(pnlContent, BorderLayout.CENTER);
    }

    // ======================
    // SIDEBAR
    // ======================
    private void initSidebar() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setPreferredSize(new Dimension(230, 0));
        pnlSidebar.setBackground(SIDEBAR_COLOR);
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));

        lblRole = new JLabel("ADMIN");
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlSidebar.add(lblRole);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        String[] menus = {
                "Trang chủ", "Đặt Món", "Thực đơn", "Nhân viên",
                "Khách hàng", "Hóa đơn", "Kho", "Doanh thu", "Thoát"
        };

        for (String m : menus) {
            JButton btn = createMenuButton(m);
            menuButtons.put(m, btn);
            pnlSidebar.add(btn);
            pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        add(pnlSidebar, BorderLayout.WEST);
    }

    // ======================
    // MENU BUTTON
    // ======================
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setBackground(SIDEBAR_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 25, 0, 0));

        btn.setIcon(loadIcon(text));

        btn.addActionListener(e -> {
            if ("Thoát".equals(text)) {
                if (JOptionPane.showConfirmDialog(
                        this,
                        "Bạn có muốn thoát không?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                ) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else {
                cardLayout.show(pnlContent, text);
                updateActiveButton(text);
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SIDEBAR_COLOR);
            }
        });

        return btn;
    }

    private ImageIcon loadIcon(String name) {
        String path = switch (name) {
            case "Trang chủ"  -> "/CH/icons/house.png";
            case "Đặt Món"    -> "/CH/icons/fast-food.png";
            case "Thực đơn"   -> "/CH/icons/menu.png";
            case "Nhân viên"  -> "/CH/icons/employee.png";
            case "Khách hàng" -> "/CH/icons/rating.png";
            case "Hóa đơn"    -> "/CH/icons/invoice.png";
            case "Kho"        -> "/CH/icons/warehouse.png";
            case "Doanh thu"  -> "/CH/icons/salary.png";
            case "Thoát"      -> "/CH/icons/exit.png";
            default -> null;
        };

        if (path == null) return null;

        Image img = new ImageIcon(getClass().getResource(path))
                .getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void updateActiveButton(String active) {
        menuButtons.forEach((k, v) -> {
            v.setForeground(k.equals(active) ? ACCENT_RED : Color.WHITE);
            v.setFont(new Font("Segoe UI",
                    k.equals(active) ? Font.BOLD : Font.PLAIN, 15));
            v.setBackground(SIDEBAR_COLOR);
        });
    }

    // ======================
    // PHÂN QUYỀN
    // ======================
    public void setRole(String role) {
        lblRole.setText(role);
        if ("NHÂN VIÊN".equals(role)) {
            hideMenu("Nhân viên");
            hideMenu("Kho");
            hideMenu("Doanh thu");
        }
    }

    private void hideMenu(String name) {
        JButton btn = menuButtons.get(name);
        if (btn != null) btn.setVisible(false);
    }
}
