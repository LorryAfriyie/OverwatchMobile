package com.reidius.lawrenceafriyie.overwatchmap.models;

public class Person {

    private Integer personID;

    private String name;

    private String surname;

    private String email;

    private String contactNo;

    private String deviceToken;

    private String personType;

    public Person(Integer personID, String name, String surname, String email, String contactNo, String deviceToken, String personType) {
        this.personID = personID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.contactNo = contactNo;
        this.deviceToken = deviceToken;
        this.personType = personType;
    }

    public Person(Integer personID) {
        this.personID = personID;
    }

    public Integer getPersonID() {
        return personID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getPersonType() {
        return personType;
    }
}
