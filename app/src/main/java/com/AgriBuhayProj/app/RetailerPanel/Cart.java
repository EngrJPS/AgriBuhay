package com.AgriBuhayProj.app.RetailerPanel;

public class Cart {

    //TODO add mobile number in this model
    private String ProducerId, ProductID, ProductName, ProductQuantity,Price,Totalprice;

    public Cart(String producerId, String productID, String productName, String productQuantity, String price, String totalprice) {
        ProducerId = producerId;
        ProductID = productID;
        ProductName = productName;
        ProductQuantity = productQuantity;
        Price = price;
        Totalprice = totalprice;
    }

    public Cart() {
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

    public String getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(String totalprice) {
        Totalprice = totalprice;
    }
}
