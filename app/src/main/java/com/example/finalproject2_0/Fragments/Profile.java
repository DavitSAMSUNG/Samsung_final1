package com.example.finalproject2_0.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finalproject2_0.MainActivity;
import com.example.finalproject2_0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {
    View view;
    EditText name, age, hobbies;
    CardView cardView;
    String[] dayarray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Inconsistent"};
    ArrayList<Integer> daylist = new ArrayList<>();
    boolean[] selecteddays;
    TextView days;
    String userID;
    FirebaseFirestore mStore;
    Button save;

    private void showDayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Select Days");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(dayarray, selecteddays, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    daylist.add(which);
                }else{
                    daylist.remove(Integer.valueOf(which));
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < daylist.size(); i++) {
                    stringBuilder.append(dayarray[daylist.get(i)]);
                    if (i != daylist.size() - 1) {
                        stringBuilder.append(", ");
                    }
                    days.setText(stringBuilder.toString());
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i=0; i < selecteddays.length; i++) {
                    selecteddays[i] = false;
                    daylist.clear();
                    days.setText("");
                }
            }
        });
        builder.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mStore = FirebaseFirestore.getInstance();

        name = view.findViewById(R.id.nameText);
        age = view.findViewById(R.id.ageText);
        hobbies = view.findViewById(R.id.hobbiesText);
        save = view.findViewById(R.id.save);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //DocumentReference userRef = mStore.collection("Users").document(userID);


        cardView = view.findViewById(R.id.availablecard);
        days = view.findViewById(R.id.items);
        selecteddays = new boolean[dayarray.length];

        cardView.setOnClickListener(v -> {
            showDayDialog();
        });


        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("saveClicked", false);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Map<String, Object> userProfile = new HashMap<>();

                //intent.putExtra("saveClicked", getIntent().getBooleanExtra("saveClicked",true));

                if (!name.getText().toString().isEmpty() && !age.getText().toString().isEmpty() && !days.getText().toString().isEmpty()) {
                    userProfile.put("name", name.getText().toString());
                    userProfile.put("age", age.getText().toString());
                    userProfile.put("hobbies", hobbies.getText().toString());
                    userProfile.put("available", days.getText().toString());
                    userProfile.put("userID", userID);

                    mStore.collection("users").document(userID).set(userProfile);
                    Toast.makeText(requireContext(), "Profile saved!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Input all fields!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        mStore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()){
                    name.setText(document.getString("name"));
                    age.setText(document.getString("age"));
                    hobbies.setText(document.getString("hobbies"));
                    days.setText(document.getString("available"));
                    // Now you can use the email value as needed
                    // ...
                } else {
                    // Document does not exist
                }
            } else {
                // Error fetching document
            }
        });


        return view;
    }
}