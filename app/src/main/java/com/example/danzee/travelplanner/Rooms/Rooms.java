package com.example.danzee.travelplanner.Rooms;

import com.example.danzee.travelplanner.Booking.Booking;

/**
 * Created by DanZee on 03/08/2017.
 */

public class Rooms {

    private String ID;
    private String Name;
    private String Details;
    private String sTotalCost;
    private double TotalCost;
    private String PhotoURL;

    private String Size;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getsTotalCost() {
        return sTotalCost;
    }

    public void setsTotalCost(String sTotalCost) {
        this.sTotalCost = sTotalCost;
    }

    public double getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(double totalCost) {
        TotalCost = totalCost;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }
}
