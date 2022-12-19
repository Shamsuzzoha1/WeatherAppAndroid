package com.apicall.weatherapp.model;

public class Latlong {
    private Double latitude;
    private Double longitude;

    public Latlong(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
