package com.example.supercompare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    TextView txtSlogan,logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        txtSlogan = findViewById(R.id.txtSlogan);

        txtSlogan.setTextSize(18);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign in button clicked : move to login activity
                Intent intentSignIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(intentSignIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign up button clicked : move to sign up activity
                Intent intentSignUp = new Intent(MainActivity.this,SignUp.class);
                startActivity(intentSignUp);
            }
        });


    }
}
