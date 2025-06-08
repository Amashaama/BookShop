/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import static gui.AdminSignIn.logger;
import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import model.MySQL2;
import net.sf.jasperreports.engine.fill.DefaultChartTheme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.logging.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;

/**
 *
 * @author Anne
 */
public class GraphDataView extends javax.swing.JPanel {

    static final Logger logger = SignIn.getLoggerObject();

    public GraphDataView() {

        try {

            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        initComponents();

        SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();
        initializeCurrentPage();
    }

    private void initializeCurrentPage() {
        logger.info("Graph Data View Page accessed");
    }

    public void loadGraphData() {

        String sort = String.valueOf(jComboBox1.getSelectedItem());
        Date start;
        Date end;

        String query = "SELECT DATE(`date`) AS `transaction_date`, SUM(`paid_amount`) AS `daily_revenue` "
                + "FROM `invoice`";

        String query2 = "SELECT `product`.`title`, SUM(`invoice_item`.`qty`) AS `total_qty` FROM `invoice_item` INNER JOIN `invoice` ON `invoice`.`id`=`invoice_item`.`invoice_id` INNER JOIN `stock` ON `stock`.`id`=`invoice_item`.`stock_id` INNER JOIN `product` ON `product`.`id`=`stock`.`product_id`";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<String> conditions = new ArrayList<>();

        if (jDateChooser1.getDate() != null) {
            start = jDateChooser1.getDate();
            conditions.add("`invoice`.`date` >= '" + sdf.format(start) + "'");

        }

        if (jDateChooser2.getDate() != null) {
            end = jDateChooser2.getDate();
            conditions.add("`invoice`.`date` <= '" + sdf.format(end) + "'");

        }

        if (sort.equals("Revenue")) {

            if (!conditions.isEmpty()) {
                query += " WHERE" + String.join(" AND", conditions);

            }

            query += "  GROUP BY DATE(`date`)";

            loadRevenue(query);

        } else {

            if (!conditions.isEmpty()) {
                query2 += " WHERE" + String.join(" AND", conditions);
            }

            query2 += "  GROUP BY `product`.`title`";
            loadStockSold(query2);

        }

    }

    private List<String> dates = new ArrayList<>();
    private List<Double> revenues = new ArrayList<>();

    private void loadRevenue(String query) {
        try {

            dates.clear();
            revenues.clear();

            ResultSet rs = MySQL2.executeSearch(query);

            while (rs.next()) {
                String date = rs.getString("transaction_date");
                double revenue = rs.getDouble("daily_revenue");
                dates.add(date);
                revenues.add(revenue);
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (int i = 0; i < dates.size(); i++) {
                dataset.addValue(revenues.get(i), "Revenue", dates.get(i));
            }

            JFreeChart barchart = ChartFactory.createBarChart(
                    "Revenue View", "Date", "Revenue", dataset,
                    PlotOrientation.VERTICAL, true, true, false
            );

            barchart.getCategoryPlot().getRenderer().setSeriesPaint(0, new Color(51, 51, 51));
            barchart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            ChartPanel chartpanel = new ChartPanel(barchart);
            chartpanel.setPreferredSize(new Dimension(800, 600));

            jPanel2.removeAll();

            jPanel2.add(chartpanel);

            jPanel2.revalidate();
            jPanel2.repaint();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in loading Revenue Graph", e);
            e.printStackTrace();
        }
    }

    List<String> titles = new ArrayList<>();
    List<Integer> qtys = new ArrayList<>();

    private void loadStockSold(String query) {

        titles.clear();
        qtys.clear();

        try {
            ResultSet rs = MySQL2.executeSearch(query);
            //System.out.println(query);
            while (rs.next()) {
                String title = rs.getString("product.title");
                Integer qty = rs.getInt("total_qty");
                titles.add(title);
                qtys.add(qty);
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (int x = 0; x < qtys.size(); x++) {
                dataset.addValue(qtys.get(x), "Quantity", titles.get(x));
            }

            JFreeChart barchart = ChartFactory.createBarChart("Stock Sold View", "Product", "Quantity", dataset, PlotOrientation.VERTICAL, true, true, false);

            barchart.getCategoryPlot().getRenderer().setSeriesPaint(0, new Color(51, 51, 51));
            barchart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            ChartPanel chartpanel = new ChartPanel(barchart);
            chartpanel.setPreferredSize(new Dimension(800, 600));

            jPanel2.removeAll();
            jPanel2.add(chartpanel);
            jPanel2.revalidate();
            jPanel2.repaint();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in loading Stock Sold Graph", e);
            e.printStackTrace();
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
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(950, 120));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 204, 204));
        jButton1.setText("Search");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 128, 255), 5));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 40, 142, 38));
        jPanel1.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 36, 180, 38));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 128, 255));
        jLabel3.setText("To");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 36, 26, -1));
        jPanel1.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(391, 36, 175, 38));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 128, 255));
        jLabel2.setText("From");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 36, -1, -1));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Revenue", "Stock Sold" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(158, 36, 175, 38));

        jLabel1.setBackground(new java.awt.Color(153, 128, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 128, 255));
        jLabel1.setText("View Graph Data For");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(153, 128, 255));
        jPanel2.setLayout(new java.awt.GridLayout());
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jDateChooser1.getDate() == null && jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a date ", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            loadGraphData();
        }

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
