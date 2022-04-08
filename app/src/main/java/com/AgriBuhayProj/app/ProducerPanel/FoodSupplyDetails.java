package com.AgriBuhayProj.app.ProducerPanel;

public class FoodSupplyDetails {

    //Added mobile phone
    public String Dishes,Quantity,Price,Description,ImageURL,RandomUID,ChefId, Mobile;

    public FoodSupplyDetails(String dishes, String quantity, String price, String description, String imageURL, String randomUID, String chefId, String mobile) {
        Dishes = dishes;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        ChefId = chefId;
        Mobile = mobile;
    }
}
