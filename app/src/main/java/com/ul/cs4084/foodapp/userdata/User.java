package com.ul.cs4084.foodapp.userdata;

public class User {
    private String Name;
    private String Password;

    public User() {
    }

    public User(String name, String password) {
        this.Name = name;
        this.Password = password;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }
}


