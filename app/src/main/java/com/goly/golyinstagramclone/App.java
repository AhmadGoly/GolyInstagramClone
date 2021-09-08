package com.goly.golyinstagramclone;
import android.app.Application;
import com.parse.Parse;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("eL61uSmU1A3tyvTAShrtdXB3Fd1zofSU93GmoaWx")
                // if defined
                .clientKey("gCSo9Oyp5L4SUk5YA1lXlBmhxyIdJiuOwN46NYQC")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
