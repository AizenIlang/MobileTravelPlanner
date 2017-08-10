package com.example.danzee.travelplanner.Hotel;

import android.net.Uri;

import com.example.danzee.travelplanner.Booking.Booking;
import com.example.danzee.travelplanner.Rooms.Rooms;
import com.google.firebase.database.Exclude;

/**
 * Created by DanZee on 03/08/2017.
 */

public class Hotel {
    private String ID;
    private String Name;
    private String Company;
    private String Details;
    private String PhotoURL;
    private String Location1;
    private String Location2;
    private Rooms Rooms;
    private String Group;

    @Exclude
    public Uri myUri;

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

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
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

    public com.example.danzee.travelplanner.Rooms.Rooms getRooms() {
        return Rooms;
    }

    public void setRooms(com.example.danzee.travelplanner.Rooms.Rooms rooms) {
        Rooms = rooms;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }
}
