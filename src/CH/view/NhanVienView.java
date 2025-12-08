package CH.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import CH.model.NhanVien;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NhanVienView extends JPanel {

    // Colors
    private final Color TEAL_COLOR = new Color(0, 77, 77);
    
    // Components
    private JTextField txtMaNV, txtTenNV, txtChucVu, txtSDT, txtDiaChi, txtTimKiem;
    private JDateChooser txtNgaySinh;
    private JRadioButton rdoNam, rdoNu;
    private ButtonGroup btnGroupGender;
    private JTable tableNhanVien;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnTimKiem;

    public NhanVienView() {
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // 1. Form Area
        JPanel pnlFormContainer = new JPanel();
        pnlFormContainer.setLayout(new BoxLayout(pnlFormContainer, BoxLayout.Y_AXIS));
        pnlFormContainer.setBackground(TEAL_COLOR);
        pnlFormContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblFormTitle = new JLabel("Thông tin nhân viên");
        lblFormTitle.setForeground(Color.WHITE);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFormTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlFormContainer.add(lblFormTitle);
        pnlFormContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(TEAL_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(pnlForm, gbc, 0, "Mã nhân viên", txtMaNV = new JTextField(15));
        txtMaNV.setEditable(false); // Không cho nhập tay
        txtMaNV.setBackground(new Color(230, 230, 230)); // Đổi màu xám nhẹ để biết là read-only
        txtMaNV.setText("Tự động sinh"); // Giá trị mặc định
        
        addFormRow(pnlForm, gbc, 2, "Tên nhân viên", txtTenNV = new JTextField(15));
        
        gbc.gridx = 4; gbc.weightx = 0; gbc.fill = GridBagConstraints.BOTH;
        JLabel lblNgaySinh = new JLabel("Ngày sinh"); lblNgaySinh.setForeground(Color.WHITE);
        pnlForm.add(lblNgaySinh, gbc);
        gbc.gridx = 5; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNgaySinh = new JDateChooser(); 
        txtNgaySinh.setDateFormatString("dd/MM/yyyy"); // Định dạng ngày Việt Nam
        txtNgaySinh.setPreferredSize(new Dimension(150, 25));
        pnlForm.add(txtNgaySinh, gbc);
        gbc.weightx = 0;
        
        addFormRow(pnlForm, gbc, 6, "Chức vụ", txtChucVu = new JTextField(15));

        // Row 2: Giới tính
        gbc.gridy = 1; 
        
        gbc.gridx = 0; gbc.weightx = 0;
        JLabel lblGender = new JLabel("Giới tính");
        lblGender.setForeground(Color.WHITE);
        pnlForm.add(lblGender, gbc);

        JPanel pnlGender = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlGender.setBackground(TEAL_COLOR);
        pnlGender.setBorder(new LineBorder(Color.WHITE));
        rdoNam = new JRadioButton("Nam"); rdoNam.setBackground(TEAL_COLOR); rdoNam.setForeground(Color.WHITE);
        rdoNu = new JRadioButton("Nữ"); rdoNu.setBackground(TEAL_COLOR); rdoNu.setForeground(Color.WHITE);
        btnGroupGender = new ButtonGroup(); btnGroupGender.add(rdoNam); btnGroupGender.add(rdoNu);
        pnlGender.add(rdoNam); pnlGender.add(rdoNu);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlForm.add(pnlGender, gbc);

        addFormRow(pnlForm, gbc, 2, "Số điện thoại", txtSDT = new JTextField(15));
        addFormRow(pnlForm, gbc, 4, "Địa chỉ", txtDiaChi = new JTextField(15));

        pnlFormContainer.add(pnlForm);

        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(TEAL_COLOR);
        btnThem = createStyledButton("Thêm"); btnSua = createStyledButton("Sửa"); btnXoa = createStyledButton("Xóa"); btnReset = createStyledButton("RESET");
        pnlButtons.add(btnThem); pnlButtons.add(btnSua); pnlButtons.add(btnXoa); pnlButtons.add(btnReset);
        pnlFormContainer.add(pnlButtons);

        // Search
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(TEAL_COLOR);
        txtTimKiem = new JTextField(15);
        btnTimKiem = createStyledButton("Tìm kiếm");
        pnlSearch.add(new JLabel("Tên NV: ")).setForeground(Color.WHITE);
        pnlSearch.add(txtTimKiem); pnlSearch.add(btnTimKiem);
        pnlFormContainer.add(pnlSearch);

        add(pnlFormContainer, BorderLayout.NORTH);

        // 2. Table Area
        String[] columnNames = {"Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Giới tính", "Chức vụ", "Số điện thoại", "Địa chỉ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(25);
        tableNhanVien.getTableHeader().setBackground(new Color(230, 230, 230));
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(tableNhanVien);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Helper methods (Copy từ MainView sang)
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int x, String labelText, Component field) {
        gbc.gridx = x; gbc.weightx = 0; gbc.fill = GridBagConstraints.BOTH;
        JLabel label = new JLabel(labelText); label.setForeground(Color.WHITE);
        panel.add(label, gbc);
        gbc.gridx = x + 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(new Dimension(150, 25));
        panel.add(field, gbc);
        gbc.weightx = 0;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE); btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(80, 30)); btn.setFocusPainted(false);
        return btn;
    }

// [SỬA] Cập nhật hàm lấy dữ liệu từ Form
    public NhanVien getNhanVienInfo() {
        String gt = rdoNam.isSelected() ? "Nam" : "Nữ";
        
        String strNgaySinh = "";
        if (txtNgaySinh.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            strNgaySinh = sdf.format(txtNgaySinh.getDate());
        }

        return new NhanVien(txtMaNV.getText(), txtTenNV.getText(), strNgaySinh, gt, txtChucVu.getText(), txtSDT.getText(), txtDiaChi.getText());
    }

    public void fillForm(NhanVien nv) {
            txtMaNV.setText(nv.getMaNV());
            txtTenNV.setText(nv.getTenNV());

            // Chuyển chuỗi String từ Model thành Date để hiển thị lên JDateChooser
            try {
                if (nv.getNgaySinh() != null && !nv.getNgaySinh().isEmpty()) {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(nv.getNgaySinh());
                    txtNgaySinh.setDate(date);
                } else {
                    txtNgaySinh.setDate(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                txtNgaySinh.setDate(null);
            }

            txtChucVu.setText(nv.getChucVu());
            txtSDT.setText(nv.getSoDienThoai());
            txtDiaChi.setText(nv.getDiaChi());
            if (nv.getGioiTinh() != null && nv.getGioiTinh().equals("Nam")) rdoNam.setSelected(true); else rdoNu.setSelected(true);
    }

    public void clearForm() {
        txtMaNV.setText("Tự động sinh"); 
        txtTenNV.setText("");
        txtNgaySinh.setDate(null);
        txtChucVu.setText(""); txtSDT.setText(""); txtDiaChi.setText("");
        btnGroupGender.clearSelection();
    }
    
    public JTable getTable() { 
        return tableNhanVien; 
    }
    public void clearTable() { 
        tableModel.setRowCount(0); 
    }
    public void addRowToTable(NhanVien nv) { 
        tableModel.addRow(nv.toObjectArray()); 
    }
    public int getSelectedRow() { 
        return tableNhanVien.getSelectedRow(); 
    }
    public void updateRowInTable(NhanVien nv, int row) {
        tableModel.setValueAt(nv.getMaNV(), row, 0); tableModel.setValueAt(nv.getTenNV(), row, 1);
        tableModel.setValueAt(nv.getNgaySinh(), row, 2); tableModel.setValueAt(nv.getGioiTinh(), row, 3);
        tableModel.setValueAt(nv.getChucVu(), row, 4); tableModel.setValueAt(nv.getSoDienThoai(), row, 5);
        tableModel.setValueAt(nv.getDiaChi(), row, 6);
    }
    public void removeRowInTable(int row) { tableModel.removeRow(row); }
    public void addTableSelectionListener(javax.swing.event.ListSelectionListener listener) { tableNhanVien.getSelectionModel().addListSelectionListener(listener); }
    public void addThemListener(ActionListener al) { btnThem.addActionListener(al); }
    public void addSuaListener(ActionListener al) { btnSua.addActionListener(al); }
    public void addXoaListener(ActionListener al) { btnXoa.addActionListener(al); }
    public void addResetListener(ActionListener al) { btnReset.addActionListener(al); }
}