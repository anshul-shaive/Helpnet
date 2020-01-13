package com.example.madad_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {


    RequestQueue queue;
//    public String url = "http://172.16.19.219:8000/login";
//    public String url = "http://192.168.0.4:8000/login";
        public String url = "http://172.16.18.33:8000/login";

    EditText editusername;
    EditText editpassword;

    public String username;
    public String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editusername = (EditText) findViewById(R.id.edit_username);
        editpassword = (EditText) findViewById(R.id.edit_password);
    }
    public void signin(View view)
    {
        username = editusername.getText().toString();
        password = editpassword.getText().toString();
        sendAndRequestResponse();
/*
        //uncomment to test MainActivity without server

        SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
        editor.putString("token", "verified");
        editor.putInt("user_id", 101);
        editor.apply();

*/

    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(login.this, Option.class);
        startActivity(intent);
        login.this.finish();
    }

    private void sendAndRequestResponse() {
        queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jObject;
                        String user_id="0";
                        String status="";

                        Log.d("", response);
                        System.out.println(response);


                        try {
                            jObject = new JSONObject(response);
                            status = jObject.getString("status");
                            user_id = jObject.getString("user_id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(login.this, "" + status, Toast.LENGTH_LONG).show();

                        System.out.println(status);
                        System.out.println(user_id);

                        SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
                        editor.putString("token", status);
                        editor.putInt("user_id", Integer.parseInt(user_id));
                        editor.apply();

                        Intent intent;

                        if(status.equals("verified")) {
                            intent = new Intent(login.this, MainActivity.class);
                        }
                        else {
                            intent = new Intent(login.this, Option.class);
                        }

                        startActivity(intent);
                        login.this.finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username","" +username);
                params.put("password",""+password);

                return params;
            }
        };
            queue.add(postRequest);
    }
}
