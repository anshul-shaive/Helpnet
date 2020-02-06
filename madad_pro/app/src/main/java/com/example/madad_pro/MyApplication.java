package com.example.madad_pro;

import android.app.Application;

public class MyApplication extends Application {

    String token;
    String user_id;
    String Role;
    String req_id="";
    String req_location;
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

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public void setRole(String role) {
        Role = role;
    }

    public void setReq_location(String req_location) {
        this.req_location = req_location;
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

    public String getReq_id() {
        return req_id;
    }

    public String getRole() {
        return Role;
    }

    public String getReq_location() {
        return req_location;
    }

}