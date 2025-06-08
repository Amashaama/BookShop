/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import static gui.AdminSignIn.logger;
import static gui.Invoice.logger;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import model.MySQL2;
import model.ReturnProductItem;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import java.util.logging.*;

public class ReturnProductManagement extends javax.swing.JPanel {

       private static HashMap<String, String> conditionMap = new HashMap<>();
    HashMap<String, ReturnProductItem> returnProductMap = new HashMap<>();
    
    static final Logger logger = SignIn.getLoggerObject();

   private final String loginEmpEmail;
    public ReturnProductManagement(String email) {
          try {
            
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
          
            SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();
        
        initComponents();
        
        
        loadCondition();
        loadInvoiceItem();
        generateReturnId();
        initializeCurrentPage();
        this.loginEmpEmail = email;
        jTextField5.setText(loginEmpEmail);
    }
    
     private void initializeCurrentPage() {
        logger.info("Return Product Management Page accessed");
    }

    private void loadInvoiceItem() {
        try {

            String stockId = jTextField1.getText();

            String invoiceId = jTextField2.getText();

            String query = "SELECT * FROM `invoice_item` INNER JOIN `stock` ON `stock`.`id`=`invoice_item`.`stock_id` INNER JOIN `invoice` ON `invoice`.`id`=`invoice_item`.`invoice_id` ";

            List<String> conditions = new ArrayList<>();

            if (!stockId.isEmpty()) {

                conditions.add("`invoice_item`.`stock_id` LIKE '" + stockId + "%'");

            }

            if (!invoiceId.isEmpty()) {
                conditions.add("`invoice_item`.`invoice_id` LIKE '" + invoiceId + "%'");
            }

            conditions.add("`return_statement` = 'yes'");

            if (!conditions.isEmpty()) {
                query += " WHERE" + String.join(" AND", conditions);
            }

            //  query +=" AND `return_statement`='NULL' ";
            ResultSet resultSet = MySQL2.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("invoice_item.invoice_id"));
                vector.add(resultSet.getString("invoice.employee_email"));
                vector.add(resultSet.getString("invoice_item.stock_id"));
                vector.add(resultSet.getString("stock.price"));
                vector.add(resultSet.getString("invoice_item.qty"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCondition() {

        try {

            ResultSet resultset = MySQL2.executeSearch("SELECT * FROM `product_condition`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultset.next()) {

                vector.add(resultset.getString("name"));
                conditionMap.put(resultset.getString("name"), resultset.getString("id"));

            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(dcm);

        } catch (Exception e) {
        }

    }
    private double total;

    private void loadReturnProductItem() {

        DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
        model2.setRowCount(0);
        total = 0;
        // double total = 0;

        for (ReturnProductItem returnProductItem : returnProductMap.values()) {
            Vector<String> vector = new Vector<>();

            vector.add(returnProductItem.getReturnID());
            vector.add(returnProductItem.getCondition());
            vector.add(returnProductItem.getStockID());
            vector.add(returnProductItem.getInvoiceID());
            vector.add(returnProductItem.getQty());
            vector.add(String.valueOf(returnProductItem.getStockPrice()));
            model2.addRow(vector);

            double itemTotal = Double.parseDouble(returnProductItem.getQty()) * returnProductItem.getStockPrice();
            total += itemTotal;
            jFormattedTextField1.setText(String.valueOf(total));

        }

    }

    private void searchReturnProductItem(String rid) {
        try {
            ResultSet rs = MySQL2.executeSearch("SELECT * FROM `returnproduct` INNER JOIN `product_condition` ON `product_condition`.`id`=`returnproduct`.`product_condition_id` WHERE `returnId` LIKE '" + rid + "%' ");

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> vector = new Vector<>();

                vector.add(rs.getString("returnId"));
                vector.add(rs.getString("product_condition.name"));
                vector.add(rs.getString("stock_id"));
                vector.add(rs.getString("invoice_id"));
                vector.add(rs.getString("qty"));
                vector.add(rs.getString("returnPriceDiscount"));
                model.addRow(vector);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String returnId;

    private void generateReturnId() {
        long timestamp = System.currentTimeMillis();
        long randomNum = new Random().nextInt(9999);

        String returnId = timestamp + "-" + randomNum;
        this.returnId = returnId;
        jTextField4.setText(String.valueOf(returnId));
    }

    public class CustomJasperViewer extends JasperViewer {

        public CustomJasperViewer(JasperPrint jasperPrint, boolean view) {
            super(jasperPrint, view);
            setAlwaysOnTop(true); // Ensure the window is always on top
        }
    }
    
       private void clearAll() {
        generateReturnId();
         jComboBox1.setSelectedIndex(0);
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTable1.clearSelection();
        jTable2.clearSelection();
        jFormattedTextField1.setText("");   
        returnProductMap.clear();
        loadInvoiceItem();
        loadReturnProductItem();
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
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        saveReturnInvoiceButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(950, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 128, 255));
        jLabel6.setText("Employee email");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(623, 18, -1, -1));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(389, 57, 216, -1));

        jButton3.setBackground(new java.awt.Color(102, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(204, 204, 255));
        jButton3.setText("Clear");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 204), 5, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(729, 57, 211, 25));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 128, 255));
        jLabel3.setText("Invoic ID");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, 54, -1));

