package com.AgriBuhayProj.app.RetailerPanel;

//RETAILER ORDER STATUS MODEL
public class AlreadyOrdered {

   private String isOrdered;

    public AlreadyOrdered(String isOrdered) {
        this.isOrdered = isOrdered;
    }

    public AlreadyOrdered()
    {

    }

    public String getIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(String isOrdered) {
        this.isOrdered = isOrdered;
    }
}
