package com.goly.golyinstagramclone;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileTab extends Fragment{
    private EditText edtProfileName,edtProfileBio,edtProfileProfession,edtProfileHobbies,edtProfileFavSport;
    private Button btnUpdateInfo;
    private TextView usernameTW,emailTW;

    public ProfileTab() {
        // Required empty public constructor
    }

    public static ProfileTab newInstance(String param1, String param2) {
        ProfileTab fragment = new ProfileTab();
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

        //return inflater.inflate(R.layout.fragment_profile_tab, container, false);
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileFavSport = view.findViewById(R.id.edtProfileFavoriteSport);
        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);

        usernameTW = view.findViewById(R.id.usernameProfileTW);
        emailTW = view.findViewById(R.id.emailProfileTW);


        ParseUser user = ParseUser.getCurrentUser();

        usernameTW.setText(user.get("username").toString());
        emailTW.setText(user.get("email").toString());
        try{edtProfileName.setText(user.get("profileName").toString());}catch (Exception e){}
        try{edtProfileBio.setText(user.get("profileBio").toString());}catch (Exception e){}
        try{edtProfileProfession.setText(user.get("profileProfession").toString());}catch (Exception e){}
        try{edtProfileHobbies.setText(user.get("profileHobbies").toString());}catch (Exception e){}
        try{edtProfileFavSport.setText(user.get("profileFavSport").toString());}catch (Exception e){};

        btnUpdateInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                user.put("profileName",edtProfileName.getText().toString());
                user.put("profileBio",edtProfileBio.getText().toString());
                user.put("profileProfession",edtProfileProfession.getText().toString());
                user.put("profileHobbies",edtProfileHobbies.getText().toString());
                user.put("profileFavSport",edtProfileFavSport.getText().toString());
                ProgressDialog loadingDialog = new ProgressDialog(getContext());
                loadingDialog.setMessage("Updating info");
                loadingDialog.show();
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(getContext(),"Your profile updated on server.",
                                    Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                        }else FancyToast.makeText(getContext(),e.getMessage(),
                                Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                });
                loadingDialog.dismiss();
            }
        });
        return view;
    }


}