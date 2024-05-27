package com.example.finalproject2_0.Interfaces;

import com.example.finalproject2_0.Models.User;

public interface UserCallback {
    void onUserFetched(User user, Boolean isAccepted);
}
