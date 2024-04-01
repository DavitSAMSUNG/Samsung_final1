package com.example.finalproject2_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject2_0.Fragments.Chat;
import com.example.finalproject2_0.Fragments.Discover;
import com.example.finalproject2_0.Fragments.MyGames;
import com.example.finalproject2_0.Fragments.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button discoverBtn, chatBtn, profileBtn,mygamesBtn;
    FirebaseAuth auth;
    //Button button;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        auth = FirebaseAuth.getInstance();
        //button = findViewById(R.id.logout);
        user = auth.getCurrentUser();


        discoverBtn = findViewById(R.id.discover_button);
        chatBtn = findViewById(R.id.chat_button);
        profileBtn = findViewById(R.id.profile_button);
        mygamesBtn = findViewById(R.id.my_games);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
//        button.setOnClickListener (new View.OnClickListener() {
//            @Override
//            public void onClick (View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//                finish();
//            }
//        });


        boolean openedByButton = getIntent().getBooleanExtra("fromButton", false);
        if (openedByButton) {
            replaceFragment(new MyGames());
        }

        mygamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new MyGames());

            }
        });

        discoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new Discover());

            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hi");
                replaceFragment(new Chat());

            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new Profile());

            }
        });
        //__________________
//        ImageView myImageView = findViewById(R.id.dice);
//
//        // Apply the blur effect
//
//        RenderEffect blur = RenderEffect.createBlurEffect((float) 25, (float) 30, Shader.TileMode.CLAMP);
//        myImageView.setRenderEffect(blur);
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }
}
