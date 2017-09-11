package com.example.danzee.travelplanner.Booking;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Rooms.Rooms;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by DanZee on 03/08/2017.
 */

public class Booking {

    private String ID;
    private String Name;
    private String OwnerID;
    private String sTotalCost;
    private double iTotalCost;
    private Hotel Hotel;
    private ArrayList<Activities> activitiesArrayList;
    private ArrayList<Restaurant> restaurantArrayList;
    private Rooms Rooms;
    private boolean Favorite;

    public Booking() {
    }

    public ArrayList<Restaurant> getRestaurantArrayList() {
        return restaurantArrayList;
    }

    public void setRestaurantArrayList(ArrayList<Restaurant> restaurantArrayList) {
        this.restaurantArrayList = restaurantArrayList;
    }

    public boolean isFavorite() {
        return Favorite;
    }

    public void setFavorite(boolean favorite) {
        Favorite = favorite;
    }

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

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getsTotalCost() {
        return sTotalCost;
    }

    public void setsTotalCost(String sTotalCost) {
        this.sTotalCost = sTotalCost;
    }

    public double getiTotalCost() {
        return iTotalCost;
    }

    public void setiTotalCost(double iTotalCost) {
        this.iTotalCost = iTotalCost;
    }

    public com.example.danzee.travelplanner.Hotel.Hotel getHotel() {
        return Hotel;
    }

    public void setHotel(com.example.danzee.travelplanner.Hotel.Hotel hotel) {
        Hotel = hotel;
    }

    public ArrayList<Activities> getActivitiesArrayList() {
        return activitiesArrayList;
    }

    public void setActivitiesArrayList(ArrayList<Activities> activitiesArrayList) {
        this.activitiesArrayList = activitiesArrayList;
    }

    public com.example.danzee.travelplanner.Rooms.Rooms getRooms() {
        return Rooms;
    }

    public void setRooms(com.example.danzee.travelplanner.Rooms.Rooms rooms) {
        Rooms = rooms;
    }
}
