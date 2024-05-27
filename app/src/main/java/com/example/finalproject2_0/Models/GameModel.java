package com.example.finalproject2_0.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GameModel {
    private String requestorId;
    public String documentID;
    public List<Map<String,Object>> requestorIds;

    public String getDocumentID() {
        return documentID;
    }

    public String getRequestorId() {
        return requestorId;
    }


    @ServerTimestamp
    Date timestamp;
    String gamename;
    String gamedescription;
    String numofplayers;
    String location;
    String place;
    public boolean visibility;
    String time;
    String agerestrictions;
    String owneruserid;

    public GameModel() {

    }

    public GameModel(String gamename, String gamedescription, String numofplayers,String location,String place, String time, String agerestrictions, String requestorId, String documentID, List<Map<String,Object>> requestorIds, Date timestamp, String owneruserid) {
        this.gamename = gamename;
        this.gamedescription = gamedescription;
        this.numofplayers = numofplayers;
        this.timestamp = timestamp;
        this.time = time;
        this.agerestrictions = agerestrictions;
        this.visibility = false;
        this.requestorId = requestorId;
        this.requestorIds = requestorIds;
        this.location = location;
        this.place = place;
        this.owneruserid = owneruserid;
        //this.documentID = documentID;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setvisibility(boolean visibility) {
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

    public Date getDate() {
        return timestamp;
    }

    public String getTime() {
        return time;
    }

    public String getAgeRes() {
        return agerestrictions;
    }


    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public void setGamedescription(String gamedescription) {
        this.gamedescription = gamedescription;
    }

    public void setNumofplayers(String numofplayers) {
        this.numofplayers = numofplayers;
    }


    public void setDate(Date date) {
        this.timestamp = timestamp;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAgeRes(String ageRes) {
        this.agerestrictions = ageRes;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAgerestrictions() {
        return agerestrictions;
    }

    public void setAgerestrictions(String agerestrictions) {
        this.agerestrictions = agerestrictions;
    }

    public List<Map<String,Object>> getRequestorIds() {
        return requestorIds;
    }


    public String getLocation() {
        return location;
    }

    public String getPlace() {
        return place;
    }

    public void setRequestorIds(List<Map<String, Object>> requestorIds) {
        this.requestorIds = requestorIds;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOwneruserid() {
        return owneruserid;
    }
}
