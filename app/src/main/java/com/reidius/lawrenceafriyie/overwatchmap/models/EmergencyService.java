package com.reidius.lawrenceafriyie.overwatchmap.models;


import com.google.gson.annotations.SerializedName;

public class EmergencyService {

    @SerializedName("emergencyServiceID")
    private Integer emergencyServiceID;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("contactNo")
    private String contactNo;
    @SerializedName("suburbID")
    private Integer suburbID;

    public EmergencyService(Integer emergencyServiceID, String name, String description, String contactNo, Integer suburbID) {
        this.emergencyServiceID = emergencyServiceID;
        this.name = name;
        this.description = description;
        this.contactNo = contactNo;
        this.suburbID = suburbID;
    }

    public EmergencyService() {
    }

    public Integer getEmergencyServiceID() {
        return emergencyServiceID;
    }

    public void setEmergencyServiceID(Integer emergencyServiceID) {
        this.emergencyServiceID = emergencyServiceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Integer getSuburbID() {
        return suburbID;
    }

    public void setSuburbID(Integer suburbID) {
        this.suburbID = suburbID;
    }

    @Override
    public String toString() {
        return "EmergencyService{" +
                "emergencyServiceID=" + emergencyServiceID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", suburbID=" + suburbID +
                '}';
    }
}
