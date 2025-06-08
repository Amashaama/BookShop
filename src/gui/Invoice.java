/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import static gui.AdminSignIn.logger;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import model.GrnItem;
import model.InvoiceItem;
import model.MySQL2;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import java.util.logging.*;

public class Invoice extends javax.swing.JPanel {

    HashMap<String, InvoiceItem> invoiceItemMap = new HashMap<>();
    HashMap<String, String> paymentMethodMap = new HashMap<>();
    static final Logger logger = SignIn.getLoggerObject();

    //stock id
    public JTextField getjTextField3() {
        return jTextField3;
    }

    //brand name
    public JTextField getjTextField6() {
        return jTextField6;
    }

    //product name
    public JTextField getjTextField2() {
        return jTextField2;
    }

    //selling price 
    public JTextField getjTextField5() {
        return jTextField5;
    }

    //product qty
    public JFormattedTextField getjFormattedTextField1() {
        return jFormattedTextField1;
    }

    //available quantity 
    public JLabel getjLabel23() {
        return jLabel23;
    }

    //customer name
    public JTextField getjTextField4() {
        return jTextField4;
    }

    //available loyalty points
    public JLabel getjLabel6() {
        return jLabel6;
    }

    //discount 
    public JTextField getjTextField7() {
        return jTextField7;
    }

    //discount from
    public JTextField getjTextField8() {
        return jTextField8;
    }

    //discount to
    public JTextField getjTextField9() {
        return jTextField9;
    }

    private final String loginEmpEmail;

