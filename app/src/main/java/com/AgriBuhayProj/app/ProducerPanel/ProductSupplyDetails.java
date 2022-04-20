package com.AgriBuhayProj.app.ProducerPanel;

public class ProductSupplyDetails {

    //Added mobile phone
    public String Products,Quantity,Price,Description,ImageURL,RandomUID, ProductId, Mobile;

    public ProductSupplyDetails(String products, String quantity, String price, String description, String imageURL, String randomUID, String productId, String mobile) {
        Products = products;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        ProductId = productId;
        Mobile = mobile;
    }
}
