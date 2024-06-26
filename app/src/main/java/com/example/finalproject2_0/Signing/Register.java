package com.example.finalproject2_0.Signing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject2_0.MainActivity;
import com.example.finalproject2_0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, editTextRetype;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView clicktologin;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent (getApplicationContext(), MainActivity. class);
//            startActivity (intent);
//            finish();
//        }else{
//            Intent intent = new Intent (getApplicationContext(), Register. class);
//            startActivity (intent);
//            finish();
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextRetype = findViewById(R.id.reenterpassword);
        buttonReg = findViewById(R.id.registerbtn);
        progressBar= findViewById(R.id.progressbar);
        clicktologin= findViewById(R.id.loginnow);

        clicktologin.setOnClickListener(new View. OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext (), Login.class);
                startActivity (intent);
                finish ();
            }});


        buttonReg.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, retypepassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                retypepassword = String.valueOf(editTextRetype.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this,"Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this,"Enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(retypepassword)) {
                    Toast.makeText(Register.this,"Retype password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                if (retypepassword.equals(password)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Account created",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent (getApplicationContext(), MainActivity. class);
                                        intent.putExtra("fromRegister", true);
                                        intent.putExtra("saveClicked1",false);
                                        startActivity (intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, task.getException().toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Unmatching passwords",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}