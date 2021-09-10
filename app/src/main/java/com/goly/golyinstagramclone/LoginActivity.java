package com.goly.golyinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtLoginEmail,edtLoginPassword;
    private Button btnLogin;
    private TextView goToSignUpActivityTW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        edtLoginEmail = findViewById(R.id.edtEmailLogin);
        edtLoginPassword = findViewById(R.id.edtPassworLogin);
        btnLogin = findViewById(R.id.btnLogin);
        goToSignUpActivityTW = findViewById(R.id.btnGoToSignUpActivity);
        btnLogin.setOnClickListener(this);
        goToSignUpActivityTW.setOnClickListener(this);

        //Pressing Enter will Login user automatically:
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                    onClick(btnLogin);
                return false;
            }
        });

        //if(ParseUser.getCurrentUser() != null)goToSocialMediaActivity();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogin:
                if(edtLoginEmail.getText().toString().equals("") ||
                        edtLoginPassword.getText().toString().equals("")
                ){
                    FancyToast.makeText(LoginActivity.this,"Check Email and Password and try again.",
                            Toast.LENGTH_LONG,FancyToast.ERROR,false).show();

                }else {
                    ProgressDialog loadingDialog = new ProgressDialog(this);
                    loadingDialog.setMessage("Logging in");
                    loadingDialog.show();
                ParseUser.logInInBackground(edtLoginEmail.getText().toString(),
                        edtLoginPassword.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null && e == null){
                                    FancyToast.makeText(LoginActivity.this,
                                            user.get("username")+ " is logged in successfully",
                                            Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                    goToSocialMediaActivity();

                                }
                                else FancyToast.makeText(LoginActivity.this,
                                        "ERROR: "+e.getMessage(),
                                        Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                            }
                        }
                );
                    loadingDialog.dismiss();
                }
                break;
            case R.id.btnGoToSignUpActivity:
                Intent intent = new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
                break;
        }

    }
    public void rootLayoutTapped(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
    private void goToSocialMediaActivity(){
        Intent intent = new Intent(LoginActivity.this,SocialMediaActivity.class);
        startActivity(intent);
    }
}