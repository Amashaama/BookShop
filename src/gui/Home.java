/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.MySQL2;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import java.util.logging.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

public class Home extends javax.swing.JPanel {

    public String email;

    Logger logger = SignIn.getLoggerObject();

    public Home() {
        try {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        initComponents();

        initializeBookShopHome();

        /*   this.email = email;
        jTextField1.setText(email);
        loadValidation();
         */
        loadRevenue();
        loadProductDetails();

        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();

        SwingUtilities.updateComponentTreeUI(this);
        this.revalidate();
        this.repaint();

        dailyEarnings();
        monthlyEarnings();
        soldProduct();
        dailySoldProducts();
        monthlySoldProducts();
        loadLowStock();
    }
    
    private void loadLowStock(){
        try {
            ResultSet rs = MySQL2.executeSearch("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `stock`.`qty`='0' ");
            jTextArea1.setText("");
           while(rs.next()){
               
               String pn = rs.getString("product.title");
               jTextArea1.append(pn + "\n");
             
           }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading low stock products", e);
            e.printStackTrace();
            
        }
    }

    private void initializeBookShopHome() {
        logger.log(Level.INFO, "BookShop Dashboard Accessed");
    }

    private void updateClock() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

        /*        jLabel2.setText(formatter.format(new Date()));
        jLabel3.setText(formatter2.format(new Date()));
         */
    }

