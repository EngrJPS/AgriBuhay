package com.AgriBuhayProj.app.RetailerPanel;

public class RetailerPendingOrders {

    private String ProducerId, ProductID, ProductName, ProductQuantity, Price, TotalPrice;

    public RetailerPendingOrders(String productID, String productName, String productQuantity, String price, String totalPrice, String producerId) {
        ProducerId = producerId;
        ProductID = productID;
        ProductName = productName;
        ProductQuantity = productQuantity;
        Price = price;
        TotalPrice = totalPrice;

    }

    public RetailerPendingOrders() {

    }

    public String getProducerId() {
        return ProducerId;
    }

    public void setProducerId(String producerId) {
        ProducerId = producerId;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }


}
