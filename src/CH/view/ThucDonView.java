package CH.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import CH.model.MonAn;

public class ThucDonView extends JPanel {
    private JTextField txtMaMon, txtTenMon, txtDonGia, txtDVT;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnReset;

    public ThucDonView() {
        setLayout(new BorderLayout());
        
        // --- FORM ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(new Color(0, 77, 77));
        pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;

        addInput(pnlForm, gbc, 0, 0, "Mã món", txtMaMon = new JTextField(15));
        txtMaMon.setEditable(false); txtMaMon.setText("Tự động");
        
        addInput(pnlForm, gbc, 0, 1, "Tên món", txtTenMon = new JTextField(15));
        addInput(pnlForm, gbc, 0, 2, "Đơn giá", txtDonGia = new JTextField(15));
        addInput(pnlForm, gbc, 0, 3, "Đơn vị tính", txtDVT = new JTextField(15)); // Ly, Đĩa...

        // Buttons
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
        String[] cols = {"Mã món", "Tên món", "Đơn giá", "Đơn vị tính"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void addInput(JPanel p, GridBagConstraints gbc, int x, int y, String lbl, Component cmp) {
        gbc.gridx = x; gbc.gridy = y; 
        JLabel l = new JLabel(lbl); l.setForeground(Color.WHITE); p.add(l, gbc);
        gbc.gridx = x+1; p.add(cmp, gbc);
    }

    public MonAn getMonAnInfo() {
        double gia = 0;
        try { gia = Double.parseDouble(txtDonGia.getText()); } catch (Exception e) {}
        return new MonAn(txtMaMon.getText(), txtTenMon.getText(), gia, txtDVT.getText());
    }

    public void fillForm(MonAn m) {
        txtMaMon.setText(m.getMaMon()); txtTenMon.setText(m.getTenMon());
        txtDonGia.setText(String.format("%.0f", m.getDonGia())); txtDVT.setText(m.getDonViTinh()); // Sửa getDonViTinh -> lấy từ model
    }
    
    public void clearForm() {
        txtMaMon.setText("Tự động"); txtTenMon.setText(""); txtDonGia.setText(""); txtDVT.setText("");
    }
    
    // Getters & Listeners
    public void addRow(MonAn m) { model.addRow(m.toObjectArray()); }
    public void clearTable() { model.setRowCount(0); }
    public int getSelectedRow() { return table.getSelectedRow(); }
    public JTable getTable() { return table; }
    public void addThemListener(ActionListener al) { btnThem.addActionListener(al); }
    public void addSuaListener(ActionListener al) { btnSua.addActionListener(al); }
    public void addXoaListener(ActionListener al) { btnXoa.addActionListener(al); }
    public void addResetListener(ActionListener al) { btnReset.addActionListener(al); }
}