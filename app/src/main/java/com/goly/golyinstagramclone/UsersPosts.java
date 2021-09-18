package com.goly.golyinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {
    private LinearLayout linearLayout;
    private TextView nameTW,professionTW,
            hobbiesTW,sportTW,bioTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        linearLayout = findViewById(R.id.usersPostsLinearLayout);
        nameTW = findViewById(R.id.nameTW);
        professionTW = findViewById(R.id.professionTW);
        hobbiesTW = findViewById(R.id.HobbiesTW);
        sportTW = findViewById(R.id.sportTW);
        bioTW = findViewById(R.id.bioTW);
        Intent intent = getIntent();
        final String userName = intent.getStringExtra("username");
        setTitle(userName+"'s Profile");

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username",userName);
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null && e==null){
                    nameTW.setText("Name: "+user.get("profileName").toString());
                    professionTW.setText("Profession: "+user.get("profileProfession").toString());
                    hobbiesTW.setText("Hobbies: "+user.get("profileHobbies").toString());
                    sportTW.setText("Sport: "+user.get("profileFavSport").toString());
                    bioTW.setText("Bio:\n"+user.get("profileBio").toString());
                }
            }
        });


        ParseQuery<ParseObject> parseQuery2 = new ParseQuery<ParseObject>("Photo");
        parseQuery2.whereEqualTo("username",userName);
        parseQuery2.orderByDescending("createdAt");

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading user posts...");
        dialog.show();

        parseQuery2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0 && e==null){
                    for(ParseObject post : objects){
                        TextView postDescription = new TextView(UsersPosts.this);
                        postDescription.setText(post.get("image_des")+"");
                        ParseFile picture = (ParseFile) post.get("picture");
                        picture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data != null && e==null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(UsersPosts.this);

                                    LinearLayout.LayoutParams imageView_paramethers = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_paramethers.setMargins(5,5,5,5);

                                    postImageView.setLayoutParams(imageView_paramethers);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams textView_paramethers = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textView_paramethers.setMargins(10,5,10,15);

                                    postDescription.setLayoutParams(textView_paramethers);

                                    postDescription.setTextColor(Color.BLACK);
                                    postDescription.setTextSize(20f);


                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);

                                }else FancyToast.makeText(UsersPosts.this,
                                        "Error. can't download the photos.",
                                        Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                            }
                        });

                    }

                }
                dialog.dismiss();
            }
        });

    }
}