package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Otp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(Otp.this, Option.class);
        Toast.makeText(this,"Verification Failed",Toast.LENGTH_SHORT).show();
        startActivity(intent);
        Otp.this.finish();
    }
    public void submit(View view)
    {
        Intent intent = new Intent(Otp.this,MainActivity.class);
        startActivity(intent);
        Otp.this.finish();
    }
}
