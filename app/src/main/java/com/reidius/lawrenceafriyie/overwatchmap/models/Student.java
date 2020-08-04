package com.reidius.lawrenceafriyie.overwatchmap.models;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("studentID")
    private Integer personID;

    public Student(Integer personID) {
        this.personID = personID;
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }
}
