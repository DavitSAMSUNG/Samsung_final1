package com.example.finalproject2_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finalproject2_0.Fragments.Chat;
import com.example.finalproject2_0.Fragments.Discover;
import com.example.finalproject2_0.Fragments.Profile;

public class MainActivity extends AppCompatActivity {
    private Button LOGIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LOGIN = (Button) findViewById(R.id.loginbtn);
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoggedIn();
            }
        });



    }
    private void openLoggedIn(){
        Intent intent = new Intent (this, LoggedIn.class);
        startActivity(intent);
    }

}
