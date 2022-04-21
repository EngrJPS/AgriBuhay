package com.AgriBuhayProj.app.Models;

import android.net.Uri;

public class History {
    private String trackingNumber,deliveryDate,retailerAddress,totalPrice,producerName,producerMobile,retailerName,retailerMobile,logisticsName,logisticsMobile,deliveryImage,producerID,retailerID,logisticsID;

    public History(){ }

    public History(String trackingNumber, String deliveryDate, String retailerAddress, String totalPrice, String producerName, String producerMobile, String retailerName, String retailerMobile, String logisticsName, String logisticsMobile, String deliveryImage, String producerID, String retailerID, String logisticsID) {
        this.trackingNumber = trackingNumber;
        this.deliveryDate = deliveryDate;
        this.retailerAddress = retailerAddress;
        this.totalPrice = totalPrice;
        this.producerName = producerName;
        this.producerMobile = producerMobile;
        this.retailerName = retailerName;
        this.retailerMobile = retailerMobile;
        this.logisticsName = logisticsName;
        this.logisticsMobile = logisticsMobile;
        this.deliveryImage = deliveryImage;
        this.producerID = producerID;
        this.retailerID = retailerID;
        this.logisticsID = logisticsID;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getRetailerAddress() {
        return retailerAddress;
    }

    public void setRetailerAddress(String retailerAddress) {
        this.retailerAddress = retailerAddress;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getProducerMobile() {
        return producerMobile;
    }

    public void setProducerMobile(String producerMobile) {
        this.producerMobile = producerMobile;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getRetailerMobile() {
        return retailerMobile;
    }

    public void setRetailerMobile(String retailerMobile) {
        this.retailerMobile = retailerMobile;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsMobile() {
        return logisticsMobile;
    }

    public void setLogisticsMobile(String logisticsMobile) {
        this.logisticsMobile = logisticsMobile;
    }

    public String getDeliveryImage() {
        return deliveryImage;
    }

    public void setDeliveryImage(String deliveryImage) {
        this.deliveryImage = deliveryImage;
    }

    public String getProducerID() {
        return producerID;
    }

    public void setProducerID(String producerID) {
        this.producerID = producerID;
    }

    public String getRetailerID() {
        return retailerID;
    }

    public void setRetailerID(String retailerID) {
        this.retailerID = retailerID;
    }

    public String getLogisticsID() {
        return logisticsID;
    }

    public void setLogisticsID(String logisticsID) {
        this.logisticsID = logisticsID;
    }
}
