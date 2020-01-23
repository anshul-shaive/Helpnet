package com.example.madad_pro;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;


public class Splash extends Activity {

    int PERMISSION_ID = 44;

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    public void onCreate(Bundle icicle) {


        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        if(checkPermissions()){
            splash();
        }
        else {
            requestPermissions();
        }

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("aa","calling");
                splash();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            splash();
        }
        else {
            requestPermissions();
        }

    }

    public void  splash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                /*
        //uncomment to test MainActivity without server

        SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
        editor.putString("token", "verified");
        editor.putInt("user_id", 101);
        editor.apply();

*/


                SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
                String token = prefs.getString("token", "");
                int user_id = prefs.getInt("user_id", 0);

                ((MyApplication) Splash.this.getApplication()).setUser_id(Integer.toString(user_id));
                ((MyApplication) Splash.this.getApplication()).setStatus(token);

                Intent mainIntent;

                if (token.equals("verified")) {
                    mainIntent = new Intent(Splash.this, MainActivity.class);
                } else {
                    mainIntent = new Intent(Splash.this, Option.class);
                }


                Splash.this.startActivity(mainIntent);
                Splash.this.finish();


            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}