/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;


public class GrnItem {

    /**
     * @return the productID
     */
    public String getProductID() {
        return productID;
    }

    /**
     * @param productID the productID to set
     */
    public void setProductID(String productID) {
        this.productID = productID;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the buyingPrice
     */
    public Double getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * @param buyingPrice the buyingPrice to set
     */
    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    /**
     * @return the sellingPrice
     */
    public Double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * @return the qty
     */
    public Double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return the discountStart
     */
    public String getDiscountStart() {
        return discountStart;
    }

    /**
     * @param discountStart the discountStart to set
     */
    public void setDiscountStart(String discountStart) {
        this.discountStart = discountStart;
    }

    /**
     * @return the discountEnd
     */
    public String getDiscountEnd() {
        return discountEnd;
    }

    /**
     * @param discountEnd the discountEnd to set
     */
    public void setDiscountEnd(String discountEnd) {
        this.discountEnd = discountEnd;
    }

   
    
    private String productID;
    private String productName;
    private String category;
    private Double buyingPrice;
    private Double sellingPrice;
    private Double qty;
    private Double discount;
    private String discountStart;
    private String discountEnd;
    
}
