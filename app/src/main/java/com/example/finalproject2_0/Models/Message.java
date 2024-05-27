package com.example.finalproject2_0.Models;

public class Message {
    private String userId;
    private String message;
    private String timestamp;
    private String username;
    User user;

    public Message() {
    }

    public Message(String userId, String message, String timestamp, String username, User user) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.username = username;
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }
    public String getMessage() {
        return message;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getUserName() {
        return username;
    }
    public User getUser() {
        return user;
    }
}
