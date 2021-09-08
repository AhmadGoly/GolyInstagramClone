package com.goly.golyinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
    public void helloWorldTapped(View v){
        Toast.makeText(SignUp.this,"Pressed.",Toast.LENGTH_LONG).show();
        ParseObject boxer = new ParseObject("Boxer");
        boxer.put("punch_speed",200);
        boxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(SignUp.this,"boxer ojbect saved on server.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void showFancyToast(Context c, String s, int state){
        //https://github.com/Shashank02051997/FancyToast-Android
        FancyToast.makeText(c,s,FancyToast.LENGTH_LONG,state,true).show();
    }
}