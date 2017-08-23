package com.example.danzee.travelplanner.Activities;

import android.net.Uri;

import com.example.danzee.travelplanner.Booking.Booking;
import com.google.firebase.database.Exclude;

/**
 * Created by DanZee on 03/08/2017.
 */

public class Activities {
    @Exclude
    private String ID;
    private String Name;
    private String Details;
    private String Company;
    private String Location1;
    private String Location2;
    private float Rating;
    private String Map;

    @Exclude
    public Uri myUri;

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getLocation1() {
        return Location1;
    }

    public void setLocation1(String location1) {
        Location1 = location1;
    }

    public String getLocation2() {
        return Location2;
    }

    public void setLocation2(String location2) {
        Location2 = location2;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getMap() {
        return Map;
    }

    public void setMap(String map) {
        Map = map;
    }

    private String sTotalCost;
    private double TotalCost;
    private String PhotoURL;
    private String Group;

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

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }
}
