package com.AgriBuhayProj.app.Models;

public class Logistics {
    private String Area,City,EmailID,Fname,House,Lname,Mobile,State,Suburban;

    public Logistics() {
    }

    public Logistics(String area, String city, String emailID, String fname, String house, String lname, String mobile, String state, String suburban) {
        Area = area;
        City = city;
        EmailID = emailID;
        Fname = fname;
        House = house;
        Lname = lname;
        Mobile = mobile;
        State = state;
        Suburban = suburban;
    }

    public String getArea() {
        return Area;
    }
    public void setArea(String area) {
        Area = area;
    }

    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }

    public String getEmailID() {
        return EmailID;
    }
    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getFname() {
        return Fname;
    }
    public void setFname(String fname) {
        Fname = fname;
    }

    public String getHouse() {
        return House;
    }
    public void setHouse(String house) {
        House = house;
    }

    public String getLname() {
        return Lname;
    }
    public void setLname(String lname) {
        Lname = lname;
    }

    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getState() {
        return State;
    }
    public void setState(String state) {
        State = state;
    }

    public String getSuburban() {
        return Suburban;
    }
    public void setSuburban(String suburban) {
        Suburban = suburban;
    }
}
