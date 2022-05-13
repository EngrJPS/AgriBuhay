package com.AgriBuhayProj.app.ProducerPanel;

//PENDING ORDERS PRODUCT MODEL
public class ProducerPendingOrders {

    private String ProducerId, ProductId, ProductName, ProductQuantity,Price,RandomUID,TotalPrice,UserId;

    public ProducerPendingOrders(String producerId, String productId, String productName, String productQuantity, String price, String randomUID, String totalPrice, String userId) {
        ProducerId = producerId;
        ProductId = productId;
        ProductName = productName;
        ProductQuantity = productQuantity;
        Price = price;
        RandomUID=randomUID;
        TotalPrice = totalPrice;
        UserId = userId;
    }

    public ProducerPendingOrders()
    {

    }

    public String getProducerId() {
        return ProducerId;
    }

    public void setProducerId(String producerId) {
        ProducerId = producerId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
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

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
