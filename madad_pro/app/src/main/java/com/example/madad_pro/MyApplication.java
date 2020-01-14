package com.example.madad_pro;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {


//    SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
//    String token = prefs.getString("token", "");
//    int user_id = prefs.getInt("user_id", 0);

    String token ;
    int user_id ;

    public void setStatus(String someVariable) {
        this.token= someVariable;
    }

    public void setUser_id(int someVariable) {
        this.user_id = someVariable;
    }

    public String getStatus() {
        return token;
    }

    public int getUser_id() {
        return user_id;
    }




}