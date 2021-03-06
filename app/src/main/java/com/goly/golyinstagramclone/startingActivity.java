package com.goly.golyinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.parse.ParseUser;

public class startingActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp,btnLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        btnSignUp = findViewById(R.id.btnStartSignUp);
        btnLogIn = findViewById(R.id.btnStartLog);
        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        if(ParseUser.getCurrentUser() != null){goToSocialMediaActivity();finish();}



    }
    private void goToSocialMediaActivity(){
        Intent intent = new Intent(startingActivity.this,SocialMediaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnStartLog:
                Intent intent = new Intent(startingActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnStartSignUp:
                Intent intent2 = new Intent(startingActivity.this,SignUp.class);
                startActivity(intent2);
                break;

        }
    }
}