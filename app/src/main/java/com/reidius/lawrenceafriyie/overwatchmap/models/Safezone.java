package com.reidius.lawrenceafriyie.overwatchmap.models;

public class Safezone {

    private Integer safezoneID;
    private String name;
    private Double longitude;
    private Double latidude;
    private String raidius;
    private Integer suburbID;
    private String adminID;


    public Integer getSafezoneID() {
        return safezoneID;
    }

    public String getName() {
        return name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatidude() {
        return latidude;
    }

    public String getRaidius() {
        return raidius;
    }

    public Integer getSuburbID() {
        return suburbID;
    }

    public String getAdminID() {
        return adminID;
    }

    @Override
    public String toString() {
        return "Safezone{" +
                "safezoneID=" + safezoneID +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latidude=" + latidude +
                ", raidius='" + raidius + '\'' +
                ", suburbID=" + suburbID +
                ", adminID='" + adminID + '\'' +
                '}';
    }
}
