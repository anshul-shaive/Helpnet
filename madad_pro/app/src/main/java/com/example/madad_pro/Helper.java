package com.example.madad_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class Helper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        ListView requestsListView = (ListView) findViewById(R.id.reqestListView);

        final ArrayList<String> requests = new ArrayList<String>(asList("Request 1", "Request 2", "Request 3", "Request 4"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requests);

        requestsListView.setAdapter(arrayAdapter);

        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Clicked " + requests.get(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Helper.this, Requests.class);
                intent.putExtra("request_id", requests.get(position));
                startActivity(intent);
            }
        });

    }
}