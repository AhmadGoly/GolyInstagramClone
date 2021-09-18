package com.goly.golyinstagramclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;


public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList <String> arrayList;
    private ArrayAdapter arrayAdapter;

    public UsersTab() {
        // Required empty public constructor
    }

    public static UsersTab newInstance(String param1, String param2) {
        UsersTab fragment = new UsersTab();
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
        //return inflater.inflate(R.layout.fragment_users_tab, container, false);
        View view  = inflater.inflate(R.layout.fragment_users_tab, container, false);
        listView = view.findViewById(R.id.listViewUsersTab);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,arrayList);

        //ADDED THIS METHOD TO SEE USERS POSTS.
        listView.setOnItemClickListener(UsersTab.this);
        //ADDED THIS METHOD TO SEE USERS POSTS.

        //ADDED THIS METHOD FOR LONG PRESS.
        listView.setOnItemLongClickListener(UsersTab.this);
        //ADDED THIS METHOD FOR LONG PRESS.

        TextView txtLoadingData = view.findViewById(R.id.txtLoadingUsers);

        //Goly : not secure to get all info about every user.
        ParseQuery <ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) if(objects.size()>0){
                        for(ParseUser user : objects){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        txtLoadingData.animate().alpha(0).setDuration(1000);
                        listView.setVisibility(View.VISIBLE);
                    }
            }
        });
        return view;
    }

    //ADDED THIS METHOD TO SEE USERS POSTS. Implemented from AdapterView.OnItemClickListener
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(),UsersPosts.class);
        intent.putExtra("username",arrayList.get(i));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayList.get(i));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null && e==null){
                    //DO NOTHING.
                }
            }
        });
        return false;
    }
}