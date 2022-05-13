package com.AgriBuhayProj.app.Models;

public class Retailer {
    //VARIABLES
    private String Province,City,Baranggay,LocalAddress,FirstName,LastName,FullName,EmailID,Mobile;

    //OBJECT INSTANCE
    public Retailer() { }

    //OBJECT CONSTRUCTOR
    public Retailer(String province, String city, String baranggay, String localAddress, String firstName, String lastName, String fullName, String emailID, String mobile) {
        Province = province;
        City = city;
        Baranggay = baranggay;
        LocalAddress = localAddress;
        FirstName = firstName;
        LastName = lastName;
        FullName = fullName;
        EmailID = emailID;
        Mobile = mobile;
    }

    //GETTER-SETTER
    public String getProvince() {
        return Province;
    }
    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }

    public String getBaranggay() {
        return Baranggay;
    }
    public void setBaranggay(String baranggay) {
        Baranggay = baranggay;
    }

    public String getLocalAddress() {
        return LocalAddress;
    }
    public void setLocalAddress(String localAddress) {
        LocalAddress = localAddress;
    }

    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }
    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFullName() {
        return FullName;
    }
    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmailID() {
        return EmailID;
    }
    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
