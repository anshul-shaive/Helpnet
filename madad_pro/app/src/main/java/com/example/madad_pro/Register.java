package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        Toast.makeText(this,"Registraion left incomplete",Toast.LENGTH_SHORT).show();
        intent = new Intent(Register.this, Option.class);
        startActivity(intent);
        Register.this.finish();
    }
    public void cancel(View view)
    {
        Intent intent =new Intent(Register.this, Option.class);
        startActivity(intent);
        Register.this.finish();
    }
    public void confirm(View view)
    {
        Intent intent =new Intent(Register.this,Otp.class);
        startActivity(intent);
        Register.this.finish();
    }
}