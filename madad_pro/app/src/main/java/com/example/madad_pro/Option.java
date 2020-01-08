package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Option extends AppCompatActivity {

    public void login(View view)
    {
        //Toast.makeText(this, "LOGIN", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
        Option.this.finish();
    }

    public void register(View view)
    {
        //Toast.makeText(this, "LOGIN", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
        Option.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
    }
}
