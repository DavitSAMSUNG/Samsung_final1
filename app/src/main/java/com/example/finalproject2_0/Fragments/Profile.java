package com.example.finalproject2_0.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.example.finalproject2_0.MainActivity;
import com.example.finalproject2_0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {
    View view;
    EditText name,age,hobbies,available;
    String userID;
    FirebaseFirestore mStore;
    Button save;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mStore = FirebaseFirestore.getInstance();

        name = view.findViewById(R.id.nameText);
        age = view.findViewById(R.id.ageText);
        hobbies = view.findViewById(R.id.hobbiesText);
        available = view.findViewById(R.id.availableText);
        save = view.findViewById(R.id.save);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //DocumentReference userRef = mStore.collection("Users").document(userID);

        Map<String, Object> userProfile = new HashMap<>();

        Intent intent = new Intent (getActivity(), MainActivity. class);
        intent.putExtra("saveClicked", false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intent.putExtra("saveClicked", getIntent().getBooleanExtra("saveClicked",true));
                userProfile.put("name", name.getText().toString());
                userProfile.put("age", age.getText().toString());
                userProfile.put("hobbies",hobbies.getText().toString());
                userProfile.put("available", available.getText().toString());
                userProfile.put("userID", userID);


                mStore.collection("users").document(userID).set(userProfile);
            }
        });







        return view;
    }
}