    public Invoice(String email) {
        try {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();

        initComponents();

        generateInvocieId();
        //   jLabel3.setText(SignIn.getEmployeeEmail());

        loadPaymentMethod();
        initializeCurrentPage();
        //    originalPoints = Double.parseDouble(jLabel6.getText());

        this.loginEmpEmail = email;
        jLabel3.setText(loginEmpEmail);
    }

    private void initializeCurrentPage() {
        logger.info("Invoice Page accessed");
    }

    private void generateInvocieId() {
        long timestamp = System.currentTimeMillis();
        long randomNum = new Random().nextInt(9999);

        String invoiceId = timestamp + "-" + randomNum;
        jTextField1.setText(String.valueOf(invoiceId));
    }
    private double fixedTotal = 0;
    private double total = 0;
    private double discount = 0;
    private double payment = 0;
    private boolean withdrowPoints = false;
    private double balance = 0;
    private String paymentMethod;
    private double typedPoints = 0;

    private double remainPoints = 0;
    private double newPoints = 0;
    private double originalPoints = 0;
    private double finalPoints = 0;

    private void setOriginalPoints(Double points) {
        this.originalPoints = points;
        jLabel6.setText(String.valueOf(originalPoints));
    }

    public void updateOriginalPoint(String points) {

        setOriginalPoints(Double.parseDouble(points));

    }
    
    public void accessCalculate(){
        calculate();
    }

    private void calculate() {

        total = Double.parseDouble(jFormattedTextField2.getText());

        if (paymentField.getText().isEmpty()) {
            payment = 0;
        } else {
            payment = Double.parseDouble(paymentField.getText());
        }

        String discountType = String.valueOf(jComboBox2.getSelectedItem());

        if (discountType.equals("None")) {
            discount = 0;
        } else {
            discount = Double.parseDouble(discountField.getText());
        }

        if (discountType.equals("Percentage")) {
            total -= total * discount / 100;
        } else if (discountType.equals("Fixed")) {
            total -= discount;
        }

        fixedTotal = Double.parseDouble(jFormattedTextField2.getText());

        // Handle loyalty points
        typedPoints = Double.parseDouble(jFormattedTextField3.getText());

        total -= typedPoints;
        balance = payment - total;
        calculateRemainPoint();

        withdrowPoints = true;

        // Calculate new points
        newPoints = calculateNewPoints(fixedTotal);

        finalPoints = remainPoints + newPoints;
        jLabel13.setText(String.valueOf(finalPoints));

        // payment method and calculate balance
        paymentMethod = String.valueOf(jComboBox1.getSelectedItem());
        payment = Double.parseDouble(paymentField.getText());

        if (paymentMethod.equals("Cash")) {
            balance = payment - total;
            paymentField.setEditable(true);

        } else {
            // For card payment
            balance = 0;
            payment = total;
            paymentField.setText(String.valueOf(payment));
            paymentField.setEditable(false);
            printInvoiceButton.setEnabled(true);
        }

        jFormattedTextField5.setText(String.valueOf(balance));

        if (balance < 0) {
            printInvoiceButton.setEnabled(false);

        } else {
            printInvoiceButton.setEnabled(true);

        }

    }

    private void calculateRemainPoint() {

        remainPoints = originalPoints - typedPoints;
        jLabel6.setText(String.valueOf(remainPoints));

    }

    private Double calculateNewPoints(double totalx) {
        if (totalx >= 100000) {
            return 200.00;
        } else if (totalx >= 50000) {
            return 190.00;
        } else if (totalx >= 25000) {
            return 180.00;
        } else if (totalx >= 20000) {
            return 170.00;
        } else if (totalx >= 15000) {
            return 150.00;
        } else if (totalx >= 10000) {
            return 100.00;
        } else if (totalx >= 5000) {
            return 50.00;
        } else {
            return 0.00;
        }
    }

    private void loadInvoiceItem() {

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        total = 0;
        // double total = 0;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = sdf.format(date);

        for (InvoiceItem invoiceItem : invoiceItemMap.values()) {
            Vector<String> vector = new Vector<>();

            vector.add(invoiceItem.getStockID());
            vector.add(invoiceItem.getCategory());
            vector.add(invoiceItem.getName());
            vector.add(String.valueOf(invoiceItem.getQty()));
            vector.add(String.valueOf(invoiceItem.getSellingPrice()));
            vector.add(String.valueOf(invoiceItem.getDiscount()));

            if (currentDate.compareTo(invoiceItem.getDiscountFrom()) >= 0 && currentDate.compareTo(invoiceItem.getDiscountTo()) <= 0 && !jTextField7.getText().equals("0.0")) {

                Double discountedPrice = Double.parseDouble(invoiceItem.getSellingPrice()) - Double.parseDouble(invoiceItem.getSellingPrice()) * invoiceItem.getDiscount() / 100;

                vector.add(String.valueOf(discountedPrice));

                double itemTotal = Double.parseDouble(invoiceItem.getQty()) * discountedPrice;
                total += itemTotal;
                vector.add(String.valueOf(itemTotal));

            } else {
                vector.add("null");

                double itemTotal = Double.parseDouble(invoiceItem.getQty()) * Double.parseDouble(invoiceItem.getSellingPrice());
                total += itemTotal;
                vector.add(String.valueOf(itemTotal));
            }

            // vector.add(invoiceItem.getDiscount());
            model.addRow(vector);

        }

        jFormattedTextField2.setText(String.valueOf(total));

        calculate();
    }

    private void loadPaymentMethod() {
        try {

            ResultSet resulSet = MySQL2.executeSearch("SELECT * FROM `payment_method`");
            Vector<String> vector = new Vector();
            // vector.add("Select");

            while (resulSet.next()) {
                vector.add(resulSet.getString("name"));
                paymentMethodMap.put(resulSet.getString("name"), resulSet.getString("id"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(dcm);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetLoyaltyPoints() {
        typedPoints = 0.0;
        remainPoints = 0.0;
        newPoints = 0.0;
        finalPoints = 0.0;
       jFormattedTextField3.setText("0.00");
      
       jLabel3.setText(String.valueOf("New Points"));
       
       calculate();
        
        

    }

    private void clearAll() {

        total = 0;
        discount = 0;
        fixedTotal = 0;
        payment = 0;
        //  withdrowPoints = false;
        balance = 0;
        finalPoints = 0;
        //added later
        typedPoints = 0;
        remainPoints = 0;
        newPoints = 0;
        originalPoints = 0;
        // added later

        jTextField1.setText("");
        generateInvocieId();
        jTextField3.setText("");
        jFormattedTextField1.setText("");
        jLabel23.setText("");
        jTextField7.setText("");
        jTextField2.setText("");
        jTextField6.setText("");
        jTextField5.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField4.setText("");

        jFormattedTextField2.setText("0.00");
        discountField.setText("0");
        jComboBox2.setSelectedIndex(0);
        jComboBox1.setSelectedIndex(0);
        jFormattedTextField3.setText("0.00");
        jLabel6.setText("0.0");
        paymentField.setText("0");
        jFormattedTextField5.setText("0.00");
        jLabel13.setText("New Point");

        invoiceItemMap.clear();
        loadInvoiceItem();

    }

    public class CustomJasperViewer extends JasperViewer {

        public CustomJasperViewer(JasperPrint jasperPrint, boolean view) {
            super(jasperPrint, view);
            setAlwaysOnTop(true); // Ensure the window is always on top
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        discountField = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        paymentField = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        printInvoiceButton = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(950, 200));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 128, 255));
        jLabel2.setText("Employee");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 12, 60, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("employee details");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 6, 165, 28));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 128, 255));
        jLabel4.setText("In.Number");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 12, -1, -1));

        jTextField1.setEditable(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 9, 240, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 51, 51));
        jLabel15.setText("Discount");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 171, 52, -1));

        jTextField7.setEditable(false);
        jPanel1.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 168, 241, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 128, 255));
        jLabel7.setText("Stock ID");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 92, -1, -1));

        jTextField3.setEditable(false);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 89, 240, -1));

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(204, 204, 255));
        jButton1.setText("Select");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 4, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 240, 31));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 128, 255));
        jLabel8.setText("QTY");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 131, -1, -1));

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
        });
        jPanel1.add(jFormattedTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 128, 128, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 51, 51));
        jLabel23.setText(" ");
        jLabel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 128, 107, 22));

        jTextField2.setEditable(false);
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(408, 9, 216, -1));

        jTextField6.setEditable(false);
        jPanel1.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(408, 46, 216, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 51, 51));
        jLabel16.setText("D.From");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 128, 43, -1));

        jTextField8.setEditable(false);
        jPanel1.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(408, 128, 216, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 51, 51));
        jLabel17.setText("D.To");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 171, 43, -1));

        jTextField9.setEditable(false);
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(408, 168, 216, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 128, 255));
        jLabel9.setText("Category");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 49, 67, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 128, 255));
        jLabel11.setText("Selling Price");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 90, -1, -1));

        jTextField5.setEditable(false);
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(408, 87, 216, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 128, 255));
        jLabel12.setText("Name");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 12, 67, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 128, 255));
        jLabel10.setText("Loyalty Customer NIC ");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 49, -1, -1));

        jTextField4.setEditable(false);
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 46, 172, -1));

        jButton5.setBackground(new java.awt.Color(102, 102, 102));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(204, 204, 255));
        jButton5.setText("Customer Loyalty Account ");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 4, true));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(638, 80, 300, 36));

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 204, 204));
        jButton2.setText("Add to Invocie");
        jButton2.setToolTipText("");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 204), 4, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 147, 302, 31));

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(153, 128, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Category", "Name", "Quantity", "Selling Price", "Discount %", "Discounted Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 858, 150));

        jButton6.setBackground(new java.awt.Color(51, 51, 51));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(204, 204, 255));
        jButton6.setText("remove");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 204), 5));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 60, 70, 36));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Total");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 43, -1));

        jFormattedTextField2.setEditable(false);
        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setText("0");
        jFormattedTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField2ActionPerformed(evt);
            }
        });
        jPanel3.add(jFormattedTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 348, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Discount");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 54, -1));

        discountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        discountField.setText("0");
        discountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountFieldKeyReleased(evt);
            }
        });
        jPanel3.add(discountField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 234, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Payment Method");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 348, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Payment");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 70, -1));

        paymentField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        paymentField.setText("0");
        paymentField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentFieldActionPerformed(evt);
            }
        });
        paymentField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paymentFieldKeyReleased(evt);
            }
        });
        jPanel3.add(paymentField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, 348, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Balance");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        jFormattedTextField5.setEditable(false);
        jFormattedTextField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField5.setText("0");
        jFormattedTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField5ActionPerformed(evt);
            }
        });
        jPanel3.add(jFormattedTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 247, -1));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Percentage", "Fixed" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel3.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 108, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("New Points");
        jLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0), 3));
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 347, -1));

        printInvoiceButton.setBackground(new java.awt.Color(153, 153, 153));
        printInvoiceButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        printInvoiceButton.setForeground(new java.awt.Color(255, 255, 255));
        printInvoiceButton.setText("Print Invoice");
        printInvoiceButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 204), 5, true));
        printInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInvoiceButtonActionPerformed(evt);
            }
        });
        jPanel3.add(printInvoiceButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 350, 31));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("New Points");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Loyalty Points");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 88, -1));

        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.setText("0");
        jFormattedTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyTyped(evt);
            }
        });
        jPanel3.add(jFormattedTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 130, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("0.0");
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3));
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 111, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 160, 500, 290));

        jButton4.setBackground(new java.awt.Color(102, 102, 102));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(204, 204, 255));
        jButton4.setText("View Invoice History");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 5, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 401, 199, 33));

        jButton3.setBackground(new java.awt.Color(102, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 204, 204));
        jButton3.setText("Clear All");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 5, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(229, 400, 199, 34));

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed
    private JFrame stock;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (stock == null || !stock.isDisplayable()) {
            stock = new JFrame("Stock Management");
            stock.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            stock.setSize(950, 650);
            stock.setLocationRelativeTo(null);

            StockManagement sm1 = new StockManagement();
            sm1.setInvoice(this);

            stock.setContentPane(sm1);

            stock.setVisible(true);
        } else {
            stock.requestFocus();
            stock.toFront();

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased

    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed
    private JFrame crv;
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if (crv == null || !crv.isDisplayable()) {
            crv = new JFrame("Customer Management");
            crv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            crv.setSize(950, 650);
            crv.setLocationRelativeTo(null);

            CustomerRegistration cr = new CustomerRegistration();
            cr.setInvoice(this);

            crv.setContentPane(cr);

            crv.setVisible(true);
            
           // resetLoyaltyPoints(); // if i change customer 
          
            

        } else {
            crv.requestFocus();
            crv.toFront();

        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (jTextField3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a stock ", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jFormattedTextField1.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please add a quantity ", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!jFormattedTextField1.getText().matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$")) {
            JOptionPane.showMessageDialog(this, "Invalid Quantity ", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            String stockID = jTextField3.getText();
            String category = jTextField6.getText();
            String productName = jTextField2.getText();
            String qty = jFormattedTextField1.getText();

            String sellinPrice = jTextField5.getText();
            // String discountType = String.valueOf(jComboBox2.getSelectedItem());

            Double discount = Double.parseDouble(jTextField7.getText());
            String discountFrom = jTextField8.getText();
            String discountTo = jTextField9.getText();

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setCategory(category);
            invoiceItem.setName(productName);
            invoiceItem.setQty(qty);
            invoiceItem.setSellingPrice(sellinPrice);
            invoiceItem.setStockID(stockID);
            invoiceItem.setDiscount(discount);
            invoiceItem.setDiscountFrom(discountFrom);
            invoiceItem.setDiscountTo(discountTo);

            if (invoiceItemMap.get(stockID) == null) {
                double validQty = Double.parseDouble(jLabel23.getText());

                if (Double.parseDouble(jFormattedTextField1.getText()) > validQty) {

                    JOptionPane.showMessageDialog(this, "Quantity exceeds available stock! 1", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    invoiceItemMap.put(stockID, invoiceItem); // map ekt data dala
                }

            } else {
                InvoiceItem found = invoiceItemMap.get(stockID);

                double oldQty = Double.parseDouble(found.getQty());
                double newQty = Double.parseDouble(qty);
                double validQty = Double.parseDouble(jLabel23.getText());
                double updatedQty = oldQty + newQty;

                if (updatedQty > validQty) {

                    JOptionPane.showMessageDialog(this, "Quantity exceeds available stock! 2", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {

                    int option = JOptionPane.showConfirmDialog(this,
                            "Do you want to update quantity of the product:" + productName,
                            "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
                    }

                }

            }
            //   loadAvailableQuantity(invoiceItem.getStockID());
            loadInvoiceItem();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showConfirmDialog(this, "Please select a row to remove", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String removeItemStockId = String.valueOf(jTable1.getValueAt(row, 0));

            if (invoiceItemMap.containsKey(removeItemStockId)) {
                invoiceItemMap.remove(removeItemStockId);
                loadInvoiceItem();
            } else {
                JOptionPane.showMessageDialog(this, "Item not found ", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jFormattedTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField2ActionPerformed

    }//GEN-LAST:event_jFormattedTextField2ActionPerformed

    private void discountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountFieldKeyReleased

        //   if (!discountField.getText().matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$|^$")) {
        //      JOptionPane.showMessageDialog(this, "Invalid Discount Price", "Warning", JOptionPane.WARNING_MESSAGE);
        //   } else {
        calculate();
        //   }

    }//GEN-LAST:event_discountFieldKeyReleased

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        calculate();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void paymentFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentFieldActionPerformed

    private void paymentFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentFieldKeyReleased
        //  if (!paymentField.getText().matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$|^$")) {
        //      JOptionPane.showMessageDialog(this, "Invalid Discount Price", "Warning", JOptionPane.WARNING_MESSAGE);
        //  } else {
        calculate();
        //  }
    }//GEN-LAST:event_paymentFieldKeyReleased

    private void jFormattedTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField5ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        calculate();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void printInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvoiceButtonActionPerformed
        try {

            String invoiceID = jTextField1.getText();

            String employeeEmail = jLabel3.getText();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String paidAmount = jFormattedTextField2.getText();
            String paymentMethod = paymentMethodMap.get(String.valueOf(jComboBox1.getSelectedItem()));
            //    String balance = jFormattedTextField5.getText();
            String discount = discountField.getText();

            if (jFormattedTextField2.getText().equals("0")) {
                JOptionPane.showMessageDialog(this, "Please add products", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                if (String.valueOf(jComboBox1.getSelectedItem()).equals("Cash")) {
                    if (paidAmount.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Warning", "Please type paid amount ", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (!discountField.getText().matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$")) {
                    JOptionPane.showMessageDialog(this, "Invalid Price ", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (!discountField.getText().matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$")) {
                    JOptionPane.showMessageDialog(this, "Invalid Price ", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (!paymentField.getText().matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$")) {
                    JOptionPane.showMessageDialog(this, "Invalid Payment Price ", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                logger.log(Level.INFO, "New Invoice Record Added:{0}", invoiceID);

                // insert to invoice
                if (jTextField4.getText().isEmpty()) {
                    MySQL2.executeIUD("INSERT INTO `invoice` (`id`,`employee_email`,`date`,`paid_amount`,`payment_method_id`,`discount`) VALUES ('" + invoiceID + "','" + employeeEmail + "','" + date + "','" + paidAmount + "','" + paymentMethod + "','" + discount + "') ");
                } else {
                    MySQL2.executeIUD("INSERT INTO `invoice` VALUES ('" + invoiceID + "','" + employeeEmail + "','" + date + "','" + paidAmount + "','" + paymentMethod + "','" + discount + "','" + jTextField4.getText() + "') ");
                }

                // insert to invoice item
                for (InvoiceItem invoiceItem : invoiceItemMap.values()) {

                    //insert into invoice item
                    MySQL2.executeIUD("INSERT INTO `invoice_item` (`stock_id`,`qty`,`invoice_id`,`return_statement`)"
                            + "VALUES('" + invoiceItem.getStockID() + "','" + invoiceItem.getQty() + "','" + invoiceID + "','yes')");

                    //stock update
                    MySQL2.executeIUD("UPDATE `stock` SET `qty`=`qty`-'" + invoiceItem.getQty() + "' "
                            + "WHERE `id`='" + invoiceItem.getStockID() + "'");

                }

                //new points generate
                //customer points update
                if (withdrowPoints) {
                    MySQL2.executeIUD("UPDATE `customer` SET `points`='" + finalPoints + "' WHERE `NIC`='" + jTextField4.getText() + "' ");

                }

                //view or print
                String s = "src//reports//bookshop.jasper";
                // InputStream s = this.getClass().getResourceAsStream("/reports/myshop.jasper");
                //   InputStream s = this.getClass().getResourceAsStream("/reports/bookshop.jasper");

                HashMap<String, Object> params = new HashMap<>();

                params.put("Parameter1", jTextField1.getText());
                params.put("Parameter2", jLabel3.getText());
                params.put("Parameter3", date);
                params.put("Parameter4", jFormattedTextField2.getText());
                params.put("Parameter5", discountField.getText());
                params.put("Parameter6", String.valueOf(jComboBox1.getSelectedItem()));
                params.put("Parameter7", paymentField.getText());
                params.put("Parameter8", jFormattedTextField5.getText());
                params.put("Parameter9", jFormattedTextField3.getText());
                
                
                if(!jTextField4.getText().isEmpty()){
                    params.put("Parameter10", jLabel13.getText());
                     params.put("Parameter11", jTextField4.getText());
                }else{
                    params.put("Parameter10", "null");
                     params.put("Parameter11", "null");
                }

                JRTableModelDataSource datasource = new JRTableModelDataSource(jTable1.getModel());

                JasperPrint jasperprint = JasperFillManager.fillReport(s, params, datasource);

                JasperViewer.viewReport(jasperprint,false);
                clearAll();

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in adding new Invoice Reocrd", e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_printInvoiceButtonActionPerformed

    private void jFormattedTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyReleased
        if (!jFormattedTextField3.getText().matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$|^$")) {
            JOptionPane.showMessageDialog(this, "Invalid points amount", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            calculate();

        }
    }//GEN-LAST:event_jFormattedTextField3KeyReleased

    private void jFormattedTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyTyped

    }//GEN-LAST:event_jFormattedTextField3KeyTyped
    private InvoiceHistory ih;
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (ih == null || !ih.isDisplayable()) {
            ih = new InvoiceHistory();
            ih.setVisible(true);
        } else {
            ih.toFront();
            ih.requestFocus();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clearAll();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField discountField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JFormattedTextField paymentField;
    private javax.swing.JButton printInvoiceButton;
    // End of variables declaration//GEN-END:variables
}
