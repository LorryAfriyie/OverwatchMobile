package com.reidius.lawrenceafriyie.overwatchmap.models;

import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ReportIncident {

    @SerializedName("description")
    private String description;
    @SerializedName("personID")
    private Integer personID;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("dateTime")
    private String dateTime;
    @SerializedName("status")
    private String status;
    @SerializedName("typeID")
    private Integer typeID;
    @SerializedName("comment")
    private String comment;
    @SerializedName("campusID")
    private Integer campusID;
    @SerializedName("address")
    private String address;

    public ReportIncident(String description, Integer personID, String longitude, String latitude,
                          String dateTime, String status, Integer typeID, String comment,
                          Integer campusID, String address) {
        this.description = description;
        this.personID = personID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dateTime = dateTime;
        this.status = status;
        this.typeID = typeID;
        this.comment = comment;
        this.campusID = campusID;
        this.address = address;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String date) {
        this.dateTime = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCampusID() {
        return campusID;
    }

    public void setCampusID(Integer campusID) {
        this.campusID = campusID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ReportIncident{" +
                "description='" + description + '\'' +
                ", personID=" + personID +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", status='" + status + '\'' +
                ", typeID=" + typeID +
                ", comment='" + comment + '\'' +
                ", campusID=" + campusID +
                ", address='" + address + '\'' +
                '}';
    }
}

