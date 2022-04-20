package com.AgriBuhayProj.app.Models;

import android.net.Uri;

public class History {
    String trackingNumber,deliveryDate,retailerAddress,totalPrice,producerName,retailerName,retailerMobile,deliveryImage,deliveryID;

    public History(){ }

    public History(String trackingNumber, String deliveryDate, String retailerAddress, String totalPrice, String producerName, String retailerName, String retailerMobile, String deliveryImage, String deliveryID) {
        this.trackingNumber = trackingNumber;
        this.deliveryDate = deliveryDate;
        this.retailerAddress = retailerAddress;
        this.totalPrice = totalPrice;
        this.producerName = producerName;
        this.retailerName = retailerName;
        this.retailerMobile = retailerMobile;
        this.deliveryImage = deliveryImage;
        this.deliveryID = deliveryID;
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

    public String getDeliveryImage() {
        return deliveryImage;
    }

    public void setDeliveryImage(String deliveryImage) {
        this.deliveryImage = deliveryImage;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }
}
