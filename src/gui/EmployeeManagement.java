/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import javax.swing.JOptionPane;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import model.MySQL2;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Arrays;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.swing.JRViewer;
import java.util.logging.*;

/**
 *
 * @author Anne
 */
public class EmployeeManagement extends javax.swing.JPanel {

    private static HashMap<String, String> employeeTypeMap = new HashMap<>();
    private static HashMap<String, String> employeeGenderMap = new HashMap<>();
    private static HashMap<String, String> employeeStatus = new HashMap<>();

    static final Logger logger = SignIn.getLoggerObject();

    public EmployeeManagement() {
        try {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();

        initComponents();

        initializeCurrentPage();
        loadTypes();
        loadGender();
        loadStatus();
        loadEmployees();
    }

    private void initializeCurrentPage() {
        logger.info("Employee Management Page accessed");
    }

    private void loadTypes() {

        try {
            ResultSet resultset = MySQL2.executeSearch("SELECT * FROM `employee_type`");

            Vector vector = new Vector();
            vector.add("Select");

            while (resultset.next()) {

                vector.add(resultset.getString("name"));
                employeeTypeMap.put(resultset.getString("name"), resultset.getString("id"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(dcm);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Employe type loading error", e);
            e.printStackTrace();

        }
    }

    private void loadGender() {

        try {

            ResultSet resultset = MySQL2.executeSearch("SELECT * FROM `gender`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultset.next()) {

                vector.add(resultset.getString("name"));
                employeeGenderMap.put(resultset.getString("name"), resultset.getString("id"));

            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(dcm);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Employe Gender type loading error", e);
            e.printStackTrace();
        }

    }

    private void loadStatus() {
        try {
            ResultSet resultSet = MySQL2.executeSearch("SELECT * FROM `status`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {

                vector.add(resultSet.getString("name"));
                employeeStatus.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox3.setModel(dcm);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Employe Active/Inactive status loading error", e);
            e.printStackTrace();
        }

    }

    private void loadEmployees() {

        try {

            ResultSet resultSet = MySQL2.executeSearch("SELECT * FROM `employee` INNER JOIN `gender` ON `employee`.`gender_id`=`gender`.`id` INNER JOIN `employee_type` ON `employee`.`employee_type_id`=`employee_type`.`id`"
                    + "INNER JOIN `status` ON `status`.`id`=`employee`.`status_id` ");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            dtm.setRowCount(0);

            while (resultSet.next()) {

                Vector<Object> vector = new Vector<>();

                vector.add(resultSet.getString("first_name"));
                vector.add(resultSet.getString("last_name"));
                vector.add(resultSet.getString("email"));
                vector.add(resultSet.getString("mobile"));
                vector.add(resultSet.getString("nic"));
                vector.add(resultSet.getString("password"));
                vector.add(resultSet.getString("gender.name"));
                vector.add(resultSet.getString("employee_type.name"));
                vector.add(resultSet.getString("status.name"));
                vector.add(resultSet.getString("image_path"));

                // Load image as ImageIcon
                dtm.addRow(vector);

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Employe details loading table error", e);
            e.printStackTrace();
        }

    }

// Set the custom renderer for the image column (last column, index 9)
    private void loadEmployees(String email) {

        try {

            ResultSet resultSet = MySQL2.executeSearch("SELECT * FROM `employee` INNER JOIN `gender` ON `employee`.`gender_id`=`gender`.`id` INNER JOIN `employee_type` ON `employee`.`employee_type_id`=`employee_type`.`id`"
                    + "INNER JOIN `status` ON `status`.`id`=`employee`.`status_id` WHERE `email` LIKE '" + email + "%' ");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            dtm.setRowCount(0);

            while (resultSet.next()) {

                Vector<String> vector = new Vector<>();

                vector.add(resultSet.getString("first_name"));
                vector.add(resultSet.getString("last_name"));
                vector.add(resultSet.getString("email"));
                vector.add(resultSet.getString("mobile"));
                vector.add(resultSet.getString("nic"));
                vector.add(resultSet.getString("password"));
                vector.add(resultSet.getString("gender.name"));
                vector.add(resultSet.getString("employee_type.name"));
                vector.add(resultSet.getString("status.name"));
                vector.add(resultSet.getString("image_path"));

                dtm.addRow(vector);

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Employe details searched loading error", e);
            e.printStackTrace();
        }

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
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(350, 650));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Show");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 3, true));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 230, 54, -1));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 200, 31));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 200, 31));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Create Account");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 3, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, 280, 28));

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Update Account");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 3, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 280, 28));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 153, 255));
        jLabel12.setText("Status");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 43, -1));

        jComboBox3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 200, 31));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 255));
        jLabel2.setText("First Name");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 92, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 255));
        jLabel3.setText("Last Name");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 255));
        jLabel4.setText("Email");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 37, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 255));
        jLabel5.setText("Mobile");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 59, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 153, 255));
        jLabel6.setText("NIC");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 37, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 153, 255));
        jLabel8.setText("Password");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 104, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 153, 255));
        jLabel9.setText("Gender");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 104, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 153, 255));
        jLabel10.setText("E.Type");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 104, -1));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 200, -1));

        jTextField2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 200, -1));

        jTextField3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 200, -1));

        jTextField4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 200, -1));

        jTextField5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 200, -1));

        jPasswordField1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel1.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 130, -1));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Clear All");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 3, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 540, 280, 28));

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(new java.awt.Color(194, 173, 247));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search by Email ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });
        jPanel2.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 570, 50));

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Change Status");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 3, true));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 200, 28));

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Print Report");
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 3, true));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 190, 28));

        jTable1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTable1.setForeground(new java.awt.Color(153, 153, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "First Name", "Last Name", "Email", "Mobile", "NIC", "Password", "Gender", "E.type", "status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        jScrollPane2.setViewportView(jTable1);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 580, 506));

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        jPasswordField1.setEchoChar('\u0000');
        jButton1.setText("Hide");
    }//GEN-LAST:event_jButton1MousePressed

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        jPasswordField1.setEchoChar('\u2022');
        jButton1.setText("Show");
    }//GEN-LAST:event_jButton1MouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {

            String fn = jTextField1.getText();
            String ln = jTextField2.getText();
            String email = jTextField3.getText();
            String mobile = jTextField4.getText();
            String nic = jTextField5.getText();
            String pw = String.valueOf(jPasswordField1.getPassword());
            String gender = String.valueOf(jComboBox1.getSelectedItem());
            String type = String.valueOf(jComboBox2.getSelectedItem());
            String status = String.valueOf(jComboBox3.getSelectedItem());

            if (fn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a first name", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (ln.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a last name", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a email address", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")) {
                JOptionPane.showMessageDialog(this, "Invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!mobile.matches("^07[01245678]{1}[0-9]{7}$")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (nic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a National Identity Card number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a password", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (gender.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please enter select a gender", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (type.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please selecta a type", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (status.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a status", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                ResultSet resultSet = MySQL2.executeSearch("SELECT * FROM `employee` WHERE `email`='" + email + "' OR `mobile`='" + mobile + "' OR `nic`='" + nic + "' ");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "User already exist with these details", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    //      MySQL2.executeIUD("INSERT INTO `employee` (`email`,`password`,`first_name`,`last_name`,"
                    //              + "`nic`,`mobile`,`date_registered`,`employee_type_id`,`gender_id`,`status_id`,`image_path`) VALUES ('" + email + "','" + pw + "','" + fn + "','" + ln + "','" + nic + "','" + mobile + "','" + sdf.format(date) + "',"
                    //             + " '" + employeeGenderMap.get(gender) + "','" + employeeTypeMap.get(type) + "','" + employeeStatus.get(status) + "','" + imagePath1 + "') ");
                    String query = "INSERT INTO `employee` (`email`,`password`,`first_name`,`last_name`,"
                            + "`nic`,`mobile`,`date_registered`,`employee_type_id`,`gender_id`,`status_id`,`image_path`) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    List<Object> params = Arrays.asList(email, pw, fn, ln, nic, mobile, sdf.format(date),
                            employeeGenderMap.get(gender), employeeTypeMap.get(type), employeeStatus.get(status), imagePath1);

                    MySQL2.executePrepIUD(query, params);

                    logger.log(Level.INFO, "New Employee account created:{0}", email);

                    loadEmployees();
                    reset();

                }

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "New Employee account creating error", e);
            e.printStackTrace();

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
     int row = jTable1.getSelectedRow();
     if(row == -1){
          JOptionPane.showMessageDialog(this, "Please select a row to update", "Warning", JOptionPane.WARNING_MESSAGE);
     }else{
        try {
           
            String fn = jTextField1.getText();
            String ln = jTextField2.getText();
            String email = jTextField3.getText();
            String mobile = jTextField4.getText();
            String nic = jTextField5.getText();
            String password = String.valueOf(jPasswordField1.getPassword());
            String gender = String.valueOf(jComboBox1.getSelectedItem());
            String type = String.valueOf(jComboBox2.getSelectedItem());
            String status = String.valueOf(jComboBox3.getSelectedItem());

            if (fn.equals(String.valueOf(jTable1.getValueAt(row, 0))) &&  ln.equals(String.valueOf(jTable1.getValueAt(row, 1))) && mobile.equals(String.valueOf(jTable1.getValueAt(row, 3))) && nic.equals(String.valueOf(jTable1.getValueAt(row, 4))) && password.equals(String.valueOf(jTable1.getValueAt(row, 5))) && gender.equals(String.valueOf(jTable1.getValueAt(row, 6))) && type.equals(String.valueOf(jTable1.getValueAt(row, 7))) &&  status.equals(String.valueOf(jTable1.getValueAt(row, 8))) ) {
                JOptionPane.showMessageDialog(this, "Please change details before update", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                ResultSet resultSet = MySQL2.executeSearch("SELECT * FROM `employee` WHERE `nic`='" + nic + "' OR `mobile`='" + mobile + "' ");
                boolean canUpdate = false;
                if (resultSet.next()) {

                    if (!resultSet.getString("email").equals(email)) {
                        JOptionPane.showMessageDialog(this, "This NIC or Mobile Number are already in use", "Warning", JOptionPane.WARNING_MESSAGE);

                    } else {
                        canUpdate = true;

                    }

                } else {
                    canUpdate = true;
                }

                if (canUpdate) {

                    //  System.out.println("update" + imagePath1);
                    //   MySQL2.executeIUD("UPDATE `employee` SET `password`='" + password + "',`first_name`='" + fn + "',`last_name`='" + ln + "',`nic`='" + nic + "',`mobile`='" + mobile + "',"
                    //           + "`employee_type_id`='" + employeeTypeMap.get(type) + "',`gender_id`='" + employeeGenderMap.get(gender) + "',`status_id`='" + employeeStatus.get(status) + "',`image_path`='" + imagePath1 + "' WHERE `email`='" + email + "' ");
                    String query = "UPDATE `employee` SET `password`=?, `first_name`=?, `last_name`=?, "
                            + "`nic`=?, `mobile`=?, `employee_type_id`=?, `gender_id`=?, `status_id`=?, `image_path`=? WHERE `email`=?";

                    List<Object> params = Arrays.asList(password, fn, ln, nic, mobile,
                            employeeTypeMap.get(type), employeeGenderMap.get(gender), employeeStatus.get(status), imagePath1, email);

                    MySQL2.executePrepIUD(query, params);

                    logger.log(Level.INFO, "Employee account updated:{0}", email);

                    loadEmployees();
                    reset();

                }

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Employe account update error", e);
            e.printStackTrace();

        }
     }
    }//GEN-LAST:event_jButton3ActionPerformed
    private String imagePath1 = null;
    private String correctPath = null;
    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        reset();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        loadEmployees(jTextField6.getText());
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int row = jTable1.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to change status", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String email = String.valueOf(jTable1.getValueAt(row, 2));
            String status = String.valueOf(jTable1.getValueAt(row, 8));

            try {

                if (status.equals("Active")) {
                    logger.log(Level.INFO, "Employee account status changed to INACTIVE:{0}", email);
                    MySQL2.executeIUD("UPDATE `employee` SET `status_id`='2' WHERE `email`='" + email + "' ");
                    loadEmployees();
                } else {
                    logger.log(Level.INFO, "Employee account status changed to ACTIVE:{0}", email);
                    MySQL2.executeIUD("UPDATE `employee` SET `status_id`='1' WHERE `email`='" + email + "'  ");
                    loadEmployees();
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Employe active/inactive status changing error", e);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            //   InputStream s = this.getClass().getResourceAsStream("/reports/employeereport.jasper");
            String s = "src//reports//newEmployee.jasper";

            HashMap<String, Object> params = new HashMap<>();

            params.put("Parameter1", sdf.format(date));

            JRTableModelDataSource datasource = new JRTableModelDataSource(jTable1.getModel());

            JasperPrint jasperprint = JasperFillManager.fillReport(s, params, datasource);
            
            JasperViewer.viewReport(jasperprint,false);

            logger.log(Level.INFO, "Employee details print");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Employe details printing error", e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed
    private ImageIcon imageIconFormat = null;
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.getSelectedRow();

        String fn = String.valueOf(jTable1.getValueAt(row, 0));
        jTextField1.setText(fn);

        String ln = String.valueOf(jTable1.getValueAt(row, 1));
        jTextField2.setText(ln);

        String email = String.valueOf(jTable1.getValueAt(row, 2));
        jTextField3.setText(email);
        jTextField3.setEditable(false);

        String mobile = String.valueOf(jTable1.getValueAt(row, 3));
        jTextField4.setText(mobile);

        String nic = String.valueOf(jTable1.getValueAt(row, 4));
        jTextField5.setText(nic);

        String pw = String.valueOf(jTable1.getValueAt(row, 5));
        jPasswordField1.setText(pw);

        String gender = String.valueOf(jTable1.getValueAt(row, 6));
        jComboBox1.setSelectedItem(gender);

        String type = String.valueOf(jTable1.getValueAt(row, 7));
        jComboBox2.setSelectedItem(type);

        String status = String.valueOf(jTable1.getValueAt(row, 8));
        jComboBox3.setSelectedItem(status);

       

       


        jButton2.setEnabled(false);

        if (evt.getClickCount() == 2) {
            int row1 = jTable1.getSelectedRow();

            String email1 = String.valueOf(jTable1.getValueAt(row1, 2));

            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            AddressView adv = new AddressView(parentFrame, true, email1);

            adv.setVisible(true);

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void reset() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jPasswordField1.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
       
        jTable1.clearSelection();
        jButton2.setEnabled(true);
        jTextField3.setEditable(true);
        loadEmployees();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
