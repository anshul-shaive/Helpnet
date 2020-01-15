package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class HelpInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_info);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(HelpInfo.this,help.class);
        startActivity(i);
        HelpInfo.this.finish();
    }
}
