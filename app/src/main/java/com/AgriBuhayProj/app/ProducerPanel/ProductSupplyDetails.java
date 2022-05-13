package com.AgriBuhayProj.app.ProducerPanel;

//POSTED PRODUCT MODEL
public class ProductSupplyDetails {

    private String Products,Quantity,Price,Description,ImageURL,RandomUID, ProducerID, Mobile;

    public ProductSupplyDetails() {
    }

    public ProductSupplyDetails(String products, String quantity, String price, String description, String imageURL, String randomUID, String producerID, String mobile) {
        Products = products;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        ProducerID = producerID;
        Mobile = mobile;
    }

    public String getProducts() {
        return Products;
    }

    public void setProducts(String products) {
        Products = products;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getProducerID() {
        return ProducerID;
    }

    public void setProducerID(String producerID) {
        ProducerID = producerID;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
