package com.example.finalproject2_0.Models;

public class User {
    String name,age,available,hobbies,userID;
    public User(){

    }
    public User(String name,String age,String available,String hobbies,String userID){
        this.name=name;
        this.age=age;
        this.available=available;
        this.hobbies=hobbies;
        this.userID=userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
