package com.example.finalproject2_0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NewGameCreation extends AppCompatActivity implements TextWatcher {
    private Button back;
    private Button datebtn, timebtn;
    private EditText editTextGameName, editTextGameDesc, editTextNmbOfPl;
    private TextView textViewNewGameName, InputedDate, InputedTime;
    Spinner AgeRes;
    TextView sdate, stime;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_creation);
        String[] options = {"6+", "12+", "16+", "18+", "21+"};
        mStore = FirebaseFirestore.getInstance();


        Spinner spinner = findViewById(R.id.age_restriction_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(adapter);


        stime = findViewById(R.id.selectedtime);
        sdate = findViewById(R.id.selecteddate);
        datebtn = findViewById(R.id.choosedate);
        timebtn = findViewById(R.id.choosetime);

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimeRangePicker();
            }
        });
//____________________
        TextView[] textViews = {
                findViewById(R.id.gamename),
                findViewById(R.id.shtdesc),
                findViewById(R.id.date),
                findViewById(R.id.nmb),
                findViewById(R.id.ageres)
        };

        int delay = 800; // Delay between animations (in milliseconds)
        Handler handler = new Handler();

        for (int i = 0; i < textViews.length; i++) {
            final int index = i; // Capture the loop variable for the Runnable
            Runnable animationStarter = new Runnable() {
                @Override
                public void run() {
                    TextView textView = textViews[index];

                    // Create your animation here (e.g., TranslateAnimation)
                    TranslateAnimation animation = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f, // Start from left (on-screen)
                            Animation.RELATIVE_TO_SELF, 1.3f, // Move to the right (off-screen)
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    animation.setDuration(650); // 1 second duration
                    animation.setRepeatCount(0); // Set animation to repeat only once
                    animation.setFillAfter(true);
                    // Start the animation on the TextView
                    textView.startAnimation(animation);

                    // Set the final X-coordinate (e.g., center of the screen)
                }
            };

            // Schedule the animation with a delay based on the loop index
            handler.postDelayed(animationStarter, delay * i);

            //___________________


        }
//_________________

        editTextGameName = findViewById(R.id.wgamename);
        textViewNewGameName = findViewById(R.id.newgame);
        editTextGameDesc = findViewById(R.id.secedit);
        editTextNmbOfPl = findViewById(R.id.editTextNumber);
        InputedDate = findViewById(R.id.selecteddate);
        InputedTime = findViewById(R.id.selectedtime);
        AgeRes = findViewById(R.id.age_restriction_spinner);

        editTextGameName.addTextChangedListener(this);


        Button create = (Button) findViewById(R.id.creating);
        back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGameCreation.this, MainActivity.class);
                intent.putExtra("toMyGames", true);
                startActivity(intent);
            }
        });
        //ImageView newgamebackground = findViewById(R.id.newgameback);
        //_________________________
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //DocumentReference documentReference = mStore.collection("users").document(userID);
        Map<String, Object> game = new HashMap<>();
        //Map<String, Object> user = new HashMap<>();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGameCreation.this, MainActivity.class);
                intent.putExtra("toMyGames", true);
//                intent.putExtra("GameName", editTextGameName.getText());
//                intent.putExtra("GameDesc", editTextGameDesc.getText());
//                intent.putExtra("NumberOfPlayers", editTextNmbOfPl.getText());
                game.put("gamename", editTextGameName.getText().toString());
                game.put("gamedescription", editTextGameDesc.getText().toString());
                game.put("numofplayers", editTextNmbOfPl.getText().toString());
                game.put("date",InputedDate.getText().toString());
                game.put("time",InputedTime.getText().toString());
                game.put("agerestrictions",AgeRes.getSelectedItem().toString());
                game.put("owneruserid", userID);



                mStore.collection("Games").add(game);

                startActivity(intent);
            }
        });


        System.out.println(editTextGameName.getText().toString());


        //________________________
//        RenderEffect blur = RenderEffect.createBlurEffect((float) 10, (float) 5, Shader.TileMode.CLAMP);
//        newgamebackground.setRenderEffect(blur);
    }

    //________________
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String newName = s.toString();
        textViewNewGameName.setText(newName);
    }

    //__________________

    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                sdate.setText(String.valueOf(day) + "." + String.valueOf(month) + "." + String.valueOf(year));
                if (day < 10) {
                    sdate.setText("0" + String.valueOf(day) + "." + String.valueOf(month) + "." + String.valueOf(year));
                }
                if (month < 10) {
                    sdate.setText(String.valueOf(day) + "." + "0" + String.valueOf(month) + "." + String.valueOf(year));
                }
                if (day < 10 && month < 10) {
                    sdate.setText("0" + String.valueOf(day) + "." + "0" + String.valueOf(month) + "." + String.valueOf(year));
                }

            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }

    private void openTimeRangePicker() {
        AtomicInteger startHour = new AtomicInteger(14);
        AtomicInteger startMinute = new AtomicInteger();

        AtomicInteger endHour = new AtomicInteger(3);
        AtomicInteger endMinute = new AtomicInteger();

        TimePickerDialog startTimePicker = new TimePickerDialog(this, (timePicker, hour, minute) -> {
            startHour.set(hour);
            startMinute.set(minute);

            updateTimeRangeText(startHour.get(), startMinute.get(), endHour.get(), endMinute.get());
        }, startHour.get(), startMinute.get(), false);

        TimePickerDialog endTimePicker = new TimePickerDialog(this, (timePicker, hour, minute) -> {
            endHour.set(hour);
            endMinute.set(minute);

            updateTimeRangeText(startHour.get(), startMinute.get(), endHour.get(), endMinute.get());
        }, endHour.get(), endMinute.get(), false);

        endTimePicker.show();
        startTimePicker.show();
    }

    private void updateTimeRangeText(int startHour, int startMinute, int endHour, int endMinute) {
        String startTime = String.format("%02d:%02d", startHour, startMinute);
        String endTime = String.format("%02d:%02d", endHour, endMinute);

        String timeRange = startTime + " to " + endTime;
        stime.setText(timeRange);
    }

}