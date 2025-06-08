/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Anne
 */
public class InvoiceItem {

    /**
     * @return the stockID
     */
    public String getStockID() {
        return stockID;
    }

    /**
     * @param stockID the stockID to set
     */
    public void setStockID(String stockID) {
        this.stockID = stockID;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the sellingPrice
     */
    public String getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
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
     * @return the discountFrom
     */
    public String getDiscountFrom() {
        return discountFrom;
    }

    /**
     * @param discountFrom the discountFrom to set
     */
    public void setDiscountFrom(String discountFrom) {
        this.discountFrom = discountFrom;
    }

    /**
     * @return the discountTo
     */
    public String getDiscountTo() {
        return discountTo;
    }

    /**
     * @param discountTo the discountTo to set
     */
    public void setDiscountTo(String discountTo) {
        this.discountTo = discountTo;
    }

   
   
    
     private String stockID;
    private String category;
    private String name;
    private String qty;
    private String sellingPrice;
    private Double discount;
    private String discountFrom;
    private String discountTo;
   
    
}
