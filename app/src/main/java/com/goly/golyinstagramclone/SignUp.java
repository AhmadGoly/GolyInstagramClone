package com.goly.golyinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail,edtUsername,edtPassword;
    private Button btnSignUp;
    private TextView goToLoginTW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        edtEmail = findViewById(R.id.edtEmailSignup);
        edtUsername = findViewById(R.id.edtUsernameSignup);
        edtPassword = findViewById(R.id.edtPasswordSignup);
        btnSignUp = findViewById(R.id.btnSignUp);
        goToLoginTW = findViewById(R.id.btnGoToLoginActivity);
        btnSignUp.setOnClickListener(this);
        goToLoginTW.setOnClickListener(this);
        //Pressing Enter will sign up user automatically:
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                    onClick(btnSignUp);
                return false;
            }
        });
        if(ParseUser.getCurrentUser() != null )ParseUser.logOut();

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignUp:
                if(edtEmail.getText().toString().equals("") ||
                        edtUsername.getText().toString().equals("") ||
                        edtPassword.getText().toString().equals("")
                ){
                    FancyToast.makeText(SignUp.this,"Check Email, Username or Password and try again.",
                            Toast.LENGTH_LONG,FancyToast.ERROR,false).show();

                }else {


                    final ParseUser newUser = new ParseUser();
                    newUser.setEmail(edtEmail.getText().toString());
                    newUser.setUsername(edtUsername.getText().toString());
                    newUser.setPassword(edtPassword.getText().toString());

                    ProgressDialog loadingDialog = new ProgressDialog(this);
                    loadingDialog.setMessage("Signing up " + edtUsername.getText().toString());
                    loadingDialog.show();
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUp.this,
                                        newUser.getUsername() + " is signed up successfully!",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                            } else FancyToast.makeText(SignUp.this, e.getMessage(),
                                    Toast.LENGTH_LONG, FancyToast.ERROR, false).show();

                        }
                    });
                    loadingDialog.dismiss();
                }
                break;
            case R.id.btnGoToLoginActivity:
                Intent intent = new Intent(SignUp.this,LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
    public void showFancyToast(Context c, String s, int state){
        //https://github.com/Shashank02051997/FancyToast-Android
        FancyToast.makeText(c,s,FancyToast.LENGTH_LONG,state,true).show();
    }
    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){}
    }
}