package com.example.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by lekha on 5/13/2018.
 */

public class TripLocation {
    private String name;
    private LatLng location;
    private String address;
    private String type;
    private int rating;

    TripLocation(String name, LatLng location, String address, String type, int rating) {
        this.name = name;
        this.location = location;
        this.address = address;
        this.type = type;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }
}
