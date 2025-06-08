/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Anne
 */
public class MainFramed extends javax.swing.JFrame {

    private String LoginEmpEmail;

    public MainFramed(String email) {
        try {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();
        initComponents();
        centerFrame();

        loadHomePage();

        this.LoginEmpEmail = email;
        
        Timer timer = new Timer(1000,e -> updateClock());
        timer.start();
    }
    
    private void  updateClock(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        
        jLabel5.setText(formatter.format(new Date()) +" "+"|"+" "+  formatter2.format(new Date()));

    }

    private void loadHomePage() {

        jPanel4.setVisible(false);
        jPanel4.removeAll();

        Home home = new Home();
        jPanel4.add(home, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Home");

    }

    private boolean isPanelExpanded = false;

    private void toggleSidePanel() {
        int expandedWidth = 200;
        int collapsedWidth = 50;
        int additionalWidth = expandedWidth - collapsedWidth;

        int step = 20; // Change in width per step
        int delay = 10; // Delay in milliseconds between each step

        boolean expand = !isPanelExpanded;
        Timer timer = new Timer(delay, null);

        timer.addActionListener(e -> {

            int currentPanelWidth = jPanel2.getWidth();
            int currentFrameWidth = getWidth();

            int originalLabelX = jLabel4.getX();

            if (expand) {

                if (currentPanelWidth < expandedWidth) {
                    jPanel2.setPreferredSize(new java.awt.Dimension(currentPanelWidth + step, jPanel2.getHeight()));
                    setSize(currentFrameWidth + step, getHeight());
                    centerFrame();
                    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-back-32.png")));
                    jButton1.setText("Employee Management");
                    jButton2.setText("Product Management");
                    jButton3.setText("Supplier Management");
                    jButton4.setText("GRN Management");
                    jButton5.setText("Stock Management");
                    jButton6.setText("Invoice Management");
                    jButton7.setText("Customer Management");
                    jButton8.setText("Return Product Managment");
                    jButton9.setText("Graph Date");
                    jButton10.setText("Home");
                   
                   
                    jLabel4.setBounds(additionalWidth, jLabel4.getY(), jLabel4.getWidth(), jLabel4.getHeight());
                    jLabel5.setBounds(additionalWidth, jLabel5.getY(), jLabel5.getWidth(), jLabel5.getHeight());

                } else {

                    ((Timer) e.getSource()).stop();
                    isPanelExpanded = true;
                }
            } else {

                if (currentPanelWidth > collapsedWidth) {
                    jPanel2.setPreferredSize(new java.awt.Dimension(currentPanelWidth - step, jPanel2.getHeight()));
                    setSize(currentFrameWidth - step, getHeight());
                    centerFrame();
                    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-menu-32.png")));
                    jButton1.setText("");
                    jButton2.setText("");
                    jButton3.setText("");
                    jButton4.setText("");
                    jButton5.setText("");
                    jButton6.setText("");
                    jButton7.setText("");
                    jButton8.setText("");
                    jButton9.setText("");
                    jButton10.setText("");
                    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-employee-32 (1).png")));
                    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-books-32.png")));
                    jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-supplier-32.png")));
                    jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-goods-32.png")));
                    jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-stock-32.png")));
                    jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-invoice-30.png")));
                    jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-customer-32.png")));
                    jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-return-book-32.png")));
                    jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-graph-32.png")));
                    jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-home-32.png")));

                   
                   
                    jLabel4.setBounds(0, jLabel4.getY(), jLabel4.getWidth(), jLabel4.getHeight());
                    jLabel5.setBounds(0, jLabel5.getY(), jLabel5.getWidth(), jLabel5.getHeight());
                } else {

                    ((Timer) e.getSource()).stop();
                    isPanelExpanded = false;
                }
            }
            jLabel4.revalidate();
            jLabel4.repaint();

            jPanel2.revalidate();
            jPanel2.repaint();
            jPanel4.revalidate();
            jPanel4.repaint();
        });
        timer.start();

    }

    private void centerFrame() {
        java.awt.Dimension screenSizeO = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidthO = screenSizeO.width;
        int screenHeightO = screenSizeO.height;

        int frameWidthO = getWidth();
        int frameHieghtO = getHeight();

        int x = (screenWidthO - frameWidthO) / 2;
        int y = (screenHeightO - frameHieghtO) / 2;

        setLocation(x, y);

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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 50));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-close-32.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 0, 50, 50));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-minimize-32 (1).png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 50, 50));

        jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_END);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-menu-32.png"))); // NOI18N
        jLabel2.setText(" ");
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 32));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel2, java.awt.BorderLayout.LINE_START);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setPreferredSize(new java.awt.Dimension(430, 100));
        jPanel6.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 23)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 204));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel6.add(jLabel4);
        jLabel4.setBounds(0, 6, 400, 38);

        jPanel5.add(jPanel6, java.awt.BorderLayout.LINE_START);

        jPanel7.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 204));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel7.add(jLabel5);
        jLabel5.setBounds(10, 0, 390, 50);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(50, 650));
        jPanel2.setLayout(new java.awt.GridLayout(10, 0, 10, 5));

        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton10.setForeground(new java.awt.Color(153, 153, 255));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-home-32.png"))); // NOI18N
        jButton10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 153, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-employee-32 (1).png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(153, 153, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-books-32.png"))); // NOI18N
        jButton2.setToolTipText("");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(153, 153, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-supplier-32.png"))); // NOI18N
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton4.setForeground(new java.awt.Color(153, 153, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-goods-32.png"))); // NOI18N
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton5.setForeground(new java.awt.Color(153, 153, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-stock-32.png"))); // NOI18N
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton6.setForeground(new java.awt.Color(153, 153, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-invoice-30.png"))); // NOI18N
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton7.setForeground(new java.awt.Color(153, 153, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-customer-32.png"))); // NOI18N
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton8.setForeground(new java.awt.Color(153, 153, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-return-book-32.png"))); // NOI18N
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8);

        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButton9.setForeground(new java.awt.Color(153, 153, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-graph-32.png"))); // NOI18N
        jButton9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 204), 4));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9);

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(950, 650));
        jPanel4.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        EmployeeManagement em = new EmployeeManagement();
        jPanel4.add(em, BorderLayout.CENTER);

        jPanel4.setBorder(null);
        em.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Employee Management");
        jButton1.grabFocus();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        ProductManagement pm = new ProductManagement();
        jPanel4.add(pm, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Product Management");
         jButton2.grabFocus();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        SubHomeA homea = new SubHomeA();
        jPanel4.add(homea, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Supplier Management");
         jButton3.grabFocus();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        GrnManagement grn = new GrnManagement(LoginEmpEmail);
        jPanel4.add(grn, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" GRN Management");
         jButton4.grabFocus();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        StockManagement sm = new StockManagement();
        jPanel4.add(sm, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Stock Management");
         jButton5.grabFocus();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        Invoice in = new Invoice(LoginEmpEmail);
        jPanel4.add(in, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Invoice Management");
         jButton6.grabFocus();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        CustomerRegistration cr = new CustomerRegistration();
        jPanel4.add(cr, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Customer Management");
         jButton7.grabFocus();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        ReturnProductManagement rp = new ReturnProductManagement(LoginEmpEmail);
        jPanel4.add(rp, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Return Management");
         jButton8.grabFocus();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        GraphDataView gr = new GraphDataView();
        jPanel4.add(gr, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Graph Data View");
         jButton9.grabFocus();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        toggleSidePanel();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jPanel4.setVisible(false);
        jPanel4.removeAll();

        Home home = new Home();
        jPanel4.add(home, BorderLayout.CENTER);

        jPanel4.setBorder(null);

        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel4.setVisible(true);
        jLabel4.setText(" Home");
         jButton10.grabFocus();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables
}
