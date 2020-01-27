package com.example.madad_pro;

import android.app.Application;
import android.app.DatePickerDialog;

public class MyApplication extends Application {

    String token;
    String user_id;
    Double lat;
    Double lng;
    Double pinnedlat;
    Double pinnedlng;

    public void setStatus(String someVariable) {
        this.token= someVariable;
    }

    public void setUser_id(String someVariable) {
        this.user_id = someVariable;
    }

    public void setLat(Double someVariable) {
        this.lat = someVariable;
    }

    public void setLng(Double someVariable) {
        this.lng = someVariable;
    }

    public void setPinnedlat(Double someVariable) {
        this.pinnedlat = someVariable;
    }

    public void setPinnedlng(Double someVariable) {
        this.pinnedlng = someVariable;
    }

    public String getStatus() {
        return token;
    }

    public String getUser_id() {
        return user_id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Double getPinnedlat() {
        return pinnedlat;
    }

    public Double getPinnedlng() {
        return pinnedlng;
    }



}