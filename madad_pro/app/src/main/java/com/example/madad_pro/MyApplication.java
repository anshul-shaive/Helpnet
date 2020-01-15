package com.example.madad_pro;

import android.app.Application;

public class MyApplication extends Application {

    String token ;
    String user_id ;

    public void setStatus(String someVariable) {
        this.token= someVariable;
    }

    public void setUser_id(String someVariable) {
        this.user_id = someVariable;
    }

    public String getStatus() {
        return token;
    }

    public String getUser_id() {
        return user_id;
    }




}