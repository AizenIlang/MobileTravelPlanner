package com.example.danzee.travelplanner.Bundle;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Rooms.Rooms;
import com.google.firebase.database.Exclude;

/**
 * Created by DanZee on 11/09/2017.
 */

public class Bundle {
    @Exclude
    private String ID;

    private Hotel hotel;
    private Activities activities;
    private Restaurant restaurant;
    private Rooms rooms;
    private float rating;
    private int NumberOfRating;
    private double cost;
    private String Details;

    public Bundle() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Activities getActivities() {
        return activities;
    }

    public void setActivities(Activities activities) {
        this.activities = activities;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfRating() {
        return NumberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        NumberOfRating = numberOfRating;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
