package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void emergency(View view)
    {
        Intent intent = new Intent(MainActivity.this,Option.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void helpOthers(View view)
    {
        Intent intent = new Intent(MainActivity.this,Helper.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
}
