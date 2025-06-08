/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/AWTForms/Panel.java to edit this template
 */
package gui;

import java.sql.ResultSet;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import java.util.logging.*;

/**
 *
 * @author Anne
 */
public class ProductManagement extends javax.swing.JPanel {

    private static HashMap<String, String> catMap = new HashMap<>();

    static final Logger logger = SignIn.getLoggerObject();

    GrnManagement grn;

    public void setGrnManagement(GrnManagement grn) {

        this.grn = grn;

    }

    public ProductManagement() {
        try {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();

        initComponents();
        loadCategory();
        loadProducts();
        initializeCurrentPage();
    }

    private void initializeCurrentPage() {
        logger.info("Product Managment Page accessed");
    }

    private void loadCategory() {
        try {

            ResultSet resultSet = MySQL2.executeSearch("SELECT * FROM `category`");
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {

                vector.add(resultSet.getString("name"));
                catMap.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(dcm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateProductId() {

        long timestamp = System.currentTimeMillis();
        long randomNum = new Random().nextInt(9999);

        return timestamp + "-" + randomNum;

    }

    private void loadProducts() {

        try {

            ResultSet rs = MySQL2.executeSearch("SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` ");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(rs.getString("product.id"));
                vector.add(rs.getString("title"));
                vector.add(rs.getString("author"));
                vector.add(rs.getString("category.name"));
                dtm.addRow(vector);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in loading product table", e);
            e.printStackTrace();
        }

    }

    private void searchProduct(String sort) {

        String query = "SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` ";

        if (sort.equals("ID DESC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `product`.`id` DESC";
            } else {
                query += " WHERE `product`.`id` LIKE '" + jTextField5.getText() + "%' ORDER BY `product`.`id` DESC ";
            }

        } else if (sort.equals("ID ASC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `product`.`id` ASC";
            } else {
                query += " WHERE `product`.`id` LIKE '" + jTextField5.getText() + "%' ORDER BY `product`.`id` ASC";
            }
        } else if (sort.equals("Title DESC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `product`.`title` DESC";
            } else {
                query += " WHERE `product`.`title` LIKE '" + jTextField5.getText() + "%' ORDER BY `product`.`title` DESC";
            }

        } else if (sort.equals("Title ASC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `product`.`title` ASC";
            } else {
                query += " WHERE `product`.`title` LIKE '" + jTextField5.getText() + "%' ORDER BY `product`.`title` ASC";
            }

        } else if (sort.equals("Author DESC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `product`.`author` DESC";
            } else {
                query += " WHERE `product`.`author` LIKE '" + jTextField5.getText() + "%' ORDER BY `product`.`author` DESC";
            }
        } else if (sort.equals("Author ASC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `product`.`author` ASC";
            } else {
                query += " WHERE `product`.`author` LIKE '" + jTextField5.getText() + "%' ORDER BY `product`.`author` ASC";
            }
        } else if (sort.equals("Category DESC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `category`.`name` DESC";
            } else {
                query += " WHERE `category`.`name` LIKE '" + jTextField5.getText() + "%' ORDER BY `category`.`name` DESC";
            }
        } else if (sort.equals("Category ASC")) {

            if (jTextField5.getText().isEmpty()) {
                query += "ORDER BY `category`.`name` ASC";
            } else {
                query += " WHERE `category`.`name` LIKE '" + jTextField5.getText() + "%' ORDER BY `category`.`name` ASC";
            }
        }

        try {
            ResultSet rs = MySQL2.executeSearch(query);

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {

                Vector<String> vector = new Vector<>();
                vector.add(rs.getString("product.id"));
                vector.add(rs.getString("title"));
                vector.add(rs.getString("author"));
                vector.add(rs.getString("category.name"));
                dtm.addRow(vector);

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in loading searched product table", e);
            e.printStackTrace();

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 219, 251, 33));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 255));
        jLabel3.setText("Title");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 103, 37, -1));

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 100, 252, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 255));
        jLabel4.setText("Author");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 161, 62, -1));

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 161, 252, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 255));
        jLabel5.setText("Category");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 224, -1, -1));

        jTextField4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 283, 185, -1));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 153));
        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(279, 283, 54, -1));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Add Product");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(185, 162, 242), 3, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 368, 260, 28));

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Update Product");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(185, 162, 242), 3, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 414, 260, 28));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Remove Product");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(185, 162, 242), 3, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 460, 260, 28));

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Clear All");
        jButton7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(185, 162, 242), 3, true));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 506, 260, 28));

        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(new java.awt.Color(185, 162, 242));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID DESC", "ID ASC", "Title DESC", "Title ASC", "Author DESC", "Author ASC", "Category DESC", "Category ASC" }));
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
        jPanel2.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 30, 139, -1));

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(185, 162, 242));
        jButton6.setText("Print Report");
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 70, 190, 36));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Search Product By :");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 30, -1, -1));

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 303, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Title", "Author", "Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 124, 591, 509));

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String new_cat = jTextField4.getText();
        
        if(new_cat.isEmpty()){
             JOptionPane.showMessageDialog(this, "Please add a new category", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{

        try {
            ResultSet rs = MySQL2.executeSearch("SELECT * FROM `category` WHERE `name`='" + new_cat + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Category already exist", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                MySQL2.executeIUD("INSERT INTO `category` (`name`) VALUES ('" + new_cat + "') ");
                JOptionPane.showMessageDialog(this, "New Category Added", "Information", JOptionPane.INFORMATION_MESSAGE);
                loadCategory();
                jTextField4.setText("");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String id = generateProductId();
        String title = jTextField2.getText();
        String author = jTextField3.getText();
        String category = String.valueOf(jComboBox1.getSelectedItem());

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please type title of the book", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please type the name of the Author", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (category.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a Category", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                ResultSet rs = MySQL2.executeSearch("SELECT * FROM `product` INNER JOIN `category`"
                        + " ON `category`.`id`=`product`.`category_id` WHERE"
                        + " `product`.`author` ='" + author + "' AND `product`.`title`='" + title + "' ");

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Book already Registered", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    logger.log(Level.INFO, "Add new product:{0}", id);
                    MySQL2.executeIUD("INSERT INTO `product`"
                            + " (`id`,`title`,`author`,`category_id`) "
                            + "VALUES ('" + id + "','" + title + "','" + author + "','" + catMap.get(category) + "') ");
                    loadProducts();
                    clearArea();
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in adding new product", e);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int row = jTable1.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String title = jTextField2.getText();
            String author = jTextField3.getText();
            String cat = String.valueOf(jComboBox1.getSelectedItem());

            if (title.equals(String.valueOf(jTable1.getValueAt(row, 1))) && author.equals(String.valueOf(jTable1.getValueAt(row, 2))) && cat.equals(String.valueOf(jTable1.getValueAt(row, 3)))) {
                JOptionPane.showMessageDialog(this, "Please change details to update", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    logger.log(Level.INFO, "Update product:{0}", String.valueOf(jTable1.getValueAt(row, 0)));

                    MySQL2.executeIUD("UPDATE `product` SET"
                            + " `title`='" + title + "' ,`author`='" + author + "' ,`category_id`='" + catMap.get(cat) + "' WHERE"
                            + " `id`='" + String.valueOf(jTable1.getValueAt(row, 0)) + "'  ");
                    loadProducts();
                    clearArea();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error in Updating product", e);
                    e.printStackTrace();

                }

            }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int row = jTable1.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to Remove", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                logger.log(Level.INFO, "Delete product:{0}", String.valueOf(jTable1.getValueAt(row, 0)));
                MySQL2.executeIUD("DELETE FROM `product` WHERE `id`='" + String.valueOf(jTable1.getValueAt(row, 0)) + "'  ");
                loadProducts();
                clearArea();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in Deleting product", e);
                e.printStackTrace();

            }

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jButton1.setEnabled(true);
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTable1.clearSelection();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        String sort = String.valueOf(jComboBox2.getSelectedItem());
        searchProduct(sort);
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            //   InputStream s = this.getClass().getResourceAsStream("/reports/donereport.jasper");
            String path = "src//reports//donereport.jasper";

            HashMap<String, Object> params = new HashMap<>();

            params.put("Parameter3", sdf.format(date));

            JRTableModelDataSource datasource = new JRTableModelDataSource(jTable1.getModel());

            JasperPrint jasperprint = JasperFillManager.fillReport(path, params, datasource);

             JasperViewer.viewReport(jasperprint,false);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in printing product details", e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        String sort = String.valueOf(jComboBox2.getSelectedItem());
        searchProduct(sort);
    }//GEN-LAST:event_jTextField5KeyReleased
    private ProductGenre pg;
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.getSelectedRow();

        jTextField2.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        jTextField3.setText(String.valueOf(jTable1.getValueAt(row, 2)));
        jComboBox1.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 3)));

      //  jButton1.setEnabled(false);
        jButton2.setEnabled(false);

        if (evt.getClickCount() == 2) {
            if (grn != null) {

                grn.getjTextField4().setText(String.valueOf(jTable1.getValueAt(row, 0)));
                grn.getjTextField5().setText(String.valueOf(jTable1.getValueAt(row, 1)));
                grn.getjTextField6().setText(String.valueOf(jTable1.getValueAt(row, 3)));

                try {

                    ResultSet rs = MySQL2.executeSearch("SELECT * FROM `product_has_genre`"
                            + " INNER JOIN `genre` ON `genre`.`id`=`product_has_genre`.`genre_id`"
                            + " WHERE `product_id`='" + String.valueOf(jTable1.getValueAt(row, 0)) + "' ");

                    String genres = "";

                    while (rs.next()) {
                        genres += rs.getString("genre.name") + ",";
                    }

                    if (!genres.isEmpty()) {
                        genres = genres.substring(0, genres.length() - 1);
                    }

                    grn.getjTextArea1().setText(genres);
                    SwingUtilities.getWindowAncestor(this).dispose();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                int row1 = jTable1.getSelectedRow();
                String id = String.valueOf(jTable1.getValueAt(row1, 0));
                String title = String.valueOf(jTable1.getValueAt(row, 1));
                String category = String.valueOf(jTable1.getValueAt(row, 3));

                if (pg == null || !pg.isDisplayable()) {
                    JFrame parentFrame1 = (JFrame) SwingUtilities.getWindowAncestor(this);
                    pg = new ProductGenre(parentFrame1, true, id, title, category);
                    pg.setVisible(true);
                } else {
                    pg.requestFocus();
                    pg.toFront();
                }
            }

        }

    }//GEN-LAST:event_jTable1MouseClicked
    private void clearArea() {
        jTextField2.setText("");
        jTextField3.setText("");
        jComboBox1.setSelectedIndex(0);
        jTable1.clearSelection();
        jButton2.setEnabled(true);
        
    }

    public class CustomJasperViewer extends JasperViewer {

        public CustomJasperViewer(JasperPrint jasperPrint, boolean view) {
            super(jasperPrint, view);
            setAlwaysOnTop(true); // Ensure the window is always on top
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