        jTextField4.setEditable(false);
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 15, 199, -1));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 58, 199, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 128, 255));
        jLabel9.setText("ReturnProductID");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 18, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 128, 255));
        jLabel2.setText("Stock ID");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 57, 52, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 128, 255));
        jLabel5.setText("Condition");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 18, 63, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(389, 15, 216, -1));

        jTextField5.setEditable(false);
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(729, 15, 211, -1));

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(179, 153, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(950, 550));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice ID", "Employee Email(sold)", "Stock ID", "Price", "qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 18, 942, 210));

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 204, 204));
        jButton1.setText("Add to Return Product");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 5, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(699, 240, 249, 37));

        jFormattedTextField1.setEditable(false);
        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel2.add(jFormattedTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(281, 240, 174, 20));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Return Price");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, -1, -1));

        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 133, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("QTY");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 37, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Condition", "Stock ID", "Invoice ID", "qty", "stock price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 283, 936, 174));

        saveReturnInvoiceButton.setBackground(new java.awt.Color(102, 102, 102));
        saveReturnInvoiceButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveReturnInvoiceButton.setForeground(new java.awt.Color(255, 204, 204));
        saveReturnInvoiceButton.setText("Save Return Invoice");
        saveReturnInvoiceButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 5, true));
        saveReturnInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveReturnInvoiceButtonActionPerformed(evt);
            }
        });
        jPanel2.add(saveReturnInvoiceButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, 900, 34));

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 204, 204));
        jButton2.setText("Clear All");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 5, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, 110, 37));

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased

        loadInvoiceItem();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        //saveReturnInvoiceButton.setEnabled(false);
        jComboBox1.setSelectedIndex(0);
        //jButton1.setEnabled(false);
        jTable1.clearSelection();
        //generateReturnId();
        //jTable2.setEnabled(false);
        //jTextField4.setEditable(true);
        //jTextField4.setText("");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        String returnProductId = jTextField4.getText();

        searchReturnProductItem(returnProductId);
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        loadInvoiceItem();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int row = jTable1.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            String invoiceid = String.valueOf(jTable1.getValueAt(row, 0));
            String stockid = String.valueOf(jTable1.getValueAt(row, 2));
            String returnPrice = String.valueOf(jTable1.getValueAt(row, 3));
            //   String returnQty =jTextField3.getText();
            //  String invoiceItmQty =  String.valueOf(jTable1.getValueAt(row, 4)) ;
            if (jComboBox1.getSelectedItem().equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a condition", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (jTextField3.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please type a quantity", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!jTextField3.getText().matches("^[1-9]\\d*$") ) {
                JOptionPane.showMessageDialog(this, "Invalid Quantity ", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                Double returnQty = Double.parseDouble(jTextField3.getText().trim());

                Double invoiceItmQty = Double.parseDouble(jTable1.getValueAt(row, 4).toString().trim());
                Double discountPrice = returnQty * Double.parseDouble(returnPrice);

                if (returnQty > invoiceItmQty) {
                    JOptionPane.showMessageDialog(this, "Add valid qty less than " + invoiceItmQty, "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    ReturnProductItem rpi = new ReturnProductItem();
                    rpi.setReturnID(returnId);
                    rpi.setCondition(conditionMap.get(String.valueOf(jComboBox1.getSelectedItem())));
                    rpi.setEmpEmail(String.valueOf(jTextField5.getText()));
                    rpi.setInvoiceID(invoiceid);
                    rpi.setQty(String.valueOf(returnQty));
                    rpi.setStockID(stockid);
                    rpi.setStockPrice(Double.parseDouble(returnPrice));

                    String uniqueKey = invoiceid + "-" + stockid;

                    if (returnProductMap.get(uniqueKey) == null) {

                        returnProductMap.put(uniqueKey, rpi);

                    } else {
                        ReturnProductItem found = returnProductMap.get(uniqueKey);

                        int option = JOptionPane.showConfirmDialog(this,
                            "Do you want to update quantity of the stock(return):" + stockid,
                            "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                        if (option == JOptionPane.YES_OPTION) {

                            Double newQty = Double.parseDouble(found.getQty()) + returnQty;

                            if (newQty > invoiceItmQty) {
                                JOptionPane.showMessageDialog(this, "Add valid qty less than " + invoiceItmQty, "Warning", JOptionPane.WARNING_MESSAGE);

                            } else {
                                found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + returnQty));
                            }
                        }
                    }
                    loadReturnProductItem();

                }

            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int row = jTable2.getSelectedRow();
        if(evt.getClickCount()==2){

            String remInvoiceId= String.valueOf(jTable2.getValueAt(row, 3));
            String remStockId =String.valueOf(jTable2.getValueAt(row, 2));

            String remuniqueKey = remInvoiceId + "-" + remStockId;
            if(returnProductMap.containsKey(remuniqueKey)){
                returnProductMap.remove(remuniqueKey);
                clearAll();
                loadReturnProductItem();

            }else {
                JOptionPane.showMessageDialog(this, "Item not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void saveReturnInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveReturnInvoiceButtonActionPerformed

        if(jFormattedTextField1.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Please add a return product", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{

            //  int row = jTable2.getSelectedRow();

            //     String returnId = this.returnId;
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //    String finalreturnQty = String.valueOf(jTable2.getValueAt(row, 4));
            String empEmail = jTextField5.getText();
            //   String invoiceId = String.valueOf(jTable2.getValueAt(row, 3));
            //    String stockID = String.valueOf(jTable2.getValueAt(row, 2));
            //    String condition = String.valueOf(jTable2.getValueAt(row, 1));
            String returnPriceDiscount = jFormattedTextField1.getText();

            try {

                logger.log(Level.INFO, "New Return Product Added:{0}", returnId);

                for (ReturnProductItem returnPItem : returnProductMap.values()) {

                    //insert to returnProduct table
                    MySQL2.executeIUD("INSERT INTO `returnproduct` "
                        + "(`returnId`, `returnDate`, `qty`, `employee_email`, `invoice_id`, `stock_id`, `product_condition_id`, `returnPriceDiscount`) "
                        + "VALUES ('" + jTextField4.getText() + "', '" + date + "', '" + returnPItem.getQty() + "', '"
                        + empEmail + "', '" + returnPItem.getInvoiceID() + "', '" + returnPItem.getStockID() + "', '"
                        + returnPItem.getCondition() + "', '" + returnPItem.getStockPrice() + "')");

                    //update invoice item
                    if (returnPItem.getCondition().equals("2")) {

                        MySQL2.executeIUD("UPDATE `stock` SET `qty`=`qty`+'" + returnPItem.getQty() + "' WHERE `id`='" + returnPItem.getStockID() + "' ");
                    }

                    MySQL2.executeIUD("UPDATE `invoice_item` SET `return_statement`='no' WHERE `stock_id`='" + returnPItem.getStockID() + "' AND `invoice_id`='" + returnPItem.getInvoiceID() + "' ");
                    loadInvoiceItem();

                }

                //  InputStream s = this.getClass().getResourceAsStream("/reports/returninvoice.jasper");

                String s = "src//reports//returninvoice.jasper";

                HashMap<String, Object> params = new HashMap<>();

                params.put("Parameter1", jTextField4.getText());
                params.put("Parameter2", jTextField5.getText());
                params.put("Parameter3", date);
                params.put("Parameter4", jFormattedTextField1.getText());

                JRTableModelDataSource datasource = new JRTableModelDataSource(jTable2.getModel());

                JasperPrint jasperprint = JasperFillManager.fillReport(s, params, datasource);

                  JasperViewer.viewReport(jasperprint,false);
               

                clearAll();

                // JOptionPane.showMessageDialog(this, "Return Products Saved", "Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {

                logger.log(Level.SEVERE,"Error in adding new Return Product", e);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_saveReturnInvoiceButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTable1.clearSelection();
        jTable2.clearSelection();
        jComboBox1.setSelectedIndex(0);
        generateReturnId();
        loadInvoiceItem();
        clearAll();

    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JButton saveReturnInvoiceButton;
    // End of variables declaration//GEN-END:variables
}
