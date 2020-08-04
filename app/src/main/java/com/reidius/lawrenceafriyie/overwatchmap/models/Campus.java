package com.reidius.lawrenceafriyie.overwatchmap.models;

import com.google.gson.annotations.SerializedName;

public class Campus {

    @SerializedName("campusID")
    private Integer campus_id;
    @SerializedName("name")
    private String name;
    @SerializedName("suburbID")
    private Integer suburb_id;

    public Campus(Integer campus_id, String name, Integer suburb_id) {
        this.campus_id = campus_id;
        this.name = name;
        this.suburb_id = suburb_id;
    }

    public Integer getCampus_id() {
        return campus_id;
    }

    public void setCampus_id(Integer campus_id) {
        this.campus_id = campus_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSuburb_id() {
        return suburb_id;
    }

    public void setSuburb_id(Integer suburb_id) {
        this.suburb_id = suburb_id;
    }

    @Override
    public String toString() {
        return "Campus{" +
                "campus_id=" + campus_id +
                ", name='" + name + '\'' +
                ", suburb_id=" + suburb_id +
                '}';
    }
}
