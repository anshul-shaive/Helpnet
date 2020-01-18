package com.example.madad_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class Helper extends AppCompatActivity {
    ArrayList<String> reqList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);


 //       {"request1": {"user_id": "0", "presponded_ids": "", "passigned_ids": "", "req_id": 35,
//               "req_type": "Emergency", "status": "active", "username": "", "req_time": "21:52:21-16/01/2020",
//               "location": "23.231043333333332:77.45816166666667", "nprespond": "", "auth_resp": "false"}
        //rtypes, dists


            try {
                JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("json"));

           String rtypes=jsonObj.getString("rtypes");
//            JSONObject requestObject = jsonObj.getJSONObject("rtypes");
//            String nameValue = requestObject.getString("name");
               rtypes= rtypes.substring(1, rtypes.length()-1);

               reqList  = new ArrayList<String>(Arrays.asList(rtypes.split(",")));

            System.out.println(asList(rtypes));



            } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView requestsListView = (ListView) findViewById(R.id.reqestListView);

        final ArrayList<String> requests = new ArrayList<String>(asList("40", "Request 2", "Request 3", "Request 4"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reqList);

        requestsListView.setAdapter(arrayAdapter);

        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Clicked " + reqList.get(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Helper.this, Requests.class);
                intent.putExtra("request_id", reqList.get(position));
                startActivity(intent);
            }
        });

    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(Helper.this, MainActivity.class);
        startActivity(intent);
        Helper.this.finish();
    }
}