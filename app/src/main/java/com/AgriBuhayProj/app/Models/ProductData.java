package com.AgriBuhayProj.app.Models;

public class ProductData {
    //VARIABLES
    private String productName,totalWeight,temperatureLevel,humidityPercentage,co2Level;

    //OBJECT INSTANCE
    public ProductData() { }

    //OBJECT CONSTRUCTOR
    public ProductData(String productName, String totalWeight, String temperatureLevel, String humidityPercentage, String co2Level) {
        this.productName = productName;
        this.totalWeight = totalWeight;
        this.temperatureLevel = temperatureLevel;
        this.humidityPercentage = humidityPercentage;
        this.co2Level = co2Level;
    }

    //GETTER-SETTER
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalWeight() {
        return totalWeight;
    }
    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getTemperatureLevel() {
        return temperatureLevel;
    }
    public void setTemperatureLevel(String temperatureLevel) {
        this.temperatureLevel = temperatureLevel;
    }

    public String getHumidityPercentage() {
        return humidityPercentage;
    }
    public void setHumidityPercentage(String humidityPercentage) {
        this.humidityPercentage = humidityPercentage;
    }

    public String getCo2Level() {
        return co2Level;
    }
    public void setCo2Level(String co2Level) {
        this.co2Level = co2Level;
    }
}
