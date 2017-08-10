package com.example.danzee.travelplanner.Restaurant;

import android.net.Uri;

import com.google.firebase.database.Exclude;

/**
 * Created by DanZee on 08/08/2017.
 */

public class Restaurant {
    private String Name;
    private double Price;
    private String sPrice;
    private String Company;
    private String PhotoUrl;
    private String Details;
    private String Location1;
    private String Location2;
    private String Group;

    @Exclude
    public Uri myUri;

    public Restaurant(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getsPrice() {
        return sPrice;
    }

    public void setsPrice(String sPrice) {
        this.sPrice = sPrice;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
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

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }
}
