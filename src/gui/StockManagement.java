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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import model.MySQL2;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import java.util.logging.*;

public class StockManagement extends javax.swing.JPanel {

    private static HashMap<String, String> catMap = new HashMap<>();
    static final Logger logger = SignIn.getLoggerObject();

    public StockManagement() {

        try {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        initComponents();

        loadStock();

        SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();

        initializeCurrentPage();
    }

    private void initializeCurrentPage() {
        logger.info("Stock Management Page accessed");
    }

    private Invoice invoice;

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    private Date start = null;
    private Date end = null;

    private void loadStock() {
        try {

            String query = "SELECT * FROM `stock` INNER JOIN `product`"
                    + "ON `stock`.`product_id` = `product`.`id` "
                    + "INNER JOIN `category` ON `category`.`id` = `product`.`category_id` ";

            String pid = jTextField1.getText();
            String pName = jTextField2.getText();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            List<String> conditions = new ArrayList<>();

            if (!pid.isEmpty()) {
                conditions.add("`product`.`id` LIKE '" + pid + "%'");
            }

            if (!pName.isEmpty()) {
                conditions.add("`product`.`title` LIKE '" + pName + "%'");
            }

            if (jDateChooser1.getDate() != null) {
                start = jDateChooser1.getDate();
                conditions.add("`stock`.`date` >= '" + format.format(start) + "'");
            }

            if (jDateChooser2.getDate() != null) {
                end = jDateChooser2.getDate();
                conditions.add("`stock`.`date` <= '" + format.format(end) + "'");
            }

            if (!jFormattedTextField2.getText().isEmpty()) {
                conditions.add("`stock`.`price` >= '" + jFormattedTextField2.getText() + "'");
            }

            if (!jFormattedTextField3.getText().isEmpty()) {
                conditions.add("`stock`.`price` <= '" + jFormattedTextField3.getText() + "'");
            }

            // conditions.add("`stock`.`qty` > '0'");
            String sort = String.valueOf(jComboBox2.getSelectedItem());

            if (sort.equals("Out Of Stock")) {
                conditions.add("`stock`.`qty` = '0'");

                if (!conditions.isEmpty()) {
                    query += " WHERE" + String.join(" AND", conditions);
                }

            } else {

                if (!conditions.isEmpty()) {
                    query += " WHERE" + String.join(" AND", conditions);
                }

                query += "ORDER BY ";
                query = query.replace("WHERE ORDER BY ", "ORDER BY ");
                query = query.replace("AND ORDER BY ", "ORDER BY ");

                if (sort.equals("Stock ID ASC")) {
                    query += "`stock`.`id` ASC";
                } else if (sort.equals("Stock ID DESC")) {
                    query += "`stock`.`id` DESC";
                } else if (sort.equals("Category ASC")) {
                    query += "`category`.`name` ASC";
                } else if (sort.equals("Category DESC")) {
                    query += "`category`.`name` DESC";
                } else if (sort.equals("Name ASC")) {
                    query += "`product`.`title` ASC";
                } else if (sort.equals("Name DESC")) {
                    query += "`product`.`title` DESC";
                } else if (sort.equals("Selling Price ASC")) {
                    query += "`stock`.`price` ASC";
                } else if (sort.equals("Selling Price DESC")) {
                    query += "`stock`.`price` DESC";
                } else if (sort.equals("Quantity ASC")) {
                    query += "`stock`.`qty` ASC";
                } else if (sort.equals("Quantity DESC")) {
                    query += "`stock`.`qty` DESC";
                }

            }

            ResultSet resultSet = MySQL2.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();

                vector.add(resultSet.getString("product.id"));
                vector.add(resultSet.getString("product.title"));

                vector.add(resultSet.getString("stock.id"));
                vector.add(resultSet.getString("category.name"));
                // if (resultSet.getString("qty").equals("0.0")) {
                //    vector.add("Out Of Stock");
                //  } else {
                vector.add(resultSet.getString("qty"));
                // }

                vector.add(resultSet.getString("stock.price"));
                vector.add(resultSet.getString("stock.date"));
                vector.add(resultSet.getString("stock.discount"));

                String disStart = resultSet.getString("stock.discount_start");
                if (disStart == null || disStart.isEmpty()) {
                    vector.add("null");
                } else {
                    vector.add(disStart);
                }

                String disEnd = resultSet.getString("stock.discount_end");
                if (disEnd == null || disEnd.isEmpty()) {
                    vector.add("null");
                } else {
                    vector.add(disEnd);
                }

                model.addRow(vector);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in loading stock details to a Table", e);
            e.printStackTrace();
        }
    }

    public class CustomJasperViewer extends JasperViewer {

        public CustomJasperViewer(JasperPrint jasperPrint, boolean view) {
            super(jasperPrint, view);
            setAlwaysOnTop(true); // Ensure the window is always on top
        }
    }

    private void clearAll() {

        jComboBox2.setSelectedIndex(0);
        jTextField1.setText("");
        jTextField2.setText("");

        jFormattedTextField1.setText("");
        jFormattedTextField2.setText("");
        jFormattedTextField3.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jTable3.clearSelection();
        loadStock();

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
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(950, 650));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(153, 128, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(950, 195));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setPreferredSize(new java.awt.Dimension(950, 133));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 255));
        jLabel3.setText("Sort By");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(127, 75, 60, -1));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stock ID ASC", "Stock ID DESC", "Category ASC", "Category DESC", "Name ASC", "Name DESC", "Selling Price ASC", "Selling Price DESC", "Quantity ASC", "Quantity DESC", "Out Of Stock" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jPanel4.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 72, 203, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 255));
        jLabel4.setText("Product ID");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(127, 21, -1, -1));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel4.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 18, 203, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 255));
        jLabel5.setText("Product Name");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(402, 75, -1, -1));

        jTextField2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
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
        jPanel4.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 72, 182, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 204, 255));
        jLabel6.setText("Filter Products");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 18, 109, -1));

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(153, 153, 255));
        jButton2.setText("Clear All");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 255), 5, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 68, 167, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 153, 255));
        jLabel11.setText("Price From");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(402, 21, 71, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 153, 255));
        jLabel12.setText("Price To ");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(689, 21, -1, -1));

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyReleased(evt);
            }
        });
        jPanel4.add(jFormattedTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 18, 182, -1));

        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyReleased(evt);
            }
        });
        jPanel4.add(jFormattedTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 18, 167, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 948, -1));

        jDateChooser2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser2KeyReleased(evt);
            }
        });
        jPanel1.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 151, 180, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("To");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(562, 151, 25, -1));

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 102, 102));
        jButton3.setText("Search");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 255), 5, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(789, 150, 140, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Selling Price");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 158, -1, -1));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 102, 102));
        jButton1.setText("Update");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 204, 255), 5, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 151, 107, 30));

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel1.add(jFormattedTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 155, 139, -1));

        jDateChooser1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser1KeyReleased(evt);
            }
        });
        jPanel1.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 151, 165, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("From");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(343, 151, -1, -1));

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(153, 128, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(950, 455));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Name", "Stock ID", "Category", "QTY", "Selling Price", "Date", "Discount (%)", "Discount From", "Discount To"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 12, 938, 389));

        jButton6.setBackground(new java.awt.Color(51, 51, 51));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(204, 255, 204));
        jButton6.setText("Print Stock");
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 255, 204), 5, true));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 413, 234, 30));

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int row = jTable3.getSelectedRow();
        

        if (row == -1) {

            JOptionPane.showMessageDialog(this, "Please select a row before update", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            Double newPrice = Double.parseDouble(jFormattedTextField1.getText());
            try {

                ResultSet rs = MySQL2.executeSearch("SELECT * FROM `stock` WHERE `id`='" + String.valueOf(jTable3.getValueAt(row, 2)) + "' AND `price`='" + newPrice + "' ");

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Please change price before update", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    logger.log(Level.INFO, "Stock price Updated:{0}", String.valueOf(jTable3.getValueAt(row, 2)));

                    MySQL2.executeIUD("UPDATE `stock` SET `stock`.`price`='" + newPrice + "' WHERE `stock`.`id`='" + String.valueOf(jTable3.getValueAt(row, 2)) + "' ");
                    loadStock();
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in Updating Stock Price", e);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadStock();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jDateChooser2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser2KeyReleased
        //  loadStock();
    }//GEN-LAST:event_jDateChooser2KeyReleased

    private void jFormattedTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyReleased
        loadStock();
    }//GEN-LAST:event_jFormattedTextField3KeyReleased

    private void jFormattedTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyReleased
        loadStock();
    }//GEN-LAST:event_jFormattedTextField2KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clearAll();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        loadStock();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        loadStock();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadStock();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jDateChooser1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser1KeyReleased
        //    loadStock();
    }//GEN-LAST:event_jDateChooser1KeyReleased
    private StockLocationAdd stockLocation;
    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        int row = jTable3.getSelectedRow();

        String sellingPrice = String.valueOf(jTable3.getValueAt(row, 5));
        jFormattedTextField1.setText(sellingPrice);

        if (evt.getClickCount() == 2) {
            if (invoice != null) {
                invoice.getjTextField3().setText(String.valueOf(jTable3.getValueAt(row, 2)));
                invoice.getjTextField2().setText(String.valueOf(jTable3.getValueAt(row, 1)));
                invoice.getjTextField6().setText(String.valueOf(jTable3.getValueAt(row, 3)));
                invoice.getjTextField5().setText(String.valueOf(jTable3.getValueAt(row, 5)));
                invoice.getjLabel23().setText(String.valueOf(jTable3.getValueAt(row, 4)));
                invoice.getjTextField7().setText(String.valueOf(jTable3.getValueAt(row, 7)));
                invoice.getjTextField8().setText(String.valueOf(jTable3.getValueAt(row, 8)));
                invoice.getjTextField9().setText(String.valueOf(jTable3.getValueAt(row, 9)));
                invoice.getjFormattedTextField1().grabFocus();

                // this.dispose();
                SwingUtilities.getWindowAncestor(this).dispose();

            } else {
                if (stockLocation == null || !stockLocation.isDisplayable()) {
                    stockLocation = new StockLocationAdd(String.valueOf(jTable3.getValueAt(row, 0)), String.valueOf(jTable3.getValueAt(row, 1)), String.valueOf(jTable3.getValueAt(row, 2)), String.valueOf(jTable3.getValueAt(row, 3)));
                    stockLocation.setVisible(true);
                } else {
                    stockLocation.requestFocus();
                    stockLocation.toFront();
                }

            }

        }


    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            //   InputStream s = this.getClass().getResourceAsStream("/reports/stockreport.jasper");
            String path = "src//reports//stockreport.jasper";

            HashMap<String, Object> params = new HashMap<>();

            params.put("Parameter3", sdf.format(date));

            JRTableModelDataSource datasource = new JRTableModelDataSource(jTable3.getModel());

            JasperPrint jasperprint = JasperFillManager.fillReport(path, params, datasource);

            JasperViewer.viewReport(jasperprint,false);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in printing Stock Details", e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
