package com.example.finalproject2_0;

public class GameModel {

    String gamename;
    String gamedescription;
    String numofplayers;
    String addtextview;
    boolean visibility;
    String date;
    String time;
    String ageRes;

    public GameModel(){

    }

    public GameModel(String gamename, String gamedescription, String numofplayers, String addtextview, String date, String time,String ageRes) {
        this.addtextview = addtextview;
        this.gamename = gamename;
        this.gamedescription = gamedescription;
        this.numofplayers = numofplayers;
        this.date = date;
        this.time = time;
        this.ageRes = ageRes;
        this.visibility = false;
    }

    public boolean isVisibility() {
        return visibility;
    }
    public void setvisibility (boolean visibility) {
        this.visibility = visibility;
    }

    public String getGamename() {
        return gamename;
    }


    public String getGamedescription() {
        return gamedescription;
    }

    public String getNumofplayers() {
        return numofplayers;
    }


    public String getAddtextview() {
        return addtextview;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAgeRes() {
        return ageRes;
    }
}
