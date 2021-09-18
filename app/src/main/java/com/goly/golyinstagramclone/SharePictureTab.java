package com.goly.golyinstagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class SharePictureTab extends Fragment implements View.OnClickListener {

    private ImageView imgShare;
    private EditText edtDescription;
    private Button btnShareImg;
    private Bitmap bitmapImage;

    public SharePictureTab() {
        // Required empty public constructor
    }


    public static SharePictureTab newInstance(String param1, String param2) {
        SharePictureTab fragment = new SharePictureTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
        imgShare = view.findViewById(R.id.imgShare);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnShareImg = view.findViewById(R.id.btnShareImage);
        imgShare.setOnClickListener(SharePictureTab.this);
        btnShareImg.setOnClickListener(SharePictureTab.this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgShare:
                if (Build.VERSION.SDK_INT >= 23 && //android marshmello. if it goes under 23 we don't need permission
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1234);
                else getChosenImage();
                break;
            case R.id.btnShareImage:
                if(bitmapImage != null){
                    if(edtDescription.getText().toString().equals(""))
                        FancyToast.makeText(getContext(),"You must specify your description",
                                Toast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    else{
                        ByteArrayOutputStream myImageBytes = new ByteArrayOutputStream();
                        bitmapImage.compress(Bitmap.CompressFormat.PNG,100,myImageBytes);
                        byte[]bytes = myImageBytes.toByteArray();
                        ParseFile myFile = new ParseFile("img.png",bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture",myFile);
                        parseObject.put("image_des",edtDescription.getText().toString());
                        //not secure:
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Uploading...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) FancyToast.makeText(getContext(),"Upload completed.",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                    else FancyToast.makeText(getContext(),e.getMessage(),
                                        Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    dialog.dismiss();
                            }
                        });
                    }
                }
                else FancyToast.makeText(getContext(),"Select your image first.",
                        Toast.LENGTH_SHORT, FancyToast.INFO, false).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1234:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FancyToast.makeText(getContext(),"Media Permission granted.",
                            Toast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    getChosenImage();
                }else FancyToast.makeText(getContext(),"Media Permission denied.",
                        Toast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //THIS IS ONLY FOR FRAGMENTS.
        switch (requestCode){
            case 2000:
                if(resultCode == Activity.RESULT_OK){   //We got the image.
                    try{
                        Uri returnUri = data.getData();
                        bitmapImage = MediaStore.Images.Media.getBitmap
                                (getActivity().getContentResolver(), returnUri);
                        imgShare.setImageBitmap(bitmapImage);

                    }catch (Exception e){
                        FancyToast.makeText(getContext(),"Error.",
                                Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        e.printStackTrace();
                    }


                }else FancyToast.makeText(getContext(),"Please select image in order to share.",
                        Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                break;

        }
    }

    private void getChosenImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }
}