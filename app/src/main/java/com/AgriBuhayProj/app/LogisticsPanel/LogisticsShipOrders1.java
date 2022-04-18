package com.AgriBuhayProj.app.LogisticsPanel;

public class LogisticsShipOrders1 {

    private String Address, ProducerId, ProducerName, GrandTotalPrice, MobileNumber, Name, RandomUID, Status, UserId;

    public LogisticsShipOrders1(String address, String producerId, String producerName, String grandTotalPrice, String mobileNumber, String name, String randomUID, String status, String userId) {
        Address = address;
        ProducerId = producerId;
        ProducerName = producerName;
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
        RandomUID = randomUID;
        Status = status;
        UserId = userId;
    }

    public LogisticsShipOrders1() {

    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProducerId() {
        return ProducerId;
    }

    public void setProducerId(String producerId) {
        ProducerId = producerId;
    }

    public String getProducerName() {
        return ProducerName;
    }

    public void setProducerName(String producerName) {
        ProducerName = producerName;
    }

    public String getGrandTotalPrice() {
        return GrandTotalPrice;
    }

    public void setGrandTotalPrice(String grandTotalPrice) {
        GrandTotalPrice = grandTotalPrice;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