    /*   private void loadValidation() {
        try {

            ResultSet rs = MySQL2.executeSearch("SELECT * FROM `employee` WHERE `email`='" + this.email + "'");
            while (rs.next()) {
                if (!rs.getString("employee_type_id").equals("1")) {

                    jButton1.setEnabled(false);
                    jButton3.setEnabled(false);
                    jButton8.setEnabled(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
    private void dailyEarnings() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);

        try {
            ResultSet rs = MySQL2.executeSearch("SELECT `date`,SUM(`paid_amount`) AS `total_earning` FROM `invoice` GROUP BY `date` ORDER BY `date` ");
            while (rs.next()) {
                if (rs.getString("date").equals(today)) {
                    jLabel5.setText(rs.getString("total_earning"));
                } else {
                    jLabel5.setText("0.0");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void monthlyEarnings() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String thisMonth = sdf.format(date);

        try {
            ResultSet rs = MySQL2.executeSearch("SELECT DATE_FORMAT(`date`,'%Y-%m') AS month,SUM(`paid_amount`) AS `total_earning` FROM `invoice` GROUP BY `month` ORDER BY `month` ");
            while (rs.next()) {
                if (rs.getString("month").equals(thisMonth)) {
                    jLabel7.setText(rs.getString("total_earning"));
                } else {
                    jLabel7.setText("0.0");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void soldProduct() {
        try {
            ResultSet rs = MySQL2.executeSearch("select invoice_item.stock_id,SUM(invoice_item.qty) AS total_sold,product.title from `invoice_item` inner join `stock` on `stock`.`id`=`invoice_item`.`stock_id` inner join `product` on `product`.`id`= `stock`.`product_id` group by `stock_id` order by `total_sold` DESC LIMIT 1 ");

            while (rs.next()) {
                jLabel9.setText(rs.getString("title"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dailySoldProducts() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        try {
            ResultSet rs = MySQL2.executeSearch("select invoice.date,SUM(invoice_item.qty) AS total_sold from `invoice_item` inner join `invoice` on `invoice`.`id`=`invoice_item`.`invoice_id` group by `date` order by `date` ");

            while (rs.next()) {
                if (rs.getString("date").equals(today)) {
                    jLabel11.setText(rs.getString("total_sold"));
                } else {
                    jLabel11.setText("0.0");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void monthlySoldProducts() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String month = sdf.format(date);
        try {
            ResultSet rs = MySQL2.executeSearch("select DATE_FORMAT(date,'%Y-%m') AS month,SUM(invoice_item.qty) AS total_sold from `invoice_item` inner join `invoice` on `invoice`.`id`=`invoice_item`.`invoice_id` group by `date` order by `date` ");

            while (rs.next()) {
                if (rs.getString("month").equals(month)) {
                    jLabel13.setText(rs.getString("total_sold"));
                } else {
                    jLabel13.setText("0.0");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<String> dates = new ArrayList<>();
    private List<Double> revenues = new ArrayList<>();

    private void loadRevenue() {

        try {
            ResultSet rs = MySQL2.executeSearch("SELECT DATE(`date`) AS `transaction_date`, SUM(`paid_amount`) AS `daily_revenue` "
                    + "FROM `invoice` GROUP BY DATE(`date`);");

            while (rs.next()) {
                String date = rs.getString("transaction_date");
                double revenue = rs.getDouble("daily_revenue");
                dates.add(date);
                revenues.add(revenue);

            }

            createChart();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private JFrame chartFrame;

    private void createChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < dates.size(); i++) {
            dataset.addValue(revenues.get(i), "Revenue", dates.get(i));
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Daily Revenue",
                "Date",
                "Revenue",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        lineChart.setBackgroundPaint(null);
        lineChart.getPlot().setBackgroundPaint(null);

        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();

        renderer.setSeriesPaint(0, new Color(153, 128, 255));
        renderer.setSeriesOutlineStroke(0, new BasicStroke(3.0f));
       

        plot.setRenderer(renderer);
         

        Font boldWhiite = new Font("SansSerif", Font.BOLD, 12);

        lineChart.getTitle().setFont(boldWhiite);
        lineChart.getTitle().setPaint(new Color(153, 128, 255));

        plot.getDomainAxis().setLabelFont(boldWhiite);
        plot.getDomainAxis().setTickLabelFont(boldWhiite);
        plot.getDomainAxis().setTickLabelPaint(Color.WHITE);
        plot.getDomainAxis().setLabelPaint(Color.WHITE);

        plot.getRangeAxis().setLabelFont(boldWhiite);
        plot.getRangeAxis().setTickLabelFont(boldWhiite);
        plot.getRangeAxis().setTickLabelPaint(Color.WHITE);
        plot.getRangeAxis().setLabelPaint(Color.WHITE);

        
        lineChart.getLegend().setItemFont(boldWhiite);
        lineChart.getLegend().setItemPaint(Color.BLACK);

        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // Rotates labels by 45 degrees

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        jPanel3.add(chartPanel);
        jPanel3.revalidate();
        jPanel3.repaint();

        chartPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (chartFrame == null || !chartFrame.isDisplayable()) {

                    chartFrame = new JFrame("Detailed Chart View");
                    chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chartFrame.setSize(1200, 800);
                    chartFrame.setAlwaysOnTop(true);
                    chartFrame.setLocationRelativeTo(null);
                    chartFrame.setTitle("Chart Data");

                    ChartPanel detailedChartPanel = new ChartPanel(lineChart);
                    detailedChartPanel.setPreferredSize(new Dimension(1200, 800));
                    chartFrame.add(detailedChartPanel);

                    chartFrame.setVisible(true);
                } else {

                    chartFrame.requestFocus();
                    chartFrame.toFront();
                }
            }
        });
    }

    private List<String> titles = new ArrayList<>();
    private List<Integer> qty = new ArrayList<>();

    private void loadProductDetails() {

        try {

            ResultSet rs = MySQL2.executeSearch("SELECT `product`.`title`, SUM(`invoice_item`.`qty`) AS `total_qty` FROM `invoice_item` INNER JOIN `stock` ON `stock`.`id`=`invoice_item`.`stock_id` INNER JOIN `product` ON `product`.`id`=`stock`.`product_id` GROUP BY `product`.`title` ORDER BY `total_qty` DESC");

            while (rs.next()) {
                String pTitle = rs.getString("product.title");
                Integer pqty = rs.getInt("total_qty");
                titles.add(pTitle);
                qty.add(pqty);

            }

            createChart2();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createChart2() {

        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

        for (int i = 0; i < titles.size(); i++) {
            dataset2.addValue(qty.get(i), "Product", titles.get(i));
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Stock Sold Graph",
                "product",
                "quantity",
                dataset2,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        
       barChart.getCategoryPlot().getRenderer().setSeriesPaint(0, new Color(153, 128, 255));
        barChart.setBackgroundPaint(null);
        barChart.getCategoryPlot().setBackgroundPaint(null);
        barChart.getTitle().setPaint(new Color(153, 128, 255));
        
         barChart.getCategoryPlot().getDomainAxis().setLabelPaint(Color.WHITE); 
    barChart.getCategoryPlot().getRangeAxis().setLabelPaint(Color.WHITE);  
    barChart.getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.WHITE); 
    barChart.getCategoryPlot().getRangeAxis().setTickLabelPaint(Color.WHITE); 
        
        ChartPanel chartPanel2 = new ChartPanel(barChart);
        chartPanel2.setPreferredSize(new Dimension(800, 600));

        jPanel2.add(chartPanel2);
        jPanel2.revalidate();
        jPanel2.repaint();

        chartPanel2.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (chartFrame == null || !chartFrame.isDisplayable()) {

                    chartFrame = new JFrame("Detailed Chart View");
                    chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chartFrame.setSize(1200, 800);
                    chartFrame.setAlwaysOnTop(true);
                    chartFrame.setLocationRelativeTo(null);
                    chartFrame.setTitle("Chart Data");

                    ChartPanel detailedChartPanel = new ChartPanel(barChart);
                    detailedChartPanel.setPreferredSize(new Dimension(1200, 800));
                    chartFrame.add(detailedChartPanel);

                    chartFrame.setVisible(true);
                } else {

                    chartFrame.requestFocus();
                    chartFrame.toFront();
                }
            }
        });

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
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(185, 162, 242));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(128, 128, 230), 5, true));
        jPanel5.setForeground(new java.awt.Color(204, 229, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 229, 255));
        jLabel6.setText("Monthly Earnings");

        jLabel7.setBackground(new java.awt.Color(196, 227, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 229, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 150, 70));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 128, 230), 5));
        jPanel4.setForeground(new java.awt.Color(204, 229, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 229, 255));
        jLabel4.setText("Daily Earnings");

        jLabel5.setBackground(new java.awt.Color(255, 196, 163));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 229, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 70));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(128, 128, 230), 5, true));
        jPanel7.setForeground(new java.awt.Color(204, 229, 255));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 229, 255));
        jLabel10.setText("Sold Items Today");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 229, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, -1, 70));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(128, 128, 230), 5, true));
        jPanel8.setForeground(new java.awt.Color(204, 229, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 229, 255));
        jLabel12.setText("Sold Items Monthly");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 229, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(19, 19, 19))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, -1, 70));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(128, 128, 230), 5, true));
        jPanel6.setForeground(new java.awt.Color(204, 229, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 229, 255));
        jLabel8.setText("Most Sold  Product");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 229, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 10, -1, 70));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 204), 3));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 92, 920, 258));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 204), 3));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 370, 300, 270));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Low Stock", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 600, 270));

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked

    }//GEN-LAST:event_jPanel3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
