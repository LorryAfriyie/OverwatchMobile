package com.reidius.lawrenceafriyie.overwatchmap.models;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("loginID")
    private Integer personID;
    private String username;
    private String password;

    public Login(Integer personID, String username, String password) {
        this.personID = personID;
        this.username = username;
        this.password = password;
    }

    public Integer getPersonID() {
        return personID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "personID=" + personID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
