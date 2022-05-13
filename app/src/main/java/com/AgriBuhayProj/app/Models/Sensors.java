package com.AgriBuhayProj.app.Models;

public class Sensors {
    //VARIABLES
    private Double CO2_PPM,Humid_Value,Temp_Value,Weight_Value;

    //OBJECT INSTANCE
    public Sensors(){}

    //OBJECT VALUES
    public Sensors(Double cO2_PPM, Double humid_Value, Double temp_Value, Double weight_Value) {
        CO2_PPM = cO2_PPM;
        Humid_Value = humid_Value;
        Temp_Value = temp_Value;
        Weight_Value = weight_Value;
    }

    //GETTER-SETTER
    public Double getCO2_PPM() {
        return CO2_PPM;
    }

    public void setCO2_PPM(Double cO2_PPM) {
        CO2_PPM = cO2_PPM;
    }

    public Double getHumid_Value() {
        return Humid_Value;
    }

    public void setHumid_Value(Double humid_Value) {
        Humid_Value = humid_Value;
    }

    public Double getTemp_Value() {
        return Temp_Value;
    }

    public void setTemp_Value(Double temp_Value) {
        Temp_Value = temp_Value;
    }

    public Double getWeight_Value() {
        return Weight_Value;
    }

    public void setWeight_Value(Double weight_Value) {
        Weight_Value = weight_Value;
    }
}
