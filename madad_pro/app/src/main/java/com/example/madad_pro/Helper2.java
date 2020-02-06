package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Helper2 extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper2);


        String rtypes ="";

        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("json"));

            rtypes = jsonObj.getString("rtypes");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        // data to populate the RecyclerView with
        rtypes= rtypes.substring(1, rtypes.length()-1);

         ArrayList reqList  = new ArrayList<String>(Arrays.asList(rtypes.split(",")));

         for (int i=0;i<reqList.size();i++){
             reqList.set(i,reqList.get(i).toString().substring(1, reqList.get(i).toString().length()-1));
         }

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, reqList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
       String json=getIntent().getStringExtra("json");
        Intent intent = new Intent(Helper2.this,Requests.class);
        intent.putExtra("json",json);
        intent.putExtra("pos",position+1);
        startActivity(intent);
        Helper2.this.finish();

        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(Helper2.this, MainActivity.class);
        startActivity(intent);
        Helper2.this.finish();
    }
}