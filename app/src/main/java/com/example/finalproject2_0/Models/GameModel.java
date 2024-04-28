package com.example.finalproject2_0.Models;

public class GameModel {
    private String requestorId;
    public String documentID;

    public String getDocumentID() {
        return documentID;
    }

    public String getRequestorId() {
        return requestorId;
    }




    String gamename;
    String gamedescription;
    String numofplayers;
    String addtextview;
    public boolean visibility;
    String date;
    String time;
    String ageRes;

    public GameModel(){

    }

    public GameModel(String gamename, String gamedescription, String numofplayers, String addtextview, String date, String time,String ageRes,String requestorId, String documentID) {
        this.addtextview = addtextview;
        this.gamename = gamename;
        this.gamedescription = gamedescription;
        this.numofplayers = numofplayers;
        this.date = date;
        this.time = time;
        this.ageRes = ageRes;
        this.visibility = false;
        this.requestorId = requestorId;
        //this.documentID = documentID;
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
