package com.AgriBuhayProj.app.Models;

public class Logistics {
    private String Province,City,Baranggay,House,Area,PostCode,FirstName,LastName,FullName,EmailID,Mobile;

    public Logistics() {
    }

    public Logistics(String province, String baranggay, String city, String area, String firstName, String lastName, String fullName, String emailID, String mobile) {
        Province = province;
        Baranggay = baranggay;
        City = city;
        Area = area;
        FirstName = firstName;
        LastName = lastName;
        FullName = fullName;
        EmailID = emailID;
        Mobile = mobile;
    }

    public String getProvince() {
        return Province;
    }
    public void setProvince(String province) {
        Province = province;
    }

    public String getBaranggay() {
        return Baranggay;
    }
    public void setBaranggay(String baranggay) {
        Baranggay = baranggay;
    }

    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }

    public String getArea() {
        return Area;
    }
    public void setArea(String area) {
        Area = area;
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
