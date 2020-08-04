package com.reidius.lawrenceafriyie.overwatchmap.models;

public class Hotspot {
    private Integer hotspotID;
    private Double longitude;
    private  Double latitude;
    private String duration;
    private String radius;
    private Integer suburbID;
    private Integer adminID;

    public Integer getHotspotID() {
        return hotspotID;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getDuration() {
        return duration;
    }

    public String getRadius() {
        return radius;
    }

    public Integer getSuburbID() {
        return suburbID;
    }

    public Integer getAdminID() {
        return adminID;
    }

    public Hotspot(Integer hotspotID, Double longitude, Double latitude, String duration, String radius, Integer suburbID, Integer adminID) {
        this.hotspotID = hotspotID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.duration = duration;
        this.radius = radius;
        this.suburbID = suburbID;
        this.adminID = adminID;
    }

    @Override
    public String toString() {
        return "Hotspot{" +
                "hotspotID=" + hotspotID +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", duration='" + duration + '\'' +
                ", radius='" + radius + '\'' +
                ", suburbID=" + suburbID +
                ", adminID=" + adminID +
                '}';
    }
}
