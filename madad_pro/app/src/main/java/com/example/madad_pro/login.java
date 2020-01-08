package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView textView1 = (TextView)findViewById(R.id.textView2);
        TextView textView2 = (TextView)findViewById(R.id.textView4);

    }
    public void login(View view)
    {
        //Toast.makeText(this, "LOGIN", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,help.class);
        startActivity(intent);
        login.this.finish();
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(login.this, Option.class);
        startActivity(intent);
        login.this.finish();
    }
}
