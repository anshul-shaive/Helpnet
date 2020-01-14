package com.example.madad_pro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;



public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {


                /*
        //uncomment to test MainActivity without server

        SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
        editor.putString("token", "verified");
        editor.putInt("user_id", 101);
        editor.apply();

*/
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
                String token = prefs.getString("token", "");
                int user_id = prefs.getInt("user_id", 0);

                Intent mainIntent;

                if(token.equals("verified")) {
                     mainIntent = new Intent(Splash.this, MainActivity.class);
                }
                else {
                     mainIntent = new Intent(Splash.this, Option.class);
                }
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}