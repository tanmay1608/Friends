package com.example.friends.Models;

public class UserProfile {

    public String username,userUID,lastname,petname;

    public UserProfile() {
    }

    public UserProfile(String username, String userUID, String lastname, String petname) {
        this.username = username;
        this.userUID = userUID;
        this.lastname = lastname;
        this.petname = petname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }
}
