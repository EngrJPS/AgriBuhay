package com.AgriBuhayProj.app.Models;

public class Crops {
    //VARIABLES
    private String crop,tempMin,tempMax,humidMin,humidMax,carbonMin,carbonMax;

    //OBJECT INSTANCE
    public Crops(){}

    //OBJECT CONSTRUCTOR
    public Crops(String crop,String tempMin, String tempMax, String humidMin, String humidMax, String carbonMin, String carbonMax) {
        this.crop = crop;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.humidMin = humidMin;
        this.humidMax = humidMax;
        this.carbonMin = carbonMin;
        this.carbonMax = carbonMax;
    }

    //GETTER-SETTER
    public String getCrop() {
        return crop;
    }
    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getTempMin() {
        return tempMin;
    }
    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }
    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getHumidMin() {
        return humidMin;
    }
    public void setHumidMin(String humidMin) {
        this.humidMin = humidMin;
    }

    public String getHumidMax() {
        return humidMax;
    }
    public void setHumidMax(String humidMax) {
        this.humidMax = humidMax;
    }

    public String getCarbonMin() {
        return carbonMin;
    }
    public void setCarbonMin(String carbonMin) {
        this.carbonMin = carbonMin;
    }

    public String getCarbonMax() {
        return carbonMax;
    }
    public void setCarbonMax(String carbonMax) {
        this.carbonMax = carbonMax;
    }

    @Override
    public String toString(){
        return crop;
    }
}
