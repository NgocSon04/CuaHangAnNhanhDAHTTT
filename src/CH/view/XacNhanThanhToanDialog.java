package CH.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class XacNhanThanhToanDialog extends JDialog {
    private JTextField txtTenKhach;
    private JLabel lblTongTienFinal;
    private JButton btnXacNhanIn;
    
    public XacNhanThanhToanDialog(JFrame parent, DefaultTableModel modelGioHang, double tongTien) {
        super(parent, "Xác nhận Thanh Toán", true); // Modal = true để chặn click ra ngoài
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHead = new JPanel(new GridLayout(2, 1));
        pnlHead.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlHead.add(new JLabel("Tên khách hàng:"));
        txtTenKhach = new JTextField("Khách vãng lai");
        pnlHead.add(txtTenKhach);
        add(pnlHead, BorderLayout.NORTH);

        // Center: Xem lại món
        JTable tblReview = new JTable();
        DefaultTableModel modelReview = new DefaultTableModel();
        // Copy cột
        for(int i=0; i<modelGioHang.getColumnCount(); i++) modelReview.addColumn(modelGioHang.getColumnName(i));
        // Copy dòng
        for(int i=0; i<modelGioHang.getRowCount(); i++) {
            Object[] row = new Object[modelGioHang.getColumnCount()];
            for(int j=0; j<modelGioHang.getColumnCount(); j++) row[j] = modelGioHang.getValueAt(i, j);
            modelReview.addRow(row);
        }
        tblReview.setModel(modelReview);
        add(new JScrollPane(tblReview), BorderLayout.CENTER);

        // Footer
        JPanel pnlFoot = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlFoot.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTongTienFinal = new JLabel("Tổng cộng: " + String.format("%,.0f VNĐ", tongTien), SwingConstants.CENTER);
        lblTongTienFinal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongTienFinal.setForeground(Color.RED);
        
        btnXacNhanIn = new JButton("XÁC NHẬN & IN");
        btnXacNhanIn.setBackground(new Color(0, 100, 0));
        btnXacNhanIn.setForeground(Color.RED);
        btnXacNhanIn.setFont(new Font("Arial", Font.BOLD, 14));
        btnXacNhanIn.setPreferredSize(new Dimension(100, 40));
        
        pnlFoot.add(lblTongTienFinal);
        pnlFoot.add(btnXacNhanIn);
        add(pnlFoot, BorderLayout.SOUTH);
    }
    
    public String getTenKhach() { return txtTenKhach.getText(); }
    public void addXacNhanListener(ActionListener al) { btnXacNhanIn.addActionListener(al); }
}