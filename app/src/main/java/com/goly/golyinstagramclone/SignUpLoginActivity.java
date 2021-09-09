package com.goly.golyinstagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUserSignUp,edtUserLogin,edtPasswordSignUp,edtPasswordLogin,edtEmailSignUp;
    private Button btnSignUp,btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);
        edtUserSignUp = findViewById(R.id.edtUserNameSignUp);
        edtUserLogin = findViewById(R.id.edtUserNameLogin);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin  = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogin:
                ParseUser.logInInBackground(edtUserLogin.getText().toString(),
                        edtPasswordLogin.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null && e == null){
                                    FancyToast.makeText(SignUpLoginActivity.this,
                                            user.get("username")+ " is logged in successfully",
                                            Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                    Intent intent = new Intent(SignUpLoginActivity.this,WelcomeActivity.class);
                                    startActivity(intent);
                                }
                                else FancyToast.makeText(SignUpLoginActivity.this,
                                            "ERROR: "+e.getMessage(),
                                            Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                            }
                        }
                );
                break;
            case R.id.btnSignUp:
                ParseUser appUser = new ParseUser();
                appUser.setUsername(edtUserSignUp.getText().toString());
                appUser.setPassword(edtPasswordSignUp.getText().toString());
                appUser.setEmail(edtEmailSignUp.getText().toString());
                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(SignUpLoginActivity.this,
                                    appUser.get("username")+ " is signed up successfully",
                                    Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                        }else FancyToast.makeText(SignUpLoginActivity.this,
                                "ERROR: "+e.getMessage(),
                                Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                });
                break;
        }
    }
}
