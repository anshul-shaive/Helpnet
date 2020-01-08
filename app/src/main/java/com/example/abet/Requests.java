package com.example.abet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Requests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Intent intent =getIntent();
        String request = intent.getStringExtra("request_id");

        TextView reqTextView = (TextView) findViewById(R.id.reqtextView);
        reqTextView.setText(request);

        Button respondButton = (Button)findViewById(R.id.resbutton);
//        Intent mapintent = new Intent(Requests.this, MapsActivity.class);
////        intent.putExtra("request_id",requests.get(position));
//        startActivity(intent);
////        https://maps.googleapis.com/maps/api/geocode/json?latlng=23.267033,77.368417&key=AIzaSyAz_I96cGBltrjwzqFP4K9y2qrR07mOSxI
        respondButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Requests.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
