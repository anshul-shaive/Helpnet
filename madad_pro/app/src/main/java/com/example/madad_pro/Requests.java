package com.example.madad_pro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Requests extends AppCompatActivity {

    String req_id,req_type,status,username,req_time,location,nprespond,auth_resp="";
    int user_id;
    long difference,min;
    String jsonstr;
    public String url = "https://helpnet-web.herokuapp.com/update";
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy", Locale.getDefault());
    private String currentDateandTime;


//    private String url = "http://192.168.0.5:8000/update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        jsonstr=getIntent().getStringExtra("json");

        try {

            Intent intent =getIntent();
            JSONObject jsonObj = new JSONObject(jsonstr);
            int pos = intent.getIntExtra("pos",0);
            JSONObject requestObject = new JSONObject(jsonObj.getString("request"+pos));

            req_id=requestObject.getString("req_id");
            req_type=requestObject.getString("req_type");
            status=requestObject.getString("status");
            username=requestObject.getString("username");
            req_time=requestObject.getString("req_time");

            currentDateandTime = sdf.format(new Date());
            Date date1 = sdf.parse(currentDateandTime);
            Date date2 = sdf.parse(req_time);
            difference = date1.getTime() - date2.getTime();
            min=difference/60000;

            location=requestObject.getString("location");
            nprespond=requestObject.getString("nprespond");
            auth_resp=requestObject.getString("auth_resp");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        TextView reqTextView = (TextView) findViewById(R.id.req_idTv);
        reqTextView.setText("Request Id: "+req_id);

        TextView statusTv = (TextView) findViewById(R.id.statusTv);
        statusTv.setText(status);

        TextView reqtypeTv = (TextView) findViewById(R.id.req_typeTv);
        reqtypeTv.setText("Request Type: "+req_type);

        TextView reqtimeTv = (TextView) findViewById(R.id.reqtimeTv);
        reqtimeTv.setText("Request Time: "+min+" mins ago");

        TextView npresTv = (TextView) findViewById(R.id.nprespondTv);
        npresTv.setText("No. of people responded: "+nprespond);

        TextView authresTv = (TextView) findViewById(R.id.authrespTv);
        authresTv.setText("Authority Informed: "+auth_resp);


        SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        Button respondButton = (Button)findViewById(R.id.respondbutton);
        respondButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                sendAndRequestResponse();
            }
        });
    }

    private void sendAndRequestResponse(){


        final SpotsDialog spotsDialog = new SpotsDialog(Requests.this);
        spotsDialog.show();
        RequestQueue queue = Volley.newRequestQueue(Requests.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        spotsDialog.dismiss();
                        Intent intent = new Intent(Requests.this, MapsActivity.class);
                        intent.putExtra("req_location",location);
                        intent.putExtra("req_id",req_id);

                        SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
                        editor.putString("req_location",location);
                        editor.putString("req_id", req_id);
                        editor.apply();

                        startActivity(intent);
                        Requests.this.finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }

        )


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id","" +user_id);
                params.put("req_id","" +req_id);
                params.put("status","responding");

                return params;
            }

        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        queue.add(postRequest);

    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(Requests.this, Helper2.class);
        intent.putExtra("json",jsonstr);
        startActivity(intent);
        Requests.this.finish();
    }
}
