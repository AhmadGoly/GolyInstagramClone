package com.goly.golyinstagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        setTitle("Goly");

        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.postImageItem:
                if (Build.VERSION.SDK_INT >= 23 && //android marshmello. if it goes under 23 we don't need permission
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},5678);
                else captureImage();
                break;
            case R.id.logoutUserItem:
                ParseUser.logOut();
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void captureImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,4000);
    }


    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 5678:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FancyToast.makeText(SocialMediaActivity.this,"Media Permission granted.",
                            Toast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    captureImage();
                }else FancyToast.makeText(SocialMediaActivity.this,"Media Permission denied.",
                        Toast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 4000:
                if(resultCode == RESULT_OK && data != null){
                    try{
                        Uri capturedImage = data.getData();
                        Bitmap bitmapImage = MediaStore.Images.Media.getBitmap
                                (this.getContentResolver(), capturedImage);
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmapImage.compress(Bitmap.CompressFormat.PNG,100,byteArray);
                        byte[] imageBytes = byteArray.toByteArray();
                        ParseFile myFile = new ParseFile("img.png",imageBytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture",myFile);
                        //parseObject.put("image_des",edtDescription.getText().toString());
                        //not secure:
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(this);
                        dialog.setMessage("Uploading...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) FancyToast.makeText(SocialMediaActivity.this,"Upload completed.",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                else FancyToast.makeText(SocialMediaActivity.this,e.getMessage(),
                                        Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        });

                    }catch (Exception e){
                        FancyToast.makeText(SocialMediaActivity.this,"Error.",
                                Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        e.printStackTrace();
                    }

                }
                break;

        }
    }
